trait UsersDao {

  import Entity._

  def update(id: Long, newData: Point): Option[Point] = None

  def delete(id: Long): Option[Point] = None

  def select(id: Long): Option[Point] = None
}