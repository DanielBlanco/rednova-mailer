package dev.rednova.mailer.support

import dev.rednova.mailer.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

trait CoreSpec extends ZIOSpec[Mailer]:

  override def aspects = Chunk(TestAspect.timeout(60.seconds))

  override val bootstrap: ZLayer[Scope, Any, Mailer] =
    ZLayer.make[Mailer](
      ZLayer.succeed(ConsoleMailer)
    )

  def spec: Spec[Scope & Mailer, Any]
