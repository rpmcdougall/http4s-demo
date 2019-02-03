package com.rm.http4sdemo

//import config._
import domain.Note._
import api.NoteEndpoints
import com.rm.http4sdemo.config.DatabaseConfig
import com.rm.http4sdemo.domain.NoteService
import repository.doobie.DoobieNoteRepositoryInterpreter
import cats.effect._
import cats.implicits._
import org.http4s.server.{Router, Server => H4Server}
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import io.circe.config.parser
import com.rm.http4sdemo.config._
import org.http4s.server.Router

object Server extends IOApp {
  def createServer[F[_] : ContextShift : ConcurrentEffect : Timer]: Resource[F, H4Server[F]] =
    for {
      conf           <- Resource.liftF(parser.decodePathF[F, NoteConfig]("notes"))
      xa             <- DatabaseConfig.dbTransactor(conf.db, global, global)
      noteRepo        =  DoobieNoteRepositoryInterpreter[F](xa)
      noteValidation  =  NoteValidationInterpreter[F](noteRepo)
      noteService     = NoteService[F] (noteRepo, noteValidation)
      services       =  NoteEndpoints.endpoints[F](noteService) <+>
      httpApp = Router("/" -> services).orNotFound
      _ <- Resource.liftF(DatabaseConfig.initializeDb(conf.db))
      server <-
        BlazeServerBuilder[F]
          .bindHttp(conf.server.port, conf.server.host)
          .withHttpApp(httpApp)
          .resource
    } yield server

  def run(args : List[String]) : IO[ExitCode] = createServer.use(_ => IO.never).as(ExitCode.Success)
}