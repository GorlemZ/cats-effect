import cats.data.Reader
import cats.syntax.applicative._ // for pure

object ReaderMonadExercise extends App {



  /**
   * the classic use of Reader consist in building programs
   * that take a conf as a parameter.
   */
  case class Db(
                 userNames: Map[Int, String],
                 passwords: Map[String, String]
               )

  type DbReader[A] = Reader[Db, A]

  def findUsername(userId: Int): DbReader[Option[String]] =
    Reader(db => db.userNames.get(userId))

  def checkPassword(username: String, password: String): DbReader[Boolean] =
    Reader(db => db.passwords(username).contains(password))

  /**
   * combining two readers to check the login:
   */

  def checkLogin(userId: Int, password: String): DbReader[Boolean] = {
    for {
      username   <- findUsername(userId)
      passwordOk <- username.map { username =>
        checkPassword(username, password)
      }.getOrElse {
        Reader[Db, Boolean](db => false)
      }
    } yield passwordOk
  }

  val users = Map(
    1 -> "dade",
    2 -> "kate",
    3 -> "margo"
  )

  val passwords = Map(
    "dade"  -> "zerocool",
    "kate"  -> "acidburn",
    "margo" -> "secret"
  )

  val db = Db(users, passwords)

  checkLogin(1, "zerocool").run(db)
  // res10: cats.Id[Boolean] = true

  checkLogin(4, "davinci").run(db)
  // res11: cats.Id[Boolean] = false


}
