package xpenses.xpenses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by HP on 17-05-2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;
    //Database Name
    private static final String DATABASE_NAME = "expenseManager";
    //Table Name
    private static final String TABLE_NAME = "records";
    //Column Names
    private static final String KEY_ID="id";
    private static final String KEY_DATE="date";
    private static final String KEY_MONTH="month";
    private static final String KEY_YEAR="year";
    private static final String KEY_AMOUNT="amount";
    private static final String KEY_PAYMENT_MODE="paymentMode";
    private static final String KEY_COMMENTS="comments";




    public DatabaseHandler(Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Query to create the table

        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_DATE + " INTEGER," +
                KEY_MONTH + " INTEGER," +
                KEY_YEAR + " INTEGER," +
                KEY_AMOUNT + " REAL," +
                KEY_PAYMENT_MODE + " TEXT," +
                KEY_COMMENTS + " TEXT)";

        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    //adding a new record
    public void addRecord(Record record)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Date d = record.getDate();
        int date = d.getDate();
        int month = d.getMonth()+1;
        int year = d.getYear();

        //assigning values to keys
        values.put(KEY_DATE,date);
        values.put(KEY_MONTH,month);
        values.put(KEY_YEAR,year);
        values.put(KEY_AMOUNT,record.getAmount());
        values.put(KEY_COMMENTS,record.getComments());
        values.put(KEY_PAYMENT_MODE,record.getPaymentMode());

        //inserting new row
        db.insert(TABLE_NAME,null,values);
        //closing db connection;
        db.close();
    }

    //fetching a record
    public Record getRecord(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[]{KEY_ID,KEY_DATE,KEY_MONTH,KEY_YEAR,KEY_AMOUNT,KEY_PAYMENT_MODE,KEY_COMMENTS},
                KEY_ID + "=?",new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor != null)
            cursor.moveToFirst();

        Date date=convertDate(Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3)));
        Record record = new Record(Integer.parseInt(cursor.getString(0)),date,cursor.getFloat(4),cursor.getString(5),cursor.getString(6));

        return record;
    }

    public List<Record> getAllRecords()
    {
        List<Record> allRecords = new ArrayList<Record>();

        String SELECT_QUERY = "SELECT * FROM  " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY,null);

        if(cursor.moveToFirst())
        {
            do {
                Record r = new Record();
//                r.setId(Integer.parseInt(cursor.getString(0)));
                r.setId(cursor.getInt(0));
                r.setDate(convertDate(Integer.parseInt(cursor.getString(1)),Integer.parseInt(cursor.getString(2)),Integer.parseInt(cursor.getString(3))));
                r.setAmount(cursor.getFloat(4));
                r.setPaymentMode(cursor.getString(5));
                r.setComments(cursor.getString(6));

                allRecords.add(r);
            }while(cursor.moveToNext());
        }
        cursor.close();

        return allRecords;
    }

    public int updateRecord(Record record)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Date d = record.getDate();
        int date = d.getDate();
        int month = d.getMonth()+1;
        int year = d.getYear();

        //assigning values to keys
        values.put(KEY_DATE,date);
        values.put(KEY_MONTH,month);
        values.put(KEY_YEAR,year);
        values.put(KEY_AMOUNT,record.getAmount());
        values.put(KEY_COMMENTS,record.getComments());
        values.put(KEY_PAYMENT_MODE,record.getPaymentMode());

        return db.update(TABLE_NAME,values,KEY_ID + " = ?",new String[]{String.valueOf(record.getId())});

    }
    public void deleteRecord(Record record)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,KEY_ID + "=?",new String[]{String.valueOf(record.getId())});
        db.close();

    }

    private Date convertDate(int date,int month,int year)
    {
        String recordDate = date + "/" + month + "/" + year;

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date returndate = null;
        try {
            returndate = df.parse(recordDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return returndate;
    }

    public float currentMonthExpenses()
    {
        float expenses=1;
        SQLiteDatabase db = this.getWritableDatabase();

        String month=DateClass.findCurrentMonthString();

        String query = "SELECT SUM(amount) FROM "+TABLE_NAME+" WHERE month="+month;
        Cursor cursor = db.rawQuery(query,null);
        Log.d("rows returned : ", String.valueOf(cursor.getCount()));

        if(cursor.moveToFirst())
        {
            expenses = cursor.getFloat(0);
            Log.d("Expenses : ","Current month expenses = "+ String.valueOf(expenses));
        }

        cursor.close();


        return expenses;
    }

    public float lastWeekExpenses() {
        float expenses=0;

        int dateOfMonth = DateClass.findDateOfMonthInt();
        Log.d("DATE = ", String.valueOf(dateOfMonth));
        String SELECT_QUERY;
        int lastWeekDate;

        if(dateOfMonth<8)
        {
            int lastMonth = DateClass.findCurrentMonthInt()-1;

            if(lastMonth==0)
                lastMonth=12;

            int offset=0;
            switch(lastMonth)
            {
                case 1:
                case 3:
                case 7:
                case 8:
                case 5:
                case 12:
                case 10:offset=24;
                        break;
                case 4:
                case 6:
                case 9:
                case 11:offset=23;
                        break;
                case 2:if(DateClass.isCurrentYearLeapYear())
                            offset=22;
                        else
                            offset=21;
                        break;
            }
            lastWeekDate=offset+dateOfMonth;

            SELECT_QUERY="SELECT SUM(amount) FROM "+TABLE_NAME+
                    " WHERE (month = "+ DateClass.findCurrentMonthString()+
                    ") or (month = "+lastMonth+" AND date>=)"+lastWeekDate;
        }
        else
        {
            lastWeekDate = dateOfMonth-7;

            SELECT_QUERY = "SELECT SUM(amount) FROM "+TABLE_NAME+
                    " WHERE month="+DateClass.findCurrentMonthString()+
                    " and date>="+lastWeekDate+
                    " and date<"+dateOfMonth;
        }

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(SELECT_QUERY,null);
        Log.d("lastWeek count : ","Rows Retunred = "+SELECT_QUERY);


        if(cursor.moveToFirst())
        {
            expenses=cursor.getFloat(0);
            Log.d("last week expenses : ","Expenses = "+expenses);
        }
        cursor.close();

        return expenses;
    }
}
