package fiuba.fp

import fiuba.fp.models.DataSetRow

import scala.util.Try

object Validator {

  def validate(id: String, date: String, open: String, high: String, low: String, last: String, close: String, dif: String, curr: String, OVol: String, ODif: String, opVol: String, unit: String, dollarBN: String, dollarItau: String, wDiff: String) : Option[DataSetRow] = {
    Try(DataSetRow(id.toInt, date, tryToDouble(open), tryToDouble(high), tryToDouble(low), last.toDouble, close.toDouble, dif.toDouble, stringSizeValidator(curr, 1), tryToInt(OVol), tryToInt(ODif), tryToInt(opVol), stringSizeValidator(unit, 4), dollarBN.toDouble, dollarItau.toDouble, wDiff.toDouble)).toOption
  }

  def stringSizeValidator(s: String, i: Int) : Option[String] = {
    s match {
      case s if(s.length <= i) => Some(s)
      case _ => None
    }
  }

  def tryToInt(s: String) : Option[Int] = Try(s.toInt).toOption

  def tryToDouble(s: String) : Option[Double] = Try(s.toDouble).toOption
}