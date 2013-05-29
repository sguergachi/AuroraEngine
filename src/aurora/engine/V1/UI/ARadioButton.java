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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * 
 * @author Sammy
 */
public class aRadioButton extends aImagePane {

	private String btnNormal;
	private String btnOver;
	public boolean isSelected = false;
	private JComponent comp;
	private String SurfaceName;
	private ActionListener onSelectHander;
	private ActionListener onUnSelectHander;
	private int Width = 0;
	private int Height = 0;
	private Boolean isEnabled = true;

	public aRadioButton(String ImgUp, String ImgOvr) {

		btnNormal = ImgUp; // Normal State of Radio button
		btnOver = ImgOvr; // Selected state of radio button

		setImage(btnNormal); // Set initial image as normal
		// Set size and add mouse handler
		setPreferredSize(new Dimension(getImageWidth(), getImageHeight()));
		addMouseListener(new Click());
		this.setOpaque(false);

	}

	public aRadioButton(String ImgUp, String ImgOvr, String SurfaceName) {
		this.SurfaceName = SurfaceName;
		btnNormal = ImgUp; // Normal State of Radio button
		btnOver = ImgOvr; // Selected state of radio button

		this.setSurfaceName(SurfaceName);
		this.setImage(btnNormal); // Set initial image as normal
		// Set size and add mouse handler
		setPreferredSize(new Dimension(getImageWidth(), getImageHeight()));
		addMouseListener(new Click());
		this.setOpaque(false);

	}

	public void setSelectedHandler(ActionListener action) {
		onSelectHander = action;
	}

	public void setUnSelectedHandler(ActionListener action) {
		onUnSelectHander = action;
	}

	public void setSelected() {
		isSelected = true;
		setImage(btnOver);

		if (Width != 0 || Height != 0) {
			this.setImageSize(Width, Height);
		}

		repaint();
		revalidate();

		// try {
		// Thread.sleep(20);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		this.requestFocus();
		if (onSelectHander != null) {

			onSelectHander.actionPerformed(new ActionEvent(this, 0, ""));
		}

		System.out.println("Checkbox SELECTED " + isSelected);

	}

	public void setUnSelected() {
		isSelected = false;
		setImage(btnNormal);
		if (Width != 0 || Height != 0) {
			this.setImageSize(Width, Height);
		}
		repaint();
		this.requestFocus();
		if (onUnSelectHander != null) {
			onUnSelectHander.actionPerformed(new ActionEvent(this, 0, ""));
		}

		System.out.println("Checkbox SELECTED " + isSelected);

	}

	public void setButtonSize(int width, int height) {
		this.Width = width;
		this.Height = height;

		this.setImageSize(width, height);
		this.setPreferredSize(new Dimension(getImageWidth(), getImageHeight()));

	}
	

	public class Click implements MouseListener {

		public void mouseClicked(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if (isEnabled) {
				if (isSelected)
					setUnSelected();
				else if (!isSelected)
					setSelected();
			}
		}

		public void mouseReleased(MouseEvent e) {

		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	public boolean isSelected() {
		return isSelected;
	}
}
