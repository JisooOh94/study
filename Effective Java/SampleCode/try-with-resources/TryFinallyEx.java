import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TryFinallyEx {
	public String getFirstLineOfFile(String path) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

		try {
			return bufferedReader.readLine();
		} finally {
			bufferedReader.close();
		}
	}

	public void reandAndWriteFile(String sourcePath, String targetPath) throws IOException{
		String line;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(sourcePath));
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(targetPath));
			try {
				while (true) {
					if ((line = bufferedReader.readLine()) != null) {
						bufferedWriter.write(line);
					}
				}
			} finally {
				bufferedWriter.close();
			}
		} finally {
			bufferedReader.close();
		}
	}
}
