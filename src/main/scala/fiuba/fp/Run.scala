package fiuba.fp

import java.nio.file.Paths

import doobie._
import doobie.implicits._
import cats.effect.IO

import scala.concurrent.ExecutionContext
import cats.effect._
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections
import fs2.{Stream, io, text}

object Main extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = processFile("/home/missPanda/fiuba/ALGOIV/algo4-tp1/train.csv").compile.drain.asInstanceOf[IO[ExitCode]]

  implicit val cs = IO.contextShift(ExecutionContext.global)
/*
  val transactor = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver",
    "jdbc:postgresql://localhost:5432/fpalgo",
    "fiuba", "password")

  val q = sql"select * from fptp.movie"
    .query[String].to[List]

  val result = q.transact(transactor).unsafeRunSync().take(5).foreach(println)
*/
  private def processFile(filePath: String): Stream[IO, Unit] = {
    Stream.resource(Blocker[IO]).flatMap { blocker =>
      io.file
        .readAll[IO](Paths.get("/home/missPanda/fiuba/ALGOIV/algo4-tp1/train.csv"), blocker, 4096)
        .through(text.utf8Decode)
        .through(text.lines)
        .map(line => {
          val cols = line.split(",").map(_.trim)
          println(cols.mkString("::"))
        })
    }
      /*
        .map(tryParseLine) // returns Option[DatasetRow]
        .filter(option => option != Nothing) // if
        .map(getFromDatasetRow)
        .through(putInDB)


      println("movie_id, release_date, name, rating")
      val bufferedSource = io.Source.fromFile()
      bufferedSource.getLines().toStream
      for (line <- bufferedSource.getLines) {
          val cols = line.split(",").map(_.trim)
          // do whatever you want with the columns here
          println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
      }
      bufferedSource.close*/


  }


}

/*
def tryParseLine(line: String) : Option[DatasetRow] = {

}

  def putInDb(row : DatasetRow) : Unit = {}
  def validate(line : String) : Boolean = {
  true
}*/