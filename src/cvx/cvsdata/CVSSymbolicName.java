package cvx.cvsdata;

public class CVSSymbolicName {

    private String name;

    private CVSRevision revision;

    public CVSSymbolicName(String name, CVSRevision r) {
        this.name = name;
        this.revision = r;
    }

    public String getName() {
        return name;
    }

    public CVSRevision getRevision() {
        return revision;
    }

    public void setRevision(CVSRevision revision) {
        this.revision = revision;
    }

}
