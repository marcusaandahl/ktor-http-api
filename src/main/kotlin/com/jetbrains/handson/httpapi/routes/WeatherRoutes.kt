package com.jetbrains.handson.httpapi.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

const val INT_REGEX = """^\d+$"""; //Any amount of digits
const val LOC_REGEX = """^(\d{4})(,\d{4})*$"""; //4 digits + infinite x 4 digits separated by commas
const val SING_LOC_REGEX = """^(\d{4})$"""; //4 digits only
const val UNIT_REGEX = """^(celsius|fahrenheit)$""";

fun Route.weatherRouting() {
    route("/weather") {
        get("/summary") {
            val unit = if (call.request.queryParameters["unit"]?.matches(Regex(UNIT_REGEX)) == true) call.request.queryParameters["unit"] else return@get call.respondText(
                "Missing or malformed unit query parameter",
                status = HttpStatusCode.BadRequest
            )
            val temperature = if (call.request.queryParameters["temperature"]?.matches(Regex(INT_REGEX)) == true) call.request.queryParameters["temperature"]?.toInt() else return@get call.respondText(
                "Missing or malformed temperature query parameter",
                status = HttpStatusCode.BadRequest
            )
            val locations = if (call.request.queryParameters["locations"]?.matches(Regex(LOC_REGEX)) == true) call.request.queryParameters["locations"]?.split(",") else return@get call.respondText(
                "Missing or malformed locations query parameter",
                status = HttpStatusCode.BadRequest
            )

            

            call.respondText("Locations are ${locations?.get(0)}");
        }

        get("/location/{latLong}") {
            val location = if (call.parameters["latLong"]?.matches(Regex(SING_LOC_REGEX)) == true) call.parameters["latLong"]?.toInt() else return@get call.respondText(
                "Missing or malformed id",
                status = HttpStatusCode.BadRequest
            )
            call.respondText("Location is $location")
        }
    }
}

fun Application.registerWeatherRoutes() {
    routing {
        weatherRouting()
    }
}