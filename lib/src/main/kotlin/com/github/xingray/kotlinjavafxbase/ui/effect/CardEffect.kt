package com.github.xingray.kotlinjavafxbase.ui.effect

import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class CardEffect(cardWidth: Double, cardHeight: Double, shadowEnable: Boolean) : Rectangle(cardWidth, cardHeight) {

    private val dropShadow = DropShadow()

    init {
        this.fill = Color.WHITE
        this.stroke = Color.LIGHTGRAY
        this.strokeWidth = 1.0

        // 圆角宽度
        this.arcWidth = 20.0
        // 圆角高度
        this.arcHeight = 20.0


        dropShadow.color = Color.GRAY
        dropShadow.radius = 10.0
        dropShadow.spread = 0.1
        dropShadow.offsetX = 1.0
        dropShadow.offsetY = 1.0

        setShadowEnable(shadowEnable)
    }

    fun setShadowEnable(shadowEnable: Boolean) {
        effect = if (shadowEnable) {
            dropShadow
        } else {
            null
        }
    }
}