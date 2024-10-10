package com.github.xingray.kotlinjavafxbase.page

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

data class FrameHolder<T>(val controller: T, val stage: Stage, val scene: Scene, val frame: Parent) {
    override fun toString(): String {
        return "FrameHolder{" +
                "controller=" + controller +
                ", stage=" + stage +
                ", scene=" + scene +
                ", frame=" + frame +
                '}'
    }
}
