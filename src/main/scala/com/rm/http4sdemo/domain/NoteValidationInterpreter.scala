package com.rm.http4sdemo.domain

import cats._
import cats.data.EitherT
import cats.implicits._

class NoteValidationInterpreter[F[_]: Monad](noteRepo: NoteRepositoryAlgebra[F]) extends NoteValidationAlgebra[F] {
  def doesNotExist(note: Note) = EitherT {
    noteRepo.findByTitle(note.title).map {
      case None => Right(())
      case Some(_) => Left(NoteAlreadyExistsError(note))
    }
  }

  def exists(noteId: Option[Long]): EitherT[F, NoteNotFoundError.type, Unit] =
    EitherT {
      noteId.map { id =>
        noteRepo.get(id).map {
          case Some(_) => Right(())
          case _ => Left(NoteNotFoundError)
        }
      }.getOrElse(
        Either.left[NoteNotFoundError.type, Unit](NoteNotFoundError).pure[F]
      )
    }
}

object NoteValidationInterpreter {
  def apply[F[_]: Monad](repo: NoteRepositoryAlgebra[F]): NoteValidationAlgebra[F] =
    new NoteValidationInterpreter[F](repo)
}
