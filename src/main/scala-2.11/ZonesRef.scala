import Entity.{ZoneKey, ZoneData}

trait ZonesRef {

  def select(key: ZoneKey): Option[ZoneData]

  def keys: Iterable[ZoneKey]
}