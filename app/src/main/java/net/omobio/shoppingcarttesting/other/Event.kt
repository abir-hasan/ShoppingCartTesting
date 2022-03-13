package net.omobio.shoppingcarttesting.other

/**
 * Mainly created for preventing firing of live data multiple times
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            return null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Return the content even if it has already been handled
     */
    fun peekContent(): T = content
}