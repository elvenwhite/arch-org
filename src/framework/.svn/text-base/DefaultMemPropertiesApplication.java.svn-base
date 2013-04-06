package framework;

import util.properties.FilePropertiesManager;
import util.properties.PropertiesManager;
import vis.Visualization;

public class DefaultMemPropertiesApplication extends Application {

    private static String superGroup = "";

    private static final String DEFAULT_PROPERTIES_NAME = "multi-runner";

    private static final String PK_ANALYIZER_CLASS = "analyzer.class";

    private static final String PK_VISUALIZER_CLASS = "visualizer.class";

    protected Analysis[] analyzers;

    protected Visualization[] visualizers;

    public static void main(String[] args) throws ApplicationInvokeException {
        superGroup = args[0];
        if (superGroup ==  null || superGroup.equals("")) {
            System.out.println("Invalid Super Group");
            return;
        }
        PropertiesManager.setDefaultGroup(DEFAULT_PROPERTIES_NAME);
        PropertiesManager.setPropertiesManager(new MyPropertiesManager());
        Application app = new DefaultMemPropertiesApplication();
        ((DefaultMemPropertiesApplication) app)
                .readProperties(DEFAULT_PROPERTIES_NAME);

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

    private static final class MyPropertiesManager extends
            FilePropertiesManager {
        public String getValue(String group, String key) {
            System.out.println(superGroup + "." + key);
            String s =super.getValue(group, superGroup + "." + key);
            return super.getValue(group, superGroup + "." + key);
        }
    }
}
