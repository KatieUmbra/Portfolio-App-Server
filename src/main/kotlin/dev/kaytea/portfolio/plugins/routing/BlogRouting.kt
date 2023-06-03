package dev.kaytea.portfolio.plugins.routing

import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureBlogRouting() {
    routing {
        route("/blog") {
            get("/page-{pageNumber}") {
                val pageNumber = call.parameters["pageNumber"]?.toIntOrNull() ?: 1
                TODO("return a list of the last 10 markdown posts data, starting at page $pageNumber")
            }
            // Posts Handlers
            get("/post/{postId}") {
                val postId = call.parameters["postId"]?.toIntOrNull()
                TODO("return the markdown post with id $postId")
            }
            post("/post/{postId}") {
                val postId = call.parameters["postId"]?.toIntOrNull()
                TODO("create post with id $postId")
            }
            put("/post/{postId}") {
                val postId = call.parameters["postId"]?.toIntOrNull()
                TODO("edit post with id $postId")
                // delete old file and create new file with new data
            }
        }
    }
}