package logic

import java.io.IOException
import java.net.InetSocketAddress

import common.rpc.protocol.Definitions
import org.apache.avro.Protocol
import org.apache.avro.generic.GenericData
import org.apache.avro.ipc.NettyServer
import org.apache.avro.ipc.generic.GenericResponder

object Server extends App with AvroProtocolReader {

  override val service = Service

  val server = new NettyServer(responder, new InetSocketAddress(9090))
  server.start()
  println(s"Starting the netty server port: ${server.getPort}")
  server.join()
}

trait AvroProtocolReader {
  val service: Service
  lazy val protocol: Protocol = Definitions.logicProtocol
  lazy val responder: GenericResponder = new GenericResponder(protocol) {
    override def respond (message: Protocol#Message, request: Any): Object = {
      if (message.getName.equals("logic")) {
        val r: GenericData.Record = request.asInstanceOf[GenericData.Record]
        service.extract(r)
      } else {
        throw new IOException(s"it doesn't know message:${message.getName}")
      }
    }
  }
}