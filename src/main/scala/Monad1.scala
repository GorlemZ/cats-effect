class Monad1 {

  /**
   * A monad’s flatMap method allows us to specify what happens next,
   * taking into account an intermediate complication
   */

  def parseInt(str: String): Option[Int] =
    scala.util.Try(str.toInt).toOption

  def divide(a: Int, b: Int): Option[Int] =
    if(b == 0) None else Some(a / b)

  def stringDivideByFirst(aStr: String, bStr: String): Option[Int] =
    parseInt(aStr).flatMap { aNum =>
      parseInt(bStr).flatMap { bNum =>
        divide(aNum, bNum)
      }
    }

  /**
   * at each step it returns a new computation or a None.
   * Every Monad is also a Functor: we can rely on both flatMap and map:
   */

  def stringDivideBy(aStr: String, bStr: String): Option[Int] =
    for {
      aNum <- parseInt(aStr)
      bNum <- parseInt(bStr)
      ans <- divide(aNum, bNum)
    } yield ans

  /**
   * monadic behaviour is formally captured in two operations:
   * • pure, of type A => F[A];
   * • flatMap, of type (F[A], A => F[B]) => F[B].
   */
}
