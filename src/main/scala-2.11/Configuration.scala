import java.nio.file.Paths

import scala.io.Source

case class Configuration(host: String, port: Int)

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
    val src = Source.fromFile({
      if (intellijIdeaPath.toFile.exists)
        intellijIdeaPath
      else
        assemblyPath
    }.toFile)
    val map = src.getLines.map(line => (
      line.split("=")(0).trim,
      line.split("=")(1).trim)
    ).toMap
    src.close
    Configuration(map("host"), map("port") toInt)
  }
}