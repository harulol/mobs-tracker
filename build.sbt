ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.1.1-RC1"

lazy val root = (project in file("."))
    .settings(
        name := "TrialPlugin",
        idePackagePrefix := Some("dev.hawu.plugins.trial"),
        resolvers ++= Seq(
            Resolver.mavenLocal,
        ),
        libraryDependencies ++= Seq(
            "dev.hawu.plugins" % "hikari-library" % "1.2.4-SNAPSHOT",
            "org.bukkit" % "bukkit" % "1.8-R0.1-SNAPSHOT",
        ),
        artifactName := { (_, _, artifact) => s"${name.value}-${version.value}.${artifact.extension}" },
    )
