package com.github.xingray.kotlinjavafxbase.widget

import com.github.xingray.kotlinbase.graphic.calcRectInCenterOfParent
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

/**
 * StackPane 中放置一个 ImageView, ImageView会自动居中
 */
class ImagePane() : StackPane() {

    val imageView = ImageView()

    init {
        val screenImageContainer = this

        imageView.isPreserveRatio = true
        screenImageContainer.children.addAll(imageView)
        imageView.imageProperty().addListener { _, _, newValue ->
            val image = newValue ?: return@addListener
            val rect = calcRectInCenterOfParent(width, height, image.width, image.height)
            imageView.fitWidth = rect.width
            imageView.fitHeight = rect.height
        }

        widthProperty().addListener { _, _, newValue ->
            val image = imageView.image
            val rect = calcRectInCenterOfParent(width, height, image.width, image.height)
            imageView.fitWidth = rect.width
            imageView.fitHeight = rect.height
        }

        heightProperty().addListener { _, _, newValue ->
            val image = imageView.image
            val rect = calcRectInCenterOfParent(width, height, image.width, image.height)
            imageView.fitWidth = rect.width
            imageView.fitHeight = rect.height
        }
    }
}