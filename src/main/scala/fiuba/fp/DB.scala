package fiuba.fp

import java.text.{DateFormat, SimpleDateFormat}
import java.time.LocalDateTime
import java.util.Calendar

import cats.effect.{IO}
import doobie.Transactor
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.implicits.javatime._
import models.DataSetRow
import doobie.util.fragment
import fiuba.fp.DB
import doobie.util.{fragment, transactor}
import fiuba.fp.Run.transactor
import fiuba.fp.models.DataSetRow


/** Database interaction for datasets using a specified transactor. */
case class DB(transactor: Transactor.Aux[IO, Unit]) {
  /** Puts a dataset row in the database. */
  def putInDb(dr: Either[Throwable, DataSetRow]): IO[Either[Throwable, Int]] = {
    dr match {
      case Right(dr) => {
        val query: fragment.Fragment =
          sql"INSERT INTO fptp.dataset" ++
            sql"(id, date, open, high, low, last, close, dif, curr, o_vol, o_dif, op_vol, unit, " ++
            sql"dollar_bn, dollar_itau, w_diff, hash_code)" ++
            sql"VALUES" ++
            sql"(${dr.id}, ${dr.date}, ${dr.open}, ${dr.high}, ${dr.low}, ${dr.last}, ${dr.close}, ${dr.diff}," ++
            sql"${dr.curr}, ${dr.OVol}, ${dr.ODiff}, ${dr.OpVol}, ${dr.unit}, ${dr.dollarBN}, ${dr.dollarItau}, " ++
            sql"${dr.wDiff}, ${dr.hashCode})"
        query.update.run.transact(transactor).attempt
      }
      case Left(throwable: Throwable) => IO(Left(throwable))
    }
  }

  /** Turns the result of an insert statement into a string that can be printed into a file. */
  def toOutputLine(e: Either[Throwable, Int]): String = {
    val format: SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd - HH:mm:ss.SSS")
    val date = format.format(Calendar.getInstance.getTime)

    e match {
      case (Left(throwable)) => f"[$date] Error inserting row: ${throwable}\n"
      case _ => ""
    }
  }

  /** Util function to create dummies for dataset rows with a specified ID */
  def datasetDummy(id: Integer): DataSetRow = {
    DataSetRow(id = id, date = LocalDateTime.of(2020, 10, 10, 0, 0),
      open = Option[Double](700.0), high = Option[Double](200.0), low = Option[Double](150.0),
      last = 175, close = 3.14, diff = -750.3, curr = "A",
      OVol = Option[Int](2), ODiff = Option[Int](3), OpVol = Option[Int](0),
      unit = "US$", dollarBN = 170, dollarItau = 170.2, wDiff = 100000.0)
  }
}
