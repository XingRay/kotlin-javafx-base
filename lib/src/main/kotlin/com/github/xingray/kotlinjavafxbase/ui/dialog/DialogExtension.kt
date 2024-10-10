package com.github.xingray.coinfarmer.ui.dialog

import com.github.xingray.coinfarmer.javafx.anchor
import com.github.xingray.coinfarmer.javafx.setHeight
import com.github.xingray.coinfarmer.ui.progress.ProgressDialog
import com.github.xingray.kotlinjavafxbase.page.Controller
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.TextArea
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle

fun Controller.showConfirmDialog(msg: String): Boolean {

    // 创建一个确认弹窗
    val alert = Alert(Alert.AlertType.CONFIRMATION)
    alert.initStyle(StageStyle.UNDECORATED)
    alert.title = null
    alert.headerText = null
    alert.contentText = msg
    alert.graphic = null
    alert.dialogPane.style = "-fx-background-color: white;-fx-border-color: darkgrey;-fx-border-width: 2;"
    alert.dialogPane.padding = Insets(10.0, 10.0, 10.0, 10.0)

    // 添加确认按钮和取消按钮
    val confirmButton: Button = alert.dialogPane.lookupButton(ButtonType.OK) as Button
    confirmButton.setText("确认")

    val cancelButton: Button = alert.dialogPane.lookupButton(ButtonType.CANCEL) as Button
    cancelButton.setText("取消")

    // 显示弹窗并等待用户响应
    alert.showAndWait()
    return alert.result == ButtonType.OK
}

fun showInfoDialog(msg: String): Boolean {

    // 创建一个确认弹窗
    val alert = Alert(Alert.AlertType.INFORMATION)
    alert.initStyle(StageStyle.UNDECORATED)
    alert.title = null
    alert.headerText = null
    alert.contentText = msg
    alert.graphic = null
    alert.dialogPane.style = "-fx-background-color: white;-fx-border-color: darkgrey;-fx-border-width: 2;"
    alert.dialogPane.padding = Insets(10.0, 10.0, 10.0, 10.0)

    // 添加确认按钮和取消按钮
    val confirmButton: Button = alert.dialogPane.lookupButton(ButtonType.OK) as Button
    confirmButton.setText("确认")

    // 显示弹窗并等待用户响应
    alert.showAndWait()
    return alert.result == ButtonType.OK
}


fun Controller.showLongTextDialog(textContent: String, title: String = "文本信息") {
    // 创建新窗口 (弹窗)
    val popupStage = Stage()

    // 设置弹窗的模态模式 (阻止与主窗口交互)
    popupStage.initModality(Modality.APPLICATION_MODAL)
    popupStage.initStyle(StageStyle.UTILITY)
    popupStage.title = title

    // 创建 TextArea 并设置文本内容
    val textArea = TextArea()
    textArea.text = textContent
    textArea.isWrapText = true
    textArea.isEditable = false // 禁止编辑

    // 创建 "确定" 按钮，点击时关闭弹窗
    val confirmButton = Button("确定")
    confirmButton.setOnAction {
        popupStage.close() // 关闭弹窗
    }
    confirmButton.setHeight(40.0)

    // 将 TextArea 和按钮添加到布局中
    AnchorPane.setBottomAnchor(textArea, 40.0)
    textArea.anchor(0.0, 0.0, 0.0, 40.0)
    confirmButton.anchor(left = 0.0, right = 0.0, bottom = 0.0)
    val pane = AnchorPane(textArea, confirmButton)
    pane.prefHeight = 600.0  // 设置弹窗高度
    pane.prefWidth = 800.0   // 设置弹窗宽度

    // 设置弹窗的场景并显示
    val scene = Scene(pane)
    popupStage.scene = scene
    popupStage.showAndWait()  // 等待弹窗关闭
}

fun Controller.showProgressDialog(title: String = "文本信息"): ProgressDialog {
    val progressDialog = ProgressDialog()
    progressDialog.title = title
    return progressDialog
}