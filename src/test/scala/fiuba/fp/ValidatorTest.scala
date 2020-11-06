package fiuba.fp

import fiuba.fp.models.DataSetRow

class ValidatorTest extends FpTpSpec {

  "A Validator" should "validate strings with specific length" in {
    val validator = Validator

    validator.stringSizeValidator("a", 1) shouldBe "a"
    validator.stringSizeValidator("defg", 4) shouldBe "defg"
    an[IllegalArgumentException] should be thrownBy (validator.stringSizeValidator("abc", 1))
  }

  "A Validator" should "validate integers" in {
    val validator = Validator

    validator.tryToInt("12") shouldBe a[Some[Int]]
    validator.tryToInt("-12") shouldBe a[Some[Int]]
    validator.tryToInt("32.4") shouldBe None
    validator.tryToInt("a") shouldBe None
  }

  "A Validator" should "validate doubles" in {
    val validator = Validator

    validator.tryToDouble("23.56") shouldBe a[Some[Double]]
    validator.tryToDouble("-47") shouldBe a[Some[Double]]
    validator.tryToDouble("cdef") shouldBe None
  }

 /* "A Validator" should "validate rows" in {
    val validator = Validator

    validator.validate("1541542", "02/04/1994 12:48:10 AM",
      "700.0", "200.0", "150.0",
      "175", "3.14", "-750.3", "A",
      "2", "3", "0",
      "US$", "170", "170.2", "100000.0") shouldBe a[Some[DataSetRow]]
  }*/
}