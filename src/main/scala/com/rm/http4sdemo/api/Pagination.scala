package com.rm.http4sdemo.api

import org.http4s.QueryParamDecoder
import org.http4s.dsl.impl.OptionalQueryParamDecoderMatcher

object Pagination {

  /* Necessary for decoding query parameters */

  import QueryParamDecoder._

  /* Parses out the optional offset and page size params */
  object OptionalPageSizeMatcher extends OptionalQueryParamDecoderMatcher[Int]("pageSize")

  object OptionalOffsetMatcher extends OptionalQueryParamDecoderMatcher[Int]("offset")

}
