package ru.itis.terraapp.feature.profile.util

import kotlin.coroutines.cancellation.CancellationException

@SuppressWarnings("TooGenericExceptionCaught")
inline fun <R> runSuspendCatching(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (c: CancellationException) {
        throw c
    } catch (e: Throwable) {
        Result.failure(e)
    }
}