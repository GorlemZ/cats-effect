import cats.Functor
import cats.instances.option._
import cats.syntax.functor._ // for map

class Functor2 {

  /**
   * the type class also provides the LIFT method, that in fact lift a function from a
   * A=>B function to a F[A]=>F[B] function
   */

  val func = (x: Int) => x + 1
  // func: Int => Int = <function1>

  val liftedFunc = Functor[Option].lift(func)
  // liftedFunc: Option[Int] => Option[Int] = cats.Functor$$Lambda$11698


  /**
   * method that applies an equation to a number no matter what functor context it’s in:
   */

  def doMath[F[_]](start: F[Int])(implicit functor: Functor[F]): F[Int] =
    start.map(n => n + 1 * 2)

  doMath(Option(20))
  // res3: Option[Int] = Some(22)
}

/**
 * We recurse over the
 * data structure, applying the function to every Leaf we find. The functor laws
 * intuitively require us to retain the same structure with the same pattern of
 * Branch and Leaf nodes
 */

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]


implicit val treeFunctor: Functor[Tree] =
  new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] =
      fa match {
        case Branch(left, right) => Branch(map(left)(f), map(right)(f))
        case Leaf(v) => Leaf(f(v))
      }
  }


/**
 * as-is we cannot create a branch with two leaves because the invariance problem:
 * the compiler found a Functor for Tree, but not for Branch or Leaf.
 */

//val b = Branch(Leaf(2), Leaf(3)).map(_ * 2) //does not compile!

/**
 * a solution: a constructor object
 */

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)
}

val t: Tree[Int] = Tree.branch(Tree.leaf(3), Tree.leaf(5)).map(_ * 2)
// res11: wrapper.Tree[Int] = Branch(Leaf(20),Leaf(40))