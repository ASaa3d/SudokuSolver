package core


import kotlinx.serialization.Serializable
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException

@Serializable
data class ApiError(val code: String, val message: String)

class ApiException(
    val httpStatus: Int,
    val code: String,
    override val message: String
) : Exception(message)

sealed class InputError : Exception() {
    data object InvalidUserName : InputError()
    data object InvalidEmail : InputError()
    data object InvalidPassword : InputError()
    data object InvalidCode : InputError()
}


/** Carries a message that is already safe to show to the user. */
class UserMessageException(
    override val message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)

suspend fun <T> safeCall(run: suspend () -> T): Result<T> =
    try {
        Result.success(run())
    } catch (e: ApiException) {
        // Your server already sends a good message: keep it untouched.
        Result.failure(e)
    } catch (e: Throwable) {
        currentCoroutineContext().ensureActive()
        Result.failure(UserMessageException(e.toUserMessage(), e))
    }

private fun Throwable.toUserMessage(): String {
    val chain = generateSequence(this) { it.cause }.take(6).toList()
    val text = chain.joinToString(" ") { "${it::class.simpleName} ${it.message}" }.lowercase()

    if (chain.any {
            it is HttpRequestTimeoutException ||
                    it is ConnectTimeoutException ||
                    it is SocketTimeoutException
        } || "timeout" in text || "timed out" in text
    ) {
        return "The server is taking too long to respond. It may be waking up, so please try again in a moment."
    }

    chain.filterIsInstance<ResponseException>().firstOrNull()?.let { ex ->
        return when (ex.response.status.value) {
            401, 403 -> "You are not allowed to do that. Please sign in again."
            413 -> "That image is too large. Try a smaller one."
            429 -> "Too many requests. Please wait a moment and try again."
            502, 503, 504 -> "The server is starting up or temporarily unavailable. Please try again in a moment."
            in 500..599 -> "The server ran into a problem. Please try again."
            else -> "The request could not be completed. Please try again."
        }
    }

    if ("fail to fetch" in text || "failed to fetch" in text ||
        "networkerror" in text || "load failed" in text ||
        "unknownhost" in text || "unresolvedaddress" in text ||
        "connection refused" in text || "connectexception" in text
    ) {
        return "Can't reach the server. Check your internet connection and try again."
    }

    // 4) Server replied with something we can't parse
    if (chain.any { it is SerializationException }) {
        return "The server sent an unexpected response. Please try again."
    }

    return "Something went wrong. Please try again."
}

