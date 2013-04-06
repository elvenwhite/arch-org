package vis.viewer;

import util.Constants;
import edu.uci.ics.jung.graph.ArchetypeVertex;
import edu.uci.ics.jung.graph.decorators.VertexStringer;

public class DotGraphVertexStringer implements VertexStringer {

    private int type = Constants.NODE_LABEL_TYPE_ID;

    public DotGraphVertexStringer() {
        this(Constants.NODE_LABEL_TYPE_ID);
    }

    public DotGraphVertexStringer(int type) {
        this.type = type;
    }

    public void setType(int type) {
        if (type != Constants.NODE_LABEL_TYPE_ID
                && type != Constants.NODE_LABEL_TYPE_NAME) {
            type = Constants.NODE_LABEL_TYPE_ID;
        }
        this.type = type;
    }

    public String getLabel(ArchetypeVertex v) {
        switch (type) {
        case Constants.NODE_LABEL_TYPE_ID:
            return (String) v.getUserDatum(Constants.KEY_ID);
        case Constants.NODE_LABEL_TYPE_NAME:
            return (String) v.getUserDatum(Constants.KEY_NAME);
        }
        return v.toString();
    }

}
