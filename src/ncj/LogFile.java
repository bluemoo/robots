package ncj;

import java.io.File;
import java.io.IOException;

import robocode.RobocodeFileWriter;

public class LogFile implements ILogFile {

	private RobocodeFileWriter _writer;

	public LogFile(File dataFile) {
		try {
			_writer = new RobocodeFileWriter(dataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void write(String s) {
		try {
			_writer.write(s+"\n");
			_writer.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			_writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
