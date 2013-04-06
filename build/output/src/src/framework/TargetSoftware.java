package framework;

public enum TargetSoftware {
	
	JUnit (
			"JUnit",
			"junit_log.txt",
			"/cvsroot/junit/junit/"
	), 
	JHotDraw (
			"JHotDraw",
			"jhotdraw_log.txt",
			"/cvsroot/jhotdraw/jhotdraw6/src/"	
	), 
	EclipseJDT (
			"EclipseJDT",
			"jdt_log.txt",
			"/cvsroot/eclipse/org.eclipse.jdt."
	);
	
	//----------------------------------------------------------
	
	private String name;
	private String cvsLogFilePath;
	private String cvsRoot;
	
	TargetSoftware(String name, String cvsLogFilePath, String cvsRoot) {
		this.name = name;
		this.cvsLogFilePath = cvsLogFilePath;
		this.cvsRoot = cvsRoot;
	}
	
	public String getName() {
		return name;
	}
	
	public String getCVSLogFilePath() {
		return cvsLogFilePath;
	}
	
	public String getCVSRoot() {
		return cvsRoot;
	}
}
