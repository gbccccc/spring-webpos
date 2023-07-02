package gatlingtest

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class Aw09 extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
  val headers_json = Map("Content-Type" -> "application/json")
  val scn = scenario("Testing")
    .repeat(10) {
      exec(
        http("check products page 0")
          .get("/products?pageId=0&numPerPage=10")
      ).pause(1).exec(
        http("check products page 1")
          .get("/products?pageId=1&numPerPage=10")
      ).pause(1).exec(
        http("check product 0071480935")
          .get("/products/0071480935")
      )
      .pause(1).exec(
        http("add order wrong password")
          .post("/orders/new-order")
          .headers(headers_json)
          .body(StringBody("{\"password\":\"wrong\",\"order\":{\"userId\":\"user444\",\"items\":[{\"asin\":\"0030672120\",\"num\":9},{\"asin\":\"007739688X\",\"num\":5},{\"asin\":\"0077479777\",\"num\":1}]}}").asJSON)
      )
      // .pause(1).exec(
      //   http("add order")
      //     .post("/orders/new-order")
      //     .headers(headers_json)
      //     .body(StringBody("{\"password\":\"444\",\"order\":{\"userId\":\"user444\",\"items\":[{\"asin\":\"0030672120\",\"num\":9},{\"asin\":\"007739688X\",\"num\":5},{\"asin\":\"0077479777\",\"num\":1}]}}").asJSON)
      // )
      .pause(1).exec(
        http("check orders")
          .get("/orders")
      )
    }

  setUp(scn.inject(atOnceUsers(2000)).protocols(httpProtocol))
}