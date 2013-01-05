package aurora.engine.V1.UI;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;
import javax.swing.JToolTip;

/**
 * The AInfoFeedLabel is used to scroll components from the right edge of the
 * panel to the left edge. Scrolling is continuous. To simulate the scrolling of
 * text you can simply add a JLabel to the panel.
 * <p/>
 * Various properties control the scrolling of the components on the panel.
 * Changes to the properties are dynamic and will take effect the next time the
 * components are scrolled.
 */
public class AInfoFeedLabel extends JLabel {

    /*
     * The URL associated with this label text
     */
    private String url;

    private String sourceName;

    private int startPosition;

    /*
     * The position along the x-axis where this JLabel ends
     */
    private int endPosition;

    /**
     *
     */
    /*
     * public AInfoFeedLabel() {
     *
     * super(null); // this.url = new URL(null); }
     */

    /*
     * public AInfoFeedLabel(String title) {
     *
     * super(title); // this.url = new URL(null); }
     */
    /**
     *
     */
    public AInfoFeedLabel(String title, String url) {

        super(title);
        this.url = url;

    }

    public void setUrl(URL url) {
        // this.url = url;
    }

    public String getUrl() {

        return url;
    }

    public void setStartPosition(int position) {

        this.startPosition = position;
    }

    public int getStartPosition() {

        return startPosition;
    }

    public void setEndPosition(int position) {

        this.endPosition = position;

    }

    public int getEndPosition() {

        return endPosition;

    }

    public void setSourceName(String name) {
        this.sourceName = name;
    }

    public String getSourceName() {
        return sourceName;
    }

    @Override
    public String toString() {

        return "Title:= " + this.getText(); // + " URL:= " + url.toString();
    }

  
}