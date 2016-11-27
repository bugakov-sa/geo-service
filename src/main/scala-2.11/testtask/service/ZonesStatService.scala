package testtask.service

import java.util.concurrent.atomic.AtomicLong
import java.util.function.Consumer

import testtask.entity._
import testtask.ref.ZonesRef

class ZonesStatService(zones: ZonesRef) extends Consumer[UserEvent] {

  private val stat = init(zones)

  private def init(zones: ZonesRef) = {
    var res = scala.collection.immutable.HashMap[ZoneKey, AtomicLong]()
    for (key <- zones.keys) {
      res = res + (key -> new AtomicLong(0))
    }
    res
  }

  override def accept(event: UserEvent): Unit = event match {
    case UserUpdatedEvent(userId, oldPoint, newPoint) =>
      if (oldPoint.isDefined)
        stat.get(zoneKey(oldPoint.get)).foreach(_.decrementAndGet())
      stat.get(zoneKey(newPoint)).foreach(_.incrementAndGet())
    case UserDeletedEvent(userId, point) =>
      stat.get(zoneKey(point)).foreach(_.decrementAndGet())
  }

  def count(point: Point) = stat.get(zoneKey(point)).map(_.get)
}