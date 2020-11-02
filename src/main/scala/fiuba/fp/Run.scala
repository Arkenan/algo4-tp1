package fiuba.fp

import java.nio.file.Paths

import cats.effect.IO

import cats.effect._
import fs2.{Stream, io, text}
import models.DummyRow

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = processFile("/home/missPanda/fiuba/ALGOIV/algo4-tp1/train.csv").compile.drain.asInstanceOf[IO[ExitCode]]


  private def processFile(filePath: String): Stream[IO, Unit] = {
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](Paths.get("/home/missPanda/fiuba/ALGOIV/algo4-tp1/train.csv"), blocker, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .map(mapToRow)
        .map(row => print(row.toString))
    }

  }

  private def mapToRow(line: String) : DummyRow = {

    val cols: Array[String] = line.split(",").map(_.trim)
    val dummy: DummyRow = DummyRow(cols(0), cols(1), cols(2), cols(3), cols(4), cols(5), cols(6), cols(7),
                                    cols(8), cols(9), cols(10), cols(11), cols(12), cols(13), cols(14), cols(15))
    print(dummy.toString)
    return dummy
  }

}
