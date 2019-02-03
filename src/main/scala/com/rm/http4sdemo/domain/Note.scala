package com.rm.http4sdemo.domain

case class Note (
   title: String,
   author: String,
   content: String,
   id: Option[Long] = None,
   synopsis: Option[String] = None
 )


