package in.co.bubblewater;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener{
RelativeLayout btnclose;
    EditText edtemail;
    Button btnsubmit;
    String Email;
    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    JSONArray responce = null;
    String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        btnclose=(RelativeLayout)findViewById(R.id.btnclose);
        edtemail=(EditText)findViewById(R.id.edtemail);
        btnsubmit=(Button)findViewById(R.id.btnsubmit);
        btnclose.setOnClickListener(this);
        btnsubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnclose:
                ForgetPassword.this.finish();
                break;
            case R.id.btnsubmit:
                Email=edtemail.getText().toString();
                if(Email.matches(emailPattern))
                {
                    new ForgotProgress().execute();
                }
                else
                {
                    edtemail.requestFocus();
                    edtemail.setText("");
                    edtemail.setError("Please fill valid email");
                }
                break;
        }
    }
    public class ForgotProgress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(ForgetPassword.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//checkuserforgotpwd=1,user_login=emailid
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/login.php?checkuserforgotpwd=1&user_login=" + URLEncoder.encode(Email));
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    message = c.getString("message");
                    // Storing each json item in variable
                    //					UserId =Integer.parseInt( c.getString("login_user_id"));
                    //					// Log.e("PickUpLocation",PickUpLocation);
                    //					msg = c.getString("message");
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

            AlertDialog alertDialog = new AlertDialog.Builder(ForgetPassword.this, AlertDialog.THEME_HOLO_LIGHT).create();
            alertDialog.setTitle(message);

            alertDialog.setIcon(R.mipmap.launchicon);
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ForgetPassword.this.finish();
                }
            });
            alertDialog.show();
        }
    }
}
