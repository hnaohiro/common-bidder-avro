package logic

import com.sksamuel.avro4s._
import common.rpc.model._
import org.apache.avro.generic.GenericRecord

object Service extends Service

trait Service {

  val requestFormat: RecordFormat[LogicRequest] = RecordFormat[LogicRequest]
  val responseFormat: RecordFormat[LogicResponse] = RecordFormat[LogicResponse]

  def extract(record: GenericRecord): GenericRecord = {
    val request: LogicRequest = requestFormat.from(record)
    val candidate = request.candidate
    val price = candidate.p1.toInt + candidate.p2.map(_.toInt).getOrElse(0)
    val response = LogicResponse(price)
    responseFormat.to(response)
  }
}
