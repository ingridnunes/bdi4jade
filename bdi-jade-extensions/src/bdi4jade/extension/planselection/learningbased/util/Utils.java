package bdi4jade.extension.planselection.learningbased.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Implements useful methods to be used in general applications.
 * 
 * @author João Faccin
 */
public class Utils {

	public Utils() {
	}

	/**
	 * Writes a given text into a file specified in parameters.
	 * 
	 * @param filePath
	 *            Specifies a path to a file which a text will be write.
	 * @param text
	 *            A string to be write in filePath.
	 * @throws IOException
	 */
	public static void writeToFile(String filePath, String text)
			throws IOException {
		FileWriter writer = new FileWriter(filePath, true);
		PrintWriter lineWriter = new PrintWriter(writer);
		lineWriter.printf("%s" + "%n", text);
		lineWriter.close();
	}

}