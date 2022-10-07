import sbt._

object Dependencies {

  object V {
    val osLib      = "0.8.1"
    val zio        = "2.0.2"
    val zioAws     = "5.17.280.1"
    val zioConfig  = "3.0.2"
    val zioLogging = "2.1.2"
    val prelude    = "1.0.0-RC16"
  }

  val core = Seq[ModuleID](
    "com.lihaoyi" %% "os-lib"       % V.osLib,
    "dev.zio"     %% "zio"          % V.zio,
    "dev.zio"     %% "zio-prelude"  % V.prelude,
    "dev.zio"     %% "zio-test"     % V.zio % Test,
    "dev.zio"     %% "zio-test-sbt" % V.zio % Test
  )

  val awsSes = core ++ Seq[ModuleID](
    "dev.zio" %% "zio-aws-core"        % V.zioAws,
    "dev.zio" %% "zio-aws-netty"       % V.zioAws,
    "dev.zio" %% "zio-aws-ses"         % V.zioAws,
    "dev.zio" %% "zio-aws-sesv2"       % V.zioAws,
    "dev.zio" %% "zio-config"          % V.zioConfig % Test,
    "dev.zio" %% "zio-config-yaml"     % V.zioConfig % Test,
    "dev.zio" %% "zio-config-typesafe" % V.zioConfig % Test
  )

  object Testing {
    val framework = new TestFramework("zio.test.sbt.ZTestFramework")
  }
}
