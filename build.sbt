import play.Project._

name := "Wazza"

version := "pre-alpha"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

// Projects
lazy val home = project.in(file(".")).dependsOn(editor)

lazy val editor = play.Project("editor", "pre-alpha", path = file("modules/editor")).settings().dependsOn(stores)

lazy val stores = play.Project("stores", "pre-alpha", path = file("modules/stores"))

play.Project.playScalaSettings
