package fiuba.fp

import java.time.LocalDate

case class Logger() {

  /** Turns the result of an insert statement into a string that can be printed into a file. */
  def toOutputLine(e: Either[Throwable, Int]): String = {
    val date = LocalDate.now();

    e match {
      case (Left(throwable)) => f"[$date] Error inserting row: ${throwable}\n"
      case Right(amount) => f"${amount} row was inserted.\n"
    }
  }
}
