package com.rm.http4sdemo.domain

sealed trait ValidationError extends Product with Serializable
case class NoteAlreadyExistsError(note: Note) extends ValidationError
case object NoteNotFoundError extends ValidationError