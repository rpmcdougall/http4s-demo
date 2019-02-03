package com.rm.http4sdemo.domain

import scala.language.higherKinds
import cats.data.EitherT

trait NoteValidationAlgebra[F[_]] {

  def doesNotExist(note: Note): EitherT[F, NoteAlreadyExistsError, Unit]
  def exists(noteId: Option[Long]): EitherT[F, NoteNotFoundError.type, Unit]
}