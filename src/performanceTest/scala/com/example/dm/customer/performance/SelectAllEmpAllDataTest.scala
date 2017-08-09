package com.example.dm.customer.performance;

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class SelectAllEmpAllDataTest extends Simulation{

val testParams = new TestParams()
			val serviceURL = testParams.serviceUrl()
			val testProfile = testParams.profile()

			val httpConf = http
			.baseURL(serviceURL)
			.acceptHeader("application/xml")
			.header("Version", "1.0")
			.header("Source", "MySQL")
			.header("Destination", "Local")
			.userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

    val scn = scenario("DmService-SelectAllEmpAllData")
    .exec(http("SelectAllEmpAllData")
    .post("/DmService/1.0")
    .body(RawFileBody("src/performanceTest/resources/SelectAllEmpAllDataPerformanceRequest.xml"))
    .check(status.is(200), responseTimeInMillis.lessThan(testProfile.restResponseTime()))
    )

    setUp(
    scn.inject(atOnceUsers(1), nothingFor(25), atOnceUsers(5))
    ).protocols(httpConf)
    .assertions(global.responseTime.max.lessThan(testProfile.restResponseTimeMax()))
}
