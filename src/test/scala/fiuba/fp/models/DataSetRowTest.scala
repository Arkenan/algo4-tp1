package fiuba.fp.models

import java.time.LocalDateTime

import fiuba.fp.FpTpSpec

class DataSetRowTest extends FpTpSpec {

    "A DataSetRow" should "represent a row" in {

        val row = DataSetRow(12,LocalDateTime.now(),None,Option(12.3),Some(13.6),89.9,123.45,-1213.0,"D", Some(12),None,None,"TONS",123.34,567.23,1234.5)

        row.id shouldBe 12
    }

    behavior of "A line"
    it should "be mapped to DataSetRow if it has all fields" in {
        val dataSetRow : Option[DataSetRow] =  DataSetRow.convertToDataSetRow("1,05/01/2004 12:00:00 a.m.,0,0,0,0,221,13,D,0,0,0,TONS,2.92,2.905,-221")
        assert(dataSetRow.nonEmpty)
        assert(dataSetRow.get.id == 1)
    }
    it should "be mapped to None if it does not have all fields" in {
        val dataSetRow : Option[DataSetRow] =  DataSetRow.convertToDataSetRow("1,05/01/2004 12:00:00 a.m.,0,0,0,0,221,13,D,0,0,0,TONS,2.92,2.905")
        assert(dataSetRow.isEmpty)
    }
}
