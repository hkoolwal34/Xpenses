package xpenses.xpenses;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        db = new DatabaseHandler(this);

        Date d=new Date(2017,5,19);
        db.addRecord(new Record(d,1000,"Cash","Testing"));
        db.addRecord(new Record(d,50,"Card","Testing"));
        db.addRecord(new Record(d,3000,"Paytm","Tesing"));

        Log.d("Reading","Reading all records");
        List<Record> records = db.getAllRecords();
        int i=0;
        for (Record r:records)
        {
            i++;
            Log.d("record : ",r.getPaymentMode()+" "+r.getComments()+" "+r.getAmount()+" "+r.getDate());

        }
        Log.d("numofrecords : ",String.valueOf(i));

        Button b= (Button) findViewById(R.id.button4);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),New.class);
                startActivity(intent);
            }
        });
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
