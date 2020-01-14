final case class Cat(name: String, age: Int, color: String)

trait Printable[A] {
  def format(value: A):String
}

object PrintableInstances{
  implicit val printableString : Printable[String] = {
    new Printable[String]{
      override def format(value: String): String = value
    }
  }

  implicit val printableInt : Printable[Int] = {
    new Printable[Int] {
      override def format(value: Int): String = s"$value"
    }
  }

  implicit val printableCat : Printable[Cat] ={
    new Printable[Cat] {
      override def format(value: Cat): String =
        s"${value.name} is a ${value.age} yo ${value.color} cat."
    }
  }
}

object Printable{
  def format[A](value: A)(implicit printable: Printable[A]): String ={
    printable.format(value)
  }

  def print[A](value: A)(implicit  printable: Printable[A]):Unit ={
    println(format(value))
  }
}

