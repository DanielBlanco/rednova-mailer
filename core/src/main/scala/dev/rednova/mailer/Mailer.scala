package dev.rednova.mailer

import zio.*
import zio.prelude.*

type MessageId = String

trait Mailer:

  def send(mail: Mail): IO[MailerException, MessageId]

object Mailer:

  /** Returns a Mail object with the `bodyHtml` field set to the provided value.
    *
    * @param v
    *   the value to set.
    * @return
    *   a Mail object.
    */
  def bodyHtml(v: String) = Mail.bodyHtml(v)

  /** Returns a Mail object with the `bodyText` field set to the provided value.
    *
    * @param v
    *   the value to set.
    * @return
    *   a Mail object.
    */
  def bodyText(v: String) = Mail.bodyText(v)

  /** Returns a Mail object with the `from` field set to the provided value.
    *
    * @param v
    *   the value to set.
    * @return
    *   a Mail object.
    */
  def from(v: String) = Mail.from(v)

  /** Returns a Mail object with the `subject` field set to the provided value.
    *
    * @param v
    *   the value to set.
    * @return
    *   a Mail object.
    */
  def subject(s: String) = Mail.subject(s)

  /** Returns a Mail object with the `recipients` field set to the provided value.
    *
    * @param v
    *   the value to set.
    * @return
    *   a Mail object.
    */
  def to(s: NonEmptyChunk[String]) = Mail.to(s)

  /** Alias type to ZIO[Mailer, MailerException, A] */
  type MailerIO[A] = ZIO[Mailer, MailerException, A]

  /** Sends the provided mail.
    *
    * @param mail
    *   to send.
    * @return
    *   ZIO with A representing the message identifier.
    */
  def send(mail: Mail): MailerIO[MessageId] =
    ZIO.serviceWithZIO(_.send(mail))
