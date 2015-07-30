package stampshub.app.stampshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;


public class ForgotPassword extends AppCompatActivity {

    TextView emailid;
    Button forgot;
    Integer i;
    TextView link_to_login;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailid=(TextView)findViewById(R.id.emailid);

        forgot=(Button)findViewById(R.id.btnLogin);
        link_to_login=(TextView)findViewById(R.id.link_to_login);

        link_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
                finish();
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ParseUser.requestPasswordReset(emailid.getText().toString());
                    emailid.setText("");
                    i=0;
                } catch (ParseException e) {
                    i=e.getCode();
                }
                finally {
                    if(i==0)
                    {
                        Toast.makeText(getApplicationContext(),"You have been mailed a link to reset your password.",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Error: "+i,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
