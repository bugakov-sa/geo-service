import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import spray.json._

object Controller {

  import Entity._

  final case class UserUpdatedResponse(userId: Long, oldPoint: Option[Point], newPoint: Point)

  final case class UserDeletedResponse(userId: Long, point: Point)

  final case class UserSelectedResponse(userId: Long, point: Point)

  final case class TestLocationResponse(answer: String)

  final case class ZoneStatResponse(count: Long)

  val NOT_FOUND_RESPONSE = (StatusCodes.NotFound, "Not found")
}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  import Controller._
  import Entity._

  implicit val userDataFormat = jsonFormat2(Point)
  implicit val userUpdatedFormat = jsonFormat3(UserUpdatedResponse)
  implicit val userDeletedFormat = jsonFormat2(UserDeletedResponse)
  implicit val userSelectedFormat = jsonFormat2(UserSelectedResponse)
  implicit val testLocationFormat = jsonFormat1(TestLocationResponse)
  implicit val zoneStatFormat = jsonFormat1(ZoneStatResponse)
}

class Controller(usersDao: UsersDao, zonesStat: ZonesStat, testLocationService: TestLocationService
                ) extends Directives with JsonSupport {

  import Controller._
  import Entity._

  val route =
    path("users" / "update") {
      get {
        parameters('id.as[Long], 'lat.as[Float], 'lon.as[Float]) { (userId, lat, lon) =>
          val newPoint = Point(lat, lon)
          val oldPoint = usersDao.update(userId, newPoint)
          complete(UserUpdatedResponse(userId, oldPoint, newPoint))
        }
      }
    } ~ path("users" / "delete") {
      get {
        parameters('id.as[Long]) { userId =>
          complete(
            usersDao.delete(userId) match {
              case None => NOT_FOUND_RESPONSE
              case Some(point) => UserDeletedResponse(userId, point)
            }
          )
        }
      }
    } ~ path("users" / "select") {
      get {
        parameters('id.as[Long]) { userId => complete(
          usersDao.select(userId) match {
            case None => NOT_FOUND_RESPONSE
            case Some(point) => UserSelectedResponse(userId, point)
          })
        }
      }
    } ~ path("users" / "location") {
      get {
        parameters('id.as[Long], 'lat.as[Float], 'lon.as[Float]) { (userId, lat, lon) => complete(
          testLocationService.test(userId, Point(lat, lon)) match {
            case None => NOT_FOUND_RESPONSE
            case Some(true) => TestLocationResponse("Near")
            case Some(false) => TestLocationResponse("Away")
          })
        }
      }
    } ~ path("zones" / "count") {
      get {
        parameters('lat.as[Float], 'lon.as[Float]) { (lat, lon) => complete(
          zonesStat.count(Point(lat, lon)) match {
            case None => NOT_FOUND_RESPONSE
            case Some(count) => ZoneStatResponse(count)
          })
        }
      }
    }
}