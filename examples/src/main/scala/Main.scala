import dev.rednova.mailer.*
import zio.*
import zio.aws.*
import zio.aws.core.config.AwsConfig
import zio.aws.sesv2.SesV2
import zio.aws.netty.NettyHttpClient
import zio.aws.core.config.CommonAwsConfig

object Main extends ZIOAppDefault:

  def run =
    program.provideSome(layers).orDie

  private val program: RIO[Mailer, Unit] = for
    _   <- ZIO.unit
    mail = buildMail
    _   <- Console.printLine(mail.toString())
  // id  <- mail.send
  // _   <- Console.printLine(s"Response ID: $id")
  yield ()

  final val buildMail =
    Mailer
      .from("example@rednova.dev")
      .to(recipients)
      .subject("rednova-mailer example")
      .bodyText("Your rednova-mailer setup is working!")

  final val recipients = NonEmptyChunk(
    "daniel.blancorojas@gmail.com"
  )

  final val layers = ZLayer.make[Mailer](
    NettyHttpClient.default,
    AwsConfig.default,
    SesV2.live,
    SesMailer.layer
  )
