package io.github.kolod

import com.sun.jna.Pointer
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import javax.swing.tree.TreePath

data class Window (val hwnd : WinDef.HWND, val title :String, val parent : Window? = null) {
    private val childs = mutableListOf<Window>()

    override fun hashCode(): Int = hwnd.hashCode()
    override fun toString(): String = title.ifBlank { "<empty>" }
    fun indexOf(child : Window) :Int = childs.indexOf(child)
    fun count() = childs.size
    operator fun get(index: Int) : Window = childs[index]
    fun isEmpty() :Boolean = childs.isEmpty()

    fun getPath() : TreePath {
        val path = mutableListOf<Window>()
        var node : Window? = this
        while (node != null) {
            node.parent?.let { path.add(it) }
            node = node.parent
        }
        return TreePath(path)
    }

    fun update() {
        childs.clear()
        if (hwnd != WinDef.HWND(Pointer.NULL)) {
            User32.INSTANCE.EnumChildWindows(hwnd, { h, _ ->
                childs.add(Window(h, WindowUtils.getWindowTitle(h), this))
                true
            }, null)
        } else {
            WindowUtils.getAllWindows(true).forEach { window ->
                childs.add(Window(window.hwnd, window.title, this))
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Window
        if (hwnd != other.hwnd) return false
        return true
    }
}
