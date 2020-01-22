import cats.data.State
import State._
import cats.syntax.applicative._ // for pure

object StateMonad extends App {

  val a = State[Int, String] { state =>
    (state, s"The state is $state")
  }
  // a: cats.data.State[Int,String] = cats.data.IndexedStateT@6cca9e53


  /**
   * Each method returns an instance of Eval,
   * which State uses to maintain stack safety.
   * We call the value method as usual to extract the actual result
   */

  // Get the state and the result:
  val (state1, result1) = a.run(10).value
  // state: Int = 10
  // result: String = The state is 10

  // Get the state, ignore the result:
  val state = a.runS(10).value
  // state: Int = 10

  // Get the result, ignore the state:
  val result = a.runA(10).value
  // result: String = The state is 10

  /**
   * we can assemble building blocks for state Monad with
   * for comprehension (ignoring the intermediate result):
   */



  val program: State[Int, (Int, Int, Int)] = for {
    a <- get[Int]
    _ <- set[Int](a + 1)
    b <- get[Int]
    _ <- modify[Int](_ + 1)
    c <- inspect[Int, Int](_ * 1000)
  } yield (a, b, c)
  // program: cats.data.State[Int,(Int, Int, Int)] = cats.data.IndexedStateT@528336cd

  val (state2, result2) = program.run(1).value
  // state: Int = 3
  // result: (Int, Int, Int) = (1,2,3000)

  //--------------------------------------------------------------------------


  /**
   * exercise: post-Order calculator
   */


  type CalcState[A] = State[List[Int], A]

  def operator(func: (Int, Int) => Int): CalcState[Int] =
    State[List[Int], Int] {
      case b :: a :: tail =>
        val ans = func(a, b)
        (ans :: tail, ans)

      case _ =>
        sys.error("Fail!")
    }

  def operand(num: Int): CalcState[Int] =
    State[List[Int], Int] { stack =>
      (num :: stack, num)
    }


  def evalOne(sym: String): CalcState[Int] =
    sym match {
      case "+" => operator(_ + _)
      case "-" => operator(_ - _)
      case "*" => operator(_ * _)
      case "/" => operator(_ / _)
      case num => operand(num.toInt)
    }

  def evalAll(input: List[String]): CalcState[Int] =
    input.foldLeft(0.pure[CalcState])((a,b) => a.flatMap(_ => evalOne(b)))

  val program2 = evalAll(List("1", "2", "+", "3", "*"))

  println(program2.runA(Nil).value)

  def evalInput(s: String) = {
    evalAll(s.split(" ").toList).runA(Nil).value
  }
}
