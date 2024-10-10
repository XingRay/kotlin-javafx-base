package com.github.xingray.kotlinjavafxbase.ext

import com.github.xingray.coinfarmer.android.resource.Resource
import javafx.scene.image.Image

fun Resource.readImage(): Image? {
    return use { Image(it) }
}