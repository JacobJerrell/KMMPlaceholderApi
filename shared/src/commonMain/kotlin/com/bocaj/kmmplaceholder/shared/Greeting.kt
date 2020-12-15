package com.bocaj.kmmplaceholder.shared

import com.bocaj.kmmplaceholder.shared.model.User
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.util.*

object SharedApi {

    @KtorExperimentalAPI
    private val client = HttpClient() {
        install(JsonFeature) {

        }
//        install(Logging) {
//            logger = Logger.DEFAULT
//            level = LogLevel.ALL
//        }
    }

    @KtorExperimentalAPI
    suspend fun getUsers(): List<User> {
        val resource = Resource.USERS
        val userList: MutableList<User> = mutableListOf()
        try {
            val usersResponse = client.get<List<User>>(resource.host + resource.path)
            userList.addAll(usersResponse)
        } catch (e: Exception) {
            println(e)
        }
        return userList
    }

    fun greeting(): String {
        return "Hello, ${Platform().platform}!"
    }
}

enum class Resource(val path: String) {
    POSTS("posts"),
    COMMENTS("comments"),
    ALBUMS("albums"),
    PHOTOS("photos"),
    TODOS("todos"),
    USERS("users");

    val host: String = "https://jsonplaceholder.typicode.com/"
}