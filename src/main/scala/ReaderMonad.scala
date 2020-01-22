import cats.data.Reader
import cats.syntax.applicative._ // for pure

object ReaderMonad extends App {

  /**
   * One common use for Readers is dependency injection.
   * If we have a number of operations that all depend on
   * some external configuration, we can chain them together
   * using a Reader to produce one large operation that accepts
   * the configuration as a parameter
   */

  case class Cat(name: String, favoriteFood: String)

  // defined class Cat

  val catName: Reader[Cat, String] = Reader(cat => cat.name)
  // catName: cats.data.Reader[Cat,String] = Kleisli(<function1>)

  /**
   * we can use catName.run to extract the function from the Reader.
   *
   * Using the map function on a Reader means pipelining in this case
   * a function that takes cat.name -String- as a parameter
   */

  val greetKitty: Reader[Cat, String] = catName.map(name => s"Hello ${name}")

  greetKitty.run(Cat("Heathcliff", "junk food"))
  // res1: cats.Id[String] = Hello Heathcliff

  /**
   * flatMap method allows us to combine more Readers:
   */

  val feedKitty: Reader[Cat, String] =
    Reader(cat => s"Have a nice bowl of ${cat.favoriteFood}")

  val greetAndFeed: Reader[Cat, String] =
    for {
      greet <- greetKitty
      feed <- feedKitty
    } yield s"$greet. $feed."

  greetAndFeed(Cat("Garfield", "lasagne"))

  // res3: cats.Id[String] = Hello Garfield. Have a nice bowl of lasagne.

}
