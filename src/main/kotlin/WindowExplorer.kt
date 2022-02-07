// Window Explorer
// Copyright (C) 2022..  Oleksandr Kolodkin <alexandr.kolodkin@gmail.com>
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.kolod

import com.formdev.flatlaf.FlatLightLaf
import com.jcabi.manifests.Manifests
import org.apache.logging.log4j.LogManager
import java.awt.*
import java.util.*
import javax.swing.*

class WindowExplorer : JFrame() {
    private val logger = LogManager.getLogger()
    private val bundle = ResourceBundle.getBundle("i18n/WindowExplorer")

    // UI
    private val updateButton = JButton()
    private val windowsModel = WindowTreeModel()
    private val propertiesModel = WindowPropertiesModel()
    private val windowsTree = JTree(windowsModel)
    private val properties = JTable(propertiesModel)

    private fun translateUI() {
        with (bundle) {
            title             = getString("title") + " " + Manifests.read("Build-Date")
            updateButton.text = getString("update-button")
        }
    }

    private fun initComponents() {
        preferredSize = Dimension(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = Toolkit.getDefaultToolkit().getImage(javaClass.classLoader.getResource("icon.png"))

        val windowsTreePane = JScrollPane(windowsTree)
        val propertiesPane = JScrollPane(properties)
        val splitter = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, windowsTreePane, propertiesPane)

        val buttons = JPanel(FlowLayout(FlowLayout.TRAILING))
        buttons.add(updateButton)

        with (contentPane) {
            add(splitter, BorderLayout.CENTER)
            add(buttons, BorderLayout.SOUTH)
        }

        pack()
        setLocationRelativeTo(null)
        splitter.dividerLocation = splitter.width / 2
    }

    /**
     * Overridden to trick sizing to respect the min.
     */
    override fun isMinimumSizeSet(): Boolean = true

    /**
     * Creates new form TestTrainer
     */
    init {
        logger.info("Application started")

        initComponents()
        translateUI()

        updateButton.addActionListener { windowsModel.update() }

        windowsTree.addTreeSelectionListener { event ->
            val window = event.path.lastPathComponent
            if (window is Window) propertiesModel.update(window)
        }
    }

    companion object {
        /**
         * @param args the command line arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            FlatLightLaf.setup()
            SwingUtilities.invokeLater { WindowExplorer().isVisible = true }
        }
    }
}
