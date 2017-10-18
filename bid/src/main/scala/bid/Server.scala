package bid

import java.net.InetSocketAddress

import bid.client.LogicClient
import com.twitter.finagle.http.{Request, Response}
import com.twitter.finagle.{Http, Service, http}
import com.twitter.logging.Logger
import com.twitter.server.TwitterServer
import com.twitter.util.{Await, Future}
import common.rpc.model.{Candidate, LogicRequest}

object Server extends TwitterServer {

  val addr = flag("bind", new InetSocketAddress(8080), "bind address")

  def main() {
    val service: Service[Request, Response] = new BidService(log)

    val server = Http.serve(addr(), service)
    onExit { server.close() }
    Await.ready(server)
  }
}

class BidService(log: Logger) extends Service[Request, Response] {

  override def apply(request: Request): Future[Response] = {
    val candidate = Candidate("100", Some("200"))
    val timeStart = System.currentTimeMillis()

    Future {
      LogicClient.request("logic", LogicRequest(candidate)) match {
        case Right(logicResponse) =>
          log.info(s"time: ${System.currentTimeMillis() - timeStart}")
          val res = http.Response(http.Status.Ok)
          res.setContentString(s"price: ${logicResponse.price}")
          res
        case Left(e) =>
          val res = http.Response(http.Status.InternalServerError)
          res.setContentString(s"InternalServerError: ${e.getMessage}")
          res
      }
    }
  }
}