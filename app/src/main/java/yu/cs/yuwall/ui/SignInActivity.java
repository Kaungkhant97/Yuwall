package yu.cs.yuwall.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import yu.cs.yuwall.R;

public class SignInActivity extends AppCompatActivity {

    TextInputLayout TLUserName, TLPassword;
    EditText usrName, Pwd;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TLUserName = (TextInputLayout)findViewById(R.id.til);
        TLPassword =(TextInputLayout)findViewById(R.id.til2);
        submitButton = (Button)findViewById(R.id.btnSingIn);

        usrName = (EditText)findViewById(R.id.etEmail);
        Pwd = (EditText)findViewById(R.id.etPass);

        TLUserName.setHint("Enter User Name"); //setting hint.
        TLPassword.setHint("Enter password");


    }

}
