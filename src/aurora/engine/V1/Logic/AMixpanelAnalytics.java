package aurora.engine.V1.Logic;

import aurora.engine.V1.Logic.JSON.JSONObject;
import aurora.engine.V1.Logic.mixpanelapi.ClientDelivery;
import aurora.engine.V1.Logic.mixpanelapi.MessageBuilder;
import aurora.engine.V1.Logic.mixpanelapi.MixpanelAPI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Queue;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Mixpanel Tracking impementation based off MixpanelAPIDemo.java
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AMixpanelAnalytics {

    public String PROJECT_TOKEN;

    private JSONObject message;

    private ClientDelivery delivery;

    private MixpanelAPI mixpanelAPI;

    public AMixpanelAnalytics(String Token) {
        PROJECT_TOKEN = Token;
        mixpanelAPI = new MixpanelAPI();
    }

    public void sendEvent(String Event) {

        MessageBuilder builder = new MessageBuilder(PROJECT_TOKEN);
        message = builder.event(getMotherboardSN(),
                Event, null);
        delivery = new ClientDelivery();
        delivery.addMessage(message);

        AThreadWorker worker = new AThreadWorker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    mixpanelAPI.deliver(delivery);
                    System.out.println(" >> Sent Event to Mixpanel <<");
                } catch (IOException ex) {
                    Logger.getLogger(AMixpanelAnalytics.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });

        worker.startOnce();

    }

    public static String getMotherboardSN() {
  String result = "";
    try {
      File file = File.createTempFile("mb-sn",".vbs");
      file.deleteOnExit();
      FileWriter fw = new java.io.FileWriter(file);

      String vbs =
         "Set objWMIService = GetObject(\"winmgmts:\\\\.\\root\\cimv2\")\n"
        + "Set colItems = objWMIService.ExecQuery _ \n"
        + "   (\"Select * from Win32_BaseBoard\") \n"
        + "For Each objItem in colItems \n"
        + "    Wscript.Echo objItem.SerialNumber \n"
        + "    exit for  ' do the first cpu only! \n"
        + "Next \n";

      fw.write(vbs);
      fw.close();
      Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
      BufferedReader input =
        new BufferedReader
          (new InputStreamReader(p.getInputStream()));
      String line;
      while ((line = input.readLine()) != null) {
         result += line;
      }
      input.close();
    }
    catch(Exception e){
        e.printStackTrace();
    }
    return result.trim();
  }
}
