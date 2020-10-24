package fiuba.fp

import doobie._
import doobie.implicits._
import cats.effect.IO
import scala.concurrent.ExecutionContext

import cats.effect._

object Run extends App {

    implicit val cs = IO.contextShift(ExecutionContext.global)

    val transactor = Transactor.fromDriverManager[IO](
        "org.postgresql.Driver",
        "jdbc:postgresql://localhost:5432/fpalgo",
        "fiuba","password")

    val q = sql"select name from fptp.movie where movie_id=1019"
      .query[String]
      .unique

    val result = q.transact(transactor).unsafeRunSync()
    print(s"La película es '$result'")
}