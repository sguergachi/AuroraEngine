package aurora.engine.V1.Logic;

import aurora.engine.V1.UI.AImagePane;
import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * This class makes it easy to drag and drop files from the operating
 * system to a Java program. Any <tt>java.awt.Component</tt> can be
 * dropped onto, but only <tt>javax.swing.JComponent</tt>s will indicate
 * the drop event with a changed border.
 * <p/>
 * To use this class, construct a new <tt>AFileDrop</tt> by passing
 * it the target component and a <tt>Listener</tt> to receive notification
 * when file(s) have been dropped. Here is an example:
 * <p/>
 * <code><pre>
 * JPanel myPanel = new JPanel();
 * new AFileDrop( myPanel, new AFileDrop.Listener()
 * {   public void filesDropped( java.io.File[] files )
 * {
 * // handle file drop
 * ...
 * }   // end filesDropped
 * }); // end AFileDrop.Listener
 * </pre></code>
 * <p/>
 * You can specify the border that will appear when files are being dragged by
 * calling the constructor with a <tt>javax.swing.border.Border</tt>. Only
 * <tt>JComponent</tt>s will show any indication with a border.
 * <p/>
 * I'm releasing this code into the Public Domain. Enjoy.
 * </p>
 * <p>
 * <em>Original author: Robert Harder, rharder@usa.net</em></p>
 * <p>
 * 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.</p>
 *
 * @author Robert Harder
 * @author rharder@users.sf.net
 * @version 1.0.1
 */
public class AFileDrop {

    private transient javax.swing.border.Border normalBorder;

    private transient java.awt.dnd.DropTargetListener dropListener;

    /**
     * Discover if the running JVM is modern enough to have drag and drop.
     */
    private static Boolean supportsDnD;

    private AImagePane initalComponent;

    private String dragImageName;
    private String initialImageName;



    /**
     * Full constructor with a specified border and debugging optionally turned on.
     * With Debugging turned on, more status messages will be displayed to
     * <tt>out</tt>. A common way to use this constructor is with
     * <tt>System.out</tt> or <tt>System.err</tt>. A <tt>null</tt> value for
     * the parameter <tt>out</tt> will result in no debugging output.
     *
     * @param c          Component on which files will be dropped.
     * @param dragBorder Border to use on <tt>JComponent</tt> when dragging occurs.
     * @param recursive  Recursively set children as drop targets.
     * @param listener   Listens for <tt>filesDropped</tt>.
     * <p>
     * @since 1.0
     */
    public AFileDrop(
            AImagePane InitialComponent,
            String DragImageName, String RejectDragImageName,
            final boolean recursive,
            final Listener listener) {

        this.initalComponent = InitialComponent;
        this.dragImageName = DragImageName;
        this.initialImageName = InitialComponent.getImageURL();

        if (supportsDnD()) {   // Make a drop listener
            dropListener = new DropTargetListener() {
                @Override
                public void dragEnter(java.awt.dnd.DropTargetDragEvent evt) {

                    // Is this an acceptable drag event?
                    if (isDragOk(evt)) {

                        initalComponent.setImage(dragImageName);

                        // Acknowledge that it's okay to enter
                        //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag(DnDConstants.ACTION_COPY);
                    } // end if: drag ok
                    else {   // Reject the drag event
                        initalComponent.setImage(dragImageName);
                        evt.rejectDrag();
                    }   // end else: drag not ok
                }   // end dragEnter

                public void dragOver(DropTargetDragEvent evt) {   // This is called continually as long as the mouse is
                    // over the drag target.
                }   // end dragOver

                public void drop(DropTargetDropEvent evt) {
                    try {   // Get whatever was dropped
                        java.awt.datatransfer.Transferable tr = evt
                                .getTransferable();

                        // Is it a file list?
                        if (tr.isDataFlavorSupported(
                               DataFlavor.javaFileListFlavor)) {
                            // Say we'll take it.
                            //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                            evt.acceptDrop(DnDConstants.ACTION_COPY);

                            // Get a useful list
                            List fileList = (List) tr
                                    .getTransferData(
                                            DataFlavor.javaFileListFlavor);
                            java.util.Iterator iterator = fileList.iterator();

                            // Convert list to array
                            File[] filesTemp = new File[fileList
                                    .size()];
                            fileList.toArray(filesTemp);
                            final File[] files = filesTemp;

                            // Alert listener to drop.
                            if (listener != null) {
                                listener.filesDropped(files);
                            }

                            // Mark that drop is completed.
                            evt.getDropTargetContext().dropComplete(true);
                        } // end if: file list
                        else // this section will check for a reader flavor.
                        {
                            // Thanks, Nathan!
                            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                            DataFlavor[] flavors = tr.getTransferDataFlavors();
                            boolean handled = false;
                            for (int zz = 0; zz < flavors.length; zz++) {
                                if (flavors[zz].isRepresentationClassReader()) {
                                    // Say we'll take it.
                                    //evt.acceptDrop ( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                                    evt.acceptDrop(
                                            java.awt.dnd.DnDConstants.ACTION_COPY);

                                    Reader reader = flavors[zz]
                                            .getReaderForText(tr);

                                    BufferedReader br = new BufferedReader(
                                            reader);

                                    if (listener != null) {
                                        listener.filesDropped(
                                                createFileArray(br));
                                    }

                                    // Mark that drop is completed.
                                    evt.getDropTargetContext()
                                            .dropComplete(true);
                                    handled = true;
                                    break;
                                }
                            }
                            if (!handled) {
                                evt.rejectDrop();
                            }
                            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
                        }   // end else: not a file list
                    } // end try
                    catch (java.io.IOException io) {
                        evt.rejectDrop();
                    } // end catch IOException
                    catch (java.awt.datatransfer.UnsupportedFlavorException ufe) {
                        evt.rejectDrop();
                    } // end catch: UnsupportedFlavorException
                    finally {
                        initalComponent.setImage(initialImageName);
                    }   // end finally
                }   // end drop

                public void dragExit(java.awt.dnd.DropTargetEvent evt) {


                         initalComponent.setImage(initialImageName);

                }   // end dragExit

                public void dropActionChanged(
                        java.awt.dnd.DropTargetDragEvent evt) {
                    // Is this an acceptable drag event?
                    if (isDragOk(evt)) {   //evt.acceptDrag( java.awt.dnd.DnDConstants.ACTION_COPY_OR_MOVE );
                        evt.acceptDrag(java.awt.dnd.DnDConstants.ACTION_COPY);
                    } // end if: drag ok
                    else {
                        evt.rejectDrag();
                    }   // end else: drag not ok
                     initalComponent.setImage(initialImageName);
                }   // end dropActionChanged
            }; // end DropTargetListener

            // Make the component (and possibly children) drop targets
            makeDropTarget(initalComponent,recursive);
        } // end if: supports dnd
        else {
        }   // end else: does not support DnD
    }   // end constructor

    private static boolean supportsDnD() {   // Static Boolean
        if (supportsDnD == null) {
            boolean support = false;
            try {
                Class arbitraryDndClass = Class.forName(
                        "java.awt.dnd.DnDConstants");
                support = true;
            } // end try
            catch (Exception e) {
                support = false;
            }   // end catch
            supportsDnD = new Boolean(support);
        }   // end if: first time through
        return supportsDnD.booleanValue();
    }   // end supportsDnD

    // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
    private static String ZERO_CHAR_STRING = "" + (char) 0;

    private static File[] createFileArray(BufferedReader bReader) {
        try {
            java.util.List list = new java.util.ArrayList();
            java.lang.String line = null;
            while ((line = bReader.readLine()) != null) {
                try {
                    // kde seems to append a 0 char to the end of the reader
                    if (ZERO_CHAR_STRING.equals(line)) {
                        continue;
                    }

                    java.io.File file = new java.io.File(new java.net.URI(line));
                    list.add(file);
                } catch (Exception ex) {
                }
            }

            return (java.io.File[]) list.toArray(new File[list.size()]);
        } catch (IOException ex) {
        }
        return new File[0];
    }
    // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.

    private void makeDropTarget(final java.awt.Component c, boolean recursive) {
        // Make drop target
        final java.awt.dnd.DropTarget dt = new java.awt.dnd.DropTarget();
        try {
            dt.addDropTargetListener(dropListener);
        } // end try
        catch (java.util.TooManyListenersException e) {
            e.printStackTrace();
        }   // end catch

        // Listen for hierarchy changes and remove the drop target when the parent gets cleared out.
        c.addHierarchyListener(new java.awt.event.HierarchyListener() {
            @Override
            public void hierarchyChanged(java.awt.event.HierarchyEvent evt) {
                java.awt.Component parent = c.getParent();
                if (parent == null) {
                    c.setDropTarget(null);
                } // end if: null parent
                else {
                    new java.awt.dnd.DropTarget(c, dropListener);
                }   // end else: parent not null
            }   // end hierarchyChanged
        }); // end hierarchy listener
        if (c.getParent() != null) {
            new java.awt.dnd.DropTarget(c, dropListener);
        }

        if (recursive && (c instanceof java.awt.Container)) {
            // Get the container
            java.awt.Container cont = (java.awt.Container) c;

            // Get it's components
            java.awt.Component[] comps = cont.getComponents();
            for (Component comp : comps) {
                makeDropTarget(comp, recursive);
            }
        }   // end if: recursively set components as listener
    }   // end dropListener

    /**
     * Determine if the dragged data is a file list.
     */
    private boolean isDragOk(
            final java.awt.dnd.DropTargetDragEvent evt) {
        boolean ok = false;

        // Get data flavors being dragged
        java.awt.datatransfer.DataFlavor[] flavors = evt.getCurrentDataFlavors();

        // See if any of the flavors are a file list
        int i = 0;
        while (!ok && i < flavors.length) {
            // BEGIN 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            // Is the flavor a file list?
            final DataFlavor curFlavor = flavors[i];
            if (curFlavor.equals(
                    java.awt.datatransfer.DataFlavor.javaFileListFlavor)
                || curFlavor.isRepresentationClassReader()) {
                ok = true;
            }
            // END 2007-09-12 Nathan Blomquist -- Linux (KDE/Gnome) support added.
            i++;
        }   // end while: through flavors

        return ok;
    }   // end isDragOk

    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     * This will recursively unregister all components contained within
     * <var>c</var> if <var>c</var> is a {@link java.awt.Container}.
     *
     * @param c The component to unregister as a drop target
     * <p>
     * @since 1.0
     */
    public static boolean remove(java.awt.Component c) {
        return remove(c, true);
    }   // end remove

    /**
     * Removes the drag-and-drop hooks from the component and optionally
     * from the all children. You should call this if you add and remove
     * components after you've set up the drag-and-drop.
     *
     * @param c         The component to unregister
     * @param recursive Recursively unregister components within
     * <p>
     * @return a container
     * <p>
     * @since 1.0
     */
    public static boolean remove(java.awt.Component c,
                                 boolean recursive) {   // Make sure we support dnd.
        if (supportsDnD()) {
            c.setDropTarget(null);
            if (recursive && (c instanceof java.awt.Container)) {
                java.awt.Component[] comps = ((java.awt.Container) c)
                        .getComponents();
                for (Component comp : comps) {
                    remove(comp, recursive);
                }
                return true;
            } // end if: recursive
            else {
                return false;
            }
        } // end if: supports DnD
        else {
            return false;
        }
    }   // end remove

    /* ********  I N N E R   I N T E R F A C E   L I S T E N E R  ******** */
    /**
     * Implement this inner interface to listen for when files are dropped. For example
     * your class declaration may begin like this:
     * <code><pre>
     * public class MyClass implements AFileDrop.Listener
     * ...
     * public void filesDropped( java.io.File[] files )
     * {
     * ...
     * }   // end filesDropped
     * ...
     * </pre></code>
     *
     * @since 1.1
     */
    public static interface Listener {

        /**
         * This method is called when files have been successfully dropped.
         *
         * @param files An array of <tt>File</tt>s that were dropped.
         * <p>
         * @since 1.0
         */
        public abstract void filesDropped(java.io.File[] files);

    }   // end inner-interface Listener

    /* ********  I N N E R   C L A S S  ******** */
    /**
     * This is the event that is passed to the
     * {@link aFileDropListener#filesDropped filesDropped(...)} method in
     * your {@link aFileDropListener} when files are dropped onto
     * a registered drop target.
     *
     * <p>
     * I'm releasing this code into the Public Domain. Enjoy.</p>
     * <p>
     * @author Robert Harder
     * @author rob@iharder.net
     * @version 1.2
     */
    public static class Event extends java.util.EventObject {

        private java.io.File[] files;

        /**
         * Constructs an {@link Event} with the array
         * of files that were dropped and the
         * {@link AFileDrop} that initiated the event.
         *
         * @param files       The array of files that were dropped
         * <p>
         * @s
         * @param sourceource The event source
         * <p>
         * @since 1.1
         */
        public Event(java.io.File[] files, Object source) {
            super(source);
            this.files = files;
        }   // end constructor

        /**
         * Returns an array of files that were dropped on a
         * registered drop target.
         *
         * @return array of files that were dropped
         * <p>
         * @since 1.1
         */
        public java.io.File[] getFiles() {
            return files;
        }   // end getFiles

    }   // end inner class Event

    /* ********  I N N E R   C L A S S  ******** */
    /**
     * At last an easy way to encapsulate your custom objects for dragging and dropping
     * in your Java programs!
     * When you need to create a {@link java.awt.datatransfer.Transferable} object,
     * use this class to wrap your object.
     * For example:
     * <pre><code>
     *      ...
     *      MyCoolClass myObj = new MyCoolClass();
     *      Transferable xfer = new TransferableObject( myObj );
     *      ...
     * </code></pre>
     * Or if you need to know when the data was actually dropped, like when you're
     * moving data out of a list, say, you can use the {@link TransferableObject.Fetcher}
     * inner class to return your object Just in Time.
     * For example:
     * <pre><code>
     *      ...
     *      final MyCoolClass myObj = new MyCoolClass();
     *
     *      TransferableObject.Fetcher fetcher = new TransferableObject.Fetcher()
     *      {   public Object getObject(){ return myObj; }
     *      }; // end fetcher
     *
     *      Transferable xfer = new TransferableObject( fetcher );
     *      ...
     * </code></pre>
     *
     * The {@link java.awt.datatransfer.DataFlavor} associated with
     * {@link TransferableObject} has the representation class
     * <tt>net.iharder.dnd.TransferableObject.class</tt> and MIME type
     * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
     * This data flavor is accessible via the static
     * {@link #DATA_FLAVOR} property.
     *
     *
     * <p>
     * I'm releasing this code into the Public Domain. Enjoy.</p>
     * <p>
     * @author Robert Harder
     * @author rob@iharder.net
     * @version 1.2
     */
    public static class TransferableObject implements
            java.awt.datatransfer.Transferable {

        /**
         * The MIME type for {@link #DATA_FLAVOR} is
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static String MIME_TYPE = "application/x-net.iharder.dnd.TransferableObject";

        /**
         * The default {@link java.awt.datatransfer.DataFlavor} for
         * {@link TransferableObject} has the representation class
         * <tt>net.iharder.dnd.TransferableObject.class</tt>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @since 1.1
         */
        public final static java.awt.datatransfer.DataFlavor DATA_FLAVOR
                                                             = new java.awt.datatransfer.DataFlavor(
                AFileDrop.TransferableObject.class, MIME_TYPE);

        private Fetcher fetcher;

        private Object data;

        private java.awt.datatransfer.DataFlavor customFlavor;

        /**
         * Creates a new {@link TransferableObject} that wraps <var>data</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class
         * determined from <code>data.getClass()</code> and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @param data The data to transfer
         * <p>
         * @since 1.1
         */
        public TransferableObject(Object data) {
            this.data = data;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(data
                    .getClass(), MIME_TYPE);
        }   // end constructor

        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * No custom data flavor is set other than the default
         * {@link #DATA_FLAVOR}.
         *
         * @see Fetcher
         * @param fetcher The {@link Fetcher} that will return the data object
         * <p>
         * @since 1.1
         */
        public TransferableObject(Fetcher fetcher) {
            this.fetcher = fetcher;
        }   // end constructor

        /**
         * Creates a new {@link TransferableObject} that will return the
         * object that is returned by <var>fetcher</var>.
         * Along with the {@link #DATA_FLAVOR} associated with this class,
         * this creates a custom data flavor with a representation class <var>dataClass</var>
         * and the MIME type
         * <tt>application/x-net.iharder.dnd.TransferableObject</tt>.
         *
         * @see Fetcher
         * @param dataClass The {@link java.lang.Class} to use in the custom data flavor
         * @param fetcher   The {@link Fetcher} that will return the data object
         * <p>
         * @since 1.1
         */
        public TransferableObject(Class dataClass, Fetcher fetcher) {
            this.fetcher = fetcher;
            this.customFlavor = new java.awt.datatransfer.DataFlavor(dataClass,
                                                                     MIME_TYPE);
        }   // end constructor

        /**
         * Returns the custom {@link java.awt.datatransfer.DataFlavor} associated
         * with the encapsulated object or <tt>null</tt> if the {@link Fetcher}
         * constructor was used without passing a {@link java.lang.Class}.
         *
         * @return The custom data flavor for the encapsulated object
         * <p>
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor getCustomDataFlavor() {
            return customFlavor;
        }   // end getCustomDataFlavor


        /* ********  T R A N S F E R A B L E   M E T H O D S  ******** */
        /**
         * Returns a two- or three-element array containing first
         * the custom data flavor, if one was created in the constructors,
         * second the default {@link #DATA_FLAVOR} associated with
         * {@link TransferableObject}, and third the
         * {@link java.awt.datatransfer.DataFlavor.stringFlavor}.
         *
         * @return An array of supported data flavors
         * <p>
         * @since 1.1
         */
        public java.awt.datatransfer.DataFlavor[] getTransferDataFlavors() {
            if (customFlavor != null) {
                return new java.awt.datatransfer.DataFlavor[]{customFlavor,
                                                              DATA_FLAVOR,
                                                              java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
            } else {
                return new java.awt.datatransfer.DataFlavor[]{DATA_FLAVOR,
                                                              java.awt.datatransfer.DataFlavor.stringFlavor
                };  // end flavors array
            }
        }   // end getTransferDataFlavors

        /**
         * Returns the data encapsulated in this {@link TransferableObject}.
         * If the {@link Fetcher} constructor was used, then this is when
         * the {@link Fetcher#getObject getObject()} method will be called.
         * If the requested data flavor is not supported, then the
         * {@link Fetcher#getObject getObject()} method will not be called.
         *
         * @param flavor The data flavor for the data to return
         * <p>
         * @return The dropped data
         * <p>
         * @throws java.io.IOException
         * @since 1.1
         */
        @Override
        public Object getTransferData(java.awt.datatransfer.DataFlavor flavor)
                throws java.awt.datatransfer.UnsupportedFlavorException,
                       java.io.IOException {
            // Native object
            if (flavor.equals(DATA_FLAVOR)) {
                return fetcher == null ? data : fetcher.getObject();
            }

            // String
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor)) {
                return fetcher == null ? data.toString() : fetcher.getObject()
                        .toString();
            }

            // We can't do anything else
            throw new java.awt.datatransfer.UnsupportedFlavorException(flavor);
        }   // end getTransferData

        /**
         * Returns <tt>true</tt> if <var>flavor</var> is one of the supported
         * flavors. Flavors are supported using the <code>equals(...)</code> method.
         *
         * @param flavor The data flavor to check
         * <p>
         * @return Whether or not the flavor is supported
         * <p>
         * @since 1.1
         */
        @Override
        public boolean isDataFlavorSupported(
                java.awt.datatransfer.DataFlavor flavor) {
            // Native object
            if (flavor.equals(DATA_FLAVOR)) {
                return true;
            }

            // String
            if (flavor.equals(java.awt.datatransfer.DataFlavor.stringFlavor)) {
                return true;
            }

            // We can't do anything else
            return false;
        }   // end isDataFlavorSupported


        /* ********  I N N E R   I N T E R F A C E   F E T C H E R  ******** */
        /**
         * Instead of passing your data directly to the {@link TransferableObject}
         * constructor, you may want to know exactly when your data was received
         * in case you need to remove it from its source (or do anyting else to it).
         * When the {@link #getTransferData getTransferData(...)} method is called
         * on the {@link TransferableObject}, the {@link Fetcher}'s
         * {@link #getObject getObject()} method will be called.
         *
         * @author Robert Harder
         * @copyright 2001
         * @version 1.1
         * @since 1.1
         */
        public static interface Fetcher {

            /**
             * Return the object being encapsulated in the
             * {@link TransferableObject}.
             *
             * @return The dropped object
             * <p>
             * @since 1.1
             */
            public abstract Object getObject();
        }   // end inner interface Fetcher

    }   // end class TransferableObject

}   // end class AFileDrop
