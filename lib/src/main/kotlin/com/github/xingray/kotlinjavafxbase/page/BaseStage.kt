package com.github.xingray.kotlinjavafxbase.page

import javafx.event.EventHandler
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.WindowEvent
import java.util.*

open class BaseStage : Stage {
    private val onCloseEventHandlers: MutableList<EventHandler<WindowEvent>> = LinkedList<EventHandler<WindowEvent>>()

    constructor() {
        initOnCloseEventHandler()
    }

    constructor(style: StageStyle?) : super(style) {
        initOnCloseEventHandler()
    }

    fun addOnCloseEventHandler(eventEventHandler: EventHandler<WindowEvent>) {
        onCloseEventHandlers.add(eventEventHandler)
    }

    private fun initOnCloseEventHandler() {
        super.setOnCloseRequest(object : EventHandler<WindowEvent?> {
            override fun handle(event: WindowEvent?) {
                val handlers: List<EventHandler<WindowEvent>> = onCloseEventHandlers
                if (handlers.isEmpty()) {
                    return
                }
                for (eventHandler in handlers) {
                    eventHandler.handle(event)
                }
            }
        })
    }
}