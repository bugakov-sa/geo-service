package testtask.ref

import testtask.entity.{ZoneData, ZoneKey}

trait ZonesRef {

  def select(key: ZoneKey): Option[ZoneData]

  def keys: Iterable[ZoneKey]
}
