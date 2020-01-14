// Define a very simple JSON AST

sealed trait Json
final case class JsObject(get: Map[String, Json]) extends Json
final case class JsString(get: String) extends Json
final case class JsNumber(get: Double) extends Json
case object JsNull extends Json
// The "serialize to JSON" behaviour is encoded in this trait
trait JsonWriter[A] {
  def write(value: A): Json
}

final case class Person(name: String, email: String)
object JsonWriterInstances {
  implicit val stringWriter: JsonWriter[String] =
    new JsonWriter[String] {
      def write(value: String): Json =
        JsString(value)
    }
  implicit val personWriter: JsonWriter[Person] =
    new JsonWriter[Person] {
      def write(value: Person): Json =
        JsObject(Map(
          "name" -> JsString(value.name),
          "email" -> JsString(value.email)
        ))
    }
  // etc...
}

object Json {
  def toJson[A](value: A)(implicit w: JsonWriter[A]): Json =
    w.write(value)
}


class OtherWriter {
  implicit def optionWriter[A]
  (implicit writer: JsonWriter[A]): JsonWriter[Option[A]] =
    new JsonWriter[Option[A]] {
      def write(option: Option[A]): Json =
        option match {
          case Some(aValue) => writer.write(aValue)
          case None => JsNull
        }
    }
}
/**
 * When the compiler sees an
 * expression like this:
 * Json.toJson(Option("A string"))
 * it searches for an implicit JsonWriter[Option[String]].
 * It finds the implicit method for JsonWriter[Option[A]]:
 *
 */
