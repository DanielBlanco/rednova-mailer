package dev.rednova.mailer

case class MailerException(
    message: String,
    cause: Option[Throwable] = None,
  ) extends Exception(message, cause.getOrElse(null))
