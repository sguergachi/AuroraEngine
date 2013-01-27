package aurora.engine.V1.Logic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * .------------------------------------------------------------------------.
 * | AParser
 * .------------------------------------------------------------------------.
 * |
 * | This class reads a File and stores the content of this file line by line
 * | in an ArrayList.
 * |
 * |
 * .........................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 *
 */

public class AParser {

	/**
	 * Content of File
	 */
	private ArrayList<String> parrsedArray;

	/**
	 * File Manager
	 */
	private AFileManager mngr;


    /**
     * .-----------------------------------------------------------------------.
     * | AParser(String RootPath)
     * .-----------------------------------------------------------------------.
     * |
     * | This constructor prepares the AFileManager to read the file from the
     * | correct path.
     * | It also ensures that the ArrayList is initialized.
     * |
     * .........................................................................
     *
     * @param RootPath path to the directory containing the file(s) you want to parse
     *
     */
	public AParser(String RootPath) {
		this.mngr = new AFileManager(RootPath);
		this.parrsedArray = new ArrayList<String>();
	}

    /**
     * .-----------------------------------------------------------------------.
     * | parseFile(String fileName)
     * .-----------------------------------------------------------------------.
     * |
     * | This function reades the given File in the directory specified in the
     * | constructor AParser and stores the contents of the File in an ArrayList.
     * |
     * | This function returns this ArrayList
     * |
     * .........................................................................
     *
     * @param fileName name of the File to be parsed
     * @throws IOException
     *
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
