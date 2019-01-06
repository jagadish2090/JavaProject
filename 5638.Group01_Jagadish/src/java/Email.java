/**
 *
 * @author JAGADISH
 */
public class Email {

    private int EmailId;
    private String SenderId;
    private String ReceiverId;
    private String Date;
    private String Title;
    private String Content;
    private boolean isRead;
    private boolean isReply;
    private int isDelete;
    private boolean isNotification;

    
    
    ////Constructor

    public Email(int EmailId, String SenderId, String ReceiverId, String Date, 
            String Title, String Content, boolean isRead, boolean isReply, int isDelete, boolean isNotificaiton) {
        
        this.EmailId = EmailId; // This will be automatically increase in MySQL database
        this.SenderId = SenderId;
        this.ReceiverId = ReceiverId;
        this.Date = Date;
        this.Title = Title;
        this.Content = Content;
        this.isRead = isRead;
        this.isReply = isReply;
        this.isDelete = isDelete;
        this.isNotification = isNotificaiton;
    }

    public boolean getIsNotification() {
        return isNotification;
    }

    public void setIsNotification(boolean isNotification) {
        this.isNotification = isNotification;
    }
    
    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    ////Other implementations

    public int getEmailId() {
        return EmailId;
    }

    public void setEmailId(int EmailId) {
        this.EmailId = EmailId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String SenderId) {
        this.SenderId = SenderId;
    }

    public String getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(String ReceiverId) {
        this.ReceiverId = ReceiverId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsReply() {
        return isReply;
    }

    public void setIsReply(boolean isReply) {
        this.isReply = isReply;
    }

}
