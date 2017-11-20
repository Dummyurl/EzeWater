package in.co.bubblewater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;


public class Profile extends Fragment implements OnClickListener {
    Button btnbilling, btnupdate;

    String Fname, Lname, Email, Phone, Password, Cpassword, Address, State, Zcode, Country;
    EditText fname, lname, email, phone, password, cpassword, address, state, zcode, country;
    //    Switch subscriber;
    TextView confirmerror;
    //    String emailPattern = "[a-zA-Z0-9.]+@[a-z]+\\.+[a-z]+";
    ArrayAdapter<String> dataAdapter;
    String value;
    JSONArray userinfo = null;
    String message;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String page;
    Fragment fragment;
    android.support.v4.app.FragmentTransaction ft;
    RelativeLayout idsignup;
    int LoginId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.profile, container, false);
        //img=(ImageView)view.findViewById(R.id.banner);

        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean("Signinpage", true);

        editor.commit();
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LoginId = sharedpreferences.getInt("LoginId", 0);
        Email = sharedpreferences.getString("email", "");
        btnbilling = (Button) view.findViewById(R.id.btnbilling);
        btnupdate = (Button) view.findViewById(R.id.btnupdate);

        fname = (EditText) view.findViewById(R.id.edtfname);
        lname = (EditText) view.findViewById(R.id.edtlname);
        email = (EditText) view.findViewById(R.id.edtemail);
        phone = (EditText) view.findViewById(R.id.edtphone);
        password = (EditText) view.findViewById(R.id.edtpassword);
        cpassword = (EditText) view.findViewById(R.id.edtconfermpassword);
        address = (EditText) view.findViewById(R.id.edtaddress);
        state = (EditText) view.findViewById(R.id.edtstate);
        zcode = (EditText) view.findViewById(R.id.edtzipcode);
        country = (EditText) view.findViewById(R.id.edtcountry);
        confirmerror = (TextView) view.findViewById(R.id.confirmerror);
        email.setText(Email);
        btnupdate.setOnClickListener(this);
        btnbilling.setOnClickListener(this);
        email.setLongClickable(false);
        fname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                fname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        lname.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                lname.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                email.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                phone.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                password.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        cpassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                cpassword.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Password = password.getText().toString();
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Password = password.getText().toString();
                if (Password.contains(s)) {
                    confirmerror.setVisibility(View.GONE);
                } else {
                    confirmerror.setVisibility(View.VISIBLE);
                }


            }
        });
        address.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                address.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        state.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                state.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        zcode.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                zcode.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        country.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                country.setError(null);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        new Progress().execute();

        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        switch (v.getId()) {
            case R.id.btnbilling:

                fragment = new Setting();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;

                break;
            case R.id.btnupdate:
                Fname = fname.getText().toString();
                Lname = lname.getText().toString();
                Email = email.getText().toString();
                Phone = phone.getText().toString();
                Password = password.getText().toString();
                Cpassword = cpassword.getText().toString();
                Address = address.getText().toString();
                State = state.getText().toString();
                Zcode = zcode.getText().toString();
                Country = country.getText().toString();
                if (Fname.equalsIgnoreCase("")) {
                    fname.requestFocus();
                    fname.setText("");
                    fname.setError("Please fill first name");
                } else {
                    if (Lname.equalsIgnoreCase("")) {
                        lname.requestFocus();
                        lname.setText("");
                        lname.setError("Please fill last name");
                    } else
                    {
                        if(Phone.equalsIgnoreCase("")) {
                            phone.requestFocus();
                            phone.setText("");
                            phone.setError("Please fill number");

                        }
                        else
                        {
                            if(Password.equalsIgnoreCase(""))
                            {
                                password.requestFocus();
                                password.setText("");
                                password.setError("Please fill password");
                            }
                            else
                            {
                                if(Cpassword.equalsIgnoreCase(""))
                                {
                                    cpassword.requestFocus();
                                    cpassword.setText("");
                                    cpassword.setError("Please fill confirm password");
                                }
                                else
                                {
                                    if(Password.equals(Cpassword))
                                    {
                                        if(Address.equalsIgnoreCase(""))
                                        {
                                            address.requestFocus();
                                            address.setText("");
                                            address.setError("Please fill address");
                                        }
                                        else
                                        {
                                            if(State.equalsIgnoreCase(""))
                                            {
                                                state.requestFocus();
                                                state.setText("");
                                                state.setError("Please fill state");
                                            }
                                            else
                                            {
                                                if(Zcode.equalsIgnoreCase(""))
                                                {
                                                    zcode.requestFocus();
                                                    zcode.setText("");
                                                    zcode.setError("Please fill zip code");
                                                }
                                                else
                                                {
                                                    if(Country.equalsIgnoreCase(""))
                                                    {
                                                        country.requestFocus();
                                                        country.setText("");
                                                        country.setError("Please fill country");
                                                    }
                                                    else
                                                    {
                                                        new Update().execute();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    else
                                    {
                                        cpassword.requestFocus();
                                        cpassword.setText("");
                                        cpassword.setError("Password do not match.");
                                    }
                                }
                            }
                        }

                    }

                }
                break;
        }
    }

    public class Update extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/myaccount.php?fname=" + URLEncoder.encode(Fname) + "&lname=" + URLEncoder.encode(Lname) + "&email=" + URLEncoder.encode(Email)+"&phone=" + URLEncoder.encode(Phone) + "&pwd=" + Password + "&pwd2=" + Cpassword + "&address=" + URLEncoder.encode(Address) + "&state=" + URLEncoder.encode(State) + "&zipcode=" + Zcode + "&country=" + URLEncoder.encode(Country) + "&updateaccount=1&user_id=" + LoginId);
            try {
                // Getting Array of Employee
                userinfo = json.getJSONArray("userinfo");
                int length = userinfo.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = userinfo.getJSONObject(i);

                    Fname = c.getString("fname");
                    Lname = c.getString("lname");
                    Email = c.getString("email");
                    Address = c.getString("address");
                    State = c.getString("state");
                    Zcode = c.getString("zipcode");
                    Country = c.getString("country");
//                    Month=c.getString()


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

            fname.setText(Fname);
            lname.setText(Lname);
            email.setText(Email);
            address.setText(Address);
            state.setText(State);
            zcode.setText(Zcode);
            country.setText(Country);
        }
    }

    public class Progress extends AsyncTask<String, String, String> {
        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Loading...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//fname,lname,email,pwd,pwd2,zipcode,month,day,subscriber
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/checkout.php?user_id=" + LoginId + "&device_id=" + Splash.device_id);
            try {
                // Getting Array of Employee
                userinfo = json.getJSONArray("userinfo");
                int length = userinfo.length();
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = userinfo.getJSONObject(i);

                    Fname = c.getString("fname");
                    Lname = c.getString("lname");
                    Email = c.getString("email");
                    Phone=c.getString("phone");
                    Address = c.getString("address");
                    State = c.getString("state");
                    Zcode = c.getString("zipcode");
                    Country = c.getString("country");
//                    Month=c.getString()


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

            fname.setText(Fname);
            lname.setText(Lname);
            email.setText(Email);
            phone.setText(Phone);
            address.setText(Address);
            state.setText(State);
            zcode.setText(Zcode);
            country.setText(Country);
        }
    }
}
