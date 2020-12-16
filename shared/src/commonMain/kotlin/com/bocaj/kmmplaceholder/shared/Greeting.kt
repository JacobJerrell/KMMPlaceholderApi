package com.bocaj.kmmplaceholder.shared

import com.bocaj.kmmplaceholder.shared.model.User
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.util.*

object SharedApi {

    @KtorExperimentalAPI
    private val client = HttpClient() {
        install(JsonFeature) {

        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }

    @KtorExperimentalAPI
    suspend fun getUsers(): List<User> {
        val resource = PlaceholderRoute.Users()
        val userList: MutableList<User> = mutableListOf()
        try {
            val usersResponse = client.get<List<User>>(resource.buildUrl())
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

/**
    GET	/comments?postId=1
    POST	/posts
    PUT	/posts/1
    PATCH	/posts/1
    DELETE	/posts/1
 */

/**
    /posts/1/comments
    /albums/1/photos
*/

sealed class PlaceholderRoute {

    private val host: String = "https://jsonplaceholder.typicode.com"

    protected abstract val path: Path
    protected open val id: Int? = null
    protected open val subPath: Path? = null

    fun buildUrl(): String {
        val components = mutableListOf(host, path.route)
        return if (subPath != null && id == null)
            components.joinToString("/")
        else
            components.plus(listOfNotNull(id?.toString(), subPath?.route)).joinToString("/")
    }

    //
    // Posts
    //      /posts
    //      /posts/1
    //      /posts/1/comments
    //
    open class Posts(override val id: Int? = null): PlaceholderRoute() {
        override val path: Path = Path.POSTS
    }
    class Comments(postId: Int): Posts(postId) {
        override val subPath: Path = Path.COMMENTS
    }

    // Users
    //    /users
    //    /users/1
    //    /users/1/albums
    //    /users/1/todos
    //    /users/1/posts
    open class Users(override val id: Int? = null): PlaceholderRoute() {
        override val path: Path = Path.USERS
    }
    class Albums(userId: Int): Users(userId) {
        override val subPath: Path = Path.ALBUMS
    }
    class Todos(userId: Int): Users(userId) {
        override val subPath: Path = Path.TODOS
    }

    protected enum class Path(val route: String) {
        POSTS("posts"),
        COMMENTS("comments"),
        ALBUMS("albums"),
        PHOTOS("photos"),
        TODOS("todos"),
        USERS("users");
    }
}

