package com.rm.http4sdemo

import io.circe.Decoder
import io.circe.generic.semiauto._

package object config {
  implicit val srDec: Decoder[ServerConfig] = deriveDecoder
  implicit val dbDec: Decoder[DatabaseConfig] = deriveDecoder
  implicit val psDec: Decoder[NoteConfig] = deriveDecoder
}
