package yu.cs.yuwall.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yu.cs.yuwall.R;
import yu.cs.yuwall.utils.CheckConnection;

/**
 * Created by All User on 10/19/2015.
 */
public class Event_DataEntry_Activity extends AppCompatActivity{
    @Bind(R.id.eventName_EditText) EditText eventName_EditText;
    @Bind(R.id.eventLocation_EditText) EditText eventLocation_EditText;
    @Bind(R.id.eventDate_EditText) EditText eventDate_EditText;
    @Bind(R.id.eventTime_EditText) EditText eventTime_EditText;
    @Bind(R.id.importance_RadioGroup) RadioGroup importance_RadioGroup;
    @Bind(R.id.relatedTo_EditText) EditText relatedTo_EditText;
    @Bind(R.id.eventDescription_EditText) EditText eventDescription_EditText;
    @Bind(R.id.requestEvent_Button) Button requestEvent_Button;
    @Bind(R.id.cancelEvent_Button) Button cancelEvent_Button;

    private DatePickerDialog eventDatePickerDialog;
    private TimePickerDialog eventTimePickerDialog;

    private String event_Name;
    private String event_Location;
    private int year;
    private int month;
    private String month_String;
    private int day;
    private int hour;
    private int minute;
    private String event_Time;
    private String am_pm_String;
    private String importance;
    private String related_To_String;
    private String event_Description;
    private ParseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_dataentry_layout);
        ButterKnife.bind(this);
        currentUser = ParseUser.getCurrentUser();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);

        //Show Date Picker Dialog
        Calendar newCalendar = Calendar.getInstance();
        eventDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int yearOfDate, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                day = dayOfMonth;
                month = monthOfYear + 1;
                year = yearOfDate;
                month_String = changeMonthToString(month);
                eventDate_EditText.setText("" + day + " / " + month_String + " / " + year);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        eventDatePickerDialog.setTitle("Select Date");

        //Clicked on Event Date EditText
        eventDate_EditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eventDatePickerDialog.show();
                return false;
            }
        });

        //Show Time Picker Dialog
        hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        minute = newCalendar.get(Calendar.MINUTE);
        eventTimePickerDialog = new TimePickerDialog(Event_DataEntry_Activity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                am_pm_String = changeToAMPM(hour);
                eventTime_EditText.setText(hour + " : " + minute + " " + am_pm_String);
            }
        }, hour, minute, true);//Yes 24 hour time
        eventTimePickerDialog.setTitle("Select Time");

        //Clicked on Event Time EditText
        eventTime_EditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                eventTimePickerDialog.show();
                return false;
            }
        });
/*
        eventTime_EditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
*/

        //Clicked on Related To EditText
        final ListPopupWindow majorListPopup;
        final List<String> majorList = Arrays.asList(getResources().getStringArray(R.array.department_list_event_dataentry));
        majorListPopup = new ListPopupWindow(this);
        majorListPopup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, majorList));
        majorListPopup.setAnchorView(relatedTo_EditText);
        majorListPopup.setModal(true);
        majorListPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                related_To_String = majorList.get(position);
                relatedTo_EditText.setText(related_To_String);
                majorListPopup.dismiss();
            }
        });
        relatedTo_EditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                majorListPopup.show();
                return false;
            }
        });


        //Clicked on Cancel Button
        cancelEvent_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveActivityOrNot();
            }
        });

        requestEvent_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!netConnectionCheck()){
                    Toast.makeText(getBaseContext(), "You have no internet connection.", Toast.LENGTH_SHORT).show();
                }
                else {
                    assignValues();
                    submitOrNot();
                }
            }
        });
    }

    private String changeToAMPM(int h) {
        if(h >= 0 && h <= 12){
            return "A.M";
        }
        else if(h >=13 && h <= 24){
            hour = hour - 12;
            return "P.M";
        }

        return null;
    }

    private void submitOrNot() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Clicked Yes
                        sendEventToParse();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //CLicked No
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Event_DataEntry_Activity.this);
        builder.setMessage("Confirm to submit this event? This event request will reach to the administrators and respective teachers. When the event has been approved, you will receive a notifcation.").setPositiveButton("Submit", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }


    private boolean netConnectionCheck() {
        CheckConnection checkConnection = new CheckConnection(Event_DataEntry_Activity.this);
        return checkConnection.isNetworkAvailable();
    }

    private void sendEventToParse() {
        ParseObject eventrequest = new ParseObject("EventRequests");
        eventrequest.put("event_Name", event_Name);
        eventrequest.put("event_Location", event_Location);
        eventrequest.put("event_Year", year);
        eventrequest.put("event_Month", month);
        eventrequest.put("event_Day", day);
        eventrequest.put("event_Time", event_Time);
        eventrequest.put("importance", importance);
        eventrequest.put("related_to", related_To_String);
        eventrequest.put("event_Description", event_Description);
        eventrequest.put("createBy", currentUser.getString("username").toString());
        eventrequest.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(Event_DataEntry_Activity.this, "Your event request was successfully submitted.", Toast.LENGTH_SHORT).show();
                    goBackToNewsFeed();
                }
                else{
                    Toast.makeText(Event_DataEntry_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //leaveActivityOrNot();
    }

    private void leaveActivityOrNot(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Clicked Yes
                        goBackToNewsFeed();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //CLicked No
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Event_DataEntry_Activity.this);
        builder.setMessage("Are you sure you want to leave? Your event's information won't be saved.").setPositiveButton("Discard", dialogClickListener)
                .setNegativeButton("Keep", dialogClickListener).show();
    }

    private void goBackToNewsFeed(){
        Intent intent = new Intent(getBaseContext(), NewsFeedActivity.class);
        startActivity(intent);
    }


    private void assignValues() {
        event_Name = eventName_EditText.getText().toString();
        event_Location = eventLocation_EditText.getText().toString();
        event_Time = eventTime_EditText.getText().toString();
        event_Description = eventDescription_EditText.getText().toString();
        RadioButton selectedRadioButton = (RadioButton) findViewById(importance_RadioGroup.getCheckedRadioButtonId());
        importance = selectedRadioButton.getText().toString();
    }

    private String changeMonthToString(int m) {
        String month = "";
        switch (m) {
            case 1:
                month = "January";
                break;
            case 2:
                month = "February";
                break;
            case 3:
                month = "March";
                break;
            case 4:
                month = "April";
                break;
            case 5:
                month = "May";
                break;
            case 6:
                month = "June";
                break;
            case 7:
                month = "July";
                break;
            case 8:
                month = "August";
                break;
            case 9:
                month = "September";
                break;
            case 10:
                month = "October";
                break;
            case 11:
                month = "November";
                break;
            case 12:
                month = "December";
                break;
            default:
                break;
        }

        return month;
    }

}
