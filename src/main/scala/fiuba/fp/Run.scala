package fiuba.fp

import java.time.LocalDateTime

import doobie._
import doobie.implicits._
import doobie.implicits.javasql._
import doobie.implicits.javatime._
import cats.effect.IO
import fs2.Stream
import fs2.io

import scala.concurrent.ExecutionContext
import models.DataSetRow
import cats.effect._
import doobie.util.fragment

object Run extends App {
    // Puts a dataset row in the database specified by a transactor.
    def putInDb(transactor: Transactor.Aux[IO, Unit], dr: DataSetRow) : IO[Either[Throwable, Int]] = {
        val query : fragment.Fragment = sql"INSERT INTO fptp.dataset(id, date, open, high, low, last, close, dif, curr, o_vol, o_dif, op_vol, unit, dollar_bn, dollar_itau, w_diff, hash_code) VALUES (${dr.id}, ${dr.date}, ${dr.open}, ${dr.high}, ${dr.low}, ${dr.last}, ${dr.close}, ${dr.diff}, ${dr.curr}, ${dr.OVol}, ${dr.Odiff}, ${dr.OpVol}, ${dr.unit}, ${dr.dollarBN}, ${dr.dollarItau}, ${dr.wDiff}, ${275})"
        query.update.run.transact(transactor).attempt
    }

    // Util function to create dummies for dataset rows with a specified ID.
    def datasetDummy(id: Integer) = {
        DataSetRow(id = id, date = LocalDateTime.of(2020, 10, 10, 0, 0),
            open = Option[Double](700.0), high = Option[Double](200.0), low = Option[Double](150.0),
            last = 175, close = 3.14, diff = -750.3, curr = "A",
            OVol = Option[Int](2), Odiff = Option[Int](3), OpVol = Option[Int](0),
            unit = "US$", dollarBN = 170, dollarItau = 170.2, wDiff = 100000.0)
    }

    implicit val cs = IO.contextShift(ExecutionContext.global)
    val transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/fpalgo",
        "fiuba","password")

    // Partially evaluated put function with a fixed transactor. Ready to add as part of a stream pipeline.
    val put = (datasetRow: DataSetRow) => putInDb(transactor, datasetRow)

    // Stream that writes dummy dataset rows to the DB.
    Stream(datasetDummy(100), datasetDummy(101), datasetDummy(102))
      .evalMap(put)
      .compile
      .drain
      .unsafeRunSync()
}

