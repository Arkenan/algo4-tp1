package fiuba.fp

import java.nio.file.{Paths, StandardOpenOption}

import cats.effect.{IO, _}
import doobie._
import fiuba.fp.models.DataSetRow
import fs2.{Stream, io, text}

import scala.concurrent.ExecutionContext

object Run extends App {

    if (args.length == 0)
        sys.exit()

    val filename = args(0)

    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

    // Transactor to connect to our DB.
    val transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/fpalgo",
        "fiuba","password")

    val db = DB(transactor)
    val logger = Logger()

    val stream: Stream[IO, Unit] = Stream.resource(Blocker[IO]).flatMap { blocker =>
        io.file
          .readAll[IO](Paths.get(filename), blocker, 4096)
          .through(text.utf8Decode)
          .through(text.lines)
          .map(DataSetRow.convertToDataSetRow)
          .evalMap(r => db.putInDb(r))
          .map(logger.toOutputLine)
          .through(text.utf8Encode)
          .through(io.file.writeAll(
            Paths.get("log.txt"), blocker, List(StandardOpenOption.APPEND, StandardOpenOption.CREATE)))
    }

    stream.compile.drain.unsafeRunSync()
}

