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

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Simple Interface to the H2 Database That performs simple Operations to
 * Manage a Single Database
 *
 * Uses PreparedStatements For Better Security
 *
 * @version 0.1
 * @author Sammy
 */
public class aSimpleDB {

    public final String DatabaseName;
    private Connection connection;
    //
    public static String TYPE_STRING = "VARCHAR(255)";
    public static String TYPE_STRING_IGNORECASE = "VARCHAR_IGNORECASE(255)";
    public static String TYPE_INTEGER = "INT";
    public static String TYPE_BOOLEAN = "BOOLEAN";
    public static String TYPE_DOUBLE = "DOUBLE";
    //
    public static String NO_NULLS = "SET NO NULL";
    public static String UNIQUE = "UNIQUE";
    public static String PRIMARY_KEY = "PRYMARY KEY";
    public static String FOREIGN_KEY = "FOREIGN KEY";
    //
    private String databasePath = System.getProperty("user.dir") + "\\lib\\";
    private String databasePath_DEV = System.getProperty("user.dir") + "//";
    //

    /**
     * Initiate a Database By Providing a Database to connect to Allows for
     * autoPreparation of the First Table
     *
     * @param autoPrepDatabase : Creates an Initial Table With a PrimaryKey
     * Column called ID
     * @param DatabaseName : Name of the Database to connect to NOTE: IF
     * autoPrep Is On It Will Creates a new Database if it does not already
     * exist
     * @param InitialTableName : You must create at least one table
     */
    public aSimpleDB(String DatabaseName, String InitialTableName, Boolean autoPrepDatabase) throws SQLException {
        this.DatabaseName = DatabaseName;

        //Create the Databse with tables
        if (autoPrepDatabase) {
            prepDatabase(InitialTableName, "ID");
        }
    }

    public aSimpleDB(String DatabaseName, String InitialTableName, Boolean autoPrepDatabase, String Path) throws SQLException {
        this.DatabaseName = DatabaseName;
        this.databasePath = Path;
        this.databasePath_DEV = Path;
        //Create the Databse with tables
        if (autoPrepDatabase) {
            prepDatabase(InitialTableName, "ID");
        }
    }
    
    
    public aSimpleDB(String DatabaseName, String Path) throws SQLException {
        this.DatabaseName = DatabaseName;
        this.databasePath = Path;
        this.databasePath_DEV = Path;
        
      
    }

    //
    //
    /////////////////////////////////////////////////CHANGE TEH DATABASE
    //
    //
    /**
     * A nice way to prep a table
     *
     * This method CREATES a Table It also ADDS a COLUMN Under The
     *
     * @param InitialTableName
     * @param ColumnID
     * @throws SQLException
     */
    private void prepDatabase(String InitialTableName, String ColumnID)
            throws SQLException {
        //Connect and create new Database
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName);
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName);
            }

            System.out.println(databasePath + DatabaseName);

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName);
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName);
            }

        }

        try {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + InitialTableName);
            statement.execute();
            addColumn(InitialTableName, ColumnID, TYPE_INTEGER);

        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }
    }

    /**
     * A Simple way to create an empty Table
     *
     * @param TableName : name of table you want to add to the database
     * @param autoPrep : creates a Table with a "ID" PrimaryKey Column already
     * set
     * @throws SQLException
     */
    public void addTable(String TableName, Boolean autoPrep)
            throws SQLException {

        if (autoPrep) {
            prepDatabase(TableName, "ID");
        } else {

            try {
                String osName = System.getProperty("os.name");
                if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                    connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName );
                } else if (osName.equals("Mac OS X")) {
                    connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName );
                }

                System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

            } catch (Exception exx) {
                String osName = System.getProperty("os.name");
                if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                    connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName );
                } else if (osName.equals("Mac OS X")) {
                    connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName );
                }

            }
            try {

                PreparedStatement statement = connection.prepareStatement("CREATE TABLE " + TableName);
                statement.execute();


            } catch (SQLException ex) {

                connection.rollback();
                System.err.println(ex);

            } finally {
                connection.close();
                System.out.println("Closed Connection");
            }

        }
    }

    /**
     * Allows for the ability to create a table As well as the Ability to add
     * some Initial Columns
     *
     * @param TableName : Table you want to create
     * @param ColumnCSV: Use this Format ["ColumnName DataType Constraint, ..."]
     * @throws SQLException
     */
    //TODO Test This
    public void addTableFlex(String TableName, String ColumnCSV)
            throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName );
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName );
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName );
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName );
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement("CREATE TABLE" + TableName + "(" + ColumnCSV + ")");
            statement.execute();


        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * Adds a Column to the given Table
     *
     * @param TableName : Table you want to access
     * @param ColumnName : the name of the Column you want to add, CANT BE NULL
     * @param ColumnType : the dataType of the Column, MUST BE SET (NOTE: Use
     * aSimpleDB.typeXYZ to get the Basic Types)
     *
     * @throws SQLException
     */
    public void addColumn(String TableName, String ColumnName, String ColumnType) throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement("ALTER TABLE " + TableName + " ADD " + ColumnName + " " + ColumnType);
            statement.execute();


        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * addRow simple way to add a row of data This method requires that All
     * Values for each Column be put in Even the NULL values
     *
     * @param TableName: name of Table we are dealing with
     * @param CSV: A String with each values SPERATED BY A COMMA ex. "0,Hi,Big"
     * ^ means there are 3 columns we are filling
     * @throws SQLException
     */
    public void addRow(String TableName, String CSV) throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TableName + " VALUES(" + CSV + ")");
            statement.execute();


        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * addRow adds values to the specific columns order of CSV and Order of
     * Column Names must be the same
     *
     * @param TableName: name of Table we are dealing with
     * @param ColumnNames: an array containing all the Columns we are dealing
     * with
     * @param CSV: A String with each values SPERATED BY A COMMA Ex. "0,Hi,Big"
     * means there are 3 columns we are filling
     *
     * @throws SQLException
     */
    //TODO: Need To Test This Method
    public void addRowFlex(String TableName, String[] ColumnNames, String CSV) throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(databasePath + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        StringBuilder ColumnCSV = new StringBuilder();

        for (String column : ColumnNames) {
            if (ColumnNames[ColumnNames.length - 1].equals(column)) {
                ColumnCSV.append(column);
            } else {
                ColumnCSV.append(column).append(",");
            }
        }

        try {
            System.out.println("INSERT INTO " + TableName + " (" + ColumnCSV.toString() + ") VALUES(" + CSV + ")");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO " + TableName + " (" + ColumnCSV.toString() + ") VALUES(" + CSV + ")");
            statement.execute();


        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * Allows ability to set most Constraints On Columns EXCEPT ForeignKey
     *
     * For ForeignKey please refer to: setForeignKey()
     *
     * @param TableName
     * @param ColumnName
     * @param constraintType : use aSimpleDB.constXYZ to get basic Types
     * @throws SQLException
     */
    public void setConstraint(String TableName, String ColumnName, String constraintType)
            throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {
            if (!constraintType.equals(NO_NULLS) || constraintType.equals(FOREIGN_KEY)) {
                PreparedStatement statement = connection.prepareStatement("ALTER TABLE " + TableName + " ADD " + constraintType + " (" + ColumnName + ")");
                statement.execute();
            } else if (constraintType.equals(NO_NULLS)) {
                PreparedStatement statement = connection.prepareStatement("ALTER TABLE " + TableName + " ALTER " + ColumnName + " " + constraintType);
                statement.execute();
            } else if (constraintType.equals(FOREIGN_KEY)) {
            }



        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * Allows setting a Column form one table to be the Foreign Key in reference
     * to the Primary Key of a Column in another Table
     *
     * @param TableName1
     * @param TableName2
     * @param ColumnName1
     * @param ColumnName2
     * @throws SQLException
     */
    //TODO Test this
    public void setForeginKey(String TableName1, String TableName2, String ColumnName1, String ColumnName2)
            throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement("ALTER TABLE " + TableName1 + " ADD FOREIGN KEY (" + ColumnName1 + ") "
                    + "REFERENCES  " + TableName2 + " (" + ColumnName2 + ")");
            statement.execute();

        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * Updates a Value in a specific column at a unique row using a unique
     * column name and a unique row value for that unique column
     *
     * @param TableName
     * @param UniqueColName
     * @param ColumnName
     * @param NewValue
     * @param UniqueRowValue
     * @throws SQLException
     */
    public void setColValue(String TableName, String ColumnName, String UniqueColName, String UniqueRowValue, Object NewValue)
            throws SQLException {


        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista"))  {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement("UPDATE " + TableName + "  SET " + ColumnName + "='" + NewValue + "' WHERE " + UniqueColName + "=" + UniqueRowValue + ";");
            System.out.println("UPDATE " + TableName + "  SET " + ColumnName + "='" + NewValue + "' WHERE " + UniqueColName + "=" + UniqueRowValue + ";");
            statement.execute();

        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }


    }

    //
    //
    /////////////////////////////////////////////// GET TEH DATA
    //
    //
    /**
     * A simple way to get a result set containing values From a row
     *
     * @param TableName : The name of the table you want to get data form
     * @param ID : The value you are looking for
     * @param ColumnName : The Column you want
     * @return An Object Array Containing Values from the Column and row of the
     * ID
     *
     * How To Get The Value out of the Object[] Array:
     *
     * Ex. String arr = (String) DataBase.getRow("TableName", 2, "Column1")[0];
     * SQL is: SELECT ID,ColumnName FROM TableName WHERE ID=?
     *
     * @throws SQLException
     */
    public Object[] getRow(String TableName, Integer ID, String ColumnName)
            throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(databasePath + DatabaseName + ";IFEXISTS=TRUE");


        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + "//" + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "//" + DatabaseName + ";IFEXISTS=TRUE");
        }
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT ID," + ColumnName + " FROM " + TableName + " WHERE ID=?");
            statement.setInt(1, ID);
            //System.out.println("SELECT ID," + ColumnName + " FROM " + TableName + " WHERE ID=?");
            ResultSet rs = statement.executeQuery();
            rs.next();


            Array ar = rs.getArray(ColumnName);
            Object[] arr = (Object[]) ar.getArray();
            return arr;


        } catch (SQLException ex) {

            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

        //incase of error return nothing 
        return null;
    }

    /**
     *
     * A Bit More Flexibility in Getting a Value Found in a Row
     *
     * @param TableName : Table we are looking at
     * @param FromColumnNames : a String Array that contains the names of each
     * Column we are looking at
     * @param WhereQuery : A String Containing a Custom SQL Query after the
     * WHERE clause Ex. WHERE + "ColumnName='Hello'"
     * @param getColumnName : The Column from which we aquire the Value (Row is
     * defined by the WhereQuery)
     *
     * @return An Object Array Containing the Value you are looking for
     *
     * To Get That Value From The Object[]:
     *
     * Ex. String arr = (String) db.getRowFlex("Test1", ColumnArray , "ID=2" ,
     * "Column1")[0];
     *
     * The SQL would be: SELECT ColumnArray[1], ColumnArray[2], ... FROM
     * TableName WHERE ID=2 And you would get the value in column: Column1
     *
     * @throws SQLException
     */
    public Object[] getRowFlex(String TableName, String[] FromColumnNames, String WhereQuery, String getColumnName)
            throws SQLException {

        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "//lib//" + DatabaseName + ";IFEXISTS=TRUE");

            //Dev Mode
        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "//" + DatabaseName + ";IFEXISTS=TRUE");
        }
        StringBuilder ColumnCSV = new StringBuilder();
        for (String column : FromColumnNames) {
            if (FromColumnNames[FromColumnNames.length - 1].equals(column)) {
                ColumnCSV.append(column);
            } else {
                ColumnCSV.append(column).append(",");
            }
        }


        System.out.println(ColumnCSV);

        try {
            System.out.println("SELECT " + ColumnCSV + " FROM " + TableName + " WHERE " + WhereQuery);
            PreparedStatement statement = connection.prepareStatement("SELECT " + ColumnCSV + " FROM " + TableName + " WHERE " + WhereQuery);

            ResultSet rs = statement.executeQuery();
            rs.next();


//            System.out.println(rs.getString(1));
            Array ar = rs.getArray(getColumnName);
            Object[] arr = (Object[]) ar.getArray();

            return arr;


        } catch (SQLException ex) {

            connection.rollback();
            Logger.getLogger(aSimpleDB.class.getName()).log(Level.SEVERE, null, ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

        //incase of error return nothing 
        return null;
    }

    /**
     * Simple Way of deleting a Row using the Predefined ID as a PrimaryKey
     *
     * @param TableName : Name Of table where row exists
     * @param ID : Integer Value of the row (Must Have A Primary Key Column
     * Called "ID"
     * @throws SQLException
     */
    public void deleteRow(String TableName, int ID) throws SQLException {
        connection = DriverManager.getConnection("jdbc:h2:~/" + DatabaseName);
        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM" + TableName + "WHERE ID=?");
            statement.setInt(1, ID);
            statement.execute();


        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * A More Flexible way of deleting a row Using a custom SQL Query
     *
     * @param TableName : Table where the row exists
     * @param WhereQuery : Custom Query after the WHERE clause (Ex. WHERE +
     * "ColumnName='Hey')
     * @throws SQLException
     */
    public void deleteRowFlex(String TableName, String WhereQuery) throws SQLException {
        // connection = DriverManager.getConnection("jdbc:h2:~/" + DatabaseName);

        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }


        try {

            PreparedStatement statement = connection.prepareStatement("DELETE FROM " + TableName + " WHERE " + WhereQuery);
            statement.execute();


        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

    }

    /**
     * A Simple way of searching the database using characters found in a field.
     *
     * @param TableName Name of table you are searching in
     * @param ColumnName Column name you want to search in
     * @param aproxString The characters in the String you are looking for
     * @return an Object Array containing the strings containing the aproxString
     * key
     *
     * To Get That Value From The Object[]:
     *
     * Ex. String arr = (String) db.searchAprox("Test1", "Column1" , "xyz")[0];
     *
     * The SQL would be: SELECT * FROM" Test1 WHERE Column1 LIKE '%xyz%'
     *
     * @throws SQLException
     */
    public Object[] searchAprox(String TableName, String ColumnNameSelect, String ColumnNameWhere, String aproxString) throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement("SELECT " + ColumnNameSelect + " FROM " + TableName + " WHERE " + ColumnNameWhere + " LIKE '%" + aproxString + "%'");

            ResultSet rs = statement.executeQuery();
            rs.first();
            Object[] array = new Object[10];
            for (int i = 0; !rs.isAfterLast() && i < 9; i++) {
                array[i] = rs.getString(ColumnNameSelect);
                rs.next();
            }

            return array;


        } catch (SQLException ex) {

//            connection.rollback();
            System.err.println(ex);

        } finally {
            connection.close();
            System.out.println("Closed Connection");
        }

        //incase of error return nothing 
        return null;
    }

    /**
     * Allows For Any SQL Statement To Be Executed
     *
     * NOTE! This Method Does Not Close The Connection You Must Manually Close
     * it after Performing the SQL commands Ex. DB.CloseConnection();
     *
     * @param SQL
     * @throws SQLException
     */
    public void flexExecute(String SQL) throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println(System.getProperty("user.dir") + "\\lib\\" + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {

            PreparedStatement statement = connection.prepareStatement(SQL);
            System.out.println(SQL);
            statement.execute();


        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex);

        }

    }

    /**
     * Allow for any SQL Query to be Done and returns the Result Set
     *
     * NOTE! This Method Does Not Close The Connection You Must Manually Close
     * it after Finishing with the Result Set Ex. DB.CloseConnection();
     *
     * @param SQL : A String containing a single SQL Query
     * @return a ResultSet Containing the Result of the Query
     * @throws SQLException
     */
    public ResultSet flexQuery(String SQL) throws SQLException {
        try {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

            System.out.println("jdbc:h2:file:" + databasePath + DatabaseName + ";IFEXISTS=TRUE");

        } catch (Exception exx) {
            String osName = System.getProperty("os.name");
            if (osName.equals("Windows 7") || osName.equals("Windows XP") || osName.equals("Windows Vista")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV + DatabaseName + ";IFEXISTS=TRUE");
            } else if (osName.equals("Mac OS X")) {
                connection = DriverManager.getConnection("jdbc:h2:file:" + databasePath_DEV.replace("\\", "//") + DatabaseName + ";IFEXISTS=TRUE");
            }

        }
        try {


            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            ResultSet rs = statement.executeQuery(SQL);
            rs.first();

            return rs;

        } catch (SQLException ex) {
            connection.rollback();
            System.err.println(ex);
            return null;
        }

    }

    /**
     * Closes the Connection to the Current Database Only Used for flex Methods
     */
    public void CloseConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(aSimpleDB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setDatabasePath(String Path) {
        databasePath = Path;
    }

    public void setDatabasePath_DEV(String Path) {
        databasePath_DEV = Path;
    }
}
