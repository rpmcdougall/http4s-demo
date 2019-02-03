package com.rm.http4sdemo.domain

import cats._
import cats.data._
import cats.syntax.functor._

// validation: UserValidationAlgebra[F]
class NoteService [F[_]: Monad](noteRepo: NoteRepositoryAlgebra[F], validation: NoteValidationAlgebra[F]) {
  def createUser(note: Note): EitherT[F, NoteAlreadyExistsError, Note] =
    for {
      _ <- validation.doesNotExist(note)
      saved <- EitherT.liftF(noteRepo.create(note))
    } yield saved

  def getNote(noteId: Long): EitherT[F, NoteNotFoundError.type, Note] =
    EitherT.fromOptionF(noteRepo.get(noteId), NoteNotFoundError)

  def getNoteByTitle(title: String): EitherT[F, NoteNotFoundError.type, Note] =
    EitherT.fromOptionF(noteRepo.findByTitle(title), NoteNotFoundError)

  def deleteNote(noteID: Long): F[Unit] = noteRepo.delete(noteID).as(())

  def deleteByTitle(title: String): F[Unit] =
    noteRepo.deleteByTitle(title).as(())

  def update(note: Note): EitherT[F, NoteNotFoundError.type, Note] =
    for {
      _ <- validation.exists(note.id)
      saved <- EitherT.fromOptionF(noteRepo.update(note), NoteNotFoundError)
    } yield saved

  def list(pageSize: Int, offset: Int): F[List[Note]] =
    noteRepo.list(pageSize, offset)

}

object NoteService {
  def apply[F[_]: Monad](repository: NoteRepositoryAlgebra[F], validation: NoteValidationAlgebra[F]): NoteService[F] =
    new NoteService[F](repository, validation)
}