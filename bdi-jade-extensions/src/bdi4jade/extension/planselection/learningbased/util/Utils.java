package bdi4jade.extension.planselection.learningbased.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Utils {

	public Utils() {
	}

	public static void writeToFile(String filePath, String text) throws IOException {
		FileWriter writer = new FileWriter(filePath, true);
		
		PrintWriter lineWriter = new PrintWriter(writer);
		lineWriter.printf("%s" + "%n", text);
		
		lineWriter.close();
		
	}

}