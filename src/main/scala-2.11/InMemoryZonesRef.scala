import Entity.{ZoneData, ZoneKey}

class InMemoryZonesRef(private val data: Map[ZoneKey, ZoneData]) extends ZonesRef {

  override def select(key: ZoneKey): Option[ZoneData] = data.get(key)

  override def keys: Iterable[ZoneKey] = data.keys
}