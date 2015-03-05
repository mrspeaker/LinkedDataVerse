enablePlugins(ScalaJSPlugin)

name := "LinkedDataVerse"

scalaVersion := "2.11.5"

scalaJSStage in Global := FastOptStage

resolvers += bintray.Opts.resolver.repo("denigma", "denigma-releases")

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"
libraryDependencies += "org.scalajs" %%% "threejs" % "0.0.68-0.1.4"

// jsDependencies += ProvidedJS / "jsonld.js" commonJSName "jsonld"
