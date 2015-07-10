package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    TextView linktoregister,forgotpassword;
    String usertypeselected;
    RadioGroup usertype;
    EditText emailid,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usertype=(RadioGroup)findViewById(R.id.usertype);

        linktoregister=(TextView)findViewById(R.id.link_to_register);
        forgotpassword=(TextView)findViewById(R.id.forgotpassword);

        emailid=(EditText)findViewById(R.id.emailid);
        password=(EditText)findViewById(R.id.password);

        linktoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),SelectUserType.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),ForgotPassword.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
            }
        });

        usertype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getUserType();
            }
        });
    }

    public void getUserType()
    {
        int var=usertype.getCheckedRadioButtonId();
        RadioButton rb1=(RadioButton)findViewById(var);
        usertypeselected=rb1.getText().toString();
    }
}

