import dev.rednova.mailer.*
import scala.util.chaining.*
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.auth.credentials.{
  AwsBasicCredentials,
  StaticCredentialsProvider
}
import zio.*
import zio.aws.*
import zio.aws.core.config.AwsConfig
import zio.aws.sesv2.SesV2
import zio.aws.netty.NettyHttpClient
import zio.aws.core.config.CommonAwsConfig
import zio.aws.core.config.ClientCustomization

object Main extends ZIOAppDefault:

  def run =
    program.provideSome(layers).orDie

  private val program: RIO[Mailer, Unit] =
    for
      _   <- ZIO.unit
      mail = buildMail
      _   <- Console.printLine(mail.toString())
      id  <- mail.send
      _   <- Console.printLine(s"Response ID: $id")
    yield ()

  final val recipients = NonEmptyChunk("daniel.blanco@maildrop.cc")

  final val buildMail: Mail =
    Mailer
      .from("example@novaself.app")
      .to(recipients)
      .subject("rednova-mailer example")
      .bodyText("Your rednova-mailer setup is working!")

  final val accessKey = "TODO"

  final val secretKey = "TODO"

  final val myAwsConfig: ULayer[CommonAwsConfig] =
    CommonAwsConfig(
      region = Some(Region.US_EAST_1),
      credentialsProvider = StaticCredentialsProvider
        .create(AwsBasicCredentials.create(accessKey, secretKey)),
      endpointOverride = None,
      commonClientConfig = None
    ).pipe(ZLayer.succeed)

  final val layers = ZLayer.make[Mailer](
    NettyHttpClient.default,
    myAwsConfig,
    AwsConfig.configured(),
    SesV2.live,
    SesMailer.layer
  )
