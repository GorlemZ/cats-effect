name := "homeworks"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies += "org.typelevel"         %% "cats-core"   % "1.6.0"
libraryDependencies += "org.typelevel"         %% "cats-effect" % "1.3.0"
libraryDependencies += "io.estatico"           %% "newtype"     % "0.4.3"
libraryDependencies += "io.monix"              %% "monix"       % "3.0.0-RC2"
libraryDependencies += "com.github.daddykotex" %% "courier"     % "2.0.0"
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % "0.8.0"

scalacOptions += "-language:higherKinds"
