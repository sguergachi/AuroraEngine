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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Store is an Object that contains data from a database
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public abstract class aStore {

    public aSimpleDB db;
    protected ArrayList database;

    public abstract void storeFromDatabase();

    public abstract void storeToDatabase();

    public abstract void setUpDatabase(Boolean FirstTime, String Path);

    protected ArrayList getDatabaseArray(String TableName, String ColumnName) {
        database = new ArrayList();
        try {

            ResultSet rs = db.flexQuery("SELECT " + ColumnName + " FROM " + TableName);



            //Check if db is still empty
           rs.beforeFirst();
            if (rs.next()) {
                rs.first();
                do {
                    database.add(rs.getObject(ColumnName));
                } while (rs.next());
            }


            return database;

        } catch (SQLException ex) {
            Logger.getLogger(aStore.class.getName()).log(Level.SEVERE, null, ex);
            return database;
        } finally {
            db.CloseConnection();
        }

    }
}
