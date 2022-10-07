package dev.rednova.mailer.support

import dev.rednova.mailer.*
import zio.*
import zio.test.*
import zio.test.Assertion.*

object ConsoleMailer extends Mailer:

  def send(mail: Mail): IO[MailerException, MessageId] =
    Console
      .printLine(mail.toString)
      .mapError(e => MailerException(e.getMessage()))
      .map(_ => "console-mailer")
