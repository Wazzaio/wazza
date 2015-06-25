lazy val appName = "Wazza"

lazy val appVersion = "alpha"

scalaVersion := "2.10.5"

lazy val dependencies = Seq(
  cache,
  filters,
  ws,
  "com.amazonaws" % "aws-java-sdk" % "1.10.2",
  "com.google.inject" % "guice" % "4.0",
  "com.typesafe.akka" %% "akka-actor" % "2.3.11",
  "com.typesafe.akka" %% "akka-slf4j" % "2.3.11",
  "com.tzavellas" %% "sse-guice" % "0.7.2",
  "commons-codec" % "commons-codec" % "1.10",
  "commons-validator" % "commons-validator" % "1.4.1",
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.mongodb" %% "casbah-commons" % "2.8.1",
  "org.mongodb" %% "casbah" % "2.8.1",
  "org.webjars" % "angular-chart.js" % "0.7.1",
  "org.webjars" % "angular-local-storage" % "0.1.5",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",
  "org.webjars" % "angular-ui-router" % "0.2.15",
  "org.webjars" % "angularjs" % "1.4.0",
  "org.webjars" % "bootstrap" % "3.3.5",
  "org.webjars" % "chartjs" % "1.0.2",
  "org.webjars" % "font-awesome" % "4.3.0-2",
  "org.webjars" % "jquery" % "1.11.3",
  "org.webjars" % "momentjs" % "2.10.3",
  "org.webjars" % "numeral-js" % "1.5.3-1",
  "org.webjars" % "underscorejs" % "1.8.3",
  "org.webjars" %% "webjars-play" % "2.3.0-3"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

resolvers ++= Seq[Resolver](
    DefaultMavenRepository,
    Classpaths.typesafeReleases,
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    Classpaths.sbtPluginReleases,
    "Eclipse repositories" at "https://repo.eclipse.org/service/local/repositories/egit-releases/content/",
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
  )

pipelineStages := Seq(closure, cssCompress, htmlMinifier, imagemin, gzip) //sbt-compat

lazy val mySettings = Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls", "-language:postfixOps", "-optimize", "-Xlint", "-Ywarn-adapted-args" )//, "-Xfatal-warnings")

lazy val common = Project("common", file("modules/common"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val dashboard = Project("dashboard", file("modules/dashboard"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(user, application)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val user = Project("user", file("modules/user"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(security, persistence, common, payments)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val application = Project("application", file("modules/application"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(persistence, security, aws, user, notifications)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val security = Project("security", file("modules/security"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(persistence)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val aws = Project("aws", file("modules/aws"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val api = Project("api", file("modules/api"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(security, aws, user, application, payments, common)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val persistence = Project("persistence", file("modules/persistence"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(common)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val analytics = Project("analytics",file("modules/analytics"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(user, application, persistence, security, payments)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val notifications = Project("notifications",file("modules/notifications"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(common)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

lazy val payments = Project("payments",file("modules/payments"))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .dependsOn(common)
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

// Root
lazy val home = Project(appName, file("."))
  .enablePlugins(play.PlayScala).enablePlugins(SbtWeb)
  .aggregate(dashboard,
    user,
    application,
    security,
    aws,
    api,
    persistence,
    analytics,
    common,
    notifications,
    payments)
  .dependsOn(dashboard,
    user,
    application,
    security,
    aws,
    api,
    persistence,
    analytics,
    common,
    notifications,
    payments
  )
  .settings(scalacOptions ++= mySettings, version := appVersion, libraryDependencies ++= dependencies)

sources in doc in Compile := List()

