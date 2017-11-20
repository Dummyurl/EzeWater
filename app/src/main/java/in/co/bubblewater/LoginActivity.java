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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtemail, edtpass;
    Button btnlogin,btnsend, btncancel;
    TextView txtforgot, txtsignup,error;
    String Email, Password;
    JSONArray responce = null;
    String userId,message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    LinearLayout rlsigninpage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txtsignup = (TextView) findViewById(R.id.txtsignup);
        txtforgot = (TextView) findViewById(R.id.txtforgot);
        btnlogin = (Button) findViewById(R.id.btnlogin);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtpass = (EditText) findViewById(R.id.edtpass);
        rlsigninpage=(LinearLayout)findViewById(R.id.rlsigninpage);
        btnlogin.setOnClickListener(this);
        txtsignup.setOnClickListener(this);
        txtforgot.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()) {

            case R.id.btnlogin:
                Email = edtemail.getText().toString();
                Password = edtpass.getText().toString();
                check();
                break;
            case R.id.txtsignup:
                 in=new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(in);
                break;
            case R.id.txtforgot:
                in=new Intent(LoginActivity.this,ForgetPassword.class);
                startActivity(in);
//                rlsigninpage.setBackgroundResource(R.color.brown);
//                LayoutInflater inflaterr = getLayoutInflater();
//                View popupVieww = inflaterr.inflate(R.layout.popup, null);
//                final PopupWindow popupWindoww = new PopupWindow(popupVieww, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                btncancel = (Button) popupVieww.findViewById(R.id.btncancel);
//                btnsend = (Button) popupVieww.findViewById(R.id.btnsend);
//                edtemail = (EditText) popupVieww.findViewById(R.id.edtemail);
//                NumberKeyListener emailinput = new NumberKeyListener() {
//
//                    public int getInputType() {
//                        return InputType.TYPE_MASK_VARIATION;
//                    }
//
//                    @Override
//                    protected char[] getAcceptedChars() {
//                        return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
//                                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '@', '.', '_'};
//                    }
//                };
//                edtemail.setKeyListener(emailinput);
//                error = (TextView) popupVieww.findViewById(R.id.txterror);
//                btncancel.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//
//                        //					rltop.setAlpha(1F);
//                        rlsigninpage.setBackgroundResource(R.color.colorPrimary);
//                        popupWindoww.dismiss();
//                    }
//                });
//                btnsend.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        // TODO Auto-generated method stub
//                        Email = edtemail.getText().toString();
//                        if (Email.trim().length() < 1) {
//                            //						edtemail.requestFocus();
//                            //						edtemail.setText("");
//                            //						edtemail.setError("Enter Email Address");
//                            error.setText("Please fill email address");
//                            error.setTextColor(Color.RED);
//                            error.setVisibility(View.VISIBLE);
//                        } else {
//
//                            //						popupWindow.dismiss();
//                            new ForgotProgress().execute();
//                            rlsigninpage.setAlpha(1F);
//                            rlsigninpage.setBackgroundResource(R.color.colorPrimary);
//                            popupWindoww.dismiss();
//
//                        }
//                        //					rltop.setAlpha(1F);
//                        //					rltop.setBackgroundResource(R.color.white);
//                        //					popupWindoww.dismiss();
//                    }
//                });
//                popupWindoww.showAtLocation(popupVieww, ViewGroup.LayoutParams.WRAP_CONTENT, 10, 200);
//                popupWindoww.setFocusable(true);
//                popupWindoww.update();
                break;
        }
    }
    public void check() {
        if (Email.matches(emailPattern)) {
            if (!Password.equalsIgnoreCase("")) {
                new Submit().execute();
            } else {
                edtpass.requestFocus();
                edtpass.setText("");
                edtpass.setError("Please fill password");
            }
        } else {
            edtemail.requestFocus();
            edtemail.setText("");
            edtemail.setError("Please fill valid email");
        }
    }

    public class Submit extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(LoginActivity.this);
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JSONParser jParser = new JSONParser();
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/login.php?checkuserlogin=1&user_login=" + URLEncoder.encode(Email) + "&user_pass=" + Password);
            try {
                // Getting Array of Employee
                responce = json.getJSONArray("success");
                int length = responce.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = responce.getJSONObject(i);
                    userId = c.getString("login_user_id");
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
                Toast.makeText(LoginActivity.this, "Warning: Invalid Email and/ or Password.", Toast.LENGTH_LONG).show();
            } else {

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("LoginId", Integer.parseInt(userId));
                editor.commit();
                Intent in = new Intent(LoginActivity.this, MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
            }
        }
    }
}
