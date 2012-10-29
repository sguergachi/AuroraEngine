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

import aurora.engine.V1.UI.*;

/**
 * A class that represents a title
 * <p/>
 * @author cmachado
 *
 */
public class ACarouselTitle {


    public enum TitleType {
        NORMAL, GLOW
    }
    private AImage normalImage;

    private AImage glowImage;

    /**
     * Creates a title object that stores the 2 states of images for the title
     * To Be Used With aCarousel
     * <p/>
     * @param normalImage
     * @param glowImage
     */
    public ACarouselTitle(AImage normalImage, AImage glowImage) {
        this.normalImage = normalImage;
        this.glowImage = glowImage;
    }

    public void setNormalImage(AImage normalImage) {
        this.normalImage = normalImage;
    }

    public void setGlowImage(AImage glowImage) {
        this.glowImage = glowImage;
    }

    public AImage getNormalImage() {
        return normalImage;
    }

    public AImage getGlowImage() {
        return glowImage;
    }
}
