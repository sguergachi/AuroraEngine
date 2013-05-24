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

package aurora.engine.V1.Logic;

import org.apache.log4j.Logger;

/**
 * Contains sorting algorithms used on arrays
 * @author Sammy
 */
public class ASort {
	
	static final Logger logger = Logger.getLogger(ASort.class);

    public void Sort_Logic() {
    }
/**
 * Number sorting smallest to biggest
 * @param int Array
 * @return sorted int array
 **/
    public int[] smallestToBigest(int[] array) {
        int temp;
        for (int i = 1; i < array.length; i++) {
            for (int a = 0; a <  array.length - i ; a++) {
                    if (array[a] > array[a + 1]) {
                        temp = array[a];
                        array[a] = array [a+1];
                        array[a + 1] = temp;
                  }
            }
        }
        return array;
    }
/**
 * Number sorting biggest to smallest
 * @param int Array
 * @return sorted int array
 **/
        public int[] biggestToSmallest(int[] array) {
        int temp;
        for (int i = 1; i < array.length; i++) {
            for (int a = 0; a < array.length - i; a++) {
                    if (array[a] < array[a + 1]) {
                        temp = array[a];
                        array[a] = array [a+1];
                        array[a + 1] = temp;
                    }
                }
            }
        return array;
    }
/**
 * Alphabetic sorting A to Z
 * @param String Array
 * @return sorted String array
 **/
        public String[] firstToLast (String[] array){
        String temp;
        for (int a=1; a < array.length; a++){
            for(int i = 0; i < array.length - a; i++){
                if(array[i].charAt(0) > array[i + 1].charAt(0)){
                    temp = array[i];
                    array[i] = array [i+1];
                    array[i + 1] = temp;
                }

            }
        }
            return array;
        }

 /**
 * Alphabetic sorting Z to A
 * @param String Array
 * @return sorted String array
 **/
        public String[] lastToFirst (String[] array){
        String temp;
        for (int a=1; a < array.length; a++){
            for(int i = 0; i < array.length - a; i++){
                if(array[i].charAt(0) < array[i + 1].charAt(0)){
                    temp = array[i];
                    array[i] = array [i+1];
                    array[i + 1] = temp;
                }

            }
        }
            return array;
        }


 /**
 * NumberSorting smallest to biggest
 * @param int Array
 * @return sorted int array
 **/

        public void smallToBigSort(int[] array)
   {
      int out, in;
      for(out=array.length-1; out>1; out--) // outer loop (backward)
      for(in=0; in<out; in++) // inner loop (forward)
      if( array[in] > array[in+1] ) // out of order?
      swap(in, in+1, array); // swap them
   }


 /**
 * NumberSorting biggest to smallest
 * @param int Array
 * @return sorted int array
 **/

      public void bigToSmallSort(int[] array)
   {
      int out, in;
      for(out=array.length-1; out>1; out--) // outer loop (backward)
      for(in=0; in<out; in++) // inner loop (forward)
      if( array[in] < array[in+1] ) // out of order?
      swap(in, in+1, array); // swap them
   }
   private void swap(int one, int two, int[] array)
   {
      int temp = array[one];
      array[one] = array[two];
      array[two] = temp;
   }
}
