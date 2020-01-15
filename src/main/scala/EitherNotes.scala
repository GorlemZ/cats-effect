import cats.syntax.either._ // for map and flatMap

class EitherNotes {

  val either1: Either[String, Int] = Right(10)
  val either2: Either[String, Int] = Right(32)

  /**
   * The modern Either makes the decision that the
   * right side represents the success case and thus
   * supports map and flatMap directly.
   * This makes for comprehensions much more pleasant:
   */

  val either3: Either[String, Int] =
    for {
    a <- either1
    b <- either2
  } yield a + b

  //SMART CONSTRUCTORS

  val a: Either[String, Int] = 3.asRight[String]
  // a: Either[String,Int] = Right(3)

  val b: Either[String, Int] = 4.asRight[String]
  // b: Either[String,Int] = Right(4)

  for {
    x <- a
    y <- b
  } yield x*x + y*y
  // res4: scala.util.Either[String,Int] = Right

  /*
  def countPositive(nums: List[Int]): Right[Nothing, Int] =
    nums.foldLeft(Right(0)) { (accumulator, num) =>
      if(num > 0) {
        accumulator.map(_ + 1)
      } else {
        Left("Negative. Stopping!")
      }
    }
   */

  /**
   * this function does not compile. The inferred type for accumulator
   * is not correct, the Left type is inferred as Nothing. Using asRight:
   */

  def countPositive(nums: List[Int]) =
    nums.foldLeft(0.asRight[String]) { (accumulator, num) =>
      if(num > 0) {
        accumulator.map(_ + 1)
      } else {
        Left("Negative. Stopping!")
      }
    }

  /**
   * can be used for catching exceptions and for error handling:
   */

  Either.fromTry(scala.util.Try("foo".toInt))
  // res9: Either[Throwable,Int] = Left(java.lang.NumberFormatException:
  //For input string: "foo")

  "Error".asLeft[Int].orElse(2.asRight[String])
  // res12: Either[String,Int] = Right(2)

  (-1).asRight[String].ensure("Must be non-negative!")(_ > 0)
  // res13: Either[String,Int] = Left(Must be non-negative!)
}
