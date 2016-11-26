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

  final case class UserCreatedResponse(userId: Long, userData: UserData)

  final case class UserUpdatedResponse(userId: Long, oldData: UserData, newData: UserData)

  final case class UserDeletedResponse(userId: Long)

  final case class UserSelectedResponse(userId: Long, userData: UserData)

}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  import Controller._
  import Entity._

  implicit val userDataFormat = jsonFormat2(UserData)
  implicit val userCreatedFormat = jsonFormat2(UserCreatedResponse)
  implicit val userUpdatedFormat = jsonFormat3(UserUpdatedResponse)
  implicit val userDeletedFormat = jsonFormat1(UserDeletedResponse)
  implicit val userSelectedFormat = jsonFormat2(UserSelectedResponse)
}

class Controller(usersDao: UsersDao) extends Directives with JsonSupport {

  import Controller._
  import Entity._

  val route =
    path("users" / "create") {
      get {
        parameters('lat.as[Float], 'lon.as[Float]).as(CreateUserRequest) { request =>
          val userData = UserData(request.lat, request.lon)
          val userId = usersDao.create(userData)
          complete((StatusCodes.OK, UserCreatedResponse(userId, userData)))
        }
      }
    } ~ path("users" / "update") {
      get {
        parameters('id.as[Long], 'lat.as[Float], 'lon.as[Float]).as(UpdateUserRequest) { request =>
          val userData = UserData(request.lat, request.lon)
          complete(
            usersDao.update(request.userId, userData) match {
              case None => (StatusCodes.NotFound, "Not found")
              case Some(oldData) => (StatusCodes.OK, UserUpdatedResponse(request.userId, oldData, userData))
            }
          )
        }
      }
    } ~ path("users" / "delete") {
      get {
        parameters('id.as[Long]).as(DeleteUserRequest) { request =>
          complete(
            usersDao.delete(request.userId) match {
              case None => (StatusCodes.NotFound, "Not found")
              case Some(oldData) => (StatusCodes.OK, UserDeletedResponse(request.userId))
            }
          )
        }
      }
    } ~ path("users" / "select") {
      get {
        parameters('id.as[Long]).as(SelectUserRequest) { request => complete(
          usersDao.select(request.userId) match {
            case None => (StatusCodes.NotFound, "Not found")
            case Some(userData) => (StatusCodes.OK, UserSelectedResponse(request.userId, userData))
          })
        }
      }
    }
}