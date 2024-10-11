package com.github.xingray.kotlinjavafxbase.ext

import com.github.xingray.kotlinbase.resource.Resource
import javafx.scene.image.Image

fun Resource.readImage(): Image? {
    return use { Image(it) }
}