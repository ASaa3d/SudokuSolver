package core


import kotlinx.coroutines.CancellationException
import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val code: Int, val message: String)

class ApiException(code:Int , message: String) : Exception()
sealed class InputError : Exception() {
    data object InvalidUserName : InputError()
    data object InvalidEmail : InputError()
    data object InvalidPassword : InputError()
    data object InvalidCode : InputError()
}
suspend fun<T> safeCall(run: suspend()->T) : Result<T>
= try {
    Result.success(run())
    }
    catch (e: CancellationException)
    {
        throw e
    }
    catch (e: ApiException)
    {
        Result.failure(e)
    }
    catch (e: Exception)
    {
        Result.failure(e)
    }

