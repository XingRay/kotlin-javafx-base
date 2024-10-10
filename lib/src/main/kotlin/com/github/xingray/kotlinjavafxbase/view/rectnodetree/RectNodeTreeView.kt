package com.github.xingray.kotlinjavafxbase.view.rectnodetree

import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.MouseButton
import javafx.scene.layout.Region
import javafx.scene.paint.Color
import javafx.scene.transform.Rotate

class RectNodeTreeView : Region() {
    private val canvas = Canvas()

    // 使用 Property 来管理 RectNode 数据
    val nodeTreeProperty: ObjectProperty<RectNodeTree?> = SimpleObjectProperty()

    // 对外暴露的 rootNodeProperty，允许外部进行数据绑定或设置值
    var nodeTree: RectNodeTree?
        get() = nodeTreeProperty.value
        set(value) {
            nodeTreeProperty.value = value
        }

    // 当前被选中的节点
    private var highlightedNode: RectNode? = null

    init {
        children.add(canvas)
        // 监听 rootNodeProperty 的变化，自动刷新显示
        nodeTreeProperty.addListener { _, _, newNodeTree ->
            drawNodeBounds(newNodeTree)
        }

        // 监听鼠标移动事件
        canvas.setOnMouseMoved { event ->
            val nodeTree = this.nodeTree ?: return@setOnMouseMoved
            // 计算缩放比例
            val scaleX = canvas.width / nodeTree.width
            val scaleY = canvas.height / nodeTree.height
            val scale = minOf(scaleX, scaleY)

            val x = (event.x / scale).toInt()
            val y = (event.y / scale).toInt()

            highlightedNode = findNodeAt(x, y, nodeTree.node)
            drawNodeBounds(nodeTree)  // 重新绘制节点，更新高亮显示
        }

        // 监听右键点击事件
        canvas.setOnMouseClicked { event ->
            if (event.button == MouseButton.SECONDARY) {
                val nodeTree = this.nodeTree ?: return@setOnMouseClicked
                // 计算缩放比例
                val scaleX = canvas.width / nodeTree.width
                val scaleY = canvas.height / nodeTree.height
                val scale = minOf(scaleX, scaleY)

                val x = (event.x / scale).toInt()
                val y = (event.y / scale).toInt()

                val clickedNode = findNodeAt(x, y, nodeTree.node)
                if (clickedNode != null) {
                    clickedNode.enable = false
                }
            }
        }
    }

    // 绘制节点及其子节点的边界框
    // 绘制节点及其子节点的边界框
    private fun drawNodeBounds(nodeTree: RectNodeTree?) {
        val gc = canvas.graphicsContext2D
        gc.clearRect(0.0, 0.0, canvas.width, canvas.height)

        if (nodeTree == null) {
            return
        }
        val node = nodeTree.node ?: return

        val scaleX = canvas.width / nodeTree.width
        val scaleY = canvas.height / nodeTree.height
        val scale = minOf(scaleX, scaleY)

        // 设置旋转角度
        val rotate = Rotate(nodeTree.rotation.toDouble(), canvas.width / 2, canvas.height / 2)
        gc.save()  // 保存当前的图形上下文
        gc.transform(rotate.mxx, rotate.myx, rotate.mxy, rotate.myy, rotate.tx, rotate.ty)

        // 按比例绘制根节点及其子节点的边界框
        drawRect(gc, node, scale)

        gc.restore()  // 恢复图形上下文
    }


    // 递归绘制每个节点的矩形边界
    private fun drawRect(gc: GraphicsContext, node: RectNode, scale: Double) {
        if (node == highlightedNode) {
            gc.stroke = Color.YELLOW  // 高亮显示的颜色
            gc.setLineDashes(0.0)   // 取消虚线
        } else {
            gc.stroke = Color.RED
            gc.setLineDashes(5.0)
        }

        val scaledLeft = node.left * scale
        val scaledTop = node.top * scale
        val scaledWidth = node.width * scale
        val scaledHeight = node.height * scale

        gc.strokeRect(scaledLeft, scaledTop, scaledWidth, scaledHeight)

        node.children?.forEach { childNode ->
            drawRect(gc, childNode, scale)
        }
    }

    override fun layoutChildren() {
        super.layoutChildren()
        canvas.width = width
        canvas.height = height
        drawNodeBounds(nodeTree)
    }

    // 根据鼠标位置查找最末端包含该位置的节点
    private fun findNodeAt(x: Int, y: Int, node: RectNode?): RectNode? {
        if (node == null || node.isEmpty || !node.enable) return null

        // 检查当前节点是否包含鼠标位置
        if (x >= node.left && x <= node.right && y >= node.top && y <= node.bottom) {
            // 递归检查子节点
            node.children?.forEach { childNode ->
                val foundNode = findNodeAt(x, y, childNode)
                if (foundNode != null) return foundNode
            }
            // 如果没有子节点包含该位置，返回当前节点
            return node
        }
        return null
    }

}