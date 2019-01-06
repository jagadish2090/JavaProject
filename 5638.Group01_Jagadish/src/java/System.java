import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;
import javax.faces.context.FacesContext;

/**
 *
 * @author JAGADISH
 */
@Named(value = "System")
@SessionScoped
public class System implements Serializable {

    ////Attributes
    private String UserId ;
    private Email Current_Email;
    private String Title;
    private String Content;
    private String Sender;
    private String Receiver;
    private String Message;
    private String Password;
    private int emailIDtoView;
    private boolean Notification;
    private ArrayList<Email> getEmails =  new ArrayList<Email>();
    
    ////Getter & Setter
    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }
    
    public String getSender() {
        return Sender;
    }

    public void setSender(String Sender) {
        this.Sender = Sender;
    }

    public String getReceiver() {
        return Receiver;
    }

    public void setReceiver(String Receiver) {
        this.Receiver = Receiver;
    }
    
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public Email getCurrent_Email() {
        return Current_Email;
    }

    public void setCurrent_Email(Email Current_Email) {
        this.Current_Email = Current_Email;
    }

    public ArrayList<Email> getGetEmails() {
        return getEmails;
    }
    
    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setGetEmails(ArrayList<Email> getEmails) {
        this.getEmails = getEmails;
    }
    
    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }
    
    public int getEmailIDtoView() {
        return emailIDtoView;
    }
  
    public void setEmailIDtoView(int emailIDtoView) {
        this.emailIDtoView = emailIDtoView;
    }

    public String ViewEmail(String emailId)   {
        /*
        This function receives emailId, and display the email content.
        Requirement as we did in project 1, such as New, Re, Title ...
        Assign this email to CurrentEmail above, in case we need to reply this email
        */
        return "viewemail";
    }

    public boolean isNotification() {
        return Notification;
    }

    public void setNotification(boolean Notification) {
        this.Notification = Notification;
    }

    ////Functions
    public String Login()   {
        ////After user login, direct to main page
        Message ="";
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        if(UserId == null) 
        {
            return "login.xhtml";
        }
        if(UserId != null && Password != null)
        {
            Connection conn= null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                rs = st.executeQuery(String.format(
                        "SELECT * FROM USER WHERE USERID ='%s' AND PASSWORD = '%s'", UserId, Password));
                if(rs.next())
                {
                    UserId  = rs.getString(1);
                    return "mainpage";
                }
                else
                {
                    Message = "Wrong username or password";
                    return "";
                }
                        
            } catch (SQLException e) {
                e.printStackTrace();
                return "error";
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            Message = "Username and password cannot be empty !";
            return "";//"error";
        }
    }
    
    ////Intermediate fuction to display list obj
    public String getEmailByID (int id)    {
        emailIDtoView = id;
        return "emaildetail";
    }
    
    public String getEmailByID2 (int id)    {
        emailIDtoView = id;
        return "emaildetail2";
    }
    
    public Email readSelectedEmail()    {
        for(Email e: getEmails)
        {
            if(e.getEmailId() == emailIDtoView)
            {
                Current_Email = e;
                return e;
            }
        }
        return null;
    }

    ////Formating email
    public String CheckNewEmail(boolean isRead)    {
        return isRead == false ? "(New) " : "";
    }
    
    public String TitleSubString(String title)    {
        if(title.length() > 20)
            return title.substring(0, 20);
        else
            return title;
    }
     
    ////Update change status
    public void UpdateIsRead(Email email) {
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message += "Session expired, login again!";
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                               
                //Check if email is in database
                rs = st.executeQuery(String.format("SELECT * FROM EMAIL WHERE EMAILID = '%s'", email.getEmailId()));
                if(rs.next())
                {
                    //FOUND UPDATE STATUS
                    st.executeUpdate("UPDATE EMAIL SET ISREAD = '1' WHERE EMAILID = '" +email.getEmailId()+"'");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }        
        }
    } ////Update from new to not new
       
    ////Compose
    public String Compose() {
      /*
        Function to write a new email, follow requirement as you did in project 1
        */
      Message = "";
      Title = "";
      Content ="";
      Receiver ="";
      Sender ="";
      return "compose.xhtml";
    }
    
    public void WriteEmail () {
        Message="";
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            //return "error";
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message = "Session expired, login again!";
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                
                //Get data
                int emailId = 0; // Auto increment
                String senderID = UserId;
                String receiverID = Receiver;
                String date = DateTime.GetDate();
                String title =  Title;
                String content = Content;
                int isRead = 0;
                int isReply = 0;
                
                //Check if receiverId is a user the database
                rs = st.executeQuery(String.format("SELECT * FROM USER WHERE USERID = '%s'", receiverID));
                if(rs.next())
                {
                    //found receiver, countinue
                    st.executeUpdate(String.format
                        ("INSERT INTO EMAIL VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s', '%s')",
                        emailId, senderID, receiverID, date, title, content, isRead, isReply, 0, Notification == true? 1: 0));
                    Receiver = null;
                    Content = null;
                    Title = null;
                    Message = ("Sent successfully");
                }
                else
                {
                    Message = "ERROR !!! Your email has not been sent, receiver's email is not exist";
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
                //return "error";
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    //return "error";
                }
            }        
        }
    } ////Function to write a new email
    
    public void DeleteEmail (Email email) {
        Message = "";
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message = "Session expired, login again!";
            
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                
                rs = st.executeQuery(String.format("SELECT * FROM EMAIL WHERE EMAILID = '%s'", email.getEmailId()));
                if(rs.next())
                {
                    //Found, 1st time: go to Trask, 2nd time: delete from database
                    if(rs.getInt("IsDelete") == 0)
                    {
                        //Trask
                        st.executeUpdate(String.format("UPDATE EMAIL SET ISDELETE = '1' WHERE EMAILID = '%s'",email.getEmailId()));
                        Message = "Deleted to trash bin";
                    }
                    else if(rs.getInt("IsDelete") == 1)
                    {
                        //Delete from database
                        st.executeUpdate(String.format("DELETE FROM EMAIL WHERE EMAILID = '%s'",email.getEmailId()));
                        Message = "Deleted from database";
                    }
                }
                else
                {
                    Message = "ERROR !!! email is not exist";
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    } ////Function to delete email
    
    ////Reply
    public String Reply(Email e) {
      /*
        Function to reply a email
        */
      Message = "";
      Content = "";
      return "reply.xhtml";
    }
    
    
    public void ReplyEmail ( Email lastEmail) {
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            //return "error";
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message += "Session expired, login again!";
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                //Update content, reply, and is not read
                //rs = st.executeQuery(String.format("SELECT * FROM EMAIL WHERE EMAILID = '%s'", lastEmail.getEmailId()));
           
                    String newContent =  Content + "\n" + ReplyContent(lastEmail) ;
                    String newTitle = "Re:"+lastEmail.getTitle();
                    //FOUND
                    int update = st.executeUpdate(String.format("UPDATE EMAIL SET SENDERID = '%s', RECEIVERID = '%s', DATE = '%s', "
                                            + "TITLE = '%s', CONTENT = '%s', ISREAD = '%s',ISREPLY = '%s' WHERE EMAILID = '%s'",
                            lastEmail.getReceiverId(),
                            lastEmail.getSenderId(),
                            DateTime.GetDate(),
                            newTitle,
                            newContent,
                            0,
                            1,
                            lastEmail.getEmailId()));

                    Message += ("Replied");
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }        
        }
    } ////Function to reply an email

    
    ////Display sent folder
    public void LoadSendingData()    {
        getEmails = getSentEmails(UserId);
    }
    
    public String DisplaySentFolder() {
      /*
        Function to write a new email, follow requirement as you did in project 1
        */
        Message = "";
        return "sentfolder.xhtml";
    }
    
    public ArrayList<Email> getSentEmails(String UserId) {
        ////Return list of emails
        ArrayList<Email> list = new ArrayList<>();
        if(UserId != null)
        {
            Connection conn= null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                rs = st.executeQuery(String.format(
                        "SELECT * FROM EMAIL WHERE ISDELETE = '0' AND SENDERID ='%s'", UserId));
                while(rs.next())
                {
                    Email email = new  Email(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
                            rs.getString(5), rs.getString(6), rs.getBoolean(7), rs.getBoolean(8), rs.getInt(9), rs.getBoolean(10));
                    list.add(email);
                }
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    
    ////Display received folder
    public void LoadReceivingData()    {
        getEmails = getReceivedEmails(UserId);
    }

    public ArrayList<Email> getReceivedEmails(String UserId) {
        ////Return list of emails
        ArrayList<Email> list = new ArrayList<>();
        if(UserId != null)
        {
            Connection conn= null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                rs = st.executeQuery(String.format(
                        "SELECT * FROM EMAIL WHERE ISDELETE = '0' AND RECEIVERID ='%s'", UserId));
                while(rs.next())
                {
                    Email email = new  Email(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
                            rs.getString(5), rs.getString(6), rs.getBoolean(7), rs.getBoolean(8), rs.getInt(9), rs.getBoolean(10));
                    list.add(email);
                }
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    
    //// Logout & About
    public String About()   {
        return "about";
      ////Professor name,  student names and ID, date of completion.  
    }  
    
    public String Logout()    {
        ////Close all connection and set AccountID = null 
        ////This function to delete all static variables, colleciton and close session
        UserId = null;
        Current_Email = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index.xhtml";
    }
    
    ////Navigation
    public String Inbox()   {
        Message = "";
        return "mainpage.xhtml";
      
    }
    
    public String ReplyContent(Email email)    {
        String s ="";
        s = String.format(
                "\n" +                
                "_____________________________________________"+ 
                "\n" +
                "Re: %s said on %s:"+
                "\n" +
                "%s",
                User.ConvertAcctIDtoName(email.getSenderId()), email.getDate(), email.getContent()) ;
        return s;
    }
    
    public String ForwardContent(Email email)    {
        String s ="";
        s = String.format(
                "\n" +                
                "_____________________________________________"+ 
                "\n" +
                "FW: %s said on %s:"+
                "\n" +
                "%s",
                User.ConvertAcctIDtoName(email.getSenderId()), email.getDate(), email.getContent()) ;
        return s;
    }
    
    ////Check an email is read
    public String IsRead(Email email) {
        String isread = "";
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message += "Session expired, login again!";
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                               
                //Check if email is in database
                rs = st.executeQuery(String.format("SELECT * FROM EMAIL WHERE EMAILID = '%s' AND ISREAD = '%s'" , email.getEmailId(), 1));
                if(rs.next())
                {
                   isread = "Read"; 
                }
                else
                {
                    isread = "";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } 
           finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }        
        }
        return isread;
    } 
    
    public String IsRead2(boolean value)    {
        return value == true ? ",      (Email was read)" : "";
    }
    
    ////Dispaly Trash folder
    public String DisplayTrashFolder()    {
        Message = "";
        return "trashfolder.xhtml";
    }
    
    public void LoadTrashData()    {
        getEmails = getTrahEmails(UserId);
    }
    
    public ArrayList<Email> getTrahEmails(String UserId) {
        ////Return list of emails
        ArrayList<Email> list = new ArrayList<>();
        if(UserId != null)
        {
            Connection conn= null;
            Statement st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                rs = st.executeQuery(String.format(
                        "SELECT * FROM EMAIL WHERE ISDELETE != '0' AND RECEIVERID ='%s'", UserId));
                while(rs.next())
                {
                    Email email = new  Email(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
                            rs.getString(5), rs.getString(6), rs.getBoolean(7), rs.getBoolean(8), rs.getInt(9), rs.getBoolean(10));
                    list.add(email);
                }
                return list;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }
    
    ////Forward
    public String Forward()    {
        return "forward.xhtml";
    }
    
    public void Forward ( Email lastEmail, String newReceiver) {
        try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message = "Session expired, login again!";
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                
                rs = st.executeQuery("SELECT * FROM USER WHERE USERID = '" + newReceiver + "'");
                if(rs.next())
                {
                    //Found userid
                    String newContent =  Content + "\n" + ForwardContent(lastEmail);
                    String newTitle = "FW:"+lastEmail.getTitle();
                    //Update
                    int update = st.executeUpdate(String.format("UPDATE EMAIL SET SENDERID ='%s', RECEIVERID = '%s', DATE = '%s', "
                            + "TITLE = '%s', CONTENT = '%s', ISREAD = '%s',ISREPLY = '%s' WHERE EMAILID = '%s'",
                            lastEmail.getReceiverId(),
                            newReceiver,
                            DateTime.GetDate(),
                            newTitle,
                            newContent,
                            0,
                            1,
                            lastEmail.getEmailId()));

                    Message = "Forwarded";
                }
                else
                {
                    //Not found
                    Message = "Email has not been found";
                }
                
            } catch (SQLException e) {
                e.printStackTrace();
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }        
        }
    } 
    
    ////Auto Notification
    public void Notification (Email email)    {
        
    if( email.isIsRead() == false && email.getIsNotification() == true)
    {
        {
            try {
            Class.forName(Helper.DRIVER_STRING);
        } catch (Exception e) {
            e.printStackTrace();
            //return "error";
        }
        if(UserId == null || UserId.length() == 0)
        {
            Message = "Session expired, login again!";
        }
        else
        {
            Connection conn = null;
            Statement  st = null;
            ResultSet rs = null;
            try {
                conn = DriverManager.getConnection(Helper.DB_URL, Helper.USER_DB, Helper.PSW_DB);
                st = conn.createStatement();
                
                // Task1: Send notification
                String content =  String.format("Your message to %s was read", User.ConvertAcctIDtoName(email.getReceiverId()));
                String title = "Sytem Notification";

                st.executeUpdate(String.format
                        ("INSERT INTO EMAIL VALUES ('%s','%s','%s','%s','%s','%s','%s','%s','%s', '%s')",
                        0, "system1", email.getSenderId(), DateTime.GetDate(), title, content, 0, 0, 0, 0));
                        
                                
                //Task2: Update IsNotification = 0
                //rs = st.executeQuery(String.format("UPDATE EMAIL SET ISNOTIFICATION = '0' WHERE EMAILID = '%s'", email.getEmailId()));
                //Message = "Notified";
                
            } catch (SQLException e) {
                e.printStackTrace();
                //return "error";
            }
            finally
            {
                try {
                    conn.close();
                    st.close();
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    //return "error";
                }
            }        
         }
            
        }
    }
    }
    
    public void Clear() {
        Message = "";
 
    }
}
