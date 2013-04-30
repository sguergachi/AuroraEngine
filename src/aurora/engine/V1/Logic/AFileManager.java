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
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

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

    static final Logger logger = Logger.getLogger(AFileManager.class);

    public AFileManager() {
    }

    /**
     * Goes directly to Documents Folder then uses RootFolderName to Find
     * Location of a Folder in the Users Documents Folder
     *
     * @param RootfolderName
     */
    public AFileManager(String currentPath) {

        this.path = currentPath;


    }

    /**
     * Reads a text File and returns each line in an Array List
     *
     * @param fileName : File Name containing the text
     * <p/>
     * @return : ArrayList containing each record
     */
    public ArrayList<String> readFile(String fileName) {

        if (logger.isDebugEnabled()) {
            logger.debug(this.path + fileName);
        }

        File file = null;
        try {
            file = new File(new URI(this.path + fileName));
        } catch (URISyntaxException ex) {
            logger.error(ex);
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
            logger.error(ex);
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
                    logger.error(ex);
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
                        logger.error(ex);
                    }

                } catch (IOException ex) {
                    logger.error(ex);
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
                logger.error(ex);
            }
        } else {
            try {
                BufferedImage bi = (BufferedImage) img.getImgIcon().getImage(); // retrieve image
                File outputfile = new File(path + "/" + fileName);
                ImageIO.write(bi, "png", outputfile);
            } catch (IOException ex) {
                logger.error(ex);
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
            logger.info("Creating file " + path + "/" + FileName);

            try {
                Folder.createNewFile();
            } catch (IOException ex) {
                logger.error(ex);
            }
        } else {
            //Check if Folder Exists
            if (checkFile(path + "/" + FolderName)) {
                File Folder = new File(path + "/" + FolderName + "/" + FileName);
                try {
                    Folder.createNewFile();
                } catch (IOException ex) {
                    logger.error(ex);
                }

                // If Does Not Exists make the Folder then Make the File
            } else {
                createFolder(FolderName);
                File Folder = new File(path + "/" + FolderName + "/" + FileName);
                try {
                    Folder.createNewFile();
                } catch (IOException ex) {
                    logger.error(ex);
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
                    logger.error(ex);
                }
            } catch (FileNotFoundException ex) {
                logger.error(ex);
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
                        logger.error(ex);
                    }
                } catch (FileNotFoundException ex) {
                    logger.error(ex);
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
                        logger.error(ex);
                    }
                } catch (FileNotFoundException ex) {
                    logger.error(ex);
                }
            }

        }

    }

    /**
     * Takes folder and Object name then deserializes it
     *
     * @param FolderName
     * @param ObjectName
     *                   <
     *                   p/>
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
                        logger.error(ex);
                    }
                } catch (IOException ex) {
                    logger.error(ex);
                }
            } catch (FileNotFoundException ex) {
                logger.error(ex);
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
                            logger.error(ex);
                        }
                    } catch (IOException ex) {
                        logger.error(ex);
                    }
                } catch (FileNotFoundException ex) {
                    logger.error(ex);
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
                            logger.error(ex);
                        }
                    } catch (IOException ex) {
                        logger.error(ex);
                    }
                } catch (FileNotFoundException ex) {
                    logger.error(ex);
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

    public void moveFolder(String source, String destination) {
        File sourceFile = new File(source);
        File destinationFile = new File(destination);


        copyDirectory(sourceFile, destinationFile);
        try {
            if (!deleteFile(sourceFile)) {
                throw new IOException("Unable to delete original folder");
            }
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    public void copyDirectory(File sourceDir, File destDir) {

        if (sourceDir.exists()) {
            if (!destDir.exists()) {
                destDir.mkdir();
            }

            File[] children = sourceDir.listFiles();

            for (File sourceChild : children) {
                String name = sourceChild.getName();
                File destChild = new File(destDir, name);
                if (sourceChild.isDirectory()) {
                    copyDirectory(sourceChild, destChild);
                } else {
                    try {
                        copyFile(sourceChild, destChild);
                    } catch (IOException ex) {
                        logger.error(ex);
                    }
                }
            }
        }
    }

    public void copyFile(File source, File dest) throws IOException {

        if (!dest.exists()) {
            logger.info("Creating new file " + dest);
            dest.createNewFile();
        }
        InputStream in = null;
        OutputStream out = null;

        logger.info("Copying " + source + " to " + dest);
        try {
            in = new FileInputStream(source);
            out = new FileOutputStream(dest);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } finally {
            in.close();
            out.close();
        }

    }

    public void downloadFile(URL location, File dest) throws IOException {


//        try {
//            URLConnection urlConn = location.openConnection();
//            BufferedInputStream is = new BufferedInputStream(urlConn
//                    .getInputStream());
//            File out = dest;
//            BufferedOutputStream bout = new BufferedOutputStream(
//                    new FileOutputStream(dest));
//            byte[] b = new byte[8 * 1024];
//            int read = 0;
//            while ((read = is.read(b)) > -1) {
//                bout.write(b, 0, read);
//            }
//            bout.flush();
//            bout.close();
//            is.close();
//
//        } catch (IOException mfu) {
//            mfu.printStackTrace();
//        }

        ReadableByteChannel rbc = Channels.newChannel(location.openStream());
        FileOutputStream fos = new FileOutputStream(dest);
        fos.getChannel().transferFrom(rbc, 0, 1 << 24);



    }

    public boolean deleteFile(File resource) throws IOException {
        if (resource.isDirectory()) {
            File[] childFiles = resource.listFiles();
            for (File child : childFiles) {
                deleteFile(child);
            }

        }
        return resource.delete();

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
    public static boolean checkFile(String fileName) {

        File file = new File(fileName);
        logger.info("Checking if " + file + " exists");

        if (file.exists()) {
            logger.info(file + " Exists!");
            return true;
        } else {
            logger.info(file + " Does Not Exist!");
            return false;
        }
    }

    /**
     * get current working path
     * <p/>
     * @return string of path
     */
    public String getPath() {
        return path;
    }

    /**
     * get my document path for windows and mac
     * <p/>
     * @return string of MyDocuments folder path
     */
    public String getMyDocPath() {
        String docPath = System.getProperty("user.home");

        //Detect Path for My Doc on Windows 7 Windows Vista Mac and XP
        if (System.getProperty("os.name").equals("Windows 7") || System
                .getProperty("os.name").equals("Windows Vista") || System
                .getProperty("os.name").equals("Windows 8")) {
            docPath = System.getProperty("user.home") + "/Documents/";
        } else if (System.getProperty("os.name").equals("Windows XP")) {
            docPath = System.getProperty("user.home") + "/My Documents/";
        } else if (System.getProperty("os.name").equals("Mac OS X")) {
            docPath = "//Users//" + USER_NAME + "//Documents//";
        }

        return docPath;
    }

    public String getUserName() {
        return USER_NAME;
    }

    public void setPath(String Path) {
        this.path = Path;
    }
}
