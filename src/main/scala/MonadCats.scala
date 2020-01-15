
import cats.Monad
import cats.instances.option._ // for Monad
import cats.instances.list._ // for Monad
import cats.instances.future._ // for Monad
import scala.concurrent._
import scala.concurrent.duration._

class MonadCats {
  val opt1 = Monad[Option].pure(3)
  // opt1: Option[Int] = Some(3)

  val opt2 = Monad[Option].flatMap(opt1)(a => Some(a + 2))
  // opt2: Option[Int] = Some(5)

  val opt3 = Monad[Option].map(opt2)(a => 100 * a)
  // opt3: Option[Int] = Some(500)

  val list1 = Monad[List].pure(3)
  // list1: List[Int] = List(3)

  val list2 = Monad[List].flatMap(List(1, 2, 3))(a => List(a, a * 10))
  // list2: List[Int] = List(1, 10, 2, 20, 3, 30)

  val list3 = Monad[List].map(list2)(a => a + 123)
  // list3: List[Int] = List(124, 133, 125, 143, 126, 153)
}


/**
 * Unlike the methods on the Future
 * class itself, the pure and flatMap methods on the monad can’t accept implicit
 * ExecutionContext parameters (because the parameters aren’t part of the
 * definitions in the Monad trait)
 */

import scala.concurrent.ExecutionContext.Implicits.global

class FutureMonad {
  val fm = Monad[Future]
  // fm: cats.Monad[scala.concurrent.Future] = cats.instances.
  //FutureInstances$$anon$1@3c5f21ab

  /**
   * The Monad instance uses the captured ExecutionContext for subsequent
   * calls to pure and flatMap:
   */

  val future = fm.flatMap(fm.pure(1))(x => fm.pure(x + 2))
  Await.result(future, 1.second)
  // res3: Int = 3
}

import cats.Monad
import cats.syntax.functor._ // for map
import cats.syntax.flatMap._ // for flatMap
import scala.language.higherKinds
import cats.instances.option._ // for Monad
import cats.instances.list._ // for Monad
import cats.Id

class CustomMonad {

  def sumSquareA[F[_] : Monad](a: F[Int], b: F[Int]): F[Int] =
  a.flatMap(x => b.map(y => x * x + y * y))

  // for comprehension version:

  def sumSquare[F[_]: Monad](a: F[Int], b: F[Int]): F[Int] =
    for {
      x <- a
      y <- b
    } yield x*x + y*y

  sumSquare(Option(3), Option(4))
  // res8: Option[Int] = Some(25)

  sumSquare(List(1, 2, 3), List(4, 5))
  // res9: List[Int] = List(17, 26, 20, 29, 25, 34)

  /**
   * as-is we can't call sumSquare with non-monadic parameters,
   * we can import the cats.Id type to provide the gap:
   */

  sumSquare(3 : Id[Int], 4 : Id[Int])
  // res2: cats.Id[Int] = 25

  /**
   * The ability to abstract over monadic and non-monadic
   * code is extremely powerful. For example, we can run code
   * asynchronously in production using Future and synchronously
   * in test using Id.
   */

}



