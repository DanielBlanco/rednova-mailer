package dev.rednova.mailer

import support.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

object SesMailerSpec extends SesSpec:

  def spec = suite("SesMailerSpec")(tests: _*)

  def tests = Chunk(
    test("sends email") {
      for id <- Mailer
                  .from(TONY_EMAIL)
                  .subject("test")
                  .bodyText("changos")
                  .send
      yield assertTrue(id == "out-id")
    }
  )
