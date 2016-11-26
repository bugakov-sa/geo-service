abstract trait ZonesRef {

  import Entity._

  def select(key: ZoneKey): Option[ZoneData]

  def keys: Iterable[ZoneKey]
}