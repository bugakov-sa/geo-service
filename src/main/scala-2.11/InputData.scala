import Entity.{ZoneData, ZoneKey}

object InputData {
  def loadZones = new InMemoryZonesRef(Map[ZoneKey, ZoneData]((ZoneKey(1, 1) -> ZoneData(1))))

  def loadUsers(usersDao: UsersDao) = {}
}
