/*
 * Copyright 2012 Sardonix Creative.
 *
 * This work is licensed under the
 * Creative Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License.
 * To view a copy of this license, visit
 *
 *      http://creativecommons.org/licenses/by-nc-nd/3.0/
 *
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900,
 * Mountain View, California, 94041, USA.
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package aurora.engine.V1.Logic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import org.apache.log4j.Logger;


/**
 *
 * @author Sammy
 * @version 0.1
 */
public class ANuance {

    private static final long serialVersionUID = 1L;
    //TODO Add DAY Sensitive Greetings, i.e. Happy Holidays OR Merry Christmas, OR Happy Birthday <- might be harder to implement
    //TODO Add ability to read greetings from text file

    private String[] fld_Welcome = {"Welcome"};

    public static final int inx_Welcome = 0;

    private String[] fld_Sure = {"Sure", "Certain", "Confident"};

    public static final int inx_Sure = 1;

    private String[] fld_Please = {"Please", "Kindly", "Calmly"};

    public static final int inx_Please = 2;

    private String[] fld_Searching = {"Searching", "Exploring", "Looking",
        "Scaning", "Probing", "Scouring", "Inspecting"};

    public static final int inx_Searching = 3;

    private String[] fld_Preparing = {"Preparing", "Accommodating", "Adjusting",
        "Modifying", "Configuring", "Altering"};

    public static final int inx_Preparing = 4;

    private String[] fld_Hi = {"Hey", "Hello", "Hi"};

    private String[] fld_Error = {"Error", "Fault", "Invalid", "Failure"};

    public static final int inx_Error = 5;

    private String[] fld_Exit = {"Exit", "Quit", "Leave", "Go", "Depart",
        "Scram", "Flee", "Terminate"};

    public static final int inx_Exit = 6;

    private String[] fld_WelcomeBack = {"Welcome Back!", "How Was Your Day!",
        "Let Me Finish Loading Your Games", "Good To See You!",
        "Lets Play Some Games!!"};

    public static final int inx_WelcomeBack = 7;

    public static final int inx_Greeting = 8;

    public static final int inx_User = 9;

    private String vi_Response;

    private final Random randomGenerator;

    private AParser parser;

    private ArrayList<String> fileContent;

    private int[] lineLength;

    public String[][] nuanceDict;

    private File file;

    private boolean useInternal;
    
    static final Logger logger = Logger.getLogger(ANuance.class);

    public ANuance() {
        randomGenerator = new Random();
        useInternal = true;
    }

    //get file from internet and parse it
    public ANuance(String FileURL, String localFile) throws IOException {
        randomGenerator = new Random();
        writeFileFromURL(FileURL, localFile);
        lineLength = new int[64];

        file = new File(localFile);


        AParser parser = new AParser(file.getCanonicalPath()
                .substring(0, file.getCanonicalPath().length() - file.getName()
                .length()));

        System.out.println("LOCAL FILE: " + file.getCanonicalPath()
                .substring(0, file.getCanonicalPath().length() - file.getName()
                .length()));

        fileContent = parser.parseFile(file.getName());
        nuanceDict = parse(fileContent, 64);
    }

    //use existing file on local hard drive
    public ANuance(String localFile) {
        randomGenerator = new Random();
        try {

            lineLength = new int[64];
            file = new File(localFile);

            AParser parser = new AParser(file.getCanonicalPath()
                    .substring(0, file.getCanonicalPath().length() - file
                    .getName()
                    .length()));
            logger.info("Path: " + file.getCanonicalPath()
                    .substring(0, file.getCanonicalPath().length() - file
                    .getName()
                    .length()));
            fileContent = parser.parseFile(file.getName());

            nuanceDict = parse(fileContent, 64);
        } catch (Exception e) {
            useInternal = true;
            logger.error(e);
        }
    }

    private void writeFileFromURL(String FileURL, String localFile) {
        try {
            int bytecount = 1024; //define how many bytes shall be read from the file
            BufferedInputStream in = new BufferedInputStream(new URL(FileURL)
                    .openStream());
            FileOutputStream fos = new FileOutputStream(localFile);
            BufferedOutputStream out = new BufferedOutputStream(fos);
            byte[] data = new byte[bytecount];
            int tmp;
            while ((tmp = in.read(data, 0, bytecount)) != -1) {
                out.write(data, 0, tmp);
            }

            out.close();
            fos.close();
            in.close();

        } catch (Exception e) {
            useInternal = true;
            logger.error(e);
        }
    }

    private String[][] parse(ArrayList<String> content, int memory) {
        int clen = content.size();
        String[][] ret = new String[clen][memory]; //memory represents the columns to be preserved in memory on runtime.
        String[] tmp;
        for (int i = 0; i < clen; i++) {
            tmp = content.get(i).toString().split(",");
            lineLength[i] = tmp.length;
            for (int j = 0; j < tmp.length; j++) {
                ret[i][j] = tmp[j];
            }
        }
        return ret;
    }

    public int getLineLength(int line) {
        if ((line >= 0) && (line < lineLength.length)) {
            return lineLength[line];
        } else {
            return -1;
        }
    }

    public String VI(int index) {
        if (!useInternal) {
            if (index == inx_Welcome) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Sure) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Please) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Searching) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Preparing) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Error) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Exit) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_WelcomeBack) {
                vi_Response = nuanceDict[index][generateNum(0, getLineLength(
                        index)
                                                               - 1)];
            } else if (index == inx_Greeting) {
                int greetingRand = generateNum(0, 1);

                if (greetingRand == 0) {
                    vi_Response = fld_Hi[generateNum(0, fld_Hi.length - 1)];
                } else {

                    // added by Carlos - time sensitive greeting
                    Calendar cal = Calendar.getInstance();
                    int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);

                    if (hourOfDay <= 12) {
                        vi_Response = "Good morning!";
                    } else if (hourOfDay >= 13 && hourOfDay <= 17) {
                        vi_Response = "Good afternoon!";
                    } else if (hourOfDay >= 18) {
                        vi_Response = "Good evening!";
                    }
                }

            } else if (index == inx_User) {
                vi_Response = System.getProperty("user.name");
            }
        } else {
            if (index == inx_Welcome) {
                vi_Response = fld_Welcome[generateNum(0, fld_Welcome.length - 1)];
            } else if (index == inx_Sure) {
                vi_Response = fld_Sure[generateNum(0, fld_Sure.length - 1)];
            } else if (index == inx_Please) {
                vi_Response = fld_Please[generateNum(0, fld_Please.length - 1)];
            } else if (index == inx_Searching) {
                vi_Response = fld_Searching[generateNum(0, fld_Searching.length
                                                           - 1)];
            } else if (index == inx_Preparing) {
                vi_Response = fld_Preparing[generateNum(0, fld_Preparing.length
                                                           - 1)];
            } else if (index == inx_Error) {
                vi_Response = fld_Error[generateNum(0, fld_Error.length - 1)];
            } else if (index == inx_Exit) {
                vi_Response = fld_Exit[generateNum(0, fld_Exit.length - 1)];
            } else if (index == inx_User) {
                vi_Response = System.getProperty("user.name");
            } else if (index == inx_WelcomeBack) {
                vi_Response = fld_WelcomeBack[generateNum(0,
                        fld_WelcomeBack.length
                        - 1)];
            } else if (index == inx_Greeting) {
                int greetingRand = generateNum(0, 1);

                if (greetingRand == 0) {
                    vi_Response = fld_Hi[generateNum(0, fld_Hi.length - 1)];
                } else {

                    // added by Carlos - time sensitive greeting
                    Calendar cal = Calendar.getInstance();
                    int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);

                    if (hourOfDay <= 12) {
                        vi_Response = "Good morning!";
                    } else if (hourOfDay >= 13 && hourOfDay <= 17) {
                        vi_Response = "Good afternoon!";
                    } else if (hourOfDay >= 18) {
                        vi_Response = "Good evening!";
                    }
                }

            }
        }

        return vi_Response;
    }

    private int generateNum(int btwNum1, int btwNum2) {
        int num = btwNum1;
        num = (int) (randomGenerator.nextInt(btwNum2 + 1));

        return num;
    }
}
