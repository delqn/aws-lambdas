javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

retrieveManaged := true

lazy val root = (project in file(".")).
  settings(
    name := "aws-lambda-get-stock-price",
    version := "1.0",
    scalaVersion := "2.12.0",
    retrieveManaged := true,
    libraryDependencies ++= {
      Seq(
        "com.amazonaws" % "aws-lambda-java-core"   % "1.1.0",
        "com.amazonaws" % "aws-lambda-java-events" % "1.3.0"
      )
    }
  )

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}