package dev.rednova.mailer

import zio.*
import zio.prelude.*
import zio.aws.core.AwsError
import zio.aws.sesv2.*
import zio.aws.sesv2.model.*
import zio.aws.sesv2.model.primitives.*
import zio.prelude.data.Optional

case class SesMailer(
    ses: SesV2
  ) extends Mailer:

  def send(
      mail: Mail
    ): IO[MailerException, MessageId] =
    for
      req <- ZIO.succeed(buildEmailRequest(mail))
      out <- ses.sendEmail(req).mapError(handleError)
      id  <- out.getMessageId.mapError(handleError)
    yield id

  private def buildEmailRequest(
      mail: Mail
    ): SendEmailRequest =
    SendEmailRequest(
      fromEmailAddress = mail.from.map(asEmailAddress),
      destination = asDestination(mail),
      // replyToAddresses: Optional[Iterable[EmailAddress]] = Optional.Absent,
      // feedbackForwardingEmailAddress: Optional[EmailAddress] = Optional.Absent,
      // feedbackForwardingEmailAddressIdentityArn: Optional[AmazonResourceName] =
      content = asEmailContent(mail),
    )

  private def asDestination(
      m: Mail
    ): Destination =
    Destination(
      toAddresses = m.to.map(_.map(asEmailAddress)),
      ccAddresses = m.cc.map(_.map(asEmailAddress)),
      bccAddresses = m.bcc.map(_.map(asEmailAddress)),
    )

  private def asEmailAddress(
      v: String
    ): EmailAddress =
    EmailAddress(v)

  private def asEmailContent(
      m: Mail
    ): EmailContent =
    EmailContent(
      simple = Message(
        subject = toContent(m.subject.getOrElse("No subject")),
        body = Body(
          text = m.bodyText.map(toContent),
          html = m.bodyHtml.map(toContent),
        ),
      )
    )

  private def toContent(
      v: String
    ): Content =
    Content(MessageData(v))

  private def handleError(
      e: AwsError
    ): MailerException =
    MailerException("SesMailer could not send the email", Some(e.toThrowable))

object SesMailer:

  /** Creates a ZLayer to connect to AWS SES using the API */
  val layer: ZLayer[SesV2, MailerException, Mailer] =
    ZLayer.fromZIO:
      for ses <- ZIO.service[SesV2]
      yield SesMailer(ses)
