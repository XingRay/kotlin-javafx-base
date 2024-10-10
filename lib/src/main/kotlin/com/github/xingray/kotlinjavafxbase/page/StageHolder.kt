package com.github.xingray.kotlinjavafxbase.page

import javafx.scene.Scene
import javafx.stage.Stage

data class StageHolder<T : Controller?>(val controller: T, val stage: Stage, val scene: Scene) {
    override fun toString(): String {
        return "StageHolder{" +
                "controller=" + controller +
                ", stage=" + stage +
                ", scene=" + scene +
                '}'
    }
}
