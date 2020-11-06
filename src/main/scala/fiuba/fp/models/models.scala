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
                     ODiff: Option[Int],
                     OpVol: Option[Int],
                     // 4 caracteres como máximo.
                     unit: String,
                     dollarBN: Double,
                     dollarItau: Double,
                     wDiff: Double
                     )

object DataSetRow {
   def convertToDataSetRow(line: String): Option[DataSetRow] ={
      val validator = Validator
      line match {
          case s"${id},${date},${open},${high},${low},${last},${close},${diff},${curr},${oVol},${oDiff},${opVol},${unit},${dollarBN},${dollarItau},${wDiff}" => {
                validator.validate(id, date, open, high, low, last, close, diff, curr, oVol, oDiff,opVol, unit, dollarBN, dollarItau, wDiff)
          }
          case _ =>  None
    }
  }
}
