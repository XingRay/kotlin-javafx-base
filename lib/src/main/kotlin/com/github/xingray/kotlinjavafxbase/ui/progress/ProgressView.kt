package com.github.xingray.kotlinjavafxbase.ui.progress

import com.github.xingray.kotlinjavafxbase.view.anchor
import com.github.xingray.kotlinjavafxbase.view.setSize
import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleIntegerProperty
import javafx.scene.control.Label
import javafx.scene.control.ProgressBar
import javafx.scene.layout.AnchorPane

class ProgressView() : AnchorPane() {

    val lbProgressInfo = Label("")
    val pbProgress = ProgressBar()
    val lbProgressText = Label("0/1")


    val progressInfoProperty = lbProgressInfo.textProperty()

    val progressProperty = SimpleIntegerProperty()
    var progress: Int
        get() = progressProperty.value
        set(value) {
            progressProperty.value = value
        }

    val totalProperty = SimpleIntegerProperty()
    var total: Int
        get() = totalProperty.value
        set(value) {
            totalProperty.value = value
        }

    init {
        setSize(400.0, 200.0)

        lbProgressInfo.anchor(10.0, 10.0, 10.0)
        pbProgress.anchor(10.0, 60.0, bottom = 10.0)
        lbProgressText.anchor(right = 10.0, bottom = 10.0)

        this.children.addAll(lbProgressInfo, pbProgress, lbProgressText)

        pbProgress.progressProperty().bind(Bindings.createDoubleBinding({
            val total = this.total
            val progress = this.progress
            if (total == 0) {
                1.0
            } else {
                progress.toDouble() / total.toDouble()
            }
        }, progressProperty, totalProperty))

        lbProgressText.textProperty().bind(Bindings.createStringBinding({
            "${progress}/${total}"
        }, progressProperty, totalProperty))
    }
}