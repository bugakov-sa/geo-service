package testtask.ref.impl

import testtask.entity.{ZoneData, ZoneKey}
import testtask.ref.ZonesRef

class InMemoryZonesRef(private val data: Map[ZoneKey, ZoneData]) extends ZonesRef {

  override def select(key: ZoneKey): Option[ZoneData] = data.get(key)

  override def keys: Iterable[ZoneKey] = data.keys
}