package simulations

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class OrderProcessingSimulation extends Simulation {
  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .acceptHeader("application/json")

  val scn = scenario("Process Orders")
    .exec(
      http("Process Order")
        .post("/api/orders/processOrder")
        .body(StringBody(
          """{
            |  "orderId": "${randomInt()}",
            |  "customerId": "cust-${randomInt(1000)}",
            |  "orderItems": [
            |    {"productId": "prod-${randomInt(50)}", "quantity": 1, "price": 10.0}
            |  ]
            |}""".stripMargin
        )).asJson
        .check(status.is(200))
    )

  setUp(
    scn.inject(
      rampUsersPerSec(10) to 1000 during (60 seconds),
      constantUsersPerSec(1000) during (120 seconds)
    )
  ).protocols(httpProtocol)
}