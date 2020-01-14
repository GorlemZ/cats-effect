abstract class Monoid[A] {
  def add(x: A, y: A): A
  def unit: A
}

object ImplicitTest {
  implicit val stringMonoid: Monoid[String] = new Monoid[String] {
    def add(x: String, y: String): String = x concat y
    def unit: String = ""
  }

  implicit val intMonoid: Monoid[Int] = new Monoid[Int] {
    def add(x: Int, y: Int): Int = x + y
    def unit: Int = 0
  }

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A =
    if (xs.isEmpty) m.unit
    else m.add(xs.head, sum(xs.tail))

  def main(args: Array[String]): Unit = {
    println(sum(List(1, 2, 3)))       // uses intMonoid implicitly
    println(sum(List("a", "b", "c"))) // uses stringMonoid implicitly
  }
}

/**
 * In our main method we call sum twice, and only provide the xs parameter.
 * Scala will now look for an implicit in the scope mentioned above.
 * The first call to sum passes a List[Int] for xs, which means that A is Int.
 * The implicit parameter list with m is left out, so Scala will look for an implicit of type Monoid[Int]
 *
 */