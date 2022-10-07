package dev.rednova.mailer

import support.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

object MailerSpec extends CoreSpec:

  def spec = suite("MailerSpec")(tests: _*)

  def tests = Chunk(
    test("sends emails") {
      for
        id  <- Mailer.subject("test").bodyText("changos").send
        out <- TestConsole.output
      yield assertTrue(out(0).contains("subject: test")) &&
        assert(out(0))(containsString("bodyText: changos"))
    }
  )
