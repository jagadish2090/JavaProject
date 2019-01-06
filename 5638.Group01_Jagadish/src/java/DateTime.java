
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author JAGADISH
 */
@Named(value = "DateTime")
@RequestScoped
public class DateTime {

    public static String GetDate()    {
        return  DateFormat.getDateInstance().format(new Date());
    }
    
    public static String GetDateTime()
    {
        return  DateFormat.getTimeInstance(DateFormat.LONG).format(new Date());
    }
    
//    public static String GetDateTime()
//    {
//        return GetDate() + " " + GetTime();
//    }
}