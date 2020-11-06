package fiuba.fp

import java.time.LocalDateTime

import cats.effect.{ContextShift, IO}
import doobie.Transactor
import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.implicits.javatime._
import models.DataSetRow
import doobie.util.fragment
import fiuba.fp.DB
import doobie.util.{fragment, transactor}
import fiuba.fp.Run.transactor
import fiuba.fp.models.DataSetRow

import scala.concurrent.ExecutionContext

case class DB(transactor : Transactor.Aux[IO, Unit]) {
  // Turns the result of an insert statement into a string that can be printed into a file.
  // TODO: make a better log (e.g. Maybe add CSV line, line content, only show errors, etc).
  def toOutputLine(e: Either[Throwable, Int]) : String = {
    e match {
      case(Left(th)) => f"Error inserting row: ${th.getMessage}\n"
      case(Right(am)) => ""
    }
  }

  // Util function to create dummies for dataset rows with a specified ID.
  def datasetDummy(id: Integer) = {
    DataSetRow(id = id, date = LocalDateTime.of(2020, 10, 10, 0, 0),
      open = Option[Double](700.0), high = Option[Double](200.0), low = Option[Double](150.0),
      last = 175, close = 3.14, diff = -750.3, curr = "A",
      OVol = Option[Int](2), Odiff = Option[Int](3), OpVol = Option[Int](0),
      unit = "US$", dollarBN = 170, dollarItau = 170.2, wDiff = 100000.0)
  }

  // Puts a dataset row in the database specified by a transactor.
  def putInDb(transactor: Transactor.Aux[IO, Unit], dr: DataSetRow) : IO[Either[Throwable, Int]] = {
    val query : fragment.Fragment = sql"INSERT INTO fptp.dataset(id, date, open, high, low, last, close, dif, curr, o_vol, o_dif, op_vol, unit, dollar_bn, dollar_itau, w_diff, hash_code) VALUES (${dr.id}, ${dr.date}, ${dr.open}, ${dr.high}, ${dr.low}, ${dr.last}, ${dr.close}, ${dr.diff}, ${dr.curr}, ${dr.OVol}, ${dr.Odiff}, ${dr.OpVol}, ${dr.unit}, ${dr.dollarBN}, ${dr.dollarItau}, ${dr.wDiff}, ${275})"
    query.update.run.transact(transactor).attempt
  }

  // Partially evaluated put function with a fixed transactor. Ready to add as part of a stream pipeline.
  def putInDb(datasetRow: DataSetRow) : IO[Either[Throwable, Int]] = {
    putInDb(transactor, datasetRow)
  }
}
