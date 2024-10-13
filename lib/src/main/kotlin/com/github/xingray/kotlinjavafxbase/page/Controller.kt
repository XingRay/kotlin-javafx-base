package com.github.xingray.kotlinjavafxbase.page

import com.github.xingray.kotlinbase.resource.JarInnerResource
import com.github.xingray.kotlinbase.util.TaskExecutor
import com.github.xingray.kotlinjavafxbase.autoconfig.AutoConfig
import com.github.xingray.kotlinjavafxbase.autoconfig.fieldconverters.FieldConverters
import com.github.xingray.kotlinjavafxbase.router.LayoutPath
import com.github.xingray.kotlinjavafxbase.router.PageTask
import com.github.xingray.kotlinjavafxbase.router.PageUtil
import com.github.xingray.kotlinjavafxbase.router.RouteUtil
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.CheckBox
import javafx.scene.control.ChoiceBox
import javafx.scene.control.DatePicker
import javafx.scene.control.TextInputControl
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.stage.Stage
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.function.Function

abstract class Controller {

    companion object {
        @JvmStatic
        private val TAG = Controller::class.java.simpleName

        init {
            TaskExecutor.setUiPoolExecutor { Platform.runLater(it) }
        }
    }

    var stage: BaseStage? = null
    var scene: Scene? = null
    var urlMapper: Function<String, URL>? = null

    private var autoConfig: AutoConfig? = null
    private var pageRouteMap: MutableMap<String, PageTask>? = null
    private var parent: Controller? = null
    private var frameHolderMap: MutableMap<Pane, MutableMap<Class<out Controller?>, FrameHolder<out Controller>>?>? = null
    private var currentFrameHolderMap: MutableMap<Pane, FrameHolder<out Controller>?>? = null

    fun openInStage(config: (BaseStage.() -> Unit)? = null) {
        val baseStage = BaseStage()
        if (config != null) {
            baseStage.apply(config)
        }
        openInStage(baseStage)
    }

    fun openInStage(baseStage: BaseStage) {
        this.stage = baseStage
        val scene = Scene(openAsView())
        baseStage.scene = scene
        this.scene = scene
        baseStage.show()
    }

    fun openAsView(): Parent? {
        val layoutPath = this.javaClass.getAnnotation(LayoutPath::class.java).value
        if (layoutPath.startsWith("/")) {
            throw IllegalArgumentException("LayoutPath can not start with / , layoutPath:$layoutPath at ${this.javaClass.name}")
        }
        val fxmlLoader = FXMLLoader()
        fxmlLoader.setControllerFactory { this }
        val root: Parent? = JarInnerResource(layoutPath).use {
            fxmlLoader.load(it)
        }
        onCreated()

        return root
    }

    fun openInStage(inputStream: InputStream, autoClose: Boolean = true, config: (BaseStage.() -> Unit)? = null) {
        val baseStage = BaseStage()
        if (config != null) {
            baseStage.apply(config)
        }
        openInStage(inputStream, autoClose, baseStage)
    }

    fun openInStage(inputStream: InputStream, autoClose: Boolean = true, baseStage: BaseStage) {
        this.stage = baseStage
        val scene = Scene(openAsView(inputStream))
        baseStage.scene = scene
        this.scene = scene
        baseStage.show()
    }

    fun openAsView(inputStream: InputStream, autoClose: Boolean = true): Parent {
        val fxmlLoader = FXMLLoader()
        fxmlLoader.setControllerFactory { this }

        val root: Parent = if (autoClose) {
            inputStream.use {
                fxmlLoader.load(inputStream)
            }
        } else {
            fxmlLoader.load(inputStream)
        }
        onCreated()

        return root
    }

    // TODO: scene manager, scene stack, back() forward() history router
    fun openInScene(){
        val root = openAsView()
        val stage = this.stage
        if (root != null && stage != null) {
            val x = stage.scene.window.x
            val y = stage.scene.window.y
            val w = stage.scene.window.width
            val h = stage.scene.window.height

            val scene = Scene(root)
            stage.scene = scene

            stage.scene.window.x = x
            stage.scene.window.y = y
            stage.scene.window.width = w
            stage.scene.window.height = h
        }
    }

    fun runOnUiThread(task: Runnable?) {
        TaskExecutor.ui(task)
    }

    fun runOnIoThread(task: Runnable?) {
        TaskExecutor.io(task)
    }

    fun create() {
        stage?.addOnCloseEventHandler {
            hide()
            destroy()
        }
        pageRouteMap = mutableMapOf()
        frameHolderMap = mutableMapOf()
        currentFrameHolderMap = mutableMapOf()

        onCreated()
    }

    protected fun show() {
        onShow()
    }

    protected fun hide() {
        onHide()
    }

    private fun destroy() {
        val autoConfig = this.autoConfig
        autoConfig?.save()
        onDestroy()
    }

    open fun onCreated() {
    }

    open fun onShow() {
    }

    open fun onCommand(args: Array<Any?>?) {
    }

    open fun onHide() {
    }

    open fun onDestroy() {
    }

    fun enableAutoConfig() {
        val autoConfig = AutoConfig(this, TaskExecutor.ioPool(), TaskExecutor.uiPool())
        autoConfig.addFieldConverter(TextInputControl::class.java, FieldConverters.textInputControlConverter)
        autoConfig.addFieldConverter(DatePicker::class.java, FieldConverters.datePickerConverter)
        autoConfig.addFieldConverter(ChoiceBox::class.java, FieldConverters.choiceBoxConverter)
        autoConfig.addFieldConverter(CheckBox::class.java, FieldConverters.checkBoxConverter)
        autoConfig.restore()

        this.autoConfig = autoConfig
    }

    fun gotoPage(cls: Class<out Controller?>?): Boolean {
        return gotoPage(cls, null)
    }

    fun gotoPage(cls: Class<out Controller?>?, vararg args: Any?): Boolean {
        val pageTask: PageTask? = pageRouteMap!![RouteUtil.getRoutePath(cls as Class<*>)]
        if (pageTask != null) {
            pageTask.open(args as Array<Any?>)
            return true
        }

        val parent = this.parent
        if (parent != null) {
            if (parent.gotoPage(cls, *args)) {
                return true
            }
        }

        if (urlMapper != null) {
            val holder: StageHolder<out Controller?> = loadFxml(cls) as StageHolder<out Controller?>
            holder.stage.show()
            holder.controller?.show()
            holder.controller?.onCommand(args as Array<Any?>)
            return true
        }

        return false
    }

    fun addPageRoute(cls: Class<out Controller?>?, task: PageTask) {
        val routePath: String = RouteUtil.getRoutePath(cls as Class<*>) ?: return
        pageRouteMap!![routePath] = task
    }

    private fun getUrl(path: String): URL {
        return urlMapper!!.apply(path)
    }

    fun <T : Controller?> loadFxml(path: String, stage: BaseStage = BaseStage(), fxmlLoader: FXMLLoader = FXMLLoader()): StageHolder<T>? {
        val url = getUrl(path)
        return loadFxml(url, stage, fxmlLoader)
    }

    fun <T : Controller?> loadFxml(cls: Class<T>?, stage: BaseStage = BaseStage(), fxmlLoader: FXMLLoader = FXMLLoader()): StageHolder<T>? {
        val url = getUrl(PageUtil.getLayoutPath(cls as Class<*>))
        return loadFxml(url, stage, fxmlLoader)
    }

    fun <T : Controller?> loadFxml(url: URL?, stage: BaseStage = BaseStage(), fxmlLoader: FXMLLoader = FXMLLoader()): StageHolder<T>? {
        fxmlLoader.location = url
        var root: Parent? = null
        try {
            root = fxmlLoader.load()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (root == null) {
            return null
        }
        if (root is Region) {
            val region = root
            val minHeight = region.minHeight
            if (minHeight >= 0) {
                stage.minHeight = minHeight
            }
            val minWidth = region.minWidth
            if (minWidth >= 0) {
                stage.minWidth = minWidth
            }
        }
        val scene = Scene(root)
        stage.scene = scene
        val controller = fxmlLoader.getController<T>()
        if (controller != null) {
            controller.scene = scene
            controller.stage = stage
            controller.parent = this

            controller.create()
        }
        return StageHolder(controller, stage, scene)
    }

    fun <T : Controller?> openPage(path: String, stage: BaseStage = BaseStage(), fxmlLoader: FXMLLoader = FXMLLoader(), vararg args: Any?): StageHolder<T>? {
        val url = getUrl(path)
        return openPage(url, stage, fxmlLoader, *args)
    }

    fun <T : Controller?> openPage(cls: Class<T>?, stage: BaseStage = BaseStage(), fxmlLoader: FXMLLoader = FXMLLoader(), vararg args: Any?): StageHolder<T>? {
        val url = getUrl(PageUtil.getLayoutPath(cls as Class<*>))
        return openPage(url, stage, fxmlLoader, *args)
    }

    fun <T : Controller?> openPage(url: URL?, stage: BaseStage = BaseStage(), fxmlLoader: FXMLLoader = FXMLLoader(), vararg args: Any?): StageHolder<T>? {
        val holder: StageHolder<T>? = loadFxml(url, stage, fxmlLoader)
        holder?.stage?.show()
        holder?.controller?.show()
        if (args != null) {
            holder?.controller?.onCommand(args as Array<Any?>)
        }
        return holder
    }

    fun <T : Controller?> loadFrame(stage: BaseStage? = this.stage, root: Pane, cls: Class<T>, vararg args: Any?): FrameHolder<T>? {
        val resource = getUrl(PageUtil.getLayoutPath(cls))
        val fxmlLoader = FXMLLoader()
        fxmlLoader.location = resource
        var frame: Parent? = null
        try {
            frame = fxmlLoader.load()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (frame == null) {
            return null
        }

        root.children.clear()
        root.children.add(frame)
        if (root is AnchorPane) {
            AnchorPane.setTopAnchor(frame, 0.0)
            AnchorPane.setBottomAnchor(frame, 0.0)
            AnchorPane.setLeftAnchor(frame, 0.0)
            AnchorPane.setRightAnchor(frame, 0.0)
        }

        val controller = fxmlLoader.getController<T>()
        if (controller != null) {
            controller.scene = root.scene
            controller.stage = stage
            controller.parent = this

            controller.create()
            controller.show()
            if (args != null) {
                controller.onCommand(args as Array<Any?>)
            }
        }

        return FrameHolder(controller, stage as Stage, root.scene, frame)
    }

    fun <T : Controller?> switchFrame(stage: BaseStage? = this.stage, root: Pane, cls: Class<T>, vararg args: Any?): FrameHolder<T>? {
        var frameHolderMap: MutableMap<Class<out Controller?>, FrameHolder<out Controller>>? = frameHolderMap!![root]
        if (frameHolderMap == null) {
            frameHolderMap = HashMap<Class<out Controller?>, FrameHolder<out Controller>>()
            this.frameHolderMap?.set(root, frameHolderMap)
        }
        val oldFrameHolder: FrameHolder<T>? = currentFrameHolderMap?.get(root) as FrameHolder<T>?

        var newFrameHolder: FrameHolder<T>? = frameHolderMap?.get(cls) as FrameHolder<T>
        if (oldFrameHolder != null && newFrameHolder === oldFrameHolder) {
            return oldFrameHolder
        }
        if (oldFrameHolder != null) {
            oldFrameHolder.controller?.hide()
        }
        if (newFrameHolder != null) {
            root.children.clear()
            root.children.add(newFrameHolder.frame)
            newFrameHolder.controller?.show()
            if (args != null) {
                newFrameHolder.controller?.onCommand(args as Array<Any?>)
            }

            currentFrameHolderMap?.set(root, newFrameHolder as FrameHolder<out Controller>)
            return newFrameHolder
        }

        newFrameHolder = loadFrame(stage, root, cls, *args)
        if (newFrameHolder != null) {
            frameHolderMap[cls] = newFrameHolder as FrameHolder<out Controller>
            currentFrameHolderMap!![root] = newFrameHolder
        }

        return newFrameHolder
    }
}