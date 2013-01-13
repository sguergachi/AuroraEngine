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
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
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

    private String ID;

    public AMixpanelAnalytics(String Token) {
        PROJECT_TOKEN = Token;
        mixpanelAPI = new MixpanelAPI();
    }

    public void sendEvent(String Event) {

        MessageBuilder builder = new MessageBuilder(PROJECT_TOKEN);
        String computername = null;
        try {
            computername = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }



        // Get Mac Address for Unique ID //
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();


            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ?
                        "-" : ""));
            }

            ID = sb.toString();

        } catch (UnknownHostException e) {

            e.printStackTrace();

        } catch (SocketException e) {

            e.printStackTrace();

        }

        // Build the Message to send //
        message = builder.event(ID,
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
}
