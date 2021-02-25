package fiuba.fp

import java.time.LocalDateTime

import fiuba.fp.models.DataSetRow
import org.scalatest.exceptions.TestFailedException

class ValidatorTest extends FpTpSpec {

  "A Validator" should "validate strings with specific length" in {
    Validator.stringSizeValidator("a", 1).getOrElse() shouldBe "a"
    Validator.stringSizeValidator("defg", 4).getOrElse() shouldBe "defg"
    an[IllegalArgumentException] should be thrownBy (Validator.stringSizeValidator("abc", 1))
  }

  "A Validator" should "validate integers" in {
    Validator.tryToInt("12").getOrElse() shouldBe a[Some[Int]]
    Validator.tryToInt("-12").getOrElse() shouldBe a[Some[Int]]
    Validator.tryToInt("32.4").getOrElse() shouldBe None
    Validator.tryToInt("a").getOrElse() shouldBe None
  }

  "A Validator" should "validate doubles" in {
    Validator.tryToDouble("23.56").getOrElse() shouldBe a[Some[Double]]
    Validator.tryToDouble("-47").getOrElse() shouldBe a[Some[Double]]
    Validator.tryToDouble("cdef").getOrElse() shouldBe None
  }

  "A Validator" should "validate dates" in {
    Validator.dateValidator("03/04/1994 10:15:23 a.m.").getOrElse() shouldBe a[LocalDateTime]
    Validator.dateValidator("30/03/1927 02:45:00 p.m.").getOrElse() shouldBe a[LocalDateTime]
    a[TestFailedException] should be thrownBy (Validator.dateValidator("20-06-1980 11:12:10 p.m.").getOrElse() shouldBe a[LocalDateTime])
    a[TestFailedException] should be thrownBy (Validator.dateValidator("25/10/1946 12:45:45").getOrElse() shouldBe a[LocalDateTime])
  }

  "A Validator" should "validate rows" in {
    /* all values are ok, should return a DataSetRow */
    Validator.validate(List("1541542", "02/04/1994 12:48:10 a.m.",
      "700.0", "200.0", "150.0",
      "175", "3.14", "-750.3", "A",
      "2", "3", "0",
      "US$", "170", "170.2", "100000.0")) shouldBe a[Right[Throwable,DataSetRow]]

    /* field open is not a Double, should return a DataSetRow */
    Validator.validate(List("1541542", "02/04/1994 12:48:10 a.m.",
      "efg", "200.0", "150.0",
      "175", "3.14", "-750.3", "A",
      "2", "3", "0",
      "US$", "170", "170.2", "100000.0")) shouldBe a[Right[Throwable,DataSetRow]]

    /* field date is wrong, should return None */
    Validator.validate(List("1541542", "02/04/1994 12:48:10",
      "700.0", "200.0", "150.0",
      "175", "3.14", "-750.3", "A",
      "2", "3", "0",
      "US$", "170", "170.2", "100000.0")) shouldBe a[Left[Throwable,DataSetRow]]

    /* field open is not a Double, should return None */
    Validator.validate(List("1541542", "02/04/1994 12:48:10 a.m.",
      "700.0", "200.0", "150.0",
      "175", "3.14", "-750.3", "ABC",
      "2", "3", "0",
      "US$", "170", "170.2", "100000.0")) shouldBe a[Left[Throwable,DataSetRow]]

    /* field id is not an Int, should return None */
    Validator.validate(List("abcd", "02/04/1994 12:48:10 a.m.",
      "700.0", "200.0", "150.0",
      "175", "3.14", "-750.3", "A",
      "2", "3", "0",
      "US$", "170", "170.2", "100000.0")) shouldBe a[Left[Throwable,DataSetRow]]
  }
}