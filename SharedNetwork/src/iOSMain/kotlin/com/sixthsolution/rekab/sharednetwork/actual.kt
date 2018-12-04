package com.sixthsolution.rekab.sharednetwork

//  Created by Mehdi Sohrabi on 12/4/18.
//  Copyright © 2018 sixthsolution. All rights reserved.

import kotlin.coroutines.*
import kotlinx.coroutines.*
import platform.darwin.*

internal actual val dispatcher: CoroutineDispatcher = NsQueueDispatcher(dispatch_get_main_queue())

internal class NsQueueDispatcher(private val dispatchQueue: dispatch_queue_t) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatchQueue) {
            block.run()
        }
    }
}
