import java.nio.file.{Path, Paths}

import scala.io.Source

case class Configuration(host: String, port: Int, usersFilePath: Path, zonesFilePath: Path)

object Configuration {
  val intellijIdeaPath = Paths.get(
    System.getProperty("user.dir"),
    "conf",
    "settings.txt"
  )
  val assemblyPath = Paths.get(
    System.getProperty("user.dir"),
    "..",
    "conf",
    "settings.txt"
  )

  def read: Configuration = {
    val settingsPath: Path = {
      if (intellijIdeaPath.toFile.exists)
        intellijIdeaPath
      else
        assemblyPath
    }
    val src = Source.fromFile(settingsPath.toFile)
    val map = src.getLines.map(line => (
      line.split("=")(0).trim,
      line.split("=")(1).trim)
    ).toMap
    src.close
    Configuration(
      map("host"),
      map("port") toInt,
      Paths.get(settingsPath.getParent.toString, map("usersFile")),
      Paths.get(settingsPath.getParent.toString, map("zonesFile"))
    )
  }
}