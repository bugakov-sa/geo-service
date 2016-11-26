package testtask.dao

import testtask.entity.Point

trait UsersDao {

  def update(id: Long, point: Point): Option[Point] = None

  def delete(id: Long): Option[Point] = None

  def select(id: Long): Option[Point] = None
}
