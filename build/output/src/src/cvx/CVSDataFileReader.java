package cvx;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import cvx.cvsdata.CVSFile;

public class CVSDataFileReader {
	private String filePath;

	public CVSDataFileReader(String filePath) {
		this.filePath = filePath;
	}

	public boolean load() throws IOException {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			CVSFile file = null;
			try {
				do {
					file = (CVSFile) ois.readObject();
					CVSFile.regist(file);
				} while (file != null);
			} catch (EOFException ex) {
			}
		} catch (IOException e) {
			throw e;
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}
}
