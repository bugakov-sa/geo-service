package testtask.service

import java.util.concurrent.atomic.AtomicLong
import java.util.function.Consumer

import testtask.entity._
import testtask.ref.ZonesRef

class ZonesStatService(zones: ZonesRef) extends Consumer[UserEvent] {

  private val stat = zones.keys.map(k => (k, new AtomicLong(0))).toMap

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