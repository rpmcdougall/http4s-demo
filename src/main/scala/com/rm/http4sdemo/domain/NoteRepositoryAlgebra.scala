package com.rm.http4sdemo.domain

trait NoteRepositoryAlgebra[F[_]] {
  def create(note: Note): F[Note]
  def update(note: Note): F[Option[Note]]
  def get(noteId: Long): F[Option[Note]]
  def delete(noteId: Long): F[Option[Note]]
  def findByTitle(title: String): F[Option[Note]]
  def deleteByTitle(title: String): F[Option[Note]]
  def list(pageSize: Int, offset: Int): F[List[Note]]
}
