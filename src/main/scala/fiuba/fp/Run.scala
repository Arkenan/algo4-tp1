package fiuba.fp

import models.DataSetRow
import scala.io.Source

object Run extends App {

  val filename = args(0)

  Source.fromFile(filename).getLines()
                           .map(line => DataSetRow.convertToDataSetRow(line))
                           

}

