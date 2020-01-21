import cats.Id
import cats.instances.vector._
import cats.syntax.applicative._
import cats.data.{Writer, WriterT}
import cats.instances.vector._
import cats.syntax.writer._ // for tell

class WriterMonad {

  /**
   * cats.data.Writer is a monad
   * that lets us carry a log along with a computation,
   * recording sequences of steps in multithreaded computations
   * where standard imperative logging techniques can result
   * in interleaved messages from different contexts.
   * Cats implements writer in terms of another type, WriterT
   */

  type Logged[A] = Writer[Vector[String], A]

  123.pure[Logged]
  // res2: Logged[Int] = WriterT((Vector(),123))

  /**
   * i can set just a logger:
   */

  Vector("msg1", "msg2", "msg3").tell
  // res3: cats.data.Writer[scala.collection.immutable.Vector[String],
  //Unit] = WriterT((Vector(msg1, msg2, msg3),()))

  /**
   * If i have both log msg and result, and extract the
   * result with .value and the message with .written,
   * or both with .run
   */

  val a: WriterT[Id, Vector[String], Int] = Writer(Vector("msg1", "msg2", "msg3"), 123)
  // a: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[
  //String],Int] = WriterT((Vector(msg1, msg2, msg3),123))

  val b: Writer[Vector[String], Int] = 123.writer(Vector("msg1", "msg2", "msg3"))
  // b: cats.data.Writer[scala.collection.immutable.Vector[String],Int]
  //= WriterT((Vector(msg1, msg2, msg3),123))

  val aResult: Int =  a.value
  // aResult: Int = 123

  val aLog: Vector[String] = a.written
  // aLog: Vector[String] = Vector(msg1, msg2, msg3)

  val (log, result) = b.run
  // log: scala.collection.immutable.Vector[String] = Vector(msg1, msg2, msg3)
  // result: Int = 123

  val writer1: WriterT[Id, Vector[String], Int] = for {
    a <- 10.pure[Logged]
    _ <- Vector("a", "b", "c").tell
    b <- 32.writer(Vector("x", "y", "z"))
  } yield a + b
  // writer1: cats.data.WriterT[cats.Id,Vector[String],Int] =
  // WriterT((Vector(a, b, c, x, y, z),42))

  /**
   * IMPORTANT NOTE
   * Notice that the type reported on the console is actually
   * WriterT[Id, Vector[String], Int] instead of
   * Writer[Vector[String], Int] as we might expect.
   * In the spirit of code reuse, Cats implements Writer in
   * terms of another type, WriterT
   */

  val writer2 = writer1.mapWritten(_.map(_.toUpperCase))
  // writer2: cats.data.WriterT[cats.Id,scala.collection.immutable.Vector[String],Int]
  // = WriterT((Vector(A, B, C, X, Y, Z),42))

  /**
   * We can transform the log in a Writer using mapWritten or both
   * simultaneously using bimap (two parameters functions) or mapBoth
   * (one function with two parameters)
   */

  /**
   * CLEARING THE LOG:
   */

  writer2.reset
  // writer5: cats.data.WriterT[cats.Id,Vector[String],Int] =
  // WriterT((Vector(),42))


}


