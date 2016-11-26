object Entity {

  case class UserData(lat: Float, lon: Float)

  case class ZoneKey(tileX: Int, tileY: Int)

  case class ZoneData(distanceError: Float)

  case class ZoneRecord(key: ZoneKey, data: ZoneData)

  abstract trait UserEvent

  case class UserCreatedEvent(userId: Long, userData: UserData) extends UserEvent

  case class UserUpdatedEvent(userId: Long, oldData: UserData, newData: UserData) extends UserEvent

  case class UserDeletedEvent(userId: Long, userData: UserData) extends UserEvent

}
