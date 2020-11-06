package fiuba.fp.models

import java.time.LocalDateTime

import fiuba.fp.Validator

case class DataSetRow(
                     id: Int,
                     date: LocalDateTime,
                     open: Option[Double],
                     high: Option[Double],
                     low: Option[Double],
                     last: Double,
                     close: Double,
                     diff: Double,
                     // 1 caracter como máximo.
                     curr: String,
                     OVol: Option[Int],
                     Odiff: Option[Int],
                     OpVol: Option[Int],
                     // 4 caracteres como máximo.
                     unit: String,
                     dollarBN: Double,
                     dollarItau: Double,
                     wDiff: Double
                     )

object DataSetRow {
   def convertToDataSetRow(line: String): Option[DataSetRow] ={
    line match {
          case s"$id, $date, $open, $high, $low ,$last ,$close , $diff , $curr, $OVol, $ODiff ,$OpVol, $unit" +
               s"$dollarBN, $dollarItau, $wDiff" => {
               Validator.validate(id, date, open, high, low, last, close, diff, curr, OVol, ODiff,OpVol,
               unit, dollarBN, dollarItau, wDiff)
      }
      case _ => None
    }
  }
}

