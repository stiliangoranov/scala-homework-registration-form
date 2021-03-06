package homework2

import org.scalatest.{FlatSpec, FunSuite, Matchers}

class ValidatedTest extends FlatSpec with Matchers {
  "zip" should "combine valid instances" in {
    Valid(1).zip(Valid("a")) shouldEqual Valid((1, "a"))
  }

  it should "combine errors from invalid instances" in {
    Invalid(1).zip(Invalid(Chain(2, 3))) shouldEqual Invalid(Chain(1, 2, 3))
    Invalid(1).zip(Invalid(Chain(2, 3))).zip(Valid(69)).zip(Invalid(Chain(4, 5))) shouldEqual Invalid(Chain(1, 2, 3, 4, 5))
  }

  it should "return errors from the invalid instance" in {
    Valid(1).zip(Invalid(Chain(2, 3))) shouldEqual Invalid(Chain(2, 3))

    Invalid(Chain(2, 3)).zip(Valid(1)) shouldEqual Invalid(Chain(2, 3))
  }

  "getOrElse" should "return default value" in {
    Invalid(45).getOrElse(69) shouldBe 69
  }

  it should "return the valid value" in {
    Valid(45).getOrElse(69) shouldBe 45
  }

  "orElse" should "return default value" in {
    Invalid(45).orElse(Valid(69)) shouldBe Valid(69)
  }

  it should "return the object itself" in {
    Valid(45).orElse(Valid(69)) shouldBe Valid(45)
  }

  "map" should "return Valid(9)" in {
    Valid(3).map(x => x * x) shouldBe Valid(9)
  }

  "map2" should "return Valid(3)" in {
    Valid(1).map2(Valid(2))(_ + _) shouldBe Valid(3)
  }

  it should "return Invalid(Chain(42))" in {
    Valid(1).map2(Invalid(Chain(42)))(_ + _) shouldBe Invalid(Chain(42))
  }

  "flatMap" should "return Valid(9)" in {
    Valid(3).flatMap(x => Valid(x * x)) shouldBe Valid(9)
  }

  it should "return Invalid(Chain(1, 2, 3))" in {
    Invalid(Chain(1, 2, 3)).flatMap(x => Valid(35, 40, 23)) shouldBe Invalid(Chain(1, 2, 3))
  }

  "zip on ValidatedTuple3" should "combine valid instances" in {
    (Valid(1), Valid("2"), Valid(3.0)).zip shouldBe Valid((1, "2", 3.0))
  }

  it should "combine errors from invalid instances" in {
    (Valid(1), Invalid(Chain(2)), Invalid(Chain(3))).zip shouldBe Invalid(Chain(2, 3))
    (Invalid(1), Invalid(Chain(2)), Invalid(Chain(3))).zip shouldBe Invalid(Chain(1, 2, 3))
  }

  "zip on ValidatedTuple4" should "combine valid instances" in {
    (Valid(1), Valid("2"), Valid(3.0), Valid(69)).zip shouldBe Valid((1, "2", 3.0, 69))
  }

  it should "combine errors from invalid instances" in {
    (Valid(1), Invalid(Chain(2)), Valid(34), Invalid(Chain(3))).zip shouldBe Invalid(Chain(2, 3))
    (Invalid(1), Invalid(Chain(2)), Invalid(Chain(3)), Invalid(4)).zip shouldBe Invalid(Chain(1, 2, 3, 4))
  }

  "zip on ValidatedTuple5" should "combine valid instances" in {
    (Valid(1), Valid("2"), Valid(3.0), Valid(3.14), Valid(69)).zip shouldBe Valid((1, "2", 3.0, 3.14, 69))
  }

  it should "combine errors from invalid instances" in {
    (Valid(1), Invalid(Chain(2)), Valid(34), Invalid(Chain(3)), Valid(3.14)).zip shouldBe Invalid(Chain(2, 3))
    (Invalid(1), Invalid(Chain(2)), Invalid(Chain(3)), Invalid(4), Invalid(5)).zip shouldBe Invalid(Chain(1, 2, 3, 4, 5))
  }

  "zipMap on ValidatedTuple" should "be the same as zip when invalid instances exist" in {
    (Valid(1), Invalid(Chain(2)), Valid(34), Invalid(Chain(3)), Valid(3.14))
      .zipMap((_: Any, _: Any, _: Any, _: Any, _: Any) => 5) shouldBe
      (Valid(1), Invalid(Chain(2)), Valid(34), Invalid(Chain(3)), Valid(3.14)).zip
  }

  it should "apply function to tuple properly" in {
    (Valid(1), Valid("2"), Valid(3.0), Valid(3.14), Valid(69))
      .zipMap((_: Any, _: Any, _: Any, _: Any, _: Any) => 23) shouldBe Valid(23)
  }

  "toValidated" should "return Valid from Option" in {
    import Validated._
    Some(23).toValidated("Error!") shouldBe Valid(23)
  }

  it should "return Invalid from Option" in {
    import Validated._
    None.toValidated("Error!") shouldBe Invalid("Error!")
  }
}
