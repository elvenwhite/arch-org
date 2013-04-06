package vis.viewer;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.GeneralPath;
import java.io.File;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.collections.Predicate;

import vis.GraphVisualization;
import vis.comodification.AuthorOrganizationDotExportVisualization;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.decorators.AbstractVertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.ConstantEdgeStringer;
import edu.uci.ics.jung.graph.decorators.ConstantVertexStringer;
import edu.uci.ics.jung.graph.decorators.EdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.EdgeShape;
import edu.uci.ics.jung.graph.decorators.EdgeStringer;
import edu.uci.ics.jung.graph.decorators.GradientEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.PickableEdgePaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexPaintFunction;
import edu.uci.ics.jung.graph.decorators.VertexShapeFunction;
import edu.uci.ics.jung.graph.decorators.VertexStringer;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.HasGraphLayout;
import edu.uci.ics.jung.visualization.PluggableRenderer;
import edu.uci.ics.jung.visualization.ShapePickSupport;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.VisualizationViewer.Paintable;
import edu.uci.ics.jung.visualization.contrib.KKLayout;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.SatelliteVisualizationViewer;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.transform.LayoutTransformer;
import edu.uci.ics.jung.visualization.transform.shape.ShapeTransformer;

public class DotGraphViewerFrame extends JFrame {

    private static final long serialVersionUID = -5670162070328021044L;

    private Graph graph;

    private Paintable viewGrid;

    private VisualizationViewer mainGraphViewer;

    private SatelliteVisualizationViewer satelliteGraphViewer;

    private PluggableRenderer mainRenderer;

    private PluggableRenderer satelliteRenderer;

    private static EdgeStringer nullEdgeStringer, edgeStringer;

    private static VertexStringer nullVertexStringer, vertexStringer;

    private static VertexShapeFunction nullVertexShapeFunction,
            vertexShapeFunction;

    protected ColoredEdgePaintFunction edgePaint;

    private DirectionDisplayPredicate directionDisplayPredicate;

    private JSlider nodeAlphaSlider, edgeAlphaSlider;

    private ColoredNodePaintFunction nodeColorFunction;

    private util.Graph originalGraph;

    public DotGraphViewerFrame(Graph g, util.Graph originalGraph) {
        this(g, "", originalGraph);
    }

    public DotGraphViewerFrame(Graph g, String title, util.Graph originalGraph) {
        super(title);
        this.graph = g;
        this.originalGraph = originalGraph;

        initUI();

        pack();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void initUI() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(createViewerPane(), BorderLayout.CENTER);
        setContentPane(contentPane);
    }

    private JComponent createViewerPane() {
        JPanel pane = new JPanel();
        pane.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel(new GridLayout(2, 1));

        viewGrid = createVisualizationViewer();
        rightPanel.add(createControlPane());
        rightPanel.add(satelliteGraphViewer);

        pane.add(new GraphZoomScrollPane(mainGraphViewer), BorderLayout.CENTER);
        pane.add(rightPanel, BorderLayout.EAST);

        return pane;
    }

    private Paintable createVisualizationViewer() {
        // Layout을 변경하려면 여기...
        KKLayout layout = new KKLayout(graph, new WeightDistance(graph));
        layout.setMaxIterations(500);
        layout.initialize(new Dimension(600, 600));

        // Graph Model
        VisualizationModel vm = new DefaultVisualizationModel(layout,
                new Dimension(600, 600));
        mainGraphViewer = createMainViewer(vm);
        satelliteGraphViewer = createSatelliteViewer(vm, mainGraphViewer);

        return new ViewGrid(satelliteGraphViewer, mainGraphViewer);
    }

    private VisualizationViewer createMainViewer(VisualizationModel model) {
        nullVertexStringer = new ConstantVertexStringer(null);
        vertexStringer = new DotGraphVertexStringer();
        nullVertexShapeFunction = new AbstractVertexShapeFunction() {
            public Shape getShape(Vertex v) {
                return factory.getEllipse(v);
            }
        };
        vertexShapeFunction = new DotGraphVertexShapeFunction();
        nullEdgeStringer = new ConstantEdgeStringer(null);
        edgeStringer = new DotGraphEdgeStringer(graph);
        directionDisplayPredicate = new DirectionDisplayPredicate(false);
        nodeColorFunction = new ColoredNodePaintFunction();

        mainRenderer = new PluggableRenderer();
        mainRenderer.setVertexShapeFunction(vertexShapeFunction);
        mainRenderer.setVertexStringer(vertexStringer);
        mainRenderer.setEdgeStringer(nullEdgeStringer);
        mainRenderer.setEdgeIncludePredicate(directionDisplayPredicate);

        VisualizationViewer viewer = new VisualizationViewer(model,
                mainRenderer, new Dimension(600, 600));
        viewer.setBackground(Color.white);
        viewer.setPickSupport(new ShapePickSupport());

        final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        viewer.setGraphMouse(graphMouse);

        edgePaint = new ColoredEdgePaintFunction(new PickableEdgePaintFunction(
                viewer.getPickedState(), null, null), viewer, viewer);
        mainRenderer.setEdgePaintFunction(edgePaint);
        mainRenderer.setVertexPaintFunction(nodeColorFunction);

        return viewer;
    }

    private SatelliteVisualizationViewer createSatelliteViewer(
            VisualizationModel model, VisualizationViewer mainViewer) {
        satelliteRenderer = new PluggableRenderer();
        return new SatelliteVisualizationViewer(mainViewer, model,
                satelliteRenderer, new Dimension(300, 300));
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
        addComponent(pane, createSatelliteController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        addComponent(pane, createNodeController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        addComponent(pane, createEdgeController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.RELATIVE;
        addComponent(pane, createExportController(), gbl, gbc);

        gbc.gridy = GridBagConstraints.REMAINDER;
        gbc.weighty = 1.0;
        addComponent(pane, Box.createHorizontalGlue(), gbl, gbc);

        return pane;
    }

    private JComponent createZoomController() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout());

        JButton zoomInButton = new JButton("Zoom In");
        panel.add(zoomInButton);

        JButton zoomOutButton = new JButton("Zoom Out");
        panel.add(zoomOutButton);

        final ScalingControl scaler = new CrossoverScalingControl();
        zoomInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(mainGraphViewer, 1.1f, satelliteGraphViewer
                        .getCenter());
            }
        });
        zoomOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scaler.scale(mainGraphViewer, 1 / 1.1f, satelliteGraphViewer
                        .getCenter());
            }
        });

        return panel;
    }

    private JComponent createSatelliteController() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Satellite"));

        JCheckBox gridBox = new JCheckBox("Show Grid");
        gridBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                showGrid(satelliteGraphViewer,
                        e.getStateChange() == ItemEvent.SELECTED);
            }
        });
        panel.add(gridBox);

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
        final JCheckBox showNodeLabel = new JCheckBox("Show Label", true);
        showNodeLabel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNodeLabel(showNodeLabel.isSelected());
            }
        });
        addComponent(panel, showNodeLabel, gbl, gbc);

        gbc.gridx = 1;
        final JCheckBox showNodeSize = new JCheckBox("Show Size", true);
        showNodeSize.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showNodeSize(showNodeSize.isSelected());
            }
        });
        addComponent(panel, showNodeSize, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        nodeAlphaSlider = new JSlider(0, 255);
        nodeAlphaSlider.setValue(255);
        nodeAlphaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = nodeAlphaSlider.getValue();
                nodeColorFunction.setAlpha(value);
                mainGraphViewer.repaint();
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
        foreGroundColorButton.setForeground(nodeColorFunction.color);
        foreGroundColorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color...",
                        nodeColorFunction.color);
                if (color == null)
                    return;
                nodeColorFunction.setColor(color);
                mainGraphViewer.repaint();
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
        final JCheckBox showEdge = new JCheckBox("Show Edge", true);
        showEdge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEdge(showEdge.isSelected());
            }
        });
        addComponent(panel, showEdge, gbl, gbc);

        gbc.gridx = 1;
        final JCheckBox showEdgeLabel = new JCheckBox("Show Length", false);
        showEdgeLabel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showEdgeLabel(showEdgeLabel.isSelected());
            }
        });
        addComponent(panel, showEdgeLabel, gbl, gbc);

        final String[] shapes = { "Line", "Wedge", "Quad Curve", "Cubic Curve" };
        final JComboBox shapeCombo = new JComboBox(shapes);
        shapeCombo.setSelectedItem("Quad Curve");
        shapeCombo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String sel = (String) shapeCombo.getSelectedItem();
                if (sel == null)
                    return;
                if (sel.equals("Line"))
                    mainRenderer.setEdgeShapeFunction(new EdgeShape.Line());
                else if (sel.equals("Wedge"))
                    mainRenderer.setEdgeShapeFunction(new EdgeShape.Wedge(10));
                else if (sel.equals("Quad Curve"))
                    mainRenderer
                            .setEdgeShapeFunction(new EdgeShape.QuadCurve());
                else if (sel.equals("Cubic Curve"))
                    mainRenderer
                            .setEdgeShapeFunction(new EdgeShape.CubicCurve());
                mainGraphViewer.repaint();
            }
        });
        JPanel shapePane = new JPanel();
        shapePane.setLayout(new FlowLayout(FlowLayout.LEFT));
        shapePane.add(new JLabel("Shape"));
        shapePane.add(shapeCombo);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.weightx = 1.0;
        gbc.gridwidth = 2;
        addComponent(panel, shapePane, gbl, gbc);

        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        edgeAlphaSlider = new JSlider(0, 255);
        edgeAlphaSlider.setValue(255);
        edgeAlphaSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = edgeAlphaSlider.getValue();
                edgePaint.setAlpha(value);
                mainGraphViewer.repaint();
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
        color1Button.setForeground(edgePaint.color1);
        color1Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color...",
                        edgePaint.color1);
                if (color == null)
                    return;
                edgePaint.setColor1(color);
                mainGraphViewer.repaint();
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
        color2Button.setForeground(edgePaint.color2);
        color2Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Color color = JColorChooser.showDialog(null, "Select Color...",
                        edgePaint.color2);
                if (color == null)
                    return;
                edgePaint.setColor2(color);
                mainGraphViewer.repaint();
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

    private JComponent createExportController() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory
                .createEtchedBorder(), "Export"));
        panel.setLayout(new GridLayout());
        JButton exportToDotButton = new JButton("DOT");
        exportToDotButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exportToDot();
            }
        });
        panel.add(exportToDotButton);
        JButton exportToPngButton = new JButton("PNG");
        panel.add(exportToPngButton);
        return panel;
    }

    private void addComponent(Container parent, Component child,
            GridBagLayout gbl, GridBagConstraints gbc) {
        gbl.setConstraints(child, gbc);
        parent.add(child);
    }

    protected void showEdge(boolean state) {
        directionDisplayPredicate.showDirected(state);
        mainGraphViewer.repaint();
    }

    protected void showEdgeLabel(boolean state) {
        if (state == true) {
            mainRenderer.setEdgeStringer(edgeStringer);
        } else {
            mainRenderer.setEdgeStringer(nullEdgeStringer);
        }
        mainGraphViewer.repaint();
    }

    protected void showNodeLabel(boolean state) {
        if (state == true) {
            mainRenderer.setVertexStringer(vertexStringer);
        } else {
            mainRenderer.setVertexStringer(nullVertexStringer);
        }
        mainGraphViewer.repaint();
    }

    protected void showNodeSize(boolean state) {
        if (state == true) {
            mainRenderer.setVertexShapeFunction(vertexShapeFunction);
        } else {
            mainRenderer.setVertexShapeFunction(nullVertexShapeFunction);
        }
        mainGraphViewer.repaint();
    }

    protected void showGrid(VisualizationViewer viewer, boolean state) {
        if (state == true) {
            viewer.addPreRenderPaintable(viewGrid);
        } else {
            viewer.removePreRenderPaintable(viewGrid);
        }
        viewer.repaint();
    }

    private void exportToDot() {
        JFileChooser chooser = new JFileChooser();
        FileFilter filter = new ExtFileFilter("DOT", "Dot File");
        chooser.addChoosableFileFilter(filter);

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File file = chooser.getSelectedFile();
        String filePath = file.getAbsolutePath();
        String ext = getExt(filePath);
        if (!ext.toLowerCase().equals("dot")) {
            filePath += ".dot";
        }

        GraphVisualization vis = new AuthorOrganizationDotExportVisualization(
                filePath);
        vis.visualize(originalGraph);
    }

    /**
     * draws a grid on the SatelliteViewer's lens
     * 
     * @author Tom Nelson - RABA Technologies
     * 
     */
    static class ViewGrid implements Paintable {

        VisualizationViewer master;

        VisualizationViewer vv;

        public ViewGrid(VisualizationViewer vv, VisualizationViewer master) {
            this.vv = vv;
            this.master = master;
        }

        public void paint(Graphics g) {
            ShapeTransformer masterViewTransformer = master
                    .getViewTransformer();
            ShapeTransformer masterLayoutTransformer = master
                    .getLayoutTransformer();
            ShapeTransformer vvLayoutTransformer = vv.getLayoutTransformer();

            Rectangle rect = master.getBounds();
            GeneralPath path = new GeneralPath();
            path.moveTo(rect.x, rect.y);
            path.lineTo(rect.width, rect.y);
            path.lineTo(rect.width, rect.height);
            path.lineTo(rect.x, rect.height);
            path.lineTo(rect.x, rect.y);

            for (int i = 0; i <= rect.width; i += rect.width / 10) {
                path.moveTo(rect.x + i, rect.y);
                path.lineTo(rect.x + i, rect.height);
            }
            for (int i = 0; i <= rect.height; i += rect.height / 10) {
                path.moveTo(rect.x, rect.y + i);
                path.lineTo(rect.width, rect.y + i);
            }
            Shape lens = path;
            lens = masterViewTransformer.inverseTransform(lens);
            lens = masterLayoutTransformer.inverseTransform(lens);
            lens = vvLayoutTransformer.transform(lens);
            Graphics2D g2d = (Graphics2D) g;
            Color old = g.getColor();
            g.setColor(Color.cyan);
            g2d.draw(lens);

            path = new GeneralPath();
            path.moveTo((float) rect.getMinX(), (float) rect.getCenterY());
            path.lineTo((float) rect.getMaxX(), (float) rect.getCenterY());
            path.moveTo((float) rect.getCenterX(), (float) rect.getMinY());
            path.lineTo((float) rect.getCenterX(), (float) rect.getMaxY());
            Shape crosshairShape = path;
            crosshairShape = masterViewTransformer
                    .inverseTransform(crosshairShape);
            crosshairShape = masterLayoutTransformer
                    .inverseTransform(crosshairShape);
            crosshairShape = vvLayoutTransformer.transform(crosshairShape);
            g.setColor(Color.black);
            g2d.setStroke(new BasicStroke(3));
            g2d.draw(crosshairShape);

            g.setColor(old);
        }

        public boolean useTransform() {
            return true;
        }
    }

    private final static class DirectionDisplayPredicate implements Predicate {
        protected boolean show;

        public DirectionDisplayPredicate(boolean show) {
            this.show = show;
        }

        public void showDirected(boolean b) {
            show = b;
        }

        public boolean evaluate(Object arg0) {
            if (show && arg0 instanceof Edge)
                return true;
            return false;
        }
    }

    public class ColoredEdgePaintFunction extends GradientEdgePaintFunction {

        protected boolean fillEdge = false;

        private Color color1, color2;

        public ColoredEdgePaintFunction(
                EdgePaintFunction defaultEdgePaintFunction, HasGraphLayout vv,
                LayoutTransformer transformer) {
            super(Color.BLACK, Color.WHITE, vv, transformer);
            this.color1 = Color.BLACK;
            this.color2 = Color.BLACK;
        }

        protected void setAlpha(int alpha) {
            color1 = new Color(color1.getRed(), color1.getGreen(), color1
                    .getBlue(), alpha);
            color2 = new Color(color2.getRed(), color2.getGreen(), color2
                    .getBlue(), alpha);
        }

        protected void setColor1(Color color) {
            color1 = new Color(color.getRed(), color.getGreen(), color
                    .getBlue(), color1.getAlpha());
        }

        protected void setColor2(Color color) {
            color2 = new Color(color.getRed(), color.getGreen(), color
                    .getBlue(), color2.getAlpha());
        }

        protected Color getColor1(Edge e) {
            return color1;
        }

        protected Color getColor2(Edge e) {
            return color2;
        }

        public void useFill(boolean b) {
            fillEdge = b;
        }
    }

    private final class ColoredNodePaintFunction implements VertexPaintFunction {

        protected Color color;

        public ColoredNodePaintFunction() {
            color = new Color(1F, 0F, 0F, 1F);
        }

        public void setColor(Color c) {
            this.color = new Color(c.getRed(), c.getGreen(), c.getBlue(), color
                    .getAlpha());
        }

        protected void setAlpha(int alpha) {
            color = new Color(color.getRed(), color.getGreen(),
                    color.getBlue(), alpha);
        }

        public Paint getDrawPaint(Vertex v) {
            return Color.BLACK;
        }

        public Paint getFillPaint(Vertex v) {
            return color;
        }
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
}
