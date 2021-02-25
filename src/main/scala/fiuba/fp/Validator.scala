package fiuba.fp

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import cats.effect.IO
import fiuba.fp.exceptions.SizeNotMatchException
import fiuba.fp.models.DataSetRow

import scala.util.Try

object Validator {

  def validate(id: String, date: String, open: String, high: String, low: String, last: String, close: String, dif: String, curr: String, OVol: String, ODif: String, opVol: String, unit: String, dollarBN: String, dollarItau: String, wDiff: String) : Either[Throwable, DataSetRow] = {
    Try(DataSetRow(id.toInt, dateValidator(date), tryToDouble(open), tryToDouble(high), tryToDouble(low), last.toDouble, close.toDouble, dif.toDouble, stringSizeValidator(curr, 1), tryToInt(OVol), tryToInt(ODif), tryToInt(opVol), stringSizeValidator(unit, 4), dollarBN.toDouble, dollarItau.toDouble, wDiff.toDouble)).toEither
  }


  def stringSizeValidator(s: String, i: Int) : String = {
    s match {
      case s if(s.length <= i) => s
      case _ => throw new SizeNotMatchException("field should have 1 character")
    }
  }

  def dateValidator(s: String): IO[LocalDateTime] = {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH)
    IO(LocalDateTime.parse(s.replace(".","").toUpperCase, formatter))
  }

  def tryToInt(s: String) : Option[Int] = s.toIntOption

  def tryToDouble(s: String) : Option[Double] = s.toDoubleOption
}
