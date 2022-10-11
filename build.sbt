import Dependencies.Testing

ThisBuild / scalaVersion           := "3.2.0"
ThisBuild / organization           := "dev.rednova"
ThisBuild / organizationName       := "Rednova"
ThisBuild / versionScheme          := Some("early-semver")
ThisBuild / versionPolicyIntention := Compatibility.BinaryAndSourceCompatible

lazy val noPublish = Seq(
  publish         := {},
  publishLocal    := {},
  publishArtifact := false,
  publish / skip  := true
)

lazy val commonSettings = Seq(
  startYear                      := Some(2022),
  licenses                       += ("Apache-2.0", new URL(
    "https://www.apache.org/licenses/LICENSE-2.0.txt"
  )),
  resolvers                      += "Apache public" at "https://repository.apache.org/content/groups/public/",
  scalafmtOnCompile              := true,
  Compile / scalacOptions       ++= Seq(
    "-encoding",
    "UTF-8",
    "-Xfatal-warnings",
    "-feature",
    "-deprecation",
    "-indent",
    "-rewrite"
  ),
  Compile / doc / scalacOptions ++= Seq(
    "-no-link-warnings" // Suppresses problems with Scaladoc links
  ),
  testFrameworks                 := Seq(Testing.framework)
)

lazy val root = project
  .in(file("."))
  .settings(noPublish)
  .settings(name := "mailer")
  .aggregate(
    `mailer-core`,
    `mailer-aws-ses`
  )

lazy val `mailer-core` = project
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name                     := "mailer-core",
    libraryDependencies     ++= Dependencies.core,
    Test / parallelExecution := false,
    Test / fork              := true
  )

/* lazy val `mailer-smtp` = ... */

lazy val `mailer-aws-ses` = project
  .in(file("aws-ses"))
  .dependsOn(`mailer-core`)
  .settings(commonSettings)
  .settings(
    name                     := "mailer-asw-ses",
    libraryDependencies     ++= Dependencies.awsSes,
    Test / parallelExecution := false,
    Test / fork              := true
  )

lazy val `mailer-examples` = project
  .in(file("examples"))
  .dependsOn(`mailer-core`, `mailer-aws-ses`)
  .settings(noPublish)
  .settings(
    name                     := "mailer-examples",
    Test / parallelExecution := false,
    Test / fork              := true
  )
