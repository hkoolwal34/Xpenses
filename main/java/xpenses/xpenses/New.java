package xpenses.xpenses;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class New extends AppCompatActivity {

    private int year;
    private int month;
    private int date;
    private TextView dateset;
    private EditText amount;
    private Spinner dropdown;
    private EditText cmt ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        dateset=(TextView) findViewById(R.id.date_new);
        amount=(EditText) findViewById(R.id.amount_new);
        dropdown= (Spinner) findViewById(R.id.spinner_payment_mode_new);
        cmt = (EditText) findViewById(R.id.comments_new);

        initialize_spinner();
        initialize_calendar();
        init();
    }

    private void initialize_calendar() {
        final Calendar cal = Calendar.getInstance();
        year=cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        date=cal.get(Calendar.DAY_OF_MONTH);

        dateset.setText(date+"/"+month+"/"+year);

    }

    private void init() {
        ImageButton date_button=(ImageButton) findViewById(R.id.imageButton_date_new);

        date_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        Button submit = (Button) findViewById(R.id.button_submit_new);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String dat= (String) dateset.getText();
                final String amt= String.valueOf(amount.getText());
                final String mode= String.valueOf(dropdown.getSelectedItem());
                final String comments = String.valueOf(cmt.getText());

                if(amt.isEmpty())
                {
                    Toast.makeText(New.this, "Amount cannot be empty", Toast.LENGTH_SHORT).show();
                }
                else {


                    String message = ("Date : " + dat + "\nAmount : " + amt + "\nMode : " + mode);


                    new AlertDialog.Builder(New.this)
                            .setTitle("Confirm Details")
                            .setMessage(message)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    DatabaseHandler db = new DatabaseHandler(New.this);
                                    Record r = new Record();

                                    r.setDate(DateClass.createDate(year, month, date));
                                    r.setPaymentMode(mode);
                                    r.setAmount(Float.parseFloat(amt));
                                    r.setComments(comments);

                                    db.addRecord(r);
                                    Toast.makeText(New.this, "Record added successfully", Toast.LENGTH_SHORT);

                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }

            }
        });



    }

    private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int y, int m, int dOM) {
            year=y;
            month=m+1;
            date=dOM;

            dateset=(TextView) findViewById(R.id.date_new);
            dateset.setText(date+"/"+month+"/"+year);
        }
    };

    private void initialize_spinner() {

        dropdown = (Spinner) findViewById(R.id.spinner_payment_mode_new);
        String[] items = new String[]{"Cash","Card","Paytm","Freecharge"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,items);
        dropdown.setAdapter(adapter);
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id)
        {
            case 0:     return  new DatePickerDialog(this,pDateSetListener,year,month,date);

        }

        return null;
    }
}
