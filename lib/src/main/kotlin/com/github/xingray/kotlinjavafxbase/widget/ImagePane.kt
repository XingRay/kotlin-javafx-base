package com.github.xingray.kotlinjavafxbase.widget

import com.github.xingray.kotlinbase.util.GraphicUtil
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.StackPane

/**
 * StackPane 中放置一个 ImageView, ImageView会自动居中
 */
class ImagePane() : StackPane() {

    val imageView = ImageView()

    init {
        children.addAll(imageView)

        imageView.imageProperty().addListener { _, _, newValue ->
            val image = newValue ?: return@addListener
            updateImageViewSize(image)
        }

        widthProperty().addListener { _, _, newValue ->
            val image: Image = imageView.image ?: return@addListener
            updateImageViewSize(image)
        }

        heightProperty().addListener { _, _, newValue ->
            val image: Image = imageView.image ?: return@addListener
            updateImageViewSize(image)
        }
    }

    fun updateImageViewSize(image: Image) {
        val parentWidth = this.width
        val parentHeight = this.height
        val imageWidth = image.width
        val imageHeight = image.height
        val rect = GraphicUtil.calcRectInCenterOfParent(parentWidth, parentHeight, imageWidth, imageHeight)
        imageView.fitWidth = rect.width
        imageView.fitHeight = rect.height
    }
}