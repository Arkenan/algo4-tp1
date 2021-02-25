package fiuba.fp

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import fiuba.fp.exceptions.SizeNotMatchException
import fiuba.fp.models.DataSetRow

import scala.util.Try

object Validator {
  def validate(data: List[String]) : Either[Throwable, DataSetRow] = {
    val dataSetRow = for {
      id <- Try{data(0).toInt}
      date <- Try{data(1)}.flatMap(d => dateValidator(d))
      open <- Try{data(2)}.flatMap(o => tryToDouble(o))
      high <- Try{data(3)}.flatMap(h => tryToDouble(h))
      low <- Try{data(4)}.flatMap(l => tryToDouble(l))
      last <- Try{data(5).toDouble}
      close <- Try{data(6).toDouble}
      dif <- Try{data(7).toDouble}
      curr <- Try{data(8)}.flatMap(s => stringSizeValidator(s, 1))
      oVol <- Try{data(9)}.flatMap(o => tryToInt(o))
      oDif <- Try{data(10)}.flatMap(o => tryToInt(o))
      opVol <- Try{data(11)}.flatMap(o => tryToInt(o))
      unit <- Try{data(12)}.flatMap(s => stringSizeValidator(s, 4))
      dollarBN <- Try{data(13).toDouble}
      dollarItau <- Try{data(14).toDouble}
      wDiff <- Try{data(15).toDouble}
    } yield {
        DataSetRow(id, date, open, high, low, last, close, dif, curr, oVol, oDif, opVol, unit, dollarBN, dollarItau, wDiff)
    }
    dataSetRow.toEither
  }


  def stringSizeValidator(s: String, i: Int) : Try[String] = {
    s match {
      case s: String if(s.length <= i) => Try{s}
      case _ => throw new SizeNotMatchException("field should have 1 character")
    }
  }

  def dateValidator(s: String): Try[LocalDateTime] = {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH)
    Try{LocalDateTime.parse(s.replace(".","").toUpperCase, formatter)}
  }

  def tryToInt(s: String) : Try[Option[Int]] = Try{s.toIntOption}

  def tryToDouble(s: String) : Try[Option[Double]] = Try{s.toDoubleOption}
}
