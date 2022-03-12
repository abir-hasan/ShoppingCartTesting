package net.omobio.shoppingcarttesting.other

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class APIResponse<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): APIResponse<T> = APIResponse(
            Status.SUCCESS,
            data = data,
            message = null
        )

        fun <T> error(data: T?, message: String): APIResponse<T> = APIResponse(
            Status.ERROR,
            data = data,
            message = message
        )

        fun <T> loading(data: T?): APIResponse<T> = APIResponse(
            Status.LOADING,
            data = data,
            message = null
        )
    }

}