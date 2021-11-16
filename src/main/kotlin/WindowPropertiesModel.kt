package io.github.kolod

import com.sun.jna.Native
import com.sun.jna.Pointer
import com.sun.jna.platform.WindowUtils
import com.sun.jna.platform.win32.User32
import com.sun.jna.platform.win32.WinDef
import java.util.*
import javax.swing.JLabel
import javax.swing.event.TableModelEvent
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

class WindowPropertiesModel : TableModel {
    private val bundle = ResourceBundle.getBundle("i18n/WindowExplorer")
    private val listeners = mutableListOf<TableModelListener>()

    private val columnNames = listOf(
        bundle.getString("prop_name"),
        bundle.getString("prop_value"),
    )

    private val rowNames = listOf(
        bundle.getString("prop_title"),           // 0
        bundle.getString("prop_handle"),          // 1
        bundle.getString("prop_class_name"),      // 2
        bundle.getString("prop_width"),           // 3
        bundle.getString("prop_height"),          // 4
        bundle.getString("prop_x"),               // 5
        bundle.getString("prop_y"),               // 6
    )

    private val rowValues = mutableListOf("", "", "", "", "", "", "")

    private fun getWindowClassName(handle :WinDef.HWND) :String {
        val className = CharArray(512)
        return if (User32.INSTANCE.GetClassName(handle, className, 512) > 0) {
            Native.toString(className)
        } else {
            ""
        }
    }

    private fun fireDataChanged() {
        listeners.forEach{ listener ->
            listener.tableChanged(TableModelEvent(this, 0, rowValues.size, 1))
        }
    }

    fun update(window :Window) {
        for (i in rowValues.indices) rowValues[i] = ""
        if (window.hwnd != WinDef.HWND(Pointer.NULL)) {
            rowValues[0] = window.title
            rowValues[1] = window.hwnd.toString().split("@")[1]
            rowValues[2] = getWindowClassName(window.hwnd)

            with (WindowUtils.getWindowLocationAndSize(window.hwnd)) {
                rowValues[3] = width.toString()
                rowValues[4] = height.toString()
                rowValues[5] = x.toString()
                rowValues[6] = y.toString()
            }
        }
        fireDataChanged()
    }

    /**
     * Returns the number of rows in the model. A
     * `JTable` uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see .getColumnCount
     */
    override fun getRowCount(): Int = rowNames.size

    /**
     * Returns the number of columns in the model. A
     * `JTable` uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see .getRowCount
     */
    override fun getColumnCount(): Int = 2

    /**
     * Returns the name of the column at `columnIndex`.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @param   columnIndex     the index of the column
     * @return  the name of the column
     */
    override fun getColumnName(columnIndex: Int): String = columnNames[columnIndex]

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the `JTable` to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex  the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    override fun getColumnClass(columnIndex: Int): Class<*> = JLabel::class.java

    /**
     * Returns true if the cell at `rowIndex` and
     * `columnIndex`
     * is editable.  Otherwise, `setValueAt` on the cell will not
     * change the value of that cell.
     *
     * @param   rowIndex        the row whose value to be queried
     * @param   columnIndex     the column whose value to be queried
     * @return  true if the cell is editable
     * @see .setValueAt
     */
    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = false

    /**
     * Returns the value for the cell at `columnIndex` and
     * `rowIndex`.
     *
     * @param   rowIndex        the row whose value is to be queried
     * @param   columnIndex     the column whose value is to be queried
     * @return  the value Object at the specified cell
     */
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any =
        if (columnIndex == 0) {
            rowNames[rowIndex]
        } else {
            rowValues[rowIndex]
        }

    /**
     * Sets the value in the cell at `columnIndex` and
     * `rowIndex` to `aValue`.
     *
     * @param   aValue           the new value
     * @param   rowIndex         the row whose value is to be changed
     * @param   columnIndex      the column whose value is to be changed
     * @see .getValueAt
     *
     * @see .isCellEditable
     */
    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {}

    /**
     * Adds a listener to the list that is notified each time a change
     * to the data model occurs.
     *
     * @param   l               the TableModelListener
     */
    override fun addTableModelListener(l: TableModelListener?) {
        if (l != null) listeners.add(l)
    }

    /**
     * Removes a listener from the list that is notified each time a
     * change to the data model occurs.
     *
     * @param   l               the TableModelListener
     */
    override fun removeTableModelListener(l: TableModelListener?) {
        if (l != null) listeners.remove(l)
    }
}