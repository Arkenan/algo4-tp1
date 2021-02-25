package fiuba.fp.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

import fiuba.fp.FpTpSpec

class DataSetRowTest extends FpTpSpec {

    "A DataSetRow" should "represent a row" in {

        val row = DataSetRow(12,LocalDateTime.now(),None,Option(12.3),Some(13.6),89.9,123.45,-1213.0,"D", Some(12),None,None,"TONS",123.34,567.23,1234.5)

        row.id shouldBe 12
    }

    behavior of "A line"
    it should "be mapped to DataSetRow if it has all fields" in {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss a", Locale.ENGLISH)
        val dateTime = LocalDateTime.parse("05/01/2004 12:00:00 a.m.".replace(".","").toUpperCase, formatter)
        val row = DataSetRow(1,dateTime,Option(0),Option(0),Option(0),0,221,13,"D",Option(0),Option(0),Option(0),"TONS",2.92,2.905,-221)
        val dataSetRow : Either[Throwable, DataSetRow] =  DataSetRow.convertToDataSetRow("1,05/01/2004 12:00:00 a.m.,0,0,0,0,221,13,D,0,0,0,TONS,2.92,2.905,-221")
        assert(dataSetRow.isRight)
        assert(dataSetRow.getOrElse() == row)
    }
    it should "be mapped to None if it does not have all fields" in {
        val dataSetRow : Either[Throwable, DataSetRow] =  DataSetRow.convertToDataSetRow("1,05/01/2004 12:00:00 a.m.,0,0,0,0,221,13,D,0,0,0,TONS,2.92,2.905")
        assert(dataSetRow.isLeft)
    }
}
