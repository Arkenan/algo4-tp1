package fiuba.fp

import java.time.LocalDateTime

import fiuba.fp.models.DataSetRow

case class Validator() {


  def validate(id: String, date: String, open: String, high: String, low: String, last: String, close: String,
               diff: String, curr: String, oVol: String, oDiff: String, opVol: String, unit: String, dollarBN: String,
               dollarItau: String, wDiff: String): Option[DataSetRow] = {
    Option(DataSetRow(id = 1, date = LocalDateTime.of(2020, 10, 10, 0, 0),
      open = Option[Double](700.0), high = Option[Double](200.0), low = Option[Double](150.0),
      last = 175, close = 3.14, diff = -750.3, curr = "A",
      OVol = Option[Int](2), Odiff = Option[Int](3), OpVol = Option[Int](0),
      unit = "US$", dollarBN = 170, dollarItau = 170.2, wDiff = 100000.0))
  }
}
