package xpenses.xpenses;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by HP on 17-05-2017.
 */

public class DateClass {

    public static String findCurrentDate()
    {
        Date d=new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(d);
        Log.d("Date : ",date);

        return date;
    }

    public static String findCurrentMonthString()
    {
        Date d=new Date();
        SimpleDateFormat df = new SimpleDateFormat("MM");
        String date = df.format(d);

        return date;
    }
    public static int findCurrentMonthInt()
    {
        String m= findCurrentMonthString();
        int month = Integer.parseInt(m);

        return month;
    }


    public static String findDateOfMonthString()
    {
        Date d=new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String date = df.format(d);

        return date;
    }

    public static int findDateOfMonthInt()
    {
        String d=findDateOfMonthString();
        int date=Integer.parseInt(d);

        return date;
    }

    public static boolean isCurrentYearLeapYear() {
        Date d=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy");

        String y = df.format(d);
        int year = Integer.parseInt(y);

        if(year%4==0){
            if(year%100==0)
            {
                if(year%400==0)
                    return true;
                else
                    return false;
            }
            else
                return true;
        }
        else
            return false;

    }
}
