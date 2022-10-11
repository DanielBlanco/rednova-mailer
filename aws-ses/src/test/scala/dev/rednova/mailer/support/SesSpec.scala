package dev.rednova.mailer.support

import dev.rednova.mailer.*
import scala.util.chaining.*
import zio.*
import zio.aws.*
import zio.aws.core.config.*
import zio.aws.netty.*
import zio.aws.sesv2.*
import zio.aws.sesv2.model.*
import zio.aws.sesv2.model.SendEmailResponse.ReadOnly
import zio.aws.sesv2.model.primitives.*
import zio.prelude.*
import zio.prelude.data.Optional
import zio.test.*
import zio.test.Assertion.*

trait SesSpec extends ZIOSpec[SpecEnv]:

  override def aspects = Chunk(TestAspect.timeout(60.seconds))

  final val TONY_EMAIL = "tony@stark.com"

  final val DUMMY_RESPONSE = SendEmailResponse(
    Optional.Present(OutboundMessageId("out-id"))
  ).asReadOnly

  val sendEmailMock: ULayer[SesV2] =
    SesV2Mock.SendEmail(
      hasField(
        "fromEmailAddress",
        (r: SendEmailRequest) => r.fromEmailAddress.toOption,
        isSome(equalTo(EmailAddress(TONY_EMAIL)))
      ),
      mock.Expectation.valueF(_ => DUMMY_RESPONSE)
    )

  override val bootstrap = ZLayer.make[SpecEnv](
    sendEmailMock,
    SesMailer.layer
  )

  def spec: Spec[SpecEnv, Any]
