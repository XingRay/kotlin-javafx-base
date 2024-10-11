package com.github.xingray.kotlinjavafxbase.ui.effect

import com.github.xingray.kotlinjavafxbase.view.anchor
import javafx.scene.effect.DropShadow
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

class CardEffect(cardWidth: Double, cardHeight: Double, shadowEnable: Boolean) : Rectangle(cardWidth, cardHeight) {

    val dropShadow = DropShadow()

    init {
        this.setFill(Color.WHITE)
        this.setStroke(Color.LIGHTGRAY)
        this.setStrokeWidth(1.0)
        this.anchor(0.0, 0.0, 0.0, 0.0)

        // 圆角宽度
        this.setArcWidth(20.0)
        // 圆角高度
        this.setArcHeight(20.0)


        dropShadow.setColor(Color.GRAY)
        dropShadow.setRadius(10.0)
        dropShadow.setSpread(0.1)
        dropShadow.setOffsetX(1.0)
        dropShadow.setOffsetY(1.0)

        setShadowEnable(shadowEnable)
    }

    fun setShadowEnable(shadowEnable: Boolean) {
        if (shadowEnable) {
            effect = dropShadow
        } else {
            effect = null
        }
    }
}