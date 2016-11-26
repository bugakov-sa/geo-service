import java.io.File
import java.nio.file.{Path, Paths}

import com.typesafe.scalalogging.Logger

import scala.io.Source

case class Configuration(
                          host: String,
                          port: Int,
                          usersFilePath: Path,
                          zonesFilePath: Path
                        )

object Configuration {

  private val log = Logger[Configuration]

  private val PORT = "port"
  private val HOST = "host"
  private val USERS_FILE = "usersFile"
  private val ZONES_FILE = "zonesFile"

  def read: Configuration = {

    val settings = readSettings

    checkSettings(settings)

    Configuration(
      settings(HOST),
      settings(PORT) toInt,
      Paths.get(settings(USERS_FILE)),
      Paths.get(settings(ZONES_FILE))
    )
  }

  private def findSettingsFile = {
    val files = Array("settings.txt", Paths.get("conf", "ide", "settings.txt").toString).
      map(path => (path, new File(path).exists)).
      filter(p => p._2).
      map(p => p._1)
    if (files.isEmpty) {
      throw new Exception("Not found settings.txt")
    }
    files(0)
  }

  private def readSettings = {
    val src = Source.fromFile(findSettingsFile)
    val settings = (for (kv <- src.getLines.map(ln => ln.split("=")) if kv.length == 2)
      yield (kv(0).trim, kv(1).trim)).toMap
    src.close

    log.info("Settings:")
    for ((k, v) <- settings) {
      log.info("{} = {}", k, v)
    }

    settings
  }

  private def checkSettings(settings: Map[String, String]) = {
    if (!settings.contains(HOST)) {
      throw new Exception("Not defined " + HOST)
    }
    if (!settings.contains(PORT)) {
      throw new Exception("Not defined " + PORT)
    }
    try {
      settings(PORT).toInt
    }
    catch {
      case e: Exception => {
        throw new Exception("Cannot parse " + PORT + " from " + settings(PORT))
      }
    }
    for (filePath <- List(USERS_FILE, ZONES_FILE)) {
      if (!settings.contains(filePath)) {
        throw new Exception("Not defined " + filePath)
      }
      if (!Paths.get(settings(filePath)).toFile.exists) {
        throw new Exception("Not found " + filePath + " " + settings(filePath))
      }
    }
  }
}