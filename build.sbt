name := """iadvize-pomme-api"""
organization := "com.zappy.pizza"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
libraryDependencies += "org.jsoup" % "jsoup" % "1.11.1"
libraryDependencies += "org.reactivemongo" %% "play2-reactivemongo" % "0.12.6-play26"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.zappy.pizza.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.zappy.pizza.binders._"
