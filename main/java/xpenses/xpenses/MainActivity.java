package xpenses.xpenses;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DatabaseHandler db;
    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
//                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.label_date_today);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        db = new DatabaseHandler(this);

//        Date d=new Date(2017,5,17);
//        db.addRecord(new Record(d,120,"Cash","None"));
//        db.addRecord(new Record(d,20,"SBI","9685815170"));
//        db.addRecord(new Record(d,30,"Paytm","9685815170"));

        Log.d("Reading","Reading all records");
        List<Record> records = db.getAllRecords();
        int i=0;
        for (Record r:records)
        {
            i++;
            Log.d("record : ",r.getPaymentMode()+" "+r.getComments()+" "+r.getAmount()+" "+r.getDate());

        }
        Log.d("numofrecords : ",String.valueOf(i));
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        TextView date_today = (TextView) findViewById(R.id.date_today);
        TextView this_month = (TextView) findViewById(R.id.this_month);
        TextView last_week = (TextView) findViewById(R.id.last7);
        date_today.setText(DateClass.findCurrentDate());
        this_month.setText("₹ "+String.valueOf( db.currentMonthExpenses()));
        last_week.setText("₹ "+String.valueOf(db.lastWeekExpenses()));
    }
}
