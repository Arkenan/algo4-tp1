package fiuba.fp.models

import java.time.LocalDateTime

import fiuba.fp.Validator
import fiuba.fp.exceptions.SizeNotMatchException

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
   def convertToDataSetRow(line: String): Either[Throwable, DataSetRow] ={
      line match {
          case s"${id},${date},${open},${high},${low},${last},${close},${diff},${curr},${oVol},${oDiff},${opVol},${unit},${dollarBN},${dollarItau},${wDiff}" => {
            Validator.validate(List(id, date, open, high, low, last, close, diff, curr, oVol, oDiff,opVol, unit, dollarBN, dollarItau, wDiff))
          }
          case _ => Left(new SizeNotMatchException("No hay campos suficientes para parsear la fila: " + line))
    }
  }
}
