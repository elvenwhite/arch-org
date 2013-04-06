package cvx;

import framework.TargetSoftware;

public class CVSLogParserFactory {
		
	public static CVSLogParser createCVSLogParser(TargetSoftware target) {
		
		String projPath = System.getProperty("project-path");
		String cvsLogFilePath = projPath+target.getCVSLogFilePath();
		String cvsRoot = target.getCVSRoot();
		switch(target) {
			case JUnit : case JHotDraw : {
				return new CVSLogParser1(cvsLogFilePath, cvsRoot);				
			}
			case EclipseJDT : {
				return new CVSLogParser2(cvsLogFilePath, cvsRoot);
			}
			default: {
				//TODO: exception을 raise해야 함.
				return null;
			}
		}
	}
}
