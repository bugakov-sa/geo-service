package testtask.conf

import java.nio.file.Path

import com.typesafe.scalalogging.Logger
import testtask.dao.UsersDao
import testtask.entity._
import testtask.ref.impl.InMemoryZonesRef

import scala.io.Source

object InputData {
  private val log = Logger("InputData")

  val DELIMITER = ","

  def loadZones(zonesFilePath: Path) = {
    log.info("Reading zones")
    var map = Map[ZoneKey, ZoneData]()
    val src = Source.fromFile(zonesFilePath.toFile)
    for (line <- src.getLines) {
      try {
        val cells = line.split(DELIMITER)
        val lat = cells(0) toInt
        val lon = cells(1) toInt
        val distanceError = cells(2) toDouble

        map = map +
          (ZoneKey(lat, lon, false) -> ZoneData(distanceError)) +
          (ZoneKey(lat, lon, true) -> ZoneData(distanceError))
      }
      catch {
        case err: Throwable =>
          log.error("Error at reading zones " + err.getMessage, err)
      }
    }
    log.info("Read {} zones", map.size)
    src.close
    new InMemoryZonesRef(map)
  }

  def loadUsers(usersFilePath: Path, usersDao: UsersDao) = {
    log.info("Reading users")
    var usersCount = 0L
    val src = Source.fromFile(usersFilePath.toFile)
    for (line <- src.getLines) {
      try {
        val cells = line.split(DELIMITER)
        usersDao.update(cells(0) toInt, Point(cells(1) toDouble, cells(2) toDouble))
        usersCount += 1
      }
      catch {
        case err: Throwable =>
          log.error("Error at reading users " + err.getMessage, err)
      }
    }
    log.info("Read {} users", usersCount)
    src.close
  }
}
