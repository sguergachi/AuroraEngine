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

/**
 * A class that represents a title
 * @author cmachado
 *
 */
public class aCarouselTitle {
	
	private aImage normalImage;
	private aImage glowImage;
	public enum Title { NORMAL, GLOW }
	
	/**
	 * Creates a title object that stores the 2 states of images for the title
         * To Be Used With aCarousel
	 * @param normalImage
	 * @param glowImage
	 */
	public aCarouselTitle(aImage normalImage, aImage glowImage) {
		this.normalImage = normalImage;
		this.glowImage = glowImage;
	}
	
	public void setNormalImage(aImage normalImage) {
		this.normalImage = normalImage;
	}
	
	public void setGlowImage(aImage glowImage) {
		this.glowImage = glowImage;
	}
	
	public aImage getNormalImage() {
		return normalImage;
	}

	public aImage getGlowImage() {
		return glowImage;
	}
}
