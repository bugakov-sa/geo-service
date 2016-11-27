package testtask.conf

import java.nio.file.Path

import com.typesafe.scalalogging.Logger
import testtask.dao.UsersDao
import testtask.entity.{Point, ZoneData, ZoneKey}
import testtask.ref.impl.InMemoryZonesRef

import scala.io.Source

object InputData {
  private val log = Logger("InputData")

  val DELIMITER = ","

  def loadZones(zonesFilePath: Path) = {
    log.info("Reading zones")
    val map = new scala.collection.mutable.HashMap[ZoneKey, ZoneData]()
    val src = Source.fromFile(zonesFilePath.toFile)
    for (line <- src.getLines) {
      try {
        val cells = line.split(DELIMITER)
        map(ZoneKey(cells(0) toInt, cells(1) toInt)) = ZoneData(cells(2) toDouble)
      }
      catch {
        case err: Throwable =>
          log.error("Error at reading zones " + err.getMessage, err)
      }
    }
    log.info("Read {} zones", map.size)
    src.close
    new InMemoryZonesRef(map.toMap)
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
