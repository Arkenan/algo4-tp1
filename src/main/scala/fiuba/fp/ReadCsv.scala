package fiuba.fp

class ReadCsv extends App {

    println("movie_id, release_date, name, rating")
    val bufferedSource = io.Source.fromFile("../train.csv")
    for (line <- bufferedSource.getLines) {
      val cols = line.split(",").map(_.trim)
      // do whatever you want with the columns here
      println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
    }
    bufferedSource.close
}
