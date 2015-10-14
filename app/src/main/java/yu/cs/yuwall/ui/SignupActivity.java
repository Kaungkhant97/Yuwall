package yu.cs.yuwall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yu.cs.yuwall.R;
import yu.cs.yuwall.utils.CheckConnection;
import yu.cs.yuwall.utils.MaterialProgressDialog;
import yu.cs.yuwall.utils.ParseConstants;

/**
 * Created by AungKo on 10/14/2015.
 */

public class SignupActivity extends AppCompatActivity {

    @Bind(R.id.input_username_su)   EditText _username;
    @Bind(R.id.input_fullname_su)   EditText _fullName;
    @Bind(R.id.input_password_su)   EditText _password;
    @Bind(R.id.input_confirm_password_su)   EditText _confirm_password;
    @Bind(R.id.spinner_majorlist_su)   EditText _majorList;
    @Bind(R.id.btn_signup)    Button _signupButton;
    @Bind(R.id.link_login)    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ButterKnife.bind(this);

        final ListPopupWindow majorListPopup;
        final List<String> majorList = Arrays.asList(getResources().getStringArray(R.array.major_list));
        majorListPopup = new ListPopupWindow(this);
        majorListPopup.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,majorList));
        majorListPopup.setAnchorView(_majorList);
        majorListPopup.setModal(true);
        majorListPopup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String item = majorList.get(position);
                _majorList.setText(item);
                majorListPopup.dismiss();
            }
        });

        _majorList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                majorListPopup.show();
                return false;
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        });

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void parseUserSignUp(final MaterialProgressDialog mpd){
        // Set up a new Parse user
        String username = _username.getText().toString().toLowerCase();
        String fullName = _fullName.getText().toString();
        String password = _password.getText().toString();
        String major = _majorList.getText().toString();

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.put("FullName", fullName);
        user.setPassword(password);
        user.put("major", major);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "Sign Up Success!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignupActivity.this,NewsFeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ParseConstants.KEY_USER_LOGGED = true;
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    mpd.dismiss("");
                }
            }
        });
    }

    public void signup() {
        Log.d("Signing Up", "Signup");

        if (!validate()) {
            return;
        }

        if(!netConnectionCheck()){
            Toast.makeText(getBaseContext(),"Network Error!",Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Implement your own signup logic here.

        final MaterialProgressDialog mProgressDialog = new MaterialProgressDialog(this);
        mProgressDialog.show("");

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        parseUserSignUp(mProgressDialog);
                        mProgressDialog.dismiss("");
                    }
                }, 5000);
    }


    private boolean netConnectionCheck(){
        CheckConnection checkConnection=new CheckConnection(SignupActivity.this);
        return checkConnection.isNetworkAvailable();
    }

    public boolean validate() {
        boolean valid = true;

        String username = _username.getText().toString();
        String fullName = _fullName.getText().toString();
        String password = _password.getText().toString();
        String confirmPwd = _confirm_password.getText().toString();

        if (username.isEmpty() || username.length() < 3) {
            _username.setError("at least 3 characters");
            valid = false;

        }else if(containWhiteSpace(username)){
            _username.setError("cannot contain whitespace");
            valid = false;
        }else {
            _username.setError(null);
        }

        if (fullName.isEmpty()) {
            _fullName.setError("enter your full Name");
            valid = false;
        } else {
            _fullName.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            _password.setError("between 8 and 16 alphanumeric characters");
            valid = false;
        } else {
            _password.setError(null);
        }

        if (!confirmPwd.equals(password)) {
            _confirm_password.setError("your passwords do not match");
            valid = false;
        } else {
            _confirm_password.setError(null);
        }

        if(_majorList.getText().toString().isEmpty()){
            _majorList.setError("Please Select one Major");
            valid=false;
        }else{
           _majorList.setError(null);
        }

        return valid;
    }

    private boolean containWhiteSpace(final String string){
        if(string != null){
            for(int i=0; i<string.length(); i++){
                if(Character.isWhitespace(string.charAt(i)))
                    return true;
            }
        }
        return false;
    }
}
