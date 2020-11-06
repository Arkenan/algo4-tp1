package fiuba.fp

import models.DataSetRow

import java.nio.file.Paths

import doobie._
import cats.effect.IO
import fs2.{Stream, io, text}

import scala.concurrent.ExecutionContext
import cats.effect._
import scala.io.Source
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
              .map(DataSetRow.convertToDataSetRow)
              .evalMap(r => db.putInDb(r))
              .map(db.toOutputLine)
              .intersperse("\n")
              .through(text.utf8Encode)
              .through(io.file.writeAll(Paths.get("output.txt"), blocker))
        }

        stream.compile.drain.unsafeRunSync()
}

