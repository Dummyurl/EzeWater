package in.co.bubblewater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ProductDetails extends Fragment implements View.OnClickListener {
    Button btnaddtocart;
    ImageView btnincrease, btndecrease;
    TextView txtname, txtregular_price, txtsale_price, txtquantity, txttotalcost;
    ImageView productimg;
    Bitmap myBitmap;
    int LoginId;
    JSONArray cartinfo = null;
    String P_id, P_image, sale_price;
    int quantity = 1;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    RelativeLayout rltop;
    ProgressBar circular_progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.product_detail, container, false);
        btnaddtocart = (Button) view.findViewById(R.id.btnaddtocart);
        btnincrease = (ImageView) view.findViewById(R.id.imgincrease);
        btndecrease = (ImageView) view.findViewById(R.id.imgdecrease);
        txtname = (TextView) view.findViewById(R.id.txtname);
        txtregular_price = (TextView) view.findViewById(R.id.txtregular_price);
        txtsale_price = (TextView) view.findViewById(R.id.txtsale_price);
        txtquantity = (TextView) view.findViewById(R.id.txtquantity);
        txttotalcost = (TextView) view.findViewById(R.id.txttotalcost);
        productimg = (ImageView) view.findViewById(R.id.productimg);
        rltop=(RelativeLayout)view.findViewById(R.id.rltop);
        circular_progress=(ProgressBar)view.findViewById(R.id.circular_progress);
        Bundle b = this.getArguments();
        sale_price = b.getString("s_price");
        txtname.setText(b.getString("name"));
        txtregular_price.setText("\u20B9" +b.getString("r_price"));
        txtregular_price.setPaintFlags(txtregular_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        txtsale_price.setText("Price : \u20B9" + b.getString("s_price"));
        txtquantity.setText("" + quantity);
        txttotalcost.setText("Water Cost : \u20B9" + quantity * Double.parseDouble(sale_price));

        P_id = b.getString("id");
        P_image = b.getString("image");
        new ImageLoad().execute();

        btnaddtocart.setOnClickListener(this);
        btnincrease.setOnClickListener(this);
        btndecrease.setOnClickListener(this);
        rltop.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnaddtocart:
                sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                LoginId = sharedpreferences.getInt("LoginId", 0);
                quantity=Integer.parseInt(txtquantity.getText().toString());
//                Toast.makeText(getActivity(),String.valueOf(quantity),Toast.LENGTH_LONG).show();
                new AddtoCartProgressBar().execute();
                break;
            case R.id.imgincrease:
                quantity = quantity + 1;
                txtquantity.setText("" + quantity);

//                String.format("%.6f", quantity * Double.parseDouble(sale_price))
                txttotalcost.setText("Water Cost : \u20B9" + String.format("%.2f", quantity * Double.parseDouble(sale_price)));
                break;
            case R.id.imgdecrease:
                if (quantity > 1) {
                    quantity = quantity - 1;
                    txtquantity.setText("" + quantity);
                    txttotalcost.setText("Water Cost : \u20B9" + String.format("%.2f", quantity * Double.parseDouble(sale_price)));
                } else {
                    Toast.makeText(getActivity(), "At least one items compulsory", Toast.LENGTH_LONG).show();
                }

                break;
        }
    }

    public class ImageLoad extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... parms) {
            try {
                if (P_image.equalsIgnoreCase("")) {

                } else {
                    InputStream input = null;
                    URL url = new URL(P_image);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.setInstanceFollowRedirects(false);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    input = connection.getInputStream();

                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    // opts.inJustDecodeBounds = true;
                    opts.inSampleSize = 1;
                    myBitmap = BitmapFactory.decodeStream(input, null, opts);
                    connection.disconnect();

                }
            } catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            productimg.setImageBitmap(myBitmap);
        }
    }

    public class AddtoCartProgressBar extends AsyncTask<String, String, String> {


        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            pdia = new ProgressDialog(getActivity());
//            pdia.setMessage("Please wait...");
//            pdia.show();
//            pdia.setCancelable(false);
            circular_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            JSONParser jParser = new JSONParser();
            // getting JSON string from URL//checkuserforgotpwd=1,user_login=emailid

//            JSONObject[] json = new JSONObject[2];
//            json[0] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/addtocart.php?user_id=" + LoginId + "&product_id=" + P_id + "&qunt=" + quantity + "&device_id=" + Splash.device_id);
//            json[1] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=" + Splash.device_id);
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/addtocart.php?user_id=" + LoginId + "&product_id=" + P_id + "&qunt=" + String.valueOf(quantity) + "&device_id=" + Splash.device_id);



//			JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/addtocart.php?user_id="+LoginId+"&product_id="+Product.Product_Id+"&qunt=1");
            try {
                //http://www.puyangan.com/api/category.php
                // Getting Array of Employee
                cartinfo = json.getJSONArray("cartinfo");
                int lengths = cartinfo.length();

                for (int j = 0; j < lengths; j++) {
                    JSONObject cc = cartinfo.getJSONObject(j);
                    MainActivity.cart_item= cc.getString("totalitems");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            circular_progress.setVisibility(View.GONE);
//            pdia.dismiss();
//            pdia = null;
            MainActivity.txt_cart_item.setText(MainActivity.cart_item);
        }
    }

}