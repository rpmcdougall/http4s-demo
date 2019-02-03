package com.rm.http4sdemo.domain

case class Note (
   title: String,
   author: String,
   content: String,
   synopsis: String,
   id: Option[Long] = None
 )


