package com.github.xingray.kotlinjavafxbase.view.rectnodetree

data class RectNode(val left: Int, val top: Int, val right: Int, val bottom: Int, val children: List<RectNode>?, val raw: Any, var enable: Boolean = true) {
    val width = right - left
    val height = bottom - top
    val isEmpty = (width <= 0) or (height <= 0)
}