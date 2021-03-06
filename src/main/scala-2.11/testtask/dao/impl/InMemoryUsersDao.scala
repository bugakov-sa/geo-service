package testtask.dao.impl

import java.util.concurrent.ConcurrentHashMap

import testtask.entity.Point
import testtask.dao.UsersDao

trait InMemoryUsersDao extends UsersDao {

  private val data = new ConcurrentHashMap[Long, Point]()

  override def update(id: Long, newPoint: Point) = Option(data.put(id, newPoint))

  override def delete(id: Long) = Option(data.remove(id))

  override def select(id: Long) = Option(data.get(id))
}