package fiuba.fp

import models.DataSetRow

import java.nio.file.Paths

import doobie._
import cats.effect.IO
import fs2.{Stream, io, text}

import scala.concurrent.ExecutionContext
import cats.effect._

object Run extends App {

    if (args.length == 0)
        print("File path is needed")
    val filename = args(0)


    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    // Transactor to connect to our DB.
    val transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/fpalgo",
        "fiuba","password")

    val db = DB(transactor)

    val stream: Stream[IO, Unit] = Stream.resource(Blocker[IO]).flatMap { blocker =>
        io.file
          .readAll[IO](Paths.get(filename), blocker, 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .map(line => DataSetRow.convertToDataSetRow(line))
          .map(dr =>print(dr.toString))
    }

    stream.compile.drain.unsafeRunSync()
}

