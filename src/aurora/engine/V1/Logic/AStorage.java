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

import org.apache.log4j.Logger;

/**
 * A Store is an Object that contains data from a database
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public abstract class AStorage {

    public ASimpleDB db;

    protected ArrayList database;

    public abstract void storeFromDatabase();

    public abstract void storeToDatabase();

    public abstract void setUpDatabase(Boolean FirstTime, String Path);
    static final Logger logger = Logger.getLogger(AStorage.class);

    protected ArrayList getDatabaseArray(String TableName, String ColumnName) throws SQLException {
        database = new ArrayList<Object>();

        ResultSet rs = db.flexQuery("SELECT " + ColumnName + " FROM "
                + TableName);

        if (logger.isDebugEnabled()) {
            logger.debug("RS " + rs);
        }

            //Check if db is still empty
        if (rs != null) {
            rs.beforeFirst();

            while (rs.next()) {
                database.add(rs.getObject(ColumnName));
            }

//            db.CloseConnection();
            return database;
        } else {
//            db.CloseConnection();
            return null;
        }

    }
}
