package aurora.engine.V1.Logic;

import aurora.engine.V1.Logic.JSON.JSONObject;
import aurora.engine.V1.Logic.mixpanelapi.ClientDelivery;
import aurora.engine.V1.Logic.mixpanelapi.MessageBuilder;
import aurora.engine.V1.Logic.mixpanelapi.MixpanelAPI;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Queue;
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
        message = builder.event(PROJECT_TOKEN,
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
