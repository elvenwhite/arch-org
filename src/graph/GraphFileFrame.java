package graph;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import util.Constants;
import util.StatisticalUtil;
import vis.viewer.EdgeStyle;
import vis.viewer.GraphSelectionListener;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.EdgeWeightLabeller;
import edu.uci.ics.jung.visualization.Layout;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

public class GraphFileFrame extends JFrame implements GraphSelectionListener {
    /**
     * <code>serialVersionUID</code>ΐΗ Ό³Έν
     */
    private static final long serialVersionUID = 146130205166605643L;

    private Graph graph;

    private GraphPane graphPane;

    private JSlider nodeAlphaSlider, edgeAlphaSlider;

    private ArrayList<JComponent> controllers;

    private DefaultModalGraphMouse graphMouse;

    private JTree propertyTree;

    private JTable propertyTable;

    private JLabel meanLabel, standardDeviationLabel;

    private String lastDirectory = null;

    private Action importAction, exportAction, captureAction;

    private Action zoomInAction, zoomOutAction;

    private Action showNodeLabelAction, showNodeSizeAction;

    public GraphFileFrame() {
        super("Graph Viewer");

        controllers = new ArrayList<JComponent>();

        graphMouse = new DefaultModalGraphMouse();

        initUI();

        pack();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        setControllerEnabled(false);
    }

    @SuppressWarnings("serial")
    private void prepareActions() {
        importAction = new AbstractAction() {
            public Object getValue(String key) {
                if (key.equals(Action.NAME)) {
                    return "Import";
                } else if (key.equals(Action.MNEMONIC_KEY)) {
                    return KeyEvent.VK_O;
                } else if (key.equals(Action.ACCELERATOR_KEY)) {
                    return KeyStroke.getKeyStroke(KeyEvent.VK_O,
                            ActionEvent.CTRL_MASK);
                }
                return super.getValue(key);
            }

            public void actionPerformed(ActionEvent e) {
                try {
                    JFileChooser chooser = new JFileChooser(lastDirectory);
                    FileFilter filter = new ExtFileFilter("graph", "Graph File");
                    chooser.addChoosableFileFilter(filter);

                    if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    File file = chooser.getSelectedFile();
                    lastDirectory = file.getParent();
                    String filePath = file.getAbsolutePath();
                    GraphFileImporter importer = new GraphFileImporter();
                    Graph g = importer.importGraph(filePath);
                    showGraph(g);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Cannot export the graph", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        exportAction = new AbstractAction() {
            public Object getValue(String key) {
                if (key.equals(Action.NAME)) {
                    return "Export";
                } else if (key.equals(Action.MNEMONIC_KEY)) {
                    return KeyEvent.VK_X;
                } else if (key.equals(Action.ACCELERATOR_KEY)) {
                    return KeyStroke.getKeyStroke(KeyEvent.VK_S,
                            ActionEvent.CTRL_MASK);
                }
                return super.getValue(key);
            }

            public void actionPerformed(ActionEvent e) {
                if (graph == null) {
                    JOptionPane.showMessageDialog(null, "No graph to export!",
                            "", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    JFileChooser chooser = new JFileChooser(lastDirectory);
                    FileFilter filter = new ExtFileFilter("graph", "Graph File");
                    chooser.addChoosableFileFilter(filter);

                    if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    File file = chooser.getSelectedFile();
                    lastDirectory = file.getParent();
                    String filePath = file.getAbsolutePath();
                    String ext = getExt(filePath);
                    if (!ext.toLowerCase().equals("graph")) {
                        filePath += ".graph";
                    }
                    GraphFileExporter exporter = new GraphFileExporter();
                    exporter.export(graphPane.getVisualizationViewer(),
                            filePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Cannot export the graph", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        captureAction = new AbstractAction() {
            public Object getValue(String key) {
                if (key.equals(Action.NAME)) {
                    return "Capture To Png";
                } else if (key.equals(Action.MNEMONIC_KEY)) {
                    return KeyEvent.VK_P;
                } else if (key.equals(Action.ACCELERATOR_KEY)) {
                    return KeyStroke.getKeyStroke(KeyEvent.VK_C,
                            ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);
                }
                return super.getValue(key);
            }

            public void actionPerformed(ActionEvent e) {

                if (graph == null) {
                    JOptionPane.showMessageDialog(null, "No graph to export!",
                            "", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                try {
                    JFileChooser chooser = new JFileChooser(lastDirectory);
                    FileFilter filter = new ExtFileFilter("png", "PNG File");
                    chooser.addChoosableFileFilter(filter);

                    if (chooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION) {
                        return;
                    }
                    File file = chooser.getSelectedFile();
                    lastDirectory = file.getParent();
                    String filePath = file.getAbsolutePath();
                    String ext = getExt(filePath);
                    if (!ext.toLowerCase().equals("png")) {
                        filePath += ".png";
                    }
                    FileOutputStream fos = new FileOutputStream(filePath);
                    graphPane.export(fos);
                    fos.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                            "Cannot export the graph", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        zoomInAction = new AbstractAction() {
            public Object getValue(String key) {
                if (key.equals(Action.NAME)) {
                    return "Zoom In";
                } else if (key.equals(Action.MNEMONIC_KEY)) {
                    return KeyEvent.VK_I;
                } else if (key.equals(Action.ACCELERATOR_KEY)) {
                    return KeyStroke.getKeyStroke(KeyEvent.VK_ADD,
                            ActionEvent.CTRL_MASK);
                }
                return super.getValue(key);
            }

            public void actionPerformed(ActionEvent e) {
                graphPane.zoomIn();
            }
        };

        zoomOutAction = new AbstractAction() {
            public Object getValue(String key) {
                if (key.equals(Action.NAME)) {
                    return "Zoom Out";
                } else if (key.equals(Action.MNEMONIC_KEY)) {
                    return KeyEvent.VK_O;
                } else if (key.equals(Action.ACCELERATOR_KEY)) {
                    return KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT,
                            ActionEvent.CTRL_MASK);
                }
                return super.getValue(key);
            }

            public void actionPerformed(ActionEvent e) {
                graphPane.zoomOut();
            }
        };

        showNodeLabelAction = new AbstractAction() {
            public Object getValue(String key) {
                if (key.equals(Action.NAME)) {
                    return "Show Label";
                } else if (key.equals(Action.MNEMONIC_KEY)) {
                    return KeyEvent.VK_L;
                } else if (key.equals(Action.ACCELERATOR_KEY)) {
                    return KeyStroke.getKeyStroke(KeyEvent.VK_L,
                            ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK);
                }
                return super.getValue(key);
            }

            public void actionPerformed(ActionEvent e) {
                Object source = e.getSource();
                if (source instanceof AbstractButton) {
                    AbstractButton button = (AbstractButton) source;
                    boolean b = button.isSelected();
                    graphPane.showNodeLabel(b);
                }
                // graphPane.showNodeLabel(isSelected());
            }
        };

        exportAction.setEnabled(false);
        captureAction.setEnabled(false);
        zoomInAction.setEnabled(false);
        zoomOutAction.setEnabled(false);
    }

    private void initUI() {
        prepareActions();

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createMenu(), BorderLayout.NORTH);
        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createGraphPane(), createControlPane());
        splitter.setOneTouchExpandable(true);
        contentPane.add(splitter, BorderLayout.CENTER);

        setContentPane(contentPane);

        moveToCenter();
    }

    private void moveToCenter() {
        Dimension size = getPreferredSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - size.width) / 2;
        int y = (screen.height - size.height) / 2;
        setBounds(x, y, size.width, size.height);
    }

    private JPanel createGraphPane() {
        graphPane = new GraphPane();
        graphPane.setPreferredSize(new Dimension(600, 400));
        graphPane.addGraphSelectionListener(this);
        return graphPane;
    }

    private JComponent createControlPane() {
        JPanel pane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        pane.setLayout(gbl);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        addComponent(pane, createZoomController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        addComponent(pane, createNodeController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        addComponent(pane, createEdgeController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        addComponent(pane, createMouseModeController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        addComponent(pane, createPropertyViewer(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weighty = 0.0;
        addComponent(pane, createOutputViewer(), gbl, gbc);

        return pane;
    }

    private JComponent createZoomController() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        JButton zoomInButton = new JButton(zoomInAction);
        controllers.add(zoomInButton);
        panel.add(zoomInButton);

        JButton zoomOutButton = new JButton(zoomOutAction);
        controllers.add(zoomOutButton);
        panel.add(zoomOutButton);

        return panel;
    }

    private JComponent createNodeController() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Node"));

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        // final JCheckBox showNodeLabelCheck = new JCheckBox("Show Label",
        // true);
        final JCheckBox showNodeLabelCheck = new JCheckBox(showNodeLabelAction);
        controllers.add(showNodeLabelCheck);
        showNodeLabelCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPane.showNodeLabel(showNodeLabelCheck.isSelected());
            }
        });
        addComponent(panel, showNodeLabelCheck, gbl, gbc);

        gbc.gridx = 1;
        final JCheckBox showNodeSizeCheck = new JCheckBox("Show Size", true);
        controllers.add(showNodeSizeCheck);
        showNodeSizeCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPane.showNodeSize(showNodeSizeCheck.isSelected());
            }
        });
        addComponent(panel, showNodeSizeCheck, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 0.0;
        addComponent(panel, new JLabel("Node Label: "), gbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        final JComboBox nodeTypeCombo = new JComboBox(new String[] { "Id",
                "Name" });
        controllers.add(nodeTypeCombo);
        nodeTypeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (nodeTypeCombo == null || graphPane == null) {
                    return;
                }
                if ("Id".equals(nodeTypeCombo.getSelectedItem())) {
                    graphPane.setNodeLabelType(Constants.NODE_LABEL_TYPE_ID);
                } else if ("Name".equals(nodeTypeCombo.getSelectedItem())) {
                    graphPane.setNodeLabelType(Constants.NODE_LABEL_TYPE_NAME);
                }
            }
        });
        addComponent(panel, nodeTypeCombo, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        nodeAlphaSlider = new JSlider(0, 255);
        nodeAlphaSlider.setValue(255);
        controllers.add(nodeAlphaSlider);
        nodeAlphaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = nodeAlphaSlider.getValue();
                graphPane.setNodeAlpha(value);
            }
        });
        JPanel alphaSliderPane = new JPanel();
        alphaSliderPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        alphaSliderPane.add(new JLabel("Alpha"));
        alphaSliderPane.add(nodeAlphaSlider);
        addComponent(panel, alphaSliderPane, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        addComponent(panel, new JLabel("Foreground Color"), gbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        final JButton foreGroundColorButton = new JButton("Select");
        controllers.add(foreGroundColorButton);
        foreGroundColorButton.setForeground(graphPane.getNodeColor());
        foreGroundColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color...",
                        graphPane.getNodeColor());
                if (color == null)
                    return;
                graphPane.setNodeColor(color);
                foreGroundColorButton.setForeground(color);
            }
        });
        addComponent(panel, foreGroundColorButton, gbl, gbc);

        gbc.gridy = GridBagConstraints.REMAINDER;
        gbc.weighty = 1.0;
        addComponent(panel, Box.createHorizontalGlue(), gbl, gbc);

        return panel;
    }

    private JComponent createEdgeController() {

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Edge"));

        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        final JCheckBox showEdgeCheck = new JCheckBox("Show Edge", false);
        controllers.add(showEdgeCheck);
        showEdgeCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPane.showEdge(showEdgeCheck.isSelected());
            }
        });
        addComponent(panel, showEdgeCheck, gbl, gbc);

        gbc.gridx = 1;
        final JCheckBox showEdgeLabelCheck = new JCheckBox("Show Label", false);
        controllers.add(showEdgeLabelCheck);
        showEdgeLabelCheck.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graphPane.showEdgeLabel(showEdgeLabelCheck.isSelected());
            }
        });
        addComponent(panel, showEdgeLabelCheck, gbl, gbc);

        final String[] shapes = { "Line", "Wedge", "Quad Curve", "Cubic Curve" };
        final JComboBox edgeStyleCombo = new JComboBox(shapes);
        controllers.add(edgeStyleCombo);
        edgeStyleCombo.setSelectedItem("Quad Curve");
        edgeStyleCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sel = (String) edgeStyleCombo.getSelectedItem();
                if (sel == null)
                    return;
                EdgeStyle edgeStyle = null;
                if (sel.equals("Line"))
                    edgeStyle = EdgeStyle.ES_LINE;
                else if (sel.equals("Wedge"))
                    edgeStyle = EdgeStyle.ES_WEDGE;
                else if (sel.equals("Quad Curve"))
                    edgeStyle = EdgeStyle.ES_QUAD;
                else if (sel.equals("Cubic Curve"))
                    edgeStyle = EdgeStyle.ES_CUBIC;
                graphPane.setEdgeStyle(edgeStyle);
            }
        });
        JPanel shapePane = new JPanel();
        shapePane.setLayout(new FlowLayout(FlowLayout.LEFT));
        shapePane.add(new JLabel("Style"));
        shapePane.add(edgeStyleCombo);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        addComponent(panel, shapePane, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        edgeAlphaSlider = new JSlider(0, 255);
        edgeAlphaSlider.setValue(255);
        controllers.add(edgeAlphaSlider);
        edgeAlphaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = edgeAlphaSlider.getValue();
                graphPane.setEdgeAlpha(value);
            }
        });
        JPanel alphaSliderPane = new JPanel();
        alphaSliderPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        alphaSliderPane.add(new JLabel("Alpha"));
        alphaSliderPane.add(edgeAlphaSlider);
        addComponent(panel, alphaSliderPane, gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        addComponent(panel, new JLabel("Color1"), gbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        final JButton color1Button = new JButton("Select");
        controllers.add(color1Button);
        color1Button.setForeground(graphPane.getEdgeColor1());
        color1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color...",
                        graphPane.getEdgeColor1());
                if (color == null)
                    return;
                graphPane.setEdgeColor1(color);
                color1Button.setForeground(color);
            }
        });
        addComponent(panel, color1Button, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 0.0;
        addComponent(panel, new JLabel("Color2"), gbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        final JButton color2Button = new JButton("Select");
        controllers.add(color2Button);
        color2Button.setForeground(graphPane.getEdgeColor2());
        color2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color...",
                        graphPane.getEdgeColor2());
                if (color == null)
                    return;
                graphPane.setEdgeColor2(color);
                color2Button.setForeground(color);
            }
        });
        addComponent(panel, color2Button, gbl, gbc);

        gbc.gridy = GridBagConstraints.REMAINDER;
        gbc.weighty = 1.0;
        gbc.gridwidth = 2;
        addComponent(panel, Box.createHorizontalGlue(), gbl, gbc);

        return panel;

    }

    private JComponent createMouseModeController() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Mouse Mode"));

        JComboBox mouseModeCombo = graphMouse.getModeComboBox();
        panel.add(mouseModeCombo, BorderLayout.CENTER);
        controllers.add(mouseModeCombo);

        return panel;
    }

    private JComponent createPropertyViewer() {
        TreeModel model = new DefaultTreeModel(makePropertyData(null, null));
        propertyTree = new JTree(model);
        propertyTree.setShowsRootHandles(true);
        propertyTree.setRootVisible(false);
        propertyTree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        propertyTree.setCellRenderer(new MyTreeRenderer());
        propertyTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getNewLeadSelectionPath();
                if (path == null)
                    return;

                Object obj = path.getLastPathComponent();
                if (obj instanceof DefaultMutableTreeNode) {
                    Object userObject = ((DefaultMutableTreeNode) obj)
                            .getUserObject();
                    if (userObject instanceof Vertex) {
                        nodeSelected((Vertex) userObject);
                    } else if (userObject instanceof Edge) {
                        edgeSelected((Edge) userObject);
                    }
                }
            }
        });

        propertyTable = new JTable(new Object[0][0], new String[] { "Key",
                "Value" });
        Dimension dim = propertyTree.getPreferredScrollableViewportSize();
        dim.height = 40;
        propertyTable.setPreferredScrollableViewportSize(dim);

        JScrollPane scroll = new JScrollPane(propertyTable);
        scroll.setPreferredSize(new Dimension(100, 20));
        JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(propertyTree), scroll);
        splitter.setDividerLocation(150);
        splitter.setOneTouchExpandable(true);
        dim.height = 300;
        splitter.setPreferredSize(new Dimension(dim));
        splitter.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Property"));
        return splitter;
    }

    private JComponent createOutputViewer() {
        JPanel panel = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        panel.setLayout(gbl);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        addComponent(panel, new JLabel("AVE: "), gbl, gbc);

        meanLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        addComponent(panel, meanLabel, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        addComponent(panel, new JLabel("S.D.: "), gbl, gbc);

        standardDeviationLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        addComponent(panel, standardDeviationLabel, gbl, gbc);

        return panel;
    }

    private void addComponent(Container parent, Component child,
            GridBagLayout gbl, GridBagConstraints gbc) {
        gbl.setConstraints(child, gbc);
        parent.add(child);
    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        fileMenu.add(importAction);
        fileMenu.add(exportAction);

        JMenu viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        menuBar.add(viewMenu);

        viewMenu.add(zoomInAction);
        viewMenu.add(zoomOutAction);

        JMenu toolMenu = new JMenu("Tools");
        toolMenu.setMnemonic(KeyEvent.VK_T);
        menuBar.add(toolMenu);

        toolMenu.add(captureAction);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(helpMenu);

        JMenuItem aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        helpMenu.add(aboutItem);

        return menuBar;
    }

    private void setControllerEnabled(boolean state) {
        exportAction.setEnabled(state);
        captureAction.setEnabled(state);
        zoomInAction.setEnabled(state);
        zoomOutAction.setEnabled(state);

        for (JComponent controller : controllers) {
            controller.setEnabled(state);
        }
    }

    private void showGraph(Graph graph) {
        this.graph = graph;
        if (graph == null)
            return;

        graphPane.removeGraph();
        graphPane.showGraph(graph);
        setControllerEnabled(true);
        graphPane.getVisualizationViewer().setGraphMouse(graphMouse);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                calculateAccuracy();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void calculateAccuracy() {
        VisualizationViewer visualizationViewer = graphPane
                .getVisualizationViewer();

        Set edges = graph.getEdges();
        int length = edges.size();
        double[] values = new double[length];

        int i = 0;
        for (Object child : edges) {
            Edge edge = (Edge) child;

            EdgeWeightLabeller labeller = EdgeWeightLabeller.getLabeller(graph);
            Number number = labeller.getNumber(edge);
            if (number == null)
                continue;

            double originalValue = number.doubleValue();
            Set verticesSet = edge.getIncidentVertices();
            Vertex[] vertices = new Vertex[2];
            verticesSet.toArray(vertices);
            double distance = calculateRealDistance(vertices,
                    visualizationViewer);

            values[i++] = distance / originalValue;
        }

        double mean = StatisticalUtil.mean(values);
        double sd = StatisticalUtil.standardDeviation(values, 0);
        meanLabel.setText(mean + "");
        standardDeviationLabel.setText(sd + "");
    }

    private double calculateRealDistance(Vertex[] verticies,
            VisualizationViewer visualizationViewer) {
        VisualizationModel model = visualizationViewer.getModel();
        Layout layout = model.getGraphLayout();
        Point2D point1 = layout.getLocation(verticies[0]);
        Point2D point2 = layout.getLocation(verticies[1]);
        return point1.distance(point2);
    }

    private final class ExtFileFilter extends FileFilter {
        private String ext;

        private String desc;

        public ExtFileFilter(String ext, String desc) {
            this.ext = ext;
            this.desc = desc;
        }

        public boolean accept(File f) {
            if (f == null)
                return false;
            if (f.isDirectory())
                return true;
            String fileName = f.getName();
            return ext.toLowerCase().equals(getExt(fileName).toLowerCase());
        }

        public String getDescription() {
            return desc;
        }
    }

    private String getExt(String fileName) {
        StringTokenizer tokenizer = new StringTokenizer(fileName, ".");
        String ex = "";
        while (tokenizer.hasMoreTokens()) {
            ex = tokenizer.nextToken();
        }
        return ex;
    }

    public static void main(String[] args) {
        GraphFileFrame frame = new GraphFileFrame();
        frame.setVisible(true);
    }

    public void graphElementsSelected(Vertex[] vertices, Edge[] edges) {
        TreeModel model = new DefaultTreeModel(
                makePropertyData(vertices, edges));
        propertyTree.setModel(model);
        expandAll(propertyTree);
    }

    public void graphElementsDeselected() {
        TreeModel model = new DefaultTreeModel(makePropertyData(null, null));
        propertyTree.setModel(model);
        expandAll(propertyTree);
    }

    private TreeNode makePropertyData(Vertex[] vertices, Edge[] edges) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();
        DefaultMutableTreeNode nodesRoot = new DefaultMutableTreeNode(
                "Selected Nodes");
        DefaultMutableTreeNode edgesRoot = new DefaultMutableTreeNode(
                "Selected Edges");
        root.add(nodesRoot);
        root.add(edgesRoot);
        if (vertices != null && vertices.length > 0) {
            for (Vertex vertex : vertices)
                nodesRoot.add(new DefaultMutableTreeNode(vertex));
        }

        if (edges != null && edges.length > 0) {
            for (Edge edge : edges)
                edgesRoot.add(new DefaultMutableTreeNode(edge));
        }

        return root;
    }

    private void expandAll(JTree tree) {
        expandAll(tree, new TreePath(tree.getModel().getRoot()), true);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    private void nodeSelected(final Vertex vertex) {
        final String[] keys = { "ID", "NAME", "SIZE" };
        propertyTable.setModel(new DefaultTableModel() {
            private static final long serialVersionUID = 1689822191525784419L;

            public String getColumnName(int column) {
                switch (column) {
                case 0:
                    return "Key";
                case 1:
                    return "Value";
                default:
                    return "";
                }
            }

            public int getRowCount() {
                return keys.length;
            }

            public int getColumnCount() {
                return 2;
            }

            public Object getValueAt(int row, int column) {
                switch (column) {
                case 0:
                    return getKeyAt(row);
                case 1:
                    return getValueAt(row);
                default:
                    return null;
                }
            }

            private Object getKeyAt(int row) {
                if (row < 0 || row > keys.length)
                    return null;
                return keys[row];
            }

            private Object getValueAt(int row) {
                switch (row) {
                case 0:
                    return vertex.getUserDatum(Constants.KEY_ID);
                case 1:
                    return vertex.getUserDatum(Constants.KEY_NAME);
                case 2:
                    return vertex.getUserDatum(Constants.KEY_SIZE);
                default:
                    return null;
                }
            }
        });
    }

    private void edgeSelected(final Edge edge) {
        final String[] keys = { "NAME", "SIZE" };
        propertyTable.setModel(new DefaultTableModel() {
            private static final long serialVersionUID = 1689822191525784419L;

            public String getColumnName(int column) {
                switch (column) {
                case 0:
                    return "Key";
                case 1:
                    return "Value";
                default:
                    return "";
                }
            }

            public int getRowCount() {
                return keys.length;
            }

            public int getColumnCount() {
                return 2;
            }

            public Object getValueAt(int row, int column) {
                switch (column) {
                case 0:
                    return getKeyAt(row);
                case 1:
                    return getValueAt(row);
                default:
                    return null;
                }
            }

            private Object getKeyAt(int row) {
                if (row < 0 || row > keys.length)
                    return null;
                return keys[row];
            }

            private Object getValueAt(int row) {
                switch (row) {
                case 0:
                    return edge.toString();
                case 1:
                    EdgeWeightLabeller labeller = EdgeWeightLabeller
                            .getLabeller(graph);
                    return labeller.getNumber(edge);
                default:
                    return null;
                }
            }
        });
    }

    public void setGraph(Graph graph, boolean show) {
        this.graph = graph;
        if (show) {
            showGraph(graph);
        }
    }

    @SuppressWarnings("serial")
    private class MyTreeRenderer extends DefaultTreeCellRenderer {
        private Icon folderIcon, vertexIcon, edgeIcon;

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                boolean sel, boolean expanded, boolean leaf, int row,
                boolean hasFocus) {
            if (folderIcon == null) {
                URL folderUrl = ClassLoader
                        .getSystemResource("resources/icons/jar24.gif");
                folderIcon = new ImageIcon(folderUrl);
                URL vertexUrl = ClassLoader
                        .getSystemResource("resources/icons/bean24.gif");
                vertexIcon = new ImageIcon(vertexUrl);
                URL edgeUrl = ClassLoader
                        .getSystemResource("resources/icons/arrow24.gif");
                edgeIcon = new ImageIcon(edgeUrl);
            }

            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                    leaf, row, hasFocus);
            if (!(value instanceof DefaultMutableTreeNode)) {
                return this;
            }

            Object uo = ((DefaultMutableTreeNode) value).getUserObject();
            if (uo == null) {
                return this;
            }

            if (uo instanceof String) {
                setMenuIcon((String) uo);
            } else if (uo instanceof Vertex) {
                setVertexIcon();
            } else if (uo instanceof Edge) {
                setEdgeIcon();
            }

            return this;
        }

        private void setMenuIcon(String value) {
            if (value.equals("Selected Edges")
                    || value.equals("Selected Nodes")) {
                setIcon(folderIcon);
            }
        }

        private void setVertexIcon() {
            setIcon(vertexIcon);
        }

        private void setEdgeIcon() {
            setIcon(edgeIcon);
        }

        protected boolean isTutorialBook(Object value) {
            // DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
            // BookInfo nodeInfo = (BookInfo) (node.getUserObject());
            // String title = nodeInfo.bookName;
            // if (title.indexOf("Tutorial") >= 0) {
            // return true;
            // }

            return false;
        }
    }
}