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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple Mixpanel Tracking implementation based off MixpanelAPI
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AMixpanelAnalytics {

    public String PROJECT_TOKEN;

    private ClientDelivery delivery;

    private MixpanelAPI mixpanelAPI;

    private String computerID;

    private final LinkedHashMap<Object, Object> propertiesMap;

    private final String computerName;

    public AMixpanelAnalytics(String Token) {
        PROJECT_TOKEN = Token;
        mixpanelAPI = new MixpanelAPI();
        computerID = Integer.toString(getMAC().hashCode());
        computerName = System.getProperty("user.name");
        propertiesMap = new LinkedHashMap<Object, Object>();
    }

    /**
     * Adds to an accumulating list of properties which will all be sent
     * with an associated even using sendEventProperty() and then cleared to
     * be usable again
     * <p/>
     * @param key
     * @param property
     */
    public void addProperty(Object key, Object property) {
        propertiesMap.put(key, property);
    }

    /**
     * Sends all that was previously added to the properties list
     * via addProperty() to Mixpanel and clears the properties list.
     *
     * if the property is a Boolean then an increment() is sent to the
     * specific key through Mixpanel API.
     * If not then the Object attached to the key is sent.
     *
     * @param Event
     * @param key
     * @param property
     */
    public void sendEventProperty(String Event) {


        JSONObject userProperties = new JSONObject();
        JSONObject properties = new JSONObject();
        JSONObject propertiesMessage;
        JSONObject userMessage;
        MessageBuilder builder = new MessageBuilder(PROJECT_TOKEN);




        try {

            // Add Properties from Map to JSONobj //
            Iterator entries = propertiesMap.entrySet().iterator();
            while (entries.hasNext()) {
                Entry propertyEntry = (Entry) entries.next();
                if (propertyEntry.getValue() instanceof Boolean) {
                    properties.increment(propertyEntry.getKey().toString());
                } else {
                    properties.put(propertyEntry.getKey().toString(),
                            propertyEntry.getValue());
                }
            }
            propertiesMap.clear();

            // Attaches User name to property //
            properties.put("mp_name_tag", computerName);

        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class.getName()).
                    log(Level.SEVERE, null, ex);
        }

        try {
            // People Analytic //
            userProperties.put("$name", computerName);

        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class
                    .getName()).
                    log(Level.SEVERE, null, ex);
        }



        // Create Messages //

        propertiesMessage = builder.event(computerID, Event, properties);
        userMessage = builder.set(computerID, userProperties);


        // Send Messages To Mixpanel //
        delivery = new ClientDelivery();
        delivery.addMessage(userMessage);
        delivery.addMessage(propertiesMessage);
        AThreadWorker worker = new AThreadWorker(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    mixpanelAPI.deliver(delivery);
                    System.out
                            .println(" >> Sent Property Events to Mixpanel <<");
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
        JSONObject message = null;
        JSONObject userProperties = new JSONObject();
        try {
            userProperties.put("$name", computerName);


        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class
                    .getName()).
                    log(Level.SEVERE, null, ex);
        }


        // Build the Message to send //

        delivery = new ClientDelivery();

        try {
            message = builder.event(computerID,
                    Event, new JSONObject().put("mp_name_tag", computerName));


        } catch (JSONException ex) {
            Logger.getLogger(AMixpanelAnalytics.class
                    .getName()).
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
                    Logger.getLogger(AMixpanelAnalytics.class
                            .getName()).
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
