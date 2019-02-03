package com.rm.http4sdemo.config

final case class ServerConfig(host: String, port: Int)
final case class NoteConfig(db: DatabaseConfig, server: ServerConfig)
