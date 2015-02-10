import org.specs2.mutable._
import spray.json._
import DefaultJsonProtocol._ // if you don't supply your own Protocol (see below)

  class HelloWorldSpec extends Specification {

    "The Seq" should {
      "be the same after serialization and deserialization" in {
        "Hello world" must have size(11)
        val seq = Seq(1, 2, 3)
        val json = seq.toJson
        val desSeq = json.convertTo[Seq[Int]]
        desSeq must_===(seq)
      }
    }
  }