package dev.rednova.mailer.support

import scala.util.chaining.*
import zio.*
import zio.config.*
import zio.config.syntax.*
import zio.config.ConfigDescriptor.*
import zio.config.typesafe.*

case class SpecConfig(
    mailer: MailerConfig
  )
case class MailerConfig(
    host: String,
    port: Int,
    ssl: Boolean,
    tls: Boolean,
    tlsRequired: Boolean,
    user: Option[String],
    password: Option[String],
    timeout: Option[Int],
    connectionTimeout: Option[Int],
  )

object SpecConfig:

  val layer =
    config
      .from(TypesafeConfigSource.fromResourcePath)
      .pipe(read)
      .pipe(ZLayer.fromZIO)

  private val mailer: ConfigDescriptor[MailerConfig] =
    (
      string("host")
        zip int("port")
        zip boolean("ssl")
        zip boolean("tls")
        zip boolean("tlsRequired")
        zip string("user").optional
        zip string("password").optional
        zip int("timeout").optional
        zip int("connectionTimeout").optional
    ).to[MailerConfig]

  private val config: ConfigDescriptor[SpecConfig] =
    nested("mailer")(mailer)
      .to[SpecConfig]
