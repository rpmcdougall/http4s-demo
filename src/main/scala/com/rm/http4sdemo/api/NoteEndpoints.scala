package com.rm.http4sdemo.api


import cats.effect.Effect
import cats.implicits._
import com.rm.http4sdemo.domain.{Note, NoteAlreadyExistsError, NoteNotFoundError, NoteService}
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{EntityDecoder, HttpRoutes}

import scala.language.higherKinds


class NoteEndpoints[F[_]: Effect] extends Http4sDsl[F] {

  import Pagination._
  implicit val noteDecoder: EntityDecoder[F, Note] = jsonOf[F, Note]

  private def createNoteEndpoint(noteService: NoteService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ POST -> Root / "notes" =>
        val action = for {
          note <- req.as[Note]
          result <- noteService.createUser(note).value
        } yield result

        action.flatMap {
          case Right(saved) =>
            Ok(saved.asJson)
          case Left(NoteAlreadyExistsError(existing)) =>
            Conflict(s"The note ${existing.title} already exists")
        }
    }

  private def updateNoteEndpoint(noteService: NoteService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case req @ PUT -> Root / "notes" / LongVar(noteId) =>
        val action = for {
          note <- req.as[Note]
          updated = note.copy(id = Some(noteId))
          result <- noteService.update(updated).value
        } yield result

        action.flatMap {
          case Right(saved) => Ok(saved.asJson)
          case Left(NoteNotFoundError) => NotFound("The note was not found")
        }
    }

  private def getNoteEndpoint(noteService: NoteService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "notes" / LongVar(id) =>
         noteService.getNote(id).value.flatMap {
          case Right(found) => Ok(found.asJson)
          case Left(NoteNotFoundError) => NotFound("The note was not found")
        }
    }

  private def deleteNoteEndpoint(noteService: NoteService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case DELETE -> Root / "notes" / LongVar(id) =>
        for {
          _ <- noteService.deleteNote(id)
          resp <- Ok()
        } yield resp
    }

  private def listNotesEndpoint(noteService: NoteService[F]): HttpRoutes[F] =
    HttpRoutes.of[F] {
      case GET -> Root / "notes" :? OptionalPageSizeMatcher(pageSize) :? OptionalOffsetMatcher(offset) =>
        for {
          retrieved <- noteService.list(pageSize.getOrElse(10), offset.getOrElse(0))
          resp <- Ok(retrieved.asJson)
        } yield resp
    }

  def endpoints(noteService: NoteService[F]): HttpRoutes[F] =
    createNoteEndpoint(noteService) <+>
      getNoteEndpoint(noteService) <+>
      deleteNoteEndpoint(noteService) <+>
      listNotesEndpoint(noteService) <+>
      updateNoteEndpoint(noteService)

}

object NoteEndpoints {
  def endpoints[F[_]: Effect](noteService: NoteService[F]): HttpRoutes[F] =
    new NoteEndpoints[F].endpoints(noteService)
}
