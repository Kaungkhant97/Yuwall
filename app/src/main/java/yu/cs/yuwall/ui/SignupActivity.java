package yu.cs.yuwall.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import yu.cs.yuwall.R;
import yu.cs.yuwall.utils.CheckConnection;
import yu.cs.yuwall.utils.ParseConstants;

public class SignupActivity extends AppCompatActivity {

    private TextInputLayout TLUserName, TLPassword;
    private EditText usrName, Pwd;
    private Button submitButton;
    private boolean available;
    private String username,password;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        progressBar =(ProgressBar) findViewById(R.id.suprogressBar);
        progressBar.setVisibility(View.GONE);
        TLUserName = (TextInputLayout)findViewById(R.id.sutil);
        TLPassword =(TextInputLayout)findViewById(R.id.sutil2);
        submitButton = (Button)findViewById(R.id.btnSignup);
        linearLayout =(LinearLayout)findViewById(R.id.sulayout);

        usrName = (EditText)findViewById(R.id.suetEmail);
        Pwd = (EditText)findViewById(R.id.suetPass);

        TLUserName.setHint("Enter User Name"); //setting hint.
        TLPassword.setHint("Enter password");


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                available =true;
                hideKeyboard();
                username=usrName.getText().toString();
                password=Pwd.getText().toString();
                checkUserName(username,TLUserName);
                checkPassword(password, TLPassword);
                networkConCheck();
                if(available==true){

                    ParseUser newuser=new ParseUser();
                    newuser.setUsername(username);
                    newuser.setPassword(password);

                        progressBar.setVisibility(View.VISIBLE);
                        linearLayout.setVisibility(View.GONE);
                    newuser.signUpInBackground(new SignUpCallback() {

                        @Override
                        public void done(ParseException e) {

                            if(e==null){
                            //Success
                                progressBar.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                Intent intent = new Intent(SignupActivity.this, NewsFeedActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                ParseConstants.KEY_USER_LOGGED=true;
                                startActivity(intent);

                            }
                            else{
                                progressBar.setVisibility(View.GONE);
                                linearLayout.setVisibility(View.VISIBLE);
                                available=false;
                                Toast.makeText(SignupActivity.this,"Error has occured during login",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }

            }
        });
    }
    private void networkConCheck(){
        CheckConnection checkConnection=new CheckConnection(SignupActivity.this);
        if(!checkConnection.isNetworkAvailable()){
            Toast.makeText(SignupActivity.this,"Connection lost",Toast.LENGTH_SHORT).show();
            available=false;
        }

    }
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    private void checkPassword(String password,TextInputLayout TLPassword) {
        if (password.length() < 8 || password.isEmpty()) {
            TLPassword.setErrorEnabled(true);
            TLPassword.setError("Your password must be more than 8");
            available = false;
        }
    }
    private void checkUserName(String username,TextInputLayout TLusername) {
        if (username.isEmpty()) {
            TLUserName.setErrorEnabled(true);
            TLUserName.setError("username cannot be empty");
            available = false;
        }
    }


}
