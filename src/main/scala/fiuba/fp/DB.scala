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
}
