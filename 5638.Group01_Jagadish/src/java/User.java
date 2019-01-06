import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author JAGADISH
 */
@Named(value = "User")
@RequestScoped
public class User {

    private String UserId;
    private String FirstName;
    private String LastName;
    private String Password1;
    private String Password2;
    private String Message;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getPassword1() {
        return Password1;
    }

    public void setPassword1(String Password1) {
        this.Password1 = Password1;
    }

    public String getPassword2() {
        return Password2;
    }

    public void setPassword2(String Password2) {
        this.Password2 = Password2;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
  
    //// Functions
    public String Register() {
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }     
        //Initial validation
        Message = "";
        if(! isAccountIDValidated(UserId))
        {
            Message += "\nEmail ID is not valid.";
            return "";
        }
        else if(! Password1.equals(Password2))
        {
            Message += "\nPasswords are not match. ";
            return "";
        }
        else if( isAccountIDValidated(UserId) && Password1.equals(Password2) )
        {
            //Database operation
            Connection conn = null;
            Statement st = null;
            ResultSet rs = null; 
            try 
            {
                //connect to the data base
                conn = DriverManager.getConnection(Helper.DB_URL,Helper.USER_DB,Helper.PSW_DB);
                st = conn.createStatement();
            
                //Check if the acctID is not in Database
                rs = st.executeQuery(String.format("SELECT * FROM USER WHERE USERID = '%s'",UserId ));
                if(rs.next())
                {
                    Message = "Email ID has been used";
                    return "";//"error";
                }
                else
                {
                    st.executeUpdate( String.format( 
                            "INSERT INTO USER VALUES ('%s','%s','%s','%s')",UserId, Password1, FirstName, LastName));
                    
                    UserId = null;
                    FirstName = null;
                    LastName = null;
                    Password1= null;
                    Password2 = null;
                    Message = "Account has been created";
                    
                    return "register.xhtml";
                }
            } 
            catch (SQLException e) 
            {
                e.printStackTrace(); // importaint for troubleshooting !!!
                return "error";
            }
            
            finally
            {
                //Close the database 
                //it may throw out exceotion of the objs  are null
                try 
                {
                    conn.close();
                    st.close();
                    rs.close();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace(); // importaint for troubleshooting !!!
                }
            }
            
            //System.out.println("The account name is " + lastName + ", " + firstName + "!");
        }
        else
        {
            //Notify: Input is not valid
        }
        return "error";
    } //// Register funciton 
    
    //// Internal validation functions
    public static String ConvertAcctIDtoName (String senderId)    {
        String s = "";
        if(senderId != null)
        {
            Connection cnn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                cnn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = cnn.createStatement();
                rs = st.executeQuery("SELECT * FROM USER WHERE USERID ='" + senderId + "'");
                if(rs.next())
                {
                    s = rs.getString(4) + ", " + rs.getString(3);
                }
            }
            catch (SQLException e) 
            {
                e.printStackTrace();
            }
            finally
            {
                try 
                {
                    cnn.close();
                    st.close();
                    rs.close();
                } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
            }            
        }
        return  s;
    }
    
    private boolean  isAccountIDValidated ( String id) {
        boolean isValidated = true;
        char[] arr = id.toCharArray();
        if(arr.length < 3 || arr.length > 10) isValidated = false;
        boolean hasDigit = false;
        boolean hasLetter = false;
        for(int i = 0; i < arr.length; i ++)
        {
            if(isDigit(arr[i])) hasDigit = true;
            if(isLetter(arr[i])) hasLetter = true;
        }
        if(! hasDigit || ! hasLetter) isValidated = false;
        
        return  isValidated;
    } ////Validate inputs
    
    private boolean isDigit(char c) {
        try 
        {
            int x = Integer.valueOf((String.valueOf(c)));
            return  true;
        } catch (Exception e) {
            return  false;
        }
    }
    
    private boolean isLetter(char c)  {
        try 
        {
            if(isDigit(c)) return  false;
            String s = String.valueOf(c);
            return  true;
        } catch (Exception e) 
        {
            return false;
        }
    } 
}
