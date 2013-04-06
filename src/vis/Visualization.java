package vis;

import framework.ApplicationInvokeException;
import framework.Pipe;

public interface Visualization {
    public void visualize(Pipe pipe) throws ApplicationInvokeException;
}
