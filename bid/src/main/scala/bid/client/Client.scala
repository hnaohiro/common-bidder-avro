package bid.client

import com.sksamuel.avro4s.RecordFormat
import org.apache.avro.Protocol
import org.apache.avro.generic.GenericData
import org.apache.avro.ipc.{Requestor, Transceiver}

import scala.util.control.Exception.allCatch

trait Client[REQUEST, RESPONSE] {

  protected val protocol: Protocol
  protected val receiver: Transceiver
  protected val requestor: Requestor

  protected val requestFormat: RecordFormat[REQUEST]
  protected val responseFormat: RecordFormat[RESPONSE]

  def request(messageName: String, content: REQUEST): Either[Throwable, RESPONSE] = {
    val req = requestFormat.to(content)
    val res = allCatch either requestor.request(messageName, req)
    res.map(_.asInstanceOf[GenericData.Record])
      .map(responseFormat.from(_))
  }

  def close(): Unit = receiver.close()
}
