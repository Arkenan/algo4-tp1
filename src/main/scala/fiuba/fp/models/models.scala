package fiuba.fp.models

import java.time.LocalDateTime

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