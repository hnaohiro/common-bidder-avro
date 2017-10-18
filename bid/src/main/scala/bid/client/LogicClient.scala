package bid.client

import java.net.InetSocketAddress

import com.sksamuel.avro4s.RecordFormat
import common.rpc.model.{LogicRequest, LogicResponse}
import common.rpc.protocol.Definitions
import org.apache.avro.Protocol
import org.apache.avro.ipc.generic.GenericRequestor
import org.apache.avro.ipc.{NettyTransceiver, Requestor, Transceiver}

object LogicClient extends Client[LogicRequest, LogicResponse] {

  val candidateHost = "localhost"
  val candidatePort = 9090

  override protected val protocol: Protocol = Definitions.logicProtocol

  override protected val receiver: Transceiver = new NettyTransceiver(new InetSocketAddress(candidateHost, candidatePort))
  override protected val requestor: Requestor   = new GenericRequestor(protocol, receiver)

  override protected val requestFormat: RecordFormat[LogicRequest] = RecordFormat[LogicRequest]
  override protected val responseFormat: RecordFormat[LogicResponse] = RecordFormat[LogicResponse]
}
