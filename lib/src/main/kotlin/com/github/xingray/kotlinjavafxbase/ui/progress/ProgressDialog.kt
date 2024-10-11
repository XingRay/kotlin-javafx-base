package com.github.xingray.kotlinjavafxbase.ui.progress

import com.github.xingray.kotlinjavafxbase.page.BaseStage
import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.StageStyle

class ProgressDialog : BaseStage() {

    val progressView = ProgressView()

    init {
        val stage = this
        // 设置弹窗的模态模式 (阻止与主窗口交互)
        stage.initModality(Modality.APPLICATION_MODAL)
        stage.initStyle(StageStyle.UNDECORATED)
        stage.title = title

        // 设置弹窗的场景并显示
        val scene = Scene(progressView)

        stage.scene = scene
    }


}