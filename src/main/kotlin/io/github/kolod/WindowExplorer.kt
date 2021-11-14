package io.github.kolod

import com.formdev.flatlaf.FlatDarkLaf
import com.formdev.flatlaf.FlatLightLaf
import com.jcabi.manifests.Manifests
import org.apache.logging.log4j.LogManager
import java.awt.*
import java.io.IOException
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

    @Suppress("SameParameterValue")
    private fun fontFromResource(name: String, defaultFont: Font? = null): Font? = try {
        Font.createFont(Font.TRUETYPE_FONT, this.javaClass.getResourceAsStream(name))
    } catch (ex: FontFormatException) {
        logger.error("Can't load font", ex)
        defaultFont
    } catch (ex: IOException) {
        logger.error("Can't load font", ex)
        defaultFont
    }

    private fun setCustomFont(font: Font) {
        UIManager.put("defaultFont", font)
        getWindows().forEach { window -> SwingUtilities.updateComponentTreeUI(window) }
    }

    private fun translateUI() {
        with (bundle) {
            title             = getString("title") + " " + Manifests.read("Build-Date")
            updateButton.text = getString("update-button")
        }
    }

    private fun initComponents() {
        preferredSize = Dimension(800, 600)
        defaultCloseOperation = EXIT_ON_CLOSE
        iconImage = Toolkit.getDefaultToolkit().getImage(javaClass.classLoader.getResource("io/github/kolod/icon.png"))
        setCustomFont(fontFromResource("FiraCode-Regular.ttf", font)!!.deriveFont(12.0f))

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
            logger.debug("listener")
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
            //FlatDarkLaf.setup()
            SwingUtilities.invokeLater { WindowExplorer().isVisible = true }
        }
    }
}