import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.higherKinds
import cats.instances.option._ // for Functor
import cats.Functor

class Functor1 {

  /**
   * what is a Functor? Informally anything with a map method
   * F[A] with an operation map with type (A => B) => F[B].
   */

  val future: Future[String] =
    Future(123).
      map(n => n + 1).
      map(n => n * 2).
      map(n => n + "!")
  Await.result(future, 1.second)
  // res3: String = 248!


  /**
   * Note that Scala’s Futures aren’t a great example of pure functional programming
   * because they aren’t referentially transparent. Future always
   * computes and caches a result and there’s no way for us to tweak this
   * behaviour. This means we can get unpredictable results when we use
   * Future to wrap side-effecting computations
   *
   * Single argument functions are also functors:
   */



  val func1: Int => Double =
    (x: Int) => x.toDouble

  val func2: Double => Double =
    (y: Double) => y * 2

  (func1 andThen func2)(1) // composition using andThen
  // res8: Double = 2.0

  func2(func1(1)) // composition written out by hand
  // res9: Double = 2.0

  /**
   * with Cats we can import the type class Functor. We can create instances using
   * the standard apply method, (line 8):
   */

  val option1: Option[Int] = Option(123)
  // option1: Option[Int] = Some(123)

  val option2: Option[String] = Functor[Option].map(option1)(_.toString)
  // option2: Option[String] = Some(123)

}















