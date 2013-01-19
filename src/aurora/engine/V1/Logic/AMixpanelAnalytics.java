package aurora.engine.V1.Logic;

import aurora.engine.V1.Logic.JSON.JSONException;
import aurora.engine.V1.Logic.JSON.JSONObject;
import aurora.engine.V1.Logic.mixpanelapi.ClientDelivery;
import aurora.engine.V1.Logic.mixpanelapi.MessageBuilder;
import aurora.engine.V1.Logic.mixpanelapi.MixpanelAPI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
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

    public void sendEventProperty(String Event, String key, Object property) {

        String computerID = getMAC();
        String computerName = System.getProperty("user.name");
        MessageBuilder builder = new MessageBuilder(PROJECT_TOKEN);
        JSONObject properties = new JSONObject();
        try {
            properties.put(key, property);
        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        message = builder.event(computerID, Event, properties);


        delivery = new ClientDelivery();
        delivery.addMessage(message);

        try {
            message = builder.event(computerID,
                    Event, new JSONObject().put("mp_name_tag", computerName));
        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        delivery.addMessage(message);


        AThreadWorker worker = new AThreadWorker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    mixpanelAPI.deliver(delivery);
                    System.out.println(" >> Sent Property Events to Mixpanel <<");
                } catch (IOException ex) {
                    Logger.getLogger(AMixpanelAnalytics.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });

        worker.startOnce();
    }

     public void sendEventProperty(String Event, String key, Boolean increment) {

        String computerID = getMAC();
        String computerName = System.getProperty("user.name");
        MessageBuilder builder = new MessageBuilder(PROJECT_TOKEN);
        JSONObject properties = new JSONObject();
        try {
            properties.increment(key);
        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        message = builder.event(computerID, Event, properties);


        delivery = new ClientDelivery();
        delivery.addMessage(message);

        try {
            message = builder.event(computerID,
                    null, new JSONObject().put("mp_name_tag", computerName));
        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        delivery.addMessage(message);


        mixpanelAPI = new MixpanelAPI();
        AThreadWorker worker = new AThreadWorker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    mixpanelAPI.deliver(delivery);
                    System.out.println(" >> Sent Property Events to Mixpanel <<");
                } catch (IOException ex) {
                    Logger.getLogger(AMixpanelAnalytics.class.getName()).
                            log(Level.SEVERE, null, ex);
                }
            }
        });

        worker.startOnce();
    }


    public void sendEvent(String Event) {

        MessageBuilder builder = new MessageBuilder(PROJECT_TOKEN);
        String computerID = getMAC();
        String computerName = System.getProperty("user.name");





        JSONObject userProperties = new JSONObject();
        try {
            userProperties.put("$name", computerName);
        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }



        // Build the Message to send //

        delivery = new ClientDelivery();
        try {
            message = builder.event(computerID,
                    Event, new JSONObject().put("mp_name_tag", computerName));
        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }
        delivery.addMessage(message);
        message = builder.set(computerID, userProperties);
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

    private String getMAC() {
        // Get Mac Address for Unique ID //
        InetAddress ip;
        String ID = null;
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
        return ID;
    }
}
