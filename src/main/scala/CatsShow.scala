/**
 * CAT SHOW IS EQUAL TO Printable, but easier in syntax
 */


import java.util.Date

import cats.instances.int._
import cats.instances.string._
import cats.Show

class CatsShow {

  val showInt: Show[Int] = Show.apply[Int]
  val showString: Show[String] = Show.apply[String]


  import cats.syntax.show._ // for show
  val shownInt = 123.show
  // shownInt: String = 123
  val shownString = "abc".show
  // shownString: String = abc

  /**
   * It is possible to implements Show for others types, using the method .show
   * Same thing with interface Eq
   */

  implicit val dateShow: Show[Date] =
    Show.show(date => s"${date.getTime}ms since the epoch.")

  import cats.Eq
  import cats.syntax.eq._ //syntax for Eq
  import cats.instances.long._ //for Eq

  implicit val dateEq: Eq[Date] =
    Eq.instance[Date] { (date1, date2) =>
      date1.getTime === date2.getTime
    }
}
