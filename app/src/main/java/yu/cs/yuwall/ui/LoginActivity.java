package yu.cs.yuwall.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import yu.cs.yuwall.R;
import yu.cs.yuwall.utils.CheckConnection;
import yu.cs.yuwall.utils.MaterialProgressDialog;
import yu.cs.yuwall.utils.ParseConstants;


public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.input_username)
    EditText _usernameText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            }
        });
    }


    private boolean netConnectionCheck() {
        CheckConnection checkConnection = new CheckConnection(LoginActivity.this);
        return checkConnection.isNetworkAvailable();
    }


    public void login() {
        Log.d("Log in Activity running", "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        if (!netConnectionCheck()) {
            Toast.makeText(getBaseContext(), "Network Error!", Toast.LENGTH_SHORT).show();
            return;
        }

        final MaterialProgressDialog mProgressDialog = new MaterialProgressDialog(this);
        mProgressDialog.show("");

        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onLoginSuccess(mProgressDialog);
                        mProgressDialog.dismiss("");
                    }
                }, 5000);
    }

    @Override
    public void onBackPressed() {
        // Disable going back
        moveTaskToBack(true);
    }

    public void onLoginSuccess(final MaterialProgressDialog mpd) {
        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Intent intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    ParseConstants.KEY_USER_LOGGED = true;
                    startActivity(intent);
                    overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                } else {
                    Toast.makeText(getBaseContext(), "Log in failed!", Toast.LENGTH_SHORT).show();
                    mpd.dismiss("");
                }
            }
        });
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        // log in fail task handling here
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            //------test username valid or not from server!!!!!
            _usernameText.setError("invalid username");
            valid = false;
        } else {
            _usernameText.setError(null);
        }

        if (password.isEmpty() || password.length() < 8 || password.length() > 16) {
            _passwordText.setError("between 8 and 16 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
