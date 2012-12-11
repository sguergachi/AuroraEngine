package aurora.engine.V1.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ScrollText extends JComponent {

    //
    private BufferedImage image;

    // encapsulates the width and height of a component
    private Dimension imageSize;

    // volatile is used to indicate that a variable's value will be modified by different threads
    private volatile int currOffset;

    private Thread internalThread;

    // volatile is used to indicate that a variable's value will be modified by different threads
    private volatile boolean noStopRequested;

    public ScrollText(String text) {
        currOffset = 0;
        buildImage(text);

        setMinimumSize(imageSize);
        setPreferredSize(imageSize);
        setMaximumSize(imageSize);
        setSize(imageSize);

        noStopRequested = true;
        Runnable r = new Runnable() {
            public void run() {
                try {
                    runWork();
                } catch (Exception x) {
                    x.printStackTrace();
                }
            }
        };

        internalThread = new Thread(r, "ScrollText");
        internalThread.start();
    }

    private void buildImage(String text) {

        // The RenderingHints class defines and manages collections of keys and
        // associated values which allow an application to provide input into
        // the choice of algorithms used by other classes which perform rendering
        // and image manipulation services.
        RenderingHints renderHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON); // rendering to be done with anti-aliasing on

        // Maps the specified key to the specified value in this RenderingHints object
        // general hint that provides a high level recommendation as to whether to bias
        // algorithm choices more for speed or quality when evaluating tradeoffs
        renderHints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY); //rendering algorithms are chosen with a preference for output quality

        // create a BufferedImage with width=1, height=1, and imageType
        // BufferedImage.TYPE_INT_RGB which represents an image with
        // 8-bit RGB color components packed into integer pixels
        BufferedImage scratchImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_RGB);

        // Graphics2D class extends the Graphics class to provide more sophisticated control
        // over geometry, coordinate transformations, color management, and text layout.
        // This is the fundamental class for rendering 2-dimensional shapes, text and images
        // on the Java(tm) platform.
        Graphics2D scratchG2 = scratchImage.createGraphics(); // Creates a Graphics2D, which can be used to draw into this BufferedImage

        // Replaces the values of all preferences for the rendering algorithms with the
        // specified hints. The existing values for all rendering hints are discarded
        // and the new set of known hints and values are initialized from the specified Map object
        scratchG2.setRenderingHints(renderHints);

        // use the Serif font and set it to be bold and in italics with size 24
        Font font = new Font("AgencyFB", Font.BOLD, 20);

        // The FontRenderContext class is a container for the information needed to correctly
        // measure text. The measurement of text can vary because of rules that map outlines
        // to pixels, and rendering hints provided by an application.

        // Get the rendering context of the Font within this Graphics2D context
        FontRenderContext frc = scratchG2.getFontRenderContext();

        /* TextLayout is an immutable graphical representation of styled character data.

         It provides the following capabilities:

         �implicit bidirectional analysis and reordering,
         �cursor positioning and movement, including split cursors for mixed directional text,
         �highlighting, including both logical and visual highlighting for mixed directional text,
         �multiple baselines (roman, hanging, and centered),
         �hit testing,
         �justification,
         �default font substitution,
         �metric information such as ascent, descent, and advance, and
         �rendering
         */
        TextLayout tl = new TextLayout(text, font, frc);

        // The Rectangle2D class describes a rectangle defined by a location (x,y) and dimension (w x h).

        // Returns the bounds of this TextLayout. The bounds are in standard coordinates
        Rectangle2D textBounds = tl.getBounds();

        // get text width
        int textWidth = (int) Math.ceil(textBounds.getWidth());
        // get text height
        int textHeight = (int) Math.ceil(textBounds.getHeight());
        System.out.println("TEXT WIDTH: " + textWidth);

        // padding values
        int horizontalPad = 10;
        int verticalPad = 28;

        // set the size of our image based on the size of the text and padding values
        imageSize = new Dimension(textWidth + horizontalPad, textHeight
                                                             + verticalPad);
//    imageSize = new Dimension(1140 + horizontalPad, 52
//            + verticalPad);

        System.out.println("image width: " + imageSize.width);
        System.out.println("image height: " + imageSize.height);

        // create the BufferedImage
        image = new BufferedImage(imageSize.width, imageSize.height,
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHints(renderHints);

        int baselineOffset = (verticalPad / 2) - ((int) textBounds.getY());

        //  g2.setColor(Color.DARK_GRAY);
        //  g2.fillRect(0, 0, imageSize.width, imageSize.height);

        g2.setColor(Color.WHITE);
        tl.draw(g2, 0, baselineOffset);

        // Free-up resources right away, but keep "image" for
        // animation.
        scratchG2.dispose();
        scratchImage.flush();
        g2.dispose();
    }

    public void paint(Graphics g) {
        // Make sure to clip the edges, regardless of curr size
        // g.setClip(0, 0, imageSize.width, imageSize.height);
        g.setClip(0, 0, 1168, imageSize.height);

        int localOffset = currOffset; // in case it changes
        g.drawImage(image, -localOffset, 0, this);
        g.drawImage(image, imageSize.width - localOffset, 0, this);

        // draw outline
        g.setColor(Color.black);
        //  g.drawRect(0, 0, imageSize.width - 1, imageSize.height - 1);
        g.drawRect(0, 0, 1168, imageSize.height - 1);

    }

    private void runWork() {
        while (noStopRequested) {
            try {
                Thread.sleep(20); // 10 frames per second

                // adjust the scroll position
                currOffset = (currOffset + 1) % imageSize.width;

                // signal the event thread to call paint()
                repaint();
            } catch (InterruptedException x) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void stopRequest() {
        noStopRequested = false;
        internalThread.interrupt();
    }

    public boolean isAlive() {
        return internalThread.isAlive();
    }
    /*  public static void main(String[] args) {
     ScrollText st = new ScrollText("Java can do animation! Java can do anything! xxxxxxxxxxxxxx");
     st.setForeground(Color.BLACK);
     st.setBackground(Color.BLACK);

     JPanel p = new JPanel(new FlowLayout());
     p.add(st);

     JFrame f = new JFrame("ScrollText Demo");
     f.setContentPane(p);
     f.setSize(1440, 100);
     f.setVisible(true);
     }*/
}