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

import aurora.engine.V1.UI.ADialog;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;

/**
 * Allows Application to play sounds in both a loop or on demand
 * //Currently is buggy and development for this has been pushed until later//
 * @author Sammy
 * @version 0.1
 */
public class ASound implements Runnable {

    private Thread runner;
    private String URL;
    private ADialog err;
    public final static String sfxButton = "button.wav";
    public final static String sfxAlert = "Alert.wav";
    public final static String sfxClunk = "Clunk.wav";
    public final static String sfxError = "Error.wav";
    public final static String sfxTheme = "loop.wav";
    private boolean loop;
    private AudioInputStream din;

    public ASound(String URL, boolean loop) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.URL = URL;

        this.loop = loop;
    }

    public void Play() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();

        } else {
            //runner.notify();
        }

    }

    private void playSound() throws UnsupportedAudioFileException, IOException, URISyntaxException, LineUnavailableException {


        AudioInputStream in = AudioSystem.getAudioInputStream(new File(getClass().getResource("/aurora/V1/resources/sound/" + URL).toURI()));



        AudioFormat baseFormat = in.getFormat();
        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED, // Encoding to use
                baseFormat.getSampleRate(), // sample rate (same as base format)
                16, // sample size in bits
                baseFormat.getChannels(), // # of Channels
                baseFormat.getChannels() * 2, // Frame Size
                baseFormat.getSampleRate(), // Frame Rate
                false // Big Endian
                );



        din = AudioSystem.getAudioInputStream(decodedFormat, in);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            line.open(decodedFormat);
        } catch (LineUnavailableException ex) {
            Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
        }


        if (line != null) {
            byte[] data = new byte[4096]; // 4k is a reasonable transfer size.
            // Start
            line.start(); // Start the line.

            int nBytesRead;
            try {
                while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
                    line.write(data, 0, nBytesRead);
                }
            } catch (IOException ex) {
                Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
            }
            // Stop
            line.drain();
            line.stop();
            line.close();
            try {
                din.close();

            } catch (IOException ex) {
                Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
            }




        }
    }

    @Override
    public void run() {

        if (loop) {
            System.out.println(Thread.currentThread() == runner);
            while (Thread.currentThread() == runner) {

                try {
                    try {
                        playSound();
                    } catch (LineUnavailableException ex) {
                        Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
                }
            }



        } else {
            try {
                try {
                    playSound();
                } catch (LineUnavailableException ex) {
                    Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (UnsupportedAudioFileException ex) {
                Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(ASound.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        runner = null;
    }
}
