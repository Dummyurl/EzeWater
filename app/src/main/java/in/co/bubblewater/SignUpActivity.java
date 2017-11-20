package in.co.bubblewater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnsubmit;
    RelativeLayout imgclose;
    EditText edtname, edtemail, edtphone, edtpass;
    String Name, Email, Phone, Pass;
    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    JSONArray responce = null;
    String userId, message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgn_up);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        imgclose = (RelativeLayout) findViewById(R.id.btnclose);
        edtname = (EditText) findViewById(R.id.edtname);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtphone = (EditText) findViewById(R.id.edtphone);
        edtpass = (EditText) findViewById(R.id.edtpass);
        btnsubmit.setOnClickListener(this);
        imgclose.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsubmit:
                Name = edtname.getText().toString();
                Email = edtemail.getText().toString().trim();
                Phone = edtphone.getText().toString().trim();
                Pass = edtpass.getText().toString().trim();
                check();

                break;
            case R.id.btnclose:
                SignUpActivity.this.finish();
                break;
        }
    }

    public void check() {
        if (!Name.equalsIgnoreCase("")) {
            if (Email.matches(emailPattern)) {
                if (Phone.length() > 9) {
                    if (Pass.length() > 2) {
                        new Register().execute();
                    } else {
                        edtpass.requestFocus();
                        edtpass.setText("");
                        edtpass.setError("Please fill password");
                    }
                } else {
                    edtphone.requestFocus();
                    edtphone.setText("");
                    edtphone.setError("Please fill valid number");
                }
            } else {
                edtemail.requestFocus();
                edtemail.setText("");
                edtemail.setError("Please fill valid email");
            }
        } else {
            edtname.requestFocus();
            edtname.setText("");
            edtname.setError("Please fill name");
        }
    }

    public class Register extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(SignUpActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/register.php?fname=" + URLEncoder.encode(Name) + "&lname=&email=" + URLEncoder.encode(Email) + "&phone=" + URLEncoder.encode(Phone) + "&pwd=" + Pass + "&pwd2=" + Pass + "&address=&state=&country=&zipcode=&month=&day=&subscriber=yes&registered=1");
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    userId = c.getString("login_user_id");
                    message = c.getString("message");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdia.dismiss();
            pdia = null;
            if (Integer.parseInt(userId) == 0) {
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
            } else {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("LoginId", Integer.parseInt(userId));
                editor.commit();
                Intent in = new Intent(SignUpActivity.this, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }
    }

}
