package framework;

import util.properties.PropertiesManager;
import vis.Visualization;

public class DefaultPropertiesApplication extends Application {

    private static final String DEFAULT_PROPERTIES_NAME = "runner";

    private static final String PK_ANALYIZER_CLASS = "analyzer.class";

    private static final String PK_VISUALIZER_CLASS = "visualizer.class";

    protected Analysis[] analyzers;

    protected Visualization[] visualizers;

    public DefaultPropertiesApplication() {
        try {
            readProperties(DEFAULT_PROPERTIES_NAME);
        } catch (ApplicationInvokeException e) {
            e.printStackTrace();
            analyzers = null;
            visualizers = null;
        }
    }

    public DefaultPropertiesApplication(Analysis[] analyzers,
            Visualization[] visualizers) {
        this.analyzers = analyzers;
        this.visualizers = visualizers;
    }

    public static void main(String[] args) throws ApplicationInvokeException {
        Application app = new DefaultPropertiesApplication();
        app.execute(TargetSoftware.JUnit);
    }

    @Override
    protected void prepare(TargetSoftware target, Pipe pipe) {
    }

    @Override
    protected void setUp() {
    }

    @Override
    protected void analyze(Pipe pipe) {
        if (analyzers != null && analyzers.length > 0) {
            for (int i = 0; i < analyzers.length; i++) {
                if (analyzers[i] == null)
                    continue;
                analyzers[i].analyze(pipe);
            }
        }
    }

    @Override
    protected void visualize(Pipe pipe) throws ApplicationInvokeException {
        if (visualizers != null && visualizers.length > 0) {
            for (int i = 0; i < visualizers.length; i++) {
                if (visualizers[i] == null)
                    continue;
                visualizers[i].visualize(pipe);
            }
        }
    }

    protected void readProperties(String propertiesName)
            throws ApplicationInvokeException {
        initAnalizer(propertiesName);
        initVisualizer(propertiesName);
    }

    private void initAnalizer(String propertiesName)
            throws ApplicationInvokeException {
        String[] analyzerClassNames = PropertiesManager.getValues(
                propertiesName, PK_ANALYIZER_CLASS);
        if (analyzerClassNames == null || analyzerClassNames.length < 1) {
            analyzers = null;
            return;
        }

        if (analyzerClassNames[0] == null || analyzerClassNames[0].equals("")) {
            analyzers = null;
            return;
        }

        analyzers = new Analysis[analyzerClassNames.length];
        for (int i = 0; i < analyzerClassNames.length; i++) {
            try {
                Class analyzerClass = Class.forName(analyzerClassNames[i]);
                analyzers[i] = (Analysis) analyzerClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new ApplicationInvokeException(e);
            } catch (InstantiationException e) {
                throw new ApplicationInvokeException(e);
            } catch (IllegalAccessException e) {
                throw new ApplicationInvokeException(e);
            }
        }
    }

    private void initVisualizer(String propertiesName)
            throws ApplicationInvokeException {
        String[] visualizerClassNames = PropertiesManager.getValues(
                propertiesName, PK_VISUALIZER_CLASS);
        if (visualizerClassNames == null || visualizerClassNames.length < 1) {
            visualizers = null;
            return;
        }

        if (visualizerClassNames[0] == null
                || visualizerClassNames[0].equals("")) {
            visualizers = null;
            return;
        }

        visualizers = new Visualization[visualizerClassNames.length];
        for (int i = 0; i < visualizerClassNames.length; i++) {
            try {
                Class visualizerClass = Class.forName(visualizerClassNames[i]);
                visualizers[i] = (Visualization) visualizerClass.newInstance();
            } catch (ClassNotFoundException e) {
                throw new ApplicationInvokeException(e);
            } catch (InstantiationException e) {
                throw new ApplicationInvokeException(e);
            } catch (IllegalAccessException e) {
                throw new ApplicationInvokeException(e);
            }
        }
    }
}
