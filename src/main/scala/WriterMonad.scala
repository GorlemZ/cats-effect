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
   * recording sequences of steps in mulঞthreaded computaঞons
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
}


