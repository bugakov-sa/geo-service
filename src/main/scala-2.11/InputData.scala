import java.nio.file.Path

import Entity.{Point, ZoneData, ZoneKey}
import com.typesafe.scalalogging.Logger

import scala.collection.mutable
import scala.io.Source

object InputData {
  private val log = Logger("InputData")

  def loadZones(zonesFilePath: Path) = {
    val map = new mutable.HashMap[ZoneKey, ZoneData]()
    val src = Source.fromFile(zonesFilePath.toFile)
    for (line <- src.getLines) {
      try {
        val cells = line.split(";")
        map(ZoneKey(cells(0) toInt, cells(1) toInt)) = ZoneData(cells(2) toFloat)
      }
      catch {
        case err: Throwable =>
          log.error("Error at reading zones " + err.getMessage, err)
      }
    }
    src.close
    new InMemoryZonesRef(map.toMap)
  }

  def loadUsers(usersFilePath: Path, usersDao: UsersDao) = {
    val src = Source.fromFile(usersFilePath.toFile)
    for (line <- src.getLines) {
      try {
        val cells = line.split(";")
        usersDao.update(cells(0) toInt, Point(cells(1) toFloat, cells(2) toFloat))
      }
      catch {
        case err: Throwable =>
          log.error("Error at reading users " + err.getMessage, err)
      }
    }
    src.close
  }
}
