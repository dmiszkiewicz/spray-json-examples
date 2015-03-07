import org.specs2.mutable._
import spray.json._

class HelloWorldSpec extends Specification {

  case class Person(name: String, age: Int)

  case class Storage[A](slot: A)

  case class Color(val name: String, val red: Int, val green: Int, val blue: Int)

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val personFormat = jsonFormat2(Person)

    //Example jsonFormat for case classes
    implicit def storageFormat[A: JsonFormat] = jsonFormat1(Storage.apply[A])

    //Example jsonFormat for more extended classes( I can just use here a jsonFormat4, but i wanted to show how to handle more complex cases)
    implicit object ColorJsonFormat extends RootJsonFormat[Color] {
      def write(c: Color) = JsObject(
        "name" -> JsString(c.name),
        "red" -> JsNumber(c.red),
        "green" -> JsNumber(c.green),
        "blue" -> JsNumber(c.blue)
      )

      def read(value: JsValue) = {
        value.asJsObject.getFields("name", "red", "green", "blue") match {
          case Seq(JsString(name), JsNumber(red), JsNumber(green), JsNumber(blue)) =>
            new Color(name, red.toInt, green.toInt, blue.toInt)
          case _ => throw new DeserializationException("Color expected")
        }
      }
    }

  }

  import MyJsonProtocol._

  "The Seq" should {
    "be the same after serialization and deserialization" in {
      val seq = Seq(1, 2, 3)
      val json = seq.toJson
      val desSeq = json.convertTo[Seq[Int]]
      desSeq must_=== (seq)
    }
  }

  "The person" should {
    "be the same after serialization and deserialization" in {
      val p = Person("Dominik", 21)
      val json = p.toJson
      val desP = json.convertTo[Person]
      desP must_=== (p)
    }
  }

  "The Option" should {
    "be the same after serialization and deserialization" in {
      val o = Option("test")
      val json = o.toJson
      val desO = json.convertTo[Option[String]]
      desO must_=== (o)
    }
  }

  "The Seq of persons" should {
    "be the same after serialization and deserialization" in {
      val seq = Seq(Person("Dominik", 21), Person("Karol", 23), Person("Piotr", 25))
      val json = seq.toJson
      val desSeq = json.convertTo[Seq[Person]]
      desSeq must_=== (seq)
    }
  }

  "The storage" should {
    "be the same after serialization and deserialization" in {
      val s = Storage("car")
      val json = s.toJson
      val desS = json.convertTo[Storage[String]]
      desS must_=== (s)
    }
  }

  "The color" should {
    "be the same after serialization and deserialization" in {
      val c = Color("Some", 50, 100, 150)
      val json = c.toJson
      val desC = json.convertTo[Color]
      desC must_=== (c)
    }
  }


}