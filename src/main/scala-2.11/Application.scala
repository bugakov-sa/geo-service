import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.scalalogging.Logger
import scala.io.{Source, StdIn}

case class Configuration(host: String, port: Int)

object Configuration {
  def read: Configuration = {
    val src = Source.fromFile(Paths.get(
      System.getProperty("user.dir"),
      "..",
      "conf",
      "settings.txt"
    ).toFile)
    val map = src.getLines.map(line => (
      line.split("=")(0).trim,
      line.split("=")(1).trim)
    ).toMap
    src.close
    Configuration(map("host"), map("port") toInt)
  }
}

case class InsertUserRequest(lat: Double, lon: Double)

case class UpdateUserRequest(id: Long, lat: Double, lon: Double)

case class DeleteUserRequest(id: Long)

object Application extends App {

  private val log = Logger("Application")

  val conf = Configuration.read

  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()

  implicit val executionContext = system.dispatcher

  val route =
    path("users" / "insert") {
      get {
        parameters('lat.as[Double], 'lon.as[Double]).as(InsertUserRequest) { rq =>
          complete(rq.toString)
        }
      }
    } ~ path("users" / "update") {
      get {
        parameters('id.as[Long], 'lat.as[Double], 'lon.as[Double]).as(UpdateUserRequest) { rq =>
          complete(rq.toString)
        }
      }
    } ~ path("users" / "delete") {
      get {
        parameters('id.as[Long]).as(DeleteUserRequest) { rq =>
          complete(rq.toString)
        }
      }
    }

  val bindingFuture = Http().bindAndHandle(route, conf.host, conf.port)

  log.info("Server online at http://{}:{}/", conf.host, conf.port.toString)
  log.info("Press ENTER to stop...")

  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}