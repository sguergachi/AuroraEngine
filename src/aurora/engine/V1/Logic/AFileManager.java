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

import aurora.engine.V1.UI.AImage;
import aurora.engine.V1.UI.AImagePane;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Sammy
 *
 */
public class AFileManager {

    private boolean userExist;

    private String USER_NAME = System.getProperty("user.name");

    private String path;

    private FileOutputStream fOutStream;

    private ObjectOutputStream oOutStream;

    private FileInputStream fInStream;

    private ObjectInputStream oInStream;

    private Object Obj;

    public AFileManager() {
    }

    /**
     * Goes directly to Documents Folder then uses RootFolderName to Find
     * Location of a Folder in the Users Documents Folder
     *
     * @param RootfolderName
     */
    public AFileManager(String RootfolderName) {


        //Detect Path for Windows 7 Windows Vista Mac and XP
        if (System.getProperty("os.name").equals("Windows 7") || System
                .getProperty("os.name").equals("Windows Vista")) {
            this.path = System.getProperty("user.home") + "/Documents/"
                        + RootfolderName;
        } else if (System.getProperty("os.name").equals("Windows XP")) {
            this.path = System.getProperty("user.home") + "/My Documents/"
                        + RootfolderName;
        } else if (System.getProperty("os.name").equals("Mac OS X")) {
            this.path = "//Users//" + USER_NAME + "//Documents//"
                        + RootfolderName;
        }
        System.out.println(path);

        //make folder
        //File Root = new File(path);
        //Root.mkdir();
    }

    /**
     * Uses Custom path to get to any Directory
     *
     * @param pathDir
     * @param on      : any value will work
     */
    public AFileManager(String pathDir, Boolean on) {
        this.path = pathDir;
    }

    /**
     * Reads a text File and returns each line in an Array List
     *
     * @param fileName : File Name containing the text
     * <p/>
     * @return : ArrayList containing each record
     */
    public ArrayList<String> readFile(String fileName) {


        System.out.println(this.path + fileName);

        File file = null;
        try {
            file = new File(new URI(this.path + fileName));
        } catch (URISyntaxException ex) {
            Logger.getLogger(AFileManager.class.getName()).log(Level.SEVERE,
                    null, ex);
        }

        ArrayList<String> fileList = new ArrayList<String>();
        try {

            BufferedReader input = new BufferedReader(new FileReader(file));
            try {

                //System.out.println(input.readLine());

                int index = 0;
                String currentLine = null;
                fileList.add(input.readLine());
                while ((currentLine = input.readLine()) != null) {

                    fileList.add(currentLine);

                    index++;
                }

                return fileList;

            } finally {
                input.close();
            }
        } catch (IOException ex) {

            System.err.println(ex);
            return null;

        }

    }

    public ImageIcon findImg(String FolderName, String fileName) {
        BufferedImage bImage = null;
        if (FolderName == null) {
            if (checkFile(path + "/" + fileName)) {
                File readImg = new File(path + "/" + fileName);

                try {
                    bImage = ImageIO.read(readImg);
                    bImage.setAccelerationPriority(1);
                } catch (IOException ex) {
                    bImage.flush();
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                ImageIcon img = new ImageIcon(bImage);
                bImage.flush();
                return img;
            } else {
                return null;
            }
        } else {
            if (checkFile(path + "/" + FolderName + "/" + fileName)) {
                File readImg = new File(path + "/" + FolderName + "/" + fileName);
                try {
                    try {
                        ImageIO.setUseCache(false);
                        Thread.sleep(10);
                        bImage = ImageIO.read(readImg);
                        bImage.setAccelerationPriority(1);
                    } catch (InterruptedException ex) {
                        bImage.flush();
                        Logger.getLogger(AFileManager.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }

                } catch (IOException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                ImageIcon img = new ImageIcon(bImage);
                bImage.flush();
                return img;

            } else {
                return null;
            }

        }




    }

    public void writeImage(AImagePane img, String fileName, String folderName) {

        if (folderName != null) {
            try {
                BufferedImage bi = AImage.resizeBufferedImg(img.getImgIcon()
                        .getImage(), img.getImgIcon().getIconWidth(), img
                        .getImgIcon().getIconHeight()); // retrieve image
                File outputfile = new File(path + "/" + folderName + "/"
                                           + fileName);
                ImageIO.write(bi, "png", outputfile);
            } catch (IOException ex) {
                Logger.getLogger(AFileManager.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        } else {
            try {
                BufferedImage bi = (BufferedImage) img.getImgIcon().getImage(); // retrieve image
                File outputfile = new File(path + "/" + fileName);
                ImageIO.write(bi, "png", outputfile);
            } catch (IOException ex) {
                Logger.getLogger(AFileManager.class.getName()).log(Level.SEVERE,
                        null, ex);
            }

        }

    }

    /**
     * Create a Folder using folder name at predefined path
     */
    public void createFolder(String FolderName) {
        File Folder = new File(path + "/" + FolderName);
        if (!Folder.exists()) {
            Folder.mkdir();
        }
    }

    /**
     * Create a File in a Folder inside the Path or In The Path its self Set
     * FolderName to NULL to have the File Created in Path
     *
     * If Folder Does Not Exists This Method Will Create A New Folder With That
     * Name!
     */
    public void createFile(String FolderName, String FileName) {

        //If No Folder Name Given Make File In Path
        if (FolderName == null) {
            File Folder = new File(path + "/" + FileName);
            System.out.println("Creating file " + path + "/" + FileName);
            try {
                Folder.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(AFileManager.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        } else {
            //Check if Folder Exists
            if (checkFile(path + "/" + FolderName)) {
                File Folder = new File(path + "/" + FolderName + "/" + FileName);
                try {
                    Folder.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                // If Does Not Exists make the Folder then Make the File
            } else {
                createFolder(FolderName);
                File Folder = new File(path + "/" + FolderName + "/" + FileName);
                try {
                    Folder.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }

        }


    }

    /**
     * Serializes an object using predefined path set FolderName to NULL if you
     * want to store in Path directly
     */
    public void serializeObject(String FolderName, Object obj) {
        //If No Folder name given
        if (FolderName == null) {
            File ObjFile = new File(path + "/" + obj.getClass().getSimpleName()
                                    + ".obj");
            try {
                fOutStream = new FileOutputStream(ObjFile);
                try {
                    oOutStream = new ObjectOutputStream(fOutStream);
                    oOutStream.writeObject(obj);
                    oOutStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AFileManager.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        } else {
            //Check if Folder Exists
            if (checkFile(path + "/" + FolderName)) {
                File ObjFile = new File(path + "/" + FolderName + "/" + obj
                        .getClass().getSimpleName() + ".obj");
                try {
                    fOutStream = new FileOutputStream(ObjFile);
                    try {
                        oOutStream = new ObjectOutputStream(fOutStream);
                        oOutStream.writeObject(obj);
                        oOutStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(AFileManager.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                //Create Folder if does not Exist
            } else {
                createFolder(FolderName);
                File ObjFile = new File(path + "/" + FolderName + "/" + obj
                        .getClass().getSimpleName() + ".obj");
                try {
                    fOutStream = new FileOutputStream(ObjFile);
                    try {
                        oOutStream = new ObjectOutputStream(fOutStream);
                        oOutStream.writeObject(obj);
                        oOutStream.close();
                    } catch (IOException ex) {
                        Logger.getLogger(AFileManager.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }

        }

    }

    /**
     * Takes folder and Object name then deserializes it
     *
     * @param FolderName
     * @param ObjectName
     * <p/>
     * @return deserialized Object
     */
    public Object deserializeObject(String FolderName, String ObjectName) {
        if (FolderName == null) {
            File ObjFile = new File(path + "/" + ObjectName + ".obj");
            try {
                fInStream = new FileInputStream(ObjFile);
                try {
                    oInStream = new ObjectInputStream(fInStream);
                    try {
                        Obj = oInStream.readObject();
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(AFileManager.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AFileManager.class.getName()).log(Level.SEVERE,
                        null, ex);
            }

            return Obj;

        } else {

            //Check if Folder Exists
            if (checkFile(path + "/" + FolderName)) {
                File ObjFile = new File(path + "/" + FolderName + "/"
                                        + ObjectName + ".obj");
                try {
                    fInStream = new FileInputStream(ObjFile);
                    try {
                        oInStream = new ObjectInputStream(fInStream);
                        try {
                            Obj = oInStream.readObject();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(AFileManager.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AFileManager.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                return Obj;

                //Create Folder if does not Exist
            } else {
                createFolder(FolderName);
                File ObjFile = new File(path + "/" + FolderName + "/"
                                        + ObjectName + ".obj");
                try {
                    fInStream = new FileInputStream(ObjFile);
                    try {
                        oInStream = new ObjectInputStream(fInStream);
                        try {
                            Obj = oInStream.readObject();
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(AFileManager.class.getName())
                                    .log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(AFileManager.class.getName())
                                .log(Level.SEVERE, null, ex);
                    }
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AFileManager.class.getName()).log(
                            Level.SEVERE, null, ex);
                }

                return Obj;
            }
        }
    }

    public File[] finder(final String endsWith) {
        File dir = new File(this.path);
        //System.out.println(dir.getPath().toString());
        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                //System.out.println(filename);
                return filename.contains(endsWith);
            }
        });

    }

    /**
     * Get the extension of a file.
     *
     * @param File
     */
    public static String getExtension(File file) {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }


    /*
     * Determine if Folder or File Already exists
     */
    public boolean checkFile(String fileName) {
        System.out.println(fileName);
        File f = new File(fileName);
        if (f.exists()) {
            return true;
        } else {
            return false;
        }
    }

///Getter & Setters
    public String getPath() {
        return path;
    }

    public String getUserName() {
        return USER_NAME;
    }

    public void setPath(String Path) {
        this.path = Path;
    }
}
