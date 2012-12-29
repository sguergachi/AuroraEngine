package aurora.engine.V1.UI;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.event.*;
import aurora.engine.V1.Logic.ASurface;

/**
 * The MarqueePanel is used to scroll components from the right edge of the
 * panel to the left edge. Scrolling is continuous. To simulate the scrolling of
 * text you can simply add a JLabel to the panel.
 * <p/>
 * Various properties control the scrolling of the components on the panel.
 * Changes to the properties are dynamic and will take effect the next time the
 * components are scrolled.
 */
public class MarqueePanel extends JPanel implements ActionListener,
        AncestorListener, WindowListener, MouseListener, MouseMotionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    protected boolean paintChildren;

    protected boolean scrollingPaused;

    protected int scrollOffset;

    protected int wrapOffset;

    private int preferredWidth = -1;

    private int scrollAmount;

    private int scrollFrequency;

    private boolean wrap = false;

    private int wrapAmount = 50;

    private boolean scrollWhenFocused = true;

    private Timer timer = new Timer(1000, this);

    private ASurface ressource;

    String imageFile = "aurora/V1/resources/dash_infoBar_bg.png";

    private ImageIcon image;

    private int imageHeight = 0;

    private int imageWidth = 0;

    public static ArrayList<AInfoFeedLabel> infoFeedLabelList;

    private ToolTipManager ttm;

    private Graphics g;

    /**
     * Convenience constructor that sets both the scroll frequency and scroll
     * amount to a value of 5.
     */
    public MarqueePanel() {
        this(5, 5);
    }

    /**
     * Create an AnimatedIcon that will continuously cycle with the default
     * (500ms).
     * <p/>
     * @param component
     *                  the component the icon will be painted on
     * @param icons
     *                  the Icons to be painted as part of the animation
     */
    public MarqueePanel(int scrollFrequency, int scrollAmount) {
        ressource = new ASurface("");
        setScrollFrequency(scrollFrequency);
        setScrollAmount(scrollAmount);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        addAncestorListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(50);
        ttm.setReshowDelay(100);
        ttm.setDismissDelay(1000);

    }

    /*
     * Translate the location of the children before they are painted so it
     * appears they are scrolling left to right
     */
    @Override
    public void paintChildren(Graphics g) {
        // Need this so we don't see a flicker of the text before scrolling

        if (!paintChildren) {
            return;
        }

        // Normal painting as the components scroll right to left
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(-scrollOffset, 0);
        super.paintChildren(g);
        g2d.translate(scrollOffset, 0);

        // Repaint the start of the components on the right edge of the panel
        // once
        // all the components are completely visible on the panel.
        // (Its like the components are in two places at the same time)

        if (isWrap()) {
            wrapOffset = scrollOffset - super.getPreferredSize().width
                         - wrapAmount;
            g2d.translate(-wrapOffset, 0);
            super.paintChildren(g);
            g2d.translate(wrapOffset, 0);
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        //  Dispaly  image on Panel\


        try {
            image = new ImageIcon(new URL(
                    ressource.getSurfacePath()
                    + "/aurora/V1/resources/dash_infoBar_bg.png"));
        } catch (MalformedURLException ex) {
            try {
                image = new ImageIcon(getClass()
                        .getResource(
                        "/aurora/V1/resources/dash_infoBar_bg.png"));
            } catch (Exception exx) {
                Logger.getLogger(AImagePane.class.getName()).log(
                        Level.SEVERE,
                        null, exx);
            }
        }

        if (image != null) {
            if (imageWidth == 0 && imageHeight == 0) {
                imageWidth = image.getIconWidth();
                imageHeight = image.getIconHeight();
            }
            g.drawImage(image.getImage(), 0, 0, imageWidth, imageHeight,
                    this);
        } else {
            g.clearRect(0, 0, imageWidth, imageHeight);

        }


    }

    /*
     * The default preferred size will be half the size of the components added
     * to the panel. This will allow room for components to be scrolled on and
     * off the panel.
     *
     * The default width can be overriden by using the setPreferredWidth()
     * method.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();

        d.width = (preferredWidth == -1) ? d.width / 2 : preferredWidth;

        return d;
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    /**
     * Specify the preferred width on the panel. A value of -1 will cause the
     * default preferred with size calculation to be used.
     * <p/>
     * @param preferredWidth
     *                       preferred width of the panel in pixels
     */
    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
        revalidate();
    }

    /**
     * Get the scroll amount.
     * <p/>
     * @return the scroll amount in pixels
     */
    public int getScrollAmount() {
        return scrollAmount;
    }

    /**
     * Specify the scroll amount. The number of pixels to scroll every time
     * scrolling is done.
     * <p/>
     * @param scrollAmount
     *                     scroll amount in pixels
     */
    public void setScrollAmount(int scrollAmount) {
        this.scrollAmount = scrollAmount;
    }

    /**
     * Get the scroll frequency.
     * <p/>
     * @return the scroll frequency
     */
    public int getScrollFrequency() {
        return scrollFrequency;
    }

    /**
     * Specify the scroll frequency. That is the number of times scrolling
     * should be performed every second.
     * <p/>
     * @param scrollFrequency
     *                        scroll frequency
     */
    public void setScrollFrequency(int scrollFrequency) {
        this.scrollFrequency = scrollFrequency;

        int delay = 1000 / scrollFrequency;
        timer.setInitialDelay(delay);
        timer.setDelay(delay);
    }

    /**
     * Get the scroll only when visible property.
     * <p/>
     * @return the scroll only when visible value
     */
    public boolean isScrollWhenFocused() {
        return scrollWhenFocused;
    }

    /**
     * Specify the scrolling property for unfocused windows.
     * <p/>
     * @param scrollWhenVisible
     *                          when true scrolling pauses when the window loses focus.
     *                          Scrolling will continue when the window regains focus. When
     *                          false scrolling is continuous unless the window is iconified.
     */
    public void setScrollWhenFocused(boolean scrollWhenFocused) {
        this.scrollWhenFocused = scrollWhenFocused;
    }

    /**
     * Get the wrap property.
     * <p/>
     * @return the wrap value
     */
    public boolean isWrap() {
        return wrap;
    }

    /**
     * Specify the wrapping property. Normal scrolling is such that all the text
     * will scroll from left to right. When the last part of the text scrolls
     * off the left edge scrolling will start again from the right edge.
     * Therefore there is a time when the component is blank as nothing is
     * displayed. Wrapping implies that as the end of the text scrolls off the
     * left edge the beginning of the text will scroll in from the right edge.
     * So the end and the start of the text is displayed at the same time.
     * <p/>
     * @param wrap
     *             when true the start of the text will scroll in from the right
     *             edge while the end of the text is still scrolling off the left
     *             edge. Otherwise the panel must be clear of text before will
     *             begin again from the right edge.
     */
    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    /**
     * Get the wrap amount.
     * <p/>
     * @return the wrap amount value
     */
    public int getWrapAmount() {
        return wrapAmount;
    }

    /**
     * Specify the wrapping amount. This specifies the space between the end of
     * the text on the left edge and the start of the text from the right edge
     * when wrapping is turned on.
     * <p/>
     * @param wrapAmount
     *                   the amount in pixels
     */
    public void setWrapAmount(int wrapAmount) {
        this.wrapAmount = wrapAmount;
    }

    /**
     * Start scrolling the components on the panel. Components will start
     * scrolling from the right edge towards the left edge.
     */
    public void startScrolling() {
        paintChildren = true;
        scrollOffset = -getSize().width;

        timer.start();
    }

    /**
     * Stop scrolling the components on the panel. The conponents will be
     * cleared from the view of the panel
     */
    public void stopScrolling() {
        timer.stop();
        paintChildren = false;
        repaint();
    }

    /**
     * The components will stop scrolling but will remain visible
     */
    public void pauseScrolling() {
        if (timer.isRunning()) {
            timer.stop();
            scrollingPaused = true;
        }
    }

    /**
     * The components will resume scrolling from where scrolling was stopped.
     */
    public void resumeScrolling() {
        if (scrollingPaused) {
            timer.restart();
            scrollingPaused = false;
        }
    }

    // Implement ActionListener
    /**
     * Adjust the offset of the components on the panel so it appears that they
     * are scrolling from right to left.
     */
    public void actionPerformed(ActionEvent ae) {
        scrollOffset = scrollOffset + scrollAmount;
        int width = super.getPreferredSize().width;

        if (scrollOffset > width) {
            scrollOffset = isWrap() ? wrapOffset + scrollAmount :
                    -getSize().width;
        }

        repaint();
    }

    // Implement AncestorListener
    /**
     * Get notified when the panel is added to a Window so we can use a
     * WindowListener to automatically start the scrolling of the components.
     */
    public void ancestorAdded(AncestorEvent e) {
        SwingUtilities.windowForComponent(this).addWindowListener(this);
    }

    public void ancestorMoved(AncestorEvent e) {
    }

    public void ancestorRemoved(AncestorEvent e) {
    }

    // Implement WindowListener
    public void windowActivated(WindowEvent e) {
        if (isScrollWhenFocused()) {
            resumeScrolling();
        }
    }

    public void windowClosed(WindowEvent e) {
        stopScrolling();
    }

    public void windowClosing(WindowEvent e) {
        stopScrolling();
    }

    public void windowDeactivated(WindowEvent e) {
        if (isScrollWhenFocused()) {
            pauseScrolling();
        }
    }

    public void windowDeiconified(WindowEvent e) {
        resumeScrolling();
    }

    public void windowIconified(WindowEvent e) {
        pauseScrolling();
    }

    public void windowOpened(WindowEvent e) {
        // startScrolling();
    }

    @Override
    public JToolTip createToolTip() {
        JToolTip tip = super.createToolTip();
        tip.setBackground(new Color(87, 140, 204));
        tip.setForeground(Color.BLACK);
        return tip;
    }

    // implement MouseListener
    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
        setScrollFrequency(25);
    }

    public void mouseExited(MouseEvent arg0) {

        setScrollFrequency(55);

        Component[] c = this.getComponents();

        for (int i = 0; i < c.length; i++) {
            if (c[i] instanceof AInfoFeedLabel) {
                AInfoFeedLabel label = (AInfoFeedLabel) c[i];
                label.setForeground(Color.WHITE);
            }
        }
        this.setToolTipText("");
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {

        int x = arg0.getX();
        int offset = scrollOffset;
        int labelClicked = x + offset;

        // get the component at the point the mouse is at
        Component[] components = this.getComponents();

        if (labelClicked >= 0) {

            int i = 0;
            boolean componentFound = false;

            while (!componentFound && i < components.length) {

                Component c = components[i];

                if (c instanceof AInfoFeedLabel) {
                    AInfoFeedLabel label = (AInfoFeedLabel) c;

                    int xPos = label.getX();
                    int width = label.getWidth();

                    if (labelClicked >= xPos && labelClicked <= (xPos + width)) {
                        System.out.println("URL = "
                                           + label.getUrl());
                        Desktop myNewBrowserDesktop = Desktop.getDesktop();
                        URI myNewLocation;
                        try {
                            myNewLocation = new URI(label.getUrl());
                            myNewBrowserDesktop.browse(myNewLocation);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        componentFound = true;
                    }
                }
                i++;
            }
        }


    }

    @Override
    public void mouseDragged(MouseEvent arg0) {
    }

    @Override
    public Point getToolTipLocation(MouseEvent e) {
        if (this.getMousePosition() != null) {
            return new Point(this.getMousePosition().x - this.getToolTipText()
                    .length(), -10);
        }
        return null;

    }

    @Override
    public void mouseMoved(MouseEvent arg0) {

        int x = arg0.getX();
        int offset = scrollOffset;
        int labelClicked = x + offset;

        // get the component at the point the mouse is at
        Component[] components = this.getComponents();

        Component lastComponent = components[components.length - 1];

        if (labelClicked >= 0) {

            int i = 0;
            boolean componentFound = false;

            while (!componentFound && i < components.length) {

                Component c = components[i];

                if (c instanceof AInfoFeedLabel) {
                    AInfoFeedLabel label = (AInfoFeedLabel) c;

                    int xPos = label.getX();
                    int width = label.getWidth();

                    if (labelClicked >= xPos && labelClicked <= (xPos + width)) {
                        label.setForeground(Color.GREEN);
                        this.setToolTipText("Source: " + label.getSourceName());
                        componentFound = true;
                    } else {
                        label.setForeground(Color.WHITE);
                    }
                    // if the AInfoFeedLabel is not hovered over then set the
                    // tool tip text to null
                } else {
                    this.setToolTipText("");
                }
                i++;
            }
        }
    }
}
