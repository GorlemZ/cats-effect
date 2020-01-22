import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import cats.data.Writer
import cats.syntax.applicative._ // for pure
import cats.syntax.writer._ // for tell
import cats.instances.vector._ // for Monoid

object FactorialWriterExercise extends App {

  def slowly[A](body: => A): A =
    try body finally Thread.sleep(100)

  def factorial1(n: Int): Int = {
    val ans = slowly(if (n == 0) 1 else n * factorial1(n - 1))
    println(s"fact $n $ans")
    ans
  }

  /**
   *  factorial(5)
   *
   *  print all the intermediate results, but with more than one
   *  call in parallel we cannot distinguish between the computations:
   */

  // Await.result(Future.sequence(Vector(
  //   Future(factorial1(3)),
  //   Future(factorial1(3))
  // )), 5.seconds)

  type Logged[A] = Writer[Vector[String], A]

  42.pure[Logged]
  // res13: Logged[Int] = WriterT((Vector(),42))

  /**
   * we can re-write factorial to capture logs independently,
   * using a semigroup for Vector (for map and flatMap)
   *
   */

  def factorial(n: Int): Logged[Int] =
    for {
      ans <- if(n == 0) {
        1.pure[Logged]
      } else {
        slowly(factorial(n - 1).map(_ * n))
      }
      _ <- Vector(s"fact $n $ans").tell
    } yield ans

  /**
   * We can run several factorials in parallel as follows,
   * capturing their logs independently without fear of interleaving
   */

  val Vector((logA, ansA), (logB, ansB)) =
    Await.result(Future.sequence(Vector(
      Future(factorial(3).run),
      Future(factorial(5).run)
    )), 5.seconds)

  println(logA + " ---> " + ansA)
  println(logB + " ---> " + ansB)
}

