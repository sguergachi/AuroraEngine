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
package aurora.engine.V1.UI;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

/**
 *
 * A Panel that contains JPanels using the GridLayout and allows for better
 * management of multiple JPanels and the components in each panel.
 *
 * @version 0.1
 * @author Sammy
 */
public class AGridPanel extends JPanel {

    private int col;

    private int row;

    private ArrayList<JComponent> componentList = new ArrayList<JComponent>();

    private int numberOfComponentsAdded;

    static final Logger logger = Logger.getLogger(AGridPanel.class);

    public AGridPanel() {
        this.setLayout(new GridLayout());
        numberOfComponentsAdded = 0;

    }

    public AGridPanel(int row, int col) {
        this.row = row;
        this.col = col;
        numberOfComponentsAdded = 0;

        this.setLayout(new GridLayout(this.row, this.col, 30, 0));

    }

    public AGridPanel(int row, int col, boolean Transparent) {
        this.row = row;
        this.col = col;
        numberOfComponentsAdded = 0;

        this.setOpaque(!Transparent);

        this.setLayout(new GridLayout(this.row, this.col, 30, 0));

    }

    /**
     * Add a component to the GridArray As well as the Panel
     *
     * @param comp an Image to be added to the grid panel
     *
     */
    public void addToGrid(JComponent comp) {
        if (!isGridFull()) {
            if (!componentList.contains(comp)) {
                componentList.add(comp);
                JPanel pane
                               = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,
                                                           0));
                pane.setName("AGrid Panel");
                pane.setOpaque(false);
                pane.add(componentList.get(componentList.indexOf(comp)));
                this.add(pane);
                numberOfComponentsAdded++;
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding to grid...");
                }
                comp.revalidate();
            }
        }
    }

    /**
     * Add a component to the GridArray As well as the Panel
     * Using a specific Index
     * <p/>
     * @param comp  an Image to be added to the grid panel
     * @param index index of where in the grid to add component
     */
    public void addToGrid(JComponent comp, int index) {
        if (!isGridFull()) {
            if (!componentList.contains(comp)) {
                componentList.add(index, comp);
                JPanel pane
                               = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,
                                                           0));
                pane.setOpaque(false);
                pane.add(componentList.get(componentList.indexOf(comp)));
                this.add(pane);
                numberOfComponentsAdded++;
                if (logger.isDebugEnabled()) {
                    logger.debug("Adding to grid... ");
                }
                comp.revalidate();
            }
        }
    }

    /**
     * Updates Whole Panel with all entities inside the GridArray
     *
     */
    public void update() {
        this.removeAll();

        for (int i = 0; i < componentList.size(); i++) {

            JPanel pane = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            pane.setOpaque(false);
            pane.add(componentList.get(componentList.indexOf(componentList
                    .get(i))));
            componentList.get(i).setPreferredSize(new Dimension(componentList
                    .get(i).getPreferredSize().width,
                                                                componentList
                                                                .get(i)
                                                                .getPreferredSize().height));
            pane.add(componentList.get(i));
            this.add(pane);
            componentList.get(i).revalidate();
        }
        this.revalidate();
        this.repaint();

    }

    /**
     * Checks if Grid is full
     *
     * @return
     */
    public boolean isGridFull() {
        return componentList.size() >= (row * col);
    }

    public int getNumberOfComponents() {
        return numberOfComponentsAdded;
    }

    public int getLastIndexOf(Object o) {
        return componentList.lastIndexOf(o);
    }

    public int getLastIndexOf(Class<?> clazz) {

        int index = -1;

        for (int i = 0; i < componentList.size(); i++) {

            Object o = componentList.get(i);
            Class<?> c = o.getClass();

            if (c.getSimpleName().equals(clazz.getSimpleName())) {
                index = i;
            }

        }

        return index;
    }

    public Object getLastComponent() {
        return componentList.get(numberOfComponentsAdded - 1);
    }

    public Object getFirstComponent() {
        return componentList.get(0);
    }

    /**
     * Calculates the column and row of the index passed
     * <p/>
     * @param index index of component in AGridPanel
     * <p/>
     * @return an integer array containing the column and row
     */
    public int[] getColumnAndRow(int index) {

        int[] columnAndRow = {0, 0};
        int elementColumn = index % col;

        if (elementColumn == 0) {
            elementColumn = col;
        }

        double quotient = ((double) 1) / ((double) col);
        double element_divby_cols = ((double) index) / ((double) col);

        int elementRow = (int) (element_divby_cols
                                        + (quotient * (((double) col)
                                                               - ((double) elementColumn))));

        columnAndRow[0] = elementColumn;
        columnAndRow[1] = elementRow;

        return columnAndRow;
    }

    public void removeComp(JComponent comp) {
        componentList.remove(comp);
    }

    /**
     * Finds element in grid array
     *
     * @param comp
     *             <p>
     * @return
     */
    public int find(JComponent comp) {

        return componentList.indexOf(comp);
    }

    /**
     * Get everything contained in this specific GridPanel
     * <p/>
     * @return Component List
     */
    public ArrayList getArray() {
        return componentList;
    }

    public void clear() {
        this.removeAll();
    }

    //Getters And Setters
    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }
}
