Object ApplicationBuild extends Build {

  val appName = "Wazza"

  val appVersion = "pre-alpha"

  lazy val dependencies = Seq(
    anorm,
    cache,
    "org.webjars" % "jquery" % "1.11.1",
    "com.google.inject" % "guice" % "3.0",
    "com.tzavellas" % "sse-guice" % "0.7.1",
    "org.webjars" % "webjars-play_2.10" % "2.2.2-1",
    "org.webjars" % "angularjs" % "1.2.25",
    "org.webjars" % "bootstrap" % "3.2.0",
    "commons-validator" % "commons-validator" % "1.4.0",
    "com.github.nscala-time" %% "nscala-time" % "1.0.0",
    "org.webjars" % "underscorejs" % "1.6.0-3",
    "com.amazonaws" % "aws-java-sdk" % "1.7.9",
    "org.mindrot" % "jbcrypt" % "0.3m",
    "commons-codec" % "commons-codec" % "1.9",
    "org.mongodb" % "casbah_2.10" % "2.7.0",
    "com.typesafe.akka" %% "akka-actor" % "2.2.3",
    "com.typesafe.akka" %% "akka-slf4j" % "2.2.3",
    "org.webjars" % "angular-ui-bootstrap" % "0.11.0-3",
    "org.webjars" % "angular-ui-router" % "0.2.11",
    "org.webjars" % "momentjs" % "2.8.3",
    "org.webjars" % "chartjs" % "1.0.1-beta.4",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2"
    )

libraryDependencies ++= dependencies

templatesImport += "org.bson.types.ObjectId"

templatesImport += "models.user._"

templatesImport += "controllers.user._"

lazy val mySettings = Seq(
  javaOptions in run += "-Dconfig.file=conf/dev/application_dev.conf",
  scalacOptions ++= Seq("-feature", "-language:reflectiveCalls"),
  scalacOptions ++= Seq("-feature", "-language:postfixOps")
  )

  // Projects
  lazy val home = (project in file(".")).enablePlugins(PlayScala)
  .aggregate(dashboardModule,
    userModule,
    applicationModule,
    securityModule,
    awsModule,
    apiModule,
    persistenceModule,
    recommendationModule,
    analyticsModule)
  .dependsOn(dashboardModule,
    userModule,
    applicationModule,
    securityModule,
    awsModule,
    apiModule,
    persistenceModule,
    recommendationModule,
    analyticsModule)
  .settings(mySettings: _*)

  lazy val dashboardModule = Project("dashboard", file("modules/DashboardModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(userModule, applicationModule).settings(mySettings: _*)

  lazy val userModule = Project("user", file("modules/UserModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(securityModule, persistenceModule).settings(mySettings: _*)

  lazy val applicationModule = Project("application", file("modules/ApplicationModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(securityModule, awsModule, userModule, persistenceModule).settings(mySettings: _*)

  lazy val securityModule = Project("security", file("modules/SecurityModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(persistenceModule).settings(mySettings: _*)

  lazy val awsModule = Project("aws", file("modules/AWSModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies).settings(mySettings: _*)

  lazy val apiModule = Project("api", file("modules/ApiModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(securityModule, awsModule, userModule, applicationModule, recommendationModule).settings(mySettings: _*)

  lazy val persistenceModule = Project("persistence", file("modules/PersistenceModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies).settings(mySettings: _*)

  lazy val recommendationModule = Project("recommendation", file("modules/RecommendationModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(userModule, applicationModule, persistenceModule).settings(mySettings: _*)

  lazy val analyticsModule = Project("analytics", file("modules/AnalyticsModule")).enablePlugins(play.PlayScala).settings(
    version := appVersion,
    libraryDependencies ++= dependencies
    ).dependsOn(userModule, applicationModule, persistenceModule, securityModule).settings(mySettings: _*)

}
