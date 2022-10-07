package dev.rednova.mailer

import zio.*

case class Mail(
    from: Option[String] = None,
    to: Option[Chunk[String]] = None,
    bcc: Option[Chunk[String]] = None,
    cc: Option[Chunk[String]] = None,
    subject: Option[String] = None,
    bodyText: Option[String] = None,
    bodyHtml: Option[String] = None,
    attachments: Option[NonEmptyChunk[MailAttachment]] = None
):

  def attachments(v: NonEmptyChunk[MailAttachment]): Mail =
    copy(attachments = Some(v))

  def bodyText(v: String): Mail = copy(bodyText = Some(v))

  def bodyHtml(v: String): Mail = copy(bodyHtml = Some(v))

  def bcc(v: NonEmptyChunk[String]): Mail = copy(bcc = Some(v))

  def cc(v: NonEmptyChunk[String]): Mail = copy(cc = Some(v))

  def from(v: String): Mail = copy(from = Some(v))

  def subject(v: String): Mail = copy(subject = Some(v))

  def to(v: NonEmptyChunk[String]): Mail = copy(to = Some(v))

  def send = Mailer.send(this)

  override def toString: String =
    s"""|Mail:
        |  subject: ${subject.getOrElse("None")}
        |  from: ${from.getOrElse("None")}
        |  to: ${to.fold("None")(_.mkString(","))}
        |  bodyText: ${bodyText.getOrElse("None")}
        |  bodyHtml: ${bodyHtml.getOrElse("None")}
        |  cc: ${cc.fold("None")(_.mkString(","))}
        |  bcc: ${bcc.fold("None")(_.mkString(","))}
        |  attachments: ${attachments.fold("None")(prettyAttachments)}
        |""".stripMargin

  private def prettyAttachments(attachments: NonEmptyChunk[MailAttachment]): String =
    attachments.map(_.name).mkString("\n")

object Mail:
  def bodyHtml(v: String) = apply(bodyHtml = Some(v))

  def bodyText(v: String) = apply(bodyText = Some(v))

  def from(v: String) = apply(from = Some(v))

  def subject(v: String) = apply(subject = Some(v))

  def to(v: NonEmptyChunk[String]) = apply(to = Some(v))
