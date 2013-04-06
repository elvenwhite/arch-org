package cvx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cvx.cvsdata.CVSFile;

public class CVSDataFileWriter {
	private String filePath;

	private Iterable<CVSFile> iterate;

	public CVSDataFileWriter(String filePath, Iterable<CVSFile> iter) {
		this.filePath = filePath;
		this.iterate = iter;
	}

	public void save() throws IOException {
		FileOutputStream fos = new FileOutputStream(filePath);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		for (CVSFile file : iterate) {
			oos.writeObject(file);
		}

		oos.flush();
		oos.close();
	}
}
