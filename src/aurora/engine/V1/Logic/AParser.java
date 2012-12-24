package aurora.engine.V1.Logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AParser {

	private ArrayList<String> parrsedArray;
	private AFileManager mngr;

	/**
	 * Interact with file system to get required data
	 * 
	 * @param RootPath
	 *            path to the directory containing the file(s) you want to parse
	 */
	public AParser(String RootPath) {
		this.mngr = new AFileManager(RootPath, true);
		this.parrsedArray = new ArrayList<String>();
	}

	/**
	 * Will Read Your File Line By Line and return a ArrayList Containing each
	 * line
	 * 
	 * @param file
	 * @throws IOException
	 */
	public ArrayList<String> parseFile(String fileName) throws IOException {
		this.parrsedArray = new ArrayList<String>();
		FileInputStream inFile = null;

		inFile = new FileInputStream(mngr.getPath() + fileName);
		DataInputStream in = new DataInputStream(inFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		String strLine;

		while ((strLine = reader.readLine()) != null && strLine.length() > 0) {
			// Print the content on the console
			parrsedArray.add(strLine);
		}

		inFile.close();

		return parrsedArray;

	}

}
