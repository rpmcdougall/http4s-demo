val Specs2Version = "4.1.0"
val CatsVersion            = "1.6.0"
val CirceVersion           = "0.11.1"
val CirceConfigVersion     = "0.6.1"
val DoobieVersion          = "0.6.0"
val EnumeratumVersion      = "1.5.13"
val EnumeratumCirceVersion = "1.5.19"
val H2Version              = "1.4.197"
val Http4sVersion          = "0.20.0-M5"
val LogbackVersion         = "1.2.3"
val ScalaCheckVersion      = "1.14.0"
val ScalaTestVersion       = "3.0.5"
val FlywayVersion          = "5.2.4"
val TsecVersion            = "0.1.0-M2"

resolvers += Resolver.sonatypeRepo("snapshots")


lazy val root = (project in file("."))
  .settings(
    organization := "com.rm",
    name := "http4s-demo",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.typelevel"         %% "cats-core"              % CatsVersion,
      "io.circe"              %% "circe-generic"          % CirceVersion,
      "io.circe"              %% "circe-literal"          % CirceVersion,
      "io.circe"              %% "circe-generic-extras"   % CirceVersion,
      "io.circe"              %% "circe-parser"           % CirceVersion,
      "io.circe"              %% "circe-java8"            % CirceVersion,
      "io.circe"              %% "circe-config"           % CirceConfigVersion,
      "org.tpolecat"          %% "doobie-core"            % DoobieVersion,
      "org.tpolecat"          %% "doobie-h2"              % DoobieVersion,
      "org.tpolecat"          %% "doobie-scalatest"       % DoobieVersion,
      "org.tpolecat"          %% "doobie-hikari"          % DoobieVersion,
      "com.beachape"          %% "enumeratum"             % EnumeratumVersion,
      "com.beachape"          %% "enumeratum-circe"       % EnumeratumCirceVersion,
      "com.h2database"        %  "h2"                     % H2Version,
      "org.http4s"            %% "http4s-blaze-server"    % Http4sVersion,
      "org.http4s"            %% "http4s-circe"           % Http4sVersion,
      "org.http4s"            %% "http4s-dsl"             % Http4sVersion,
      "ch.qos.logback"        %  "logback-classic"        % LogbackVersion,
      "org.flywaydb"          %  "flyway-core"            % FlywayVersion,
      "org.http4s"            %% "http4s-blaze-client"    % Http4sVersion     % Test,
      "org.scalacheck"        %% "scalacheck"             % ScalaCheckVersion % Test,
      "org.scalatest"         %% "scalatest"              % ScalaTestVersion  % Test,

      // Authentication dependencies
//      "io.github.jmcardon"    %% "tsec-common"            % TsecVersion,
//      "io.github.jmcardon"    %% "tsec-password"          % TsecVersion,
//      "io.github.jmcardon"    %% "tsec-mac"               % TsecVersion,
//      "io.github.jmcardon"    %% "tsec-signatures"        % TsecVersion,
//      "io.github.jmcardon"    %% "tsec-jwt-mac"           % TsecVersion,
//      "io.github.jmcardon"    %% "tsec-jwt-sig"           % TsecVersion,
//      "io.github.jmcardon"    %% "tsec-http4s"            % TsecVersion


    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )
