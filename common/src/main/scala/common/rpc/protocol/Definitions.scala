package common.rpc.protocol

import com.sksamuel.avro4s.AvroSchema
import common.rpc.model.{LogicRequest, LogicResponse}
import org.apache.avro.Protocol

object Definitions {

  val packageName = "common"

  val logicProtocol: Protocol = {
    val protocol = new Protocol("BidCommunication", packageName)
    val message = protocol.createMessage(
      "logic",
      "",
      null,
      AvroSchema[LogicRequest],
      AvroSchema[LogicResponse],
      Protocol.SYSTEM_ERRORS
    )
    protocol.getMessages.put("logic", message)
    protocol
  }
}
