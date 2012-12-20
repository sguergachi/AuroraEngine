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

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * .------------------------------------------------------------------------.
 * | ASound
 * .------------------------------------------------------------------------.
 * |
 * | This class handles sound playback. 
 * | The sound runs on a new Thread to avoid interruption of the main program.
 * | 
 * .........................................................................
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 * @author Carlos Machado <camachado at gmail.com>
 * @author Marius Brandt <marius dot brandt at hotmail.com>
 * 
 */

public class ASound implements Runnable {

	/**
	 * Thread to run on.
	 */
    private Thread runner;
    
    /**
     * Path to sound.
     */
    private String URL;
    
    /**
     * Name Constant.
     */
    public final static String sfxButton = "button.wav";
    
    /**
     * Name Constant.
     */
    public final static String sfxAlert = "Alert.wav";
    
    /**
     * Name Constant.
     */
    public final static String sfxClunk = "Clunk.wav";
    
    /**
     * Name Constant.
     */
    public final static String sfxError = "Error.wav";
    
    /**
     * Name Constant.
     */
    public final static String sfxTheme = "loop.wav";
    
    /**
     * Bool Constant.
     */
    private boolean loop;
    
    /**
     * Audio stream constant
     */
    private AudioInputStream in;
    
    /**
     * Audio clip
     */
    private Clip audioClip;
    
    /**
     * Audio clip
     */
    private AudioClip sound;
    
    /**
     * Location to File
     */
    private URL path;
    
    
    /**
     * .-----------------------------------------------------------------------.
     * | ASound(String, boolean)
     * .-----------------------------------------------------------------------.
     * |
     * | This is the constructor for the ASound class.
     * | It will prepare the given sound for playback.
     * | 
     * | Note: The URL should be something like this -> "yoursound.wav"
     * | 
     * | If you want the sound to be played in a loop, set the boolean loop to true
     * | otherwise set it to false.
     * |
     * |
     * .........................................................................
     *
     * @param URL String, loop boolean
     *
     */
    public ASound(String URL, boolean loop) throws MalformedURLException, UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.URL = URL;

        this.loop = loop;
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | Play()
     * .-----------------------------------------------------------------------.
     * |
     * | Use this method to play the sound defined in the constructor.
     * |
     * | This method MUST either be embedded into a try-catch statement,
     * | or the method which calls this method must have certain throws declarations
     * | Note: Eclipse will offer you to do any of both options for you.
     * |
     * .........................................................................
     *
     */

    public void Play() throws UnsupportedAudioFileException, IOException, LineUnavailableException, InterruptedException {
        if (runner == null) {
            runner = new Thread(this);
            runner.start();

        } else {
            //runner.notify();
        }
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | Stop()
     * .-----------------------------------------------------------------------.
     * |
     * | This method will manually stop the current audio.
     * | It will also try to free all used resources.
     * | 
     * .........................................................................
     *
     */
    public void Stop(){
    	if(sound != null){
    		sound.stop();
    	}
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | playSound()
     * .-----------------------------------------------------------------------.
     * |
     * | This private method will be called once a Thread of this class has been
     * | created successfully.
     * | 
     * | If you are using custom resources, change "/aurora/V1/resources/Sound/" 
     * | to whatever fits your project setup.
     * | 
     * .........................................................................
     *
     */
    
    private void playSound() throws UnsupportedAudioFileException, IOException, URISyntaxException, LineUnavailableException {
    	path = new URL(getClass().getResource("/aurora/V1/resources/Sound/" + URL).toString());
    	in = AudioSystem.getAudioInputStream(new URL(getClass().getResource("/aurora/V1/resources/Sound/" + URL).toString()));
    	audioClip = AudioSystem.getClip();
    	sound = Applet.newAudioClip(path);
    	if(loop){
    		sound.loop();
    	}else{
    		sound.play();
    	}
    }
    
    /**
     * .-----------------------------------------------------------------------.
     * | run()
     * .-----------------------------------------------------------------------.
     * |
     * | This method is the main method of any Thread.
     * | It will be executed as soon as the Thread has been created.
     * | 
     * .........................................................................
     *
     */
    @Override
    public void run() {
    	try {
			playSound();
		} catch (Exception e) {
			e.printStackTrace();
		}
        runner = null;
    }
}
