
import cats.Monad
import cats.effect.{Async, IO, Sync}
import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
 * this is a pure program, has non-testable side effects
 */

class SimpleIOProgram {
  val program: IO[Unit] =
    for {
      _ <- IO(println("Enter your name:"))
      n <- IO(scala.io.StdIn.readLine())
      _ <- IO(println(s"Hello, $n!"))
    } yield ()
}

trait Console[F[_]] {
  def putString(str: String): F[Unit]
  def readLn: F[Unit]
}


/*

  def program[F[Unit] : Monad](implicit C: Console[F]): F[Unit] =
    for {
      _ <- C.putString("Enter your name:")
      n <- C.readLn
      _ <- C.putString(s"Hello, $n!")
    } yield ()

*/

class StdConsole[F[_] : Sync] extends Console[F] {
  override def putString(str: String): F[Unit] = Sync[F].delay(println(str))

  override def readLn: F[Unit] = Sync[F].delay(scala.io.StdIn.readLine)
}
/*
class RemoteConsole[F[_] : Async] extends Console[F] {
  private def fromFuture[A](fa: F[Future[A]]): F[A] =
    fa.flatMap { future =>
      Async[F].async { cb =>
        future.onComplete {
          case Success(x) => cb(Right(x))
          case Failure(e) => cb(Left(e))
        }
      }
    }

  //per i seguenti metodi, con una console remota si potrebbe usare un servizio POST

  override def putString(str: String): F[Unit] = fromFuture((Sync[F].delay(???)))

  override def readLn: F[Unit] = fromFuture(Sync[F].delay(???))
}

 */

