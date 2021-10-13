package com.qpmini.app.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class ChatTaskDispatchers : TaskDispatchers{
    override fun io() = Dispatchers.IO

    override fun main() = Dispatchers.Main

    override fun default() = Dispatchers.Default

    override fun unconfined() = Dispatchers.Unconfined
}