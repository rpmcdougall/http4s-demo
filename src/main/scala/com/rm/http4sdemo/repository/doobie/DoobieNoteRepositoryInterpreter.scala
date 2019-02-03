package com.rm.http4sdemo.repository.doobie

import cats._
import cats.data.OptionT
import cats.implicits._
import doobie._
import doobie.implicits._
import com.rm.http4sdemo.domain.{Note, NoteRepositoryAlgebra}
import com.rm.http4sdemo.repository.doobie.SQLPagination._

import scala.language.higherKinds

private object NoteSql {
  def insert(note: Note): Update0 = sql"""
    INSERT INTO NOTES (TITLE, AUTHOR, CONTENT, SYNOPSIS)
    VALUES (${note.title}, ${note.author}, ${note.content}, ${note.synopsis})
  """.update

  def update(note: Note, id: Long): Update0 = sql"""
    UPDATE NOTES
    SET TITLE = ${note.title}, AUTHOR = ${note.author}, CONTENT = ${note.content}, SYNOPSIS = ${note.synopsis}
    WHERE ID = $id
  """.update

  def select(noteId: Long): Query0[Note] = sql"""
    SELECT TITLE, AUTHOR, CONTENT, SYNOPSIS, ID
    FROM NOTES
    WHERE ID = $noteId
  """.query

  def byTitle(title: String): Query0[Note] = sql"""
    SELECT TITLE, AUTHOR, CONTENT, SYNOPSIS, ID
    FROM NOTES
    WHERE TITLE = $title
  """.query[Note]

  def delete(noteId: Long): Update0 = sql"""
    DELETE FROM NOTES WHERE ID = $noteId
  """.update

  val selectAll: Query0[Note] = sql"""
   SELECT TITLE, AUTHOR, CONTENT, SYNOPSIS, ID
   FROM NOTES
  """.query
}

class DoobieNoteRepositoryInterpreter[F[_]: Monad](val xa: Transactor[F])
  extends NoteRepositoryAlgebra[F] {

  import NoteSql._

  def create(note: Note): F[Note] =
    insert(note).withUniqueGeneratedKeys[Long]("ID").map(id => note.copy(id = id.some)).transact(xa)

  def update(note: Note): F[Option[Note]] = OptionT.fromOption[F](note.id).semiflatMap { id =>
    NoteSql.update(note, id).run.transact(xa).as(note)
  }.value

  def get(noteId: Long): F[Option[Note]] = select(noteId).option.transact(xa)

  def findByTitle(title: String): F[Option[Note]] = byTitle(title).option.transact(xa)

  def delete(noteId: Long): F[Option[Note]] = OptionT(get(noteId)).semiflatMap(note =>
    NoteSql.delete(noteId).run.transact(xa).as(note)
  ).value

  def deleteByTitle(title: String): F[Option[Note]] =
    OptionT(findByTitle(title)).mapFilter(_.id).flatMapF(delete).value

  def list(pageSize: Int, offset: Int): F[List[Note]] =
    paginate(pageSize, offset)(selectAll).to[List].transact(xa)
}

object DoobieNoteRepositoryInterpreter {
  def apply[F[_]: Monad](xa: Transactor[F]): DoobieNoteRepositoryInterpreter[F] =
    new DoobieNoteRepositoryInterpreter[F](xa)
}
