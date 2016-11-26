import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import spray.json._

object Controller {

  import Entity._

  final case class CreateUserRequest(lat: Float, lon: Float)

  final case class UpdateUserRequest(userId: Long, lat: Float, lon: Float)

  final case class DeleteUserRequest(userId: Long)

  final case class SelectUserRequest(userId: Long)

  final case class StatRequest(lat: Float, lon: Float)

  final case class UserUpdatedResponse(userId: Long, oldPoint: Option[Point], newPoint: Point)

  final case class UserDeletedResponse(userId: Long, point: Point)

  final case class UserSelectedResponse(userId: Long, point: Point)

  final case class ZoneStatResponse(count: Long)

}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  import Controller._
  import Entity._

  implicit val userDataFormat = jsonFormat2(Point)
  implicit val userUpdatedFormat = jsonFormat3(UserUpdatedResponse)
  implicit val userDeletedFormat = jsonFormat2(UserDeletedResponse)
  implicit val userSelectedFormat = jsonFormat2(UserSelectedResponse)
  implicit val zoneStatResponse = jsonFormat1(ZoneStatResponse)
}

class Controller(usersDao: UsersDao, zonesStat: ZonesStat) extends Directives with JsonSupport {

  import Controller._
  import Entity._

  val route =
    path("users" / "update") {
      get {
        parameters('id.as[Long], 'lat.as[Float], 'lon.as[Float]).as(UpdateUserRequest) { request =>
          val newPoint = Point(request.lat, request.lon)
          val userId = request.userId
          val oldPoint = usersDao.update(userId, newPoint)
          complete((StatusCodes.OK, UserUpdatedResponse(userId, oldPoint, newPoint)))
        }
      }
    } ~ path("users" / "delete") {
      get {
        parameters('id.as[Long]).as(DeleteUserRequest) { request =>
          complete(
            usersDao.delete(request.userId) match {
              case None => (StatusCodes.NotFound, "Not found")
              case Some(point) => (StatusCodes.OK, UserDeletedResponse(request.userId, point))
            }
          )
        }
      }
    } ~ path("users" / "select") {
      get {
        parameters('id.as[Long]).as(SelectUserRequest) { request => complete(
          usersDao.select(request.userId) match {
            case None => (StatusCodes.NotFound, "Not found")
            case Some(point) => (StatusCodes.OK, UserSelectedResponse(request.userId, point))
          })
        }
      }
    } ~ path("zones" / "count") {
      get {
        parameters('lat.as[Float], 'lon.as[Float]).as(StatRequest) { request => complete(
          zonesStat.count(Point(request.lat, request.lon)) match {
            case None => (StatusCodes.NotFound, "Not found")
            case Some(count) => (StatusCodes.OK, ZoneStatResponse(count))
          })
        }
      }
    }
}