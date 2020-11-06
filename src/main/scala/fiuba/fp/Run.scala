package fiuba.fp

import java.nio.file.Paths

import doobie._

import cats.effect.IO
import fs2.{Stream, io, text}

import scala.concurrent.ExecutionContext
import cats.effect._

object Run extends App {
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    // Transactor to connect to our DB.
    val transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/fpalgo",
        "fiuba","password")

    val db = DB(transactor)

    val stream: Stream[IO, Unit] = Stream.resource(Blocker[IO]).flatMap { blocker =>
        Stream(db.datasetDummy(1), db.datasetDummy(2), db.datasetDummy(3))
          .evalMap(db.putInDb)
          .map(db.toOutputLine)
          .through(text.utf8Encode)
          .through(io.file.writeAll(Paths.get("output.txt"), blocker))
    }

    stream.compile.drain.unsafeRunSync()
}

