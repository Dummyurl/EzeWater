package in.co.bubblewater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
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
import java.util.ArrayList;


public class Cart extends Fragment implements View.OnClickListener {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences.Editor editor;
    ListView cartlist;
    JSONArray productcart = null;
    JSONArray availability = null;
    ArrayList<Holder> arraylist = new ArrayList<Holder>();
    String Product_id, Product_name, Product_price, Product_quantity, Product_image;
    Bitmap myBitmap, bitmap;
    int length, LoginId, Order_Count;
    Double Product_totalprice, Totalprice = 0.00;
    TextView txttotalprice, txtavailable;
    Button btnupdate, btncheckout, btncheck;
    ArrayList<String> UrlarrayforUpdate = new ArrayList<String>();
    String productquantity, productid;
    String UrlarrayupdateString, Url, Zipcode;
    View view;
    Fragment fragment;
    FragmentTransaction ft;
    RelativeLayout idcart, emptytext, rlfirst,rltop;
    Button btnshop;
    public static Double Total_Price = 0.0;
    JSONArray cartinfo = null;
    EditText edtzip;
    Boolean status;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.cart, container, false);
        txttotalprice = (TextView) view.findViewById(R.id.txttotalprice);
        btnupdate = (Button) view.findViewById(R.id.btnupdatecart);
        btncheckout = (Button) view.findViewById(R.id.btncheckout);
        btnshop = (Button) view.findViewById(R.id.btnshop);
        cartlist = (ListView) view.findViewById(R.id.cartlist);
        idcart = (RelativeLayout) view.findViewById(R.id.rl1);
        rlfirst = (RelativeLayout) view.findViewById(R.id.rlfirst);
        rltop = (RelativeLayout) view.findViewById(R.id.rltop);
        emptytext = (RelativeLayout) view.findViewById(R.id.txtempty);
        btncheck = (Button) view.findViewById(R.id.btncheck);
        edtzip = (EditText) view.findViewById(R.id.edtzip);
        txtavailable = (TextView) view.findViewById(R.id.txtavailability);
        rlfirst.setVisibility(View.GONE);
        rltop.setVisibility(View.GONE);
//		idcart.setVisibility(View.GONE);
        //img=(ImageView)view.findViewById(R.id.banner);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LoginId = sharedpreferences.getInt("LoginId", 0);
        editor = sharedpreferences.edit();
        btnupdate.setOnClickListener(this);
        btncheckout.setOnClickListener(this);
        btncheck.setOnClickListener(this);
        btnshop.setOnClickListener(this);
        btncheckout.setEnabled(false);
        btncheckout.setBackgroundColor(getResources().getColor(R.color.btndeactiveColor));

        edtzip.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // put the code of save Database here
                edtzip.setError(null);
                if(s.length()<6)
                {
                    btncheckout.setEnabled(false);
                    btncheckout.setBackgroundColor(getResources().getColor(R.color.btndeactiveColor));
                    txtavailable.setText("");
                    txtavailable.setTextColor(Color.RED);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        ConnectionDetector cd = new ConnectionDetector(getActivity());
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getActivity(), "Please connect to working internet connection", Toast.LENGTH_LONG).show();

        } else {
            new CartProgressBar().execute();
        }

        return view;
    }

    public class CartProgressBar extends AsyncTask<String, String, String> {

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

            arraylist.clear();
            Totalprice = 0.0;
            Product_totalprice = 0.0;
            JSONParser jParser = new JSONParser();
            //http://www.puyangan.com/api/cart.php?user_id=67
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=" + Splash.device_id);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                productcart = json.getJSONArray("productcart");
                length = productcart.length();
                Order_Count = length;
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = productcart.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;

                    Product_id = c.getString("product_id");
                    Product_name = c.getString("product_name");
                    Product_price = c.getString("product_price");
                    Product_quantity = c.getString("product_quantity");
                    Product_image = c.getString("product_image");
                    Holder h = new Holder();
                    if (Product_image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Product_image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 4;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setProduct_id(Product_id);
                    h.setProduct_name(Product_name);
                    h.setProduct_price(Product_price);
                    h.setProduct_quantity(Product_quantity);
                    h.setProduct_image(Product_image);
                    Product_totalprice = Double.parseDouble(Product_quantity) * Double.parseDouble(Product_price);
                    h.setProduct_total_price(Product_totalprice);
                    arraylist.add(h);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            pdia.dismiss();
            pdia = null;
            cartlist.setAdapter(new MyCustomAdapter(getActivity(), arraylist));
            ListUtils.setDynamicHeight(cartlist);
            if (length == 0) {
                idcart.setVisibility(View.GONE);
                emptytext.setVisibility(View.VISIBLE);
                rlfirst.setVisibility(View.GONE);
                rltop.setVisibility(View.GONE);
            } else {
                idcart.setVisibility(View.VISIBLE);
                emptytext.setVisibility(View.GONE);
                rlfirst.setVisibility(View.VISIBLE);
                rltop.setVisibility(View.VISIBLE);
            }

            //			txttotalprice.setText("$"+Total_Price);
            if (Totalprice == 0.0) {
//                txttotalprice.setText("Total $"+Totalprice);
//                Total_Price=Totalprice;
                txttotalprice.setText("Total \u20B9" + String.format("%.2f", Totalprice));
                Total_Price = Double.parseDouble(String.format("%.2f", Totalprice));
            } else {
//                if(Totalprice.toString().substring(Totalprice.toString().indexOf("."),Totalprice.toString().length()).length()>2)
//                {
//                    txttotalprice.setText("Total $" + Totalprice.toString().substring(0, Totalprice.toString().indexOf(".")+3));
//                    Total_Price=Double.parseDouble(Totalprice.toString().substring(0, Totalprice.toString().indexOf(".")+3));
//                }
//                else
//                {
//                    txttotalprice.setText("Total $" + Totalprice.toString().substring(0, Totalprice.toString().indexOf(".")+2));
//                    Total_Price=Double.parseDouble(Totalprice.toString().substring(0, Totalprice.toString().indexOf(".")+2));
//                }
                txttotalprice.setText("Total \u20B9" + String.format("%.2f", Totalprice));
                Total_Price = Double.parseDouble(String.format("%.2f", Totalprice));

            }
//			idcart.setVisibility(View.VISIBLE);
        }

    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView orderlist) {
            ListAdapter mListAdapter = orderlist.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(orderlist.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, orderlist);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = orderlist.getLayoutParams();
            params.height = height;//+ (orderlist.getHeight() * (mListAdapter.getCount() - 1));
            orderlist.setLayoutParams(params);
            orderlist.requestLayout();
        }
    }

    class MyCustomAdapter extends BaseAdapter {

        LayoutInflater inflater;
        ArrayList<Holder> list;

        public MyCustomAdapter(FragmentActivity fragmentActivity, ArrayList<Holder> list) {
            inflater = LayoutInflater.from(fragmentActivity);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int paramInt) {
            return paramInt;
        }

        class ViewHolder {
            TextView productname, total, price;
            ImageView productImage;
            EditText quan_value;
            Button btndelete, btnincrease, btndecrease;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            final ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.cartitem, paramViewGroup, false);
                holder = new ViewHolder();

                holder.productname = (TextView) paramView.findViewById(R.id.txtproductname);
                holder.total = (TextView) paramView.findViewById(R.id.txttotal);
                holder.btnincrease = (Button) paramView.findViewById(R.id.btnincrease);
                holder.btndecrease = (Button) paramView.findViewById(R.id.btndecrease);
                holder.price = (TextView) paramView.findViewById(R.id.txtprice);
                holder.btndelete = (Button) paramView.findViewById(R.id.btndelete);
                holder.quan_value = (EditText) paramView.findViewById(R.id.edtquantity);
                holder.productImage = (ImageView) paramView.findViewById(R.id.img);

                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            final Holder h = list.get(paramInt);
            String pname = h.getProduct_name();
            holder.productname.setText(pname);

            String pprice = h.getProduct_price();
            holder.price.setText("\u20B9" + String.format("%.2f", Double.parseDouble(pprice)));

            String pquantity = h.getProduct_quantity();
            holder.quan_value.setText(pquantity);

            Double total = h.getProduct_total_price();
            holder.total.setText("\u20B9" + String.format("%.2f", total));

            Totalprice = Totalprice + total;

            holder.btnincrease.setText(">");
            holder.btndecrease.setText("<");
            bitmap = h.getBitmap();

            if (h.getProduct_image().equalsIgnoreCase("")) {
//                holder.productImage.setBackgroundResource(R.drawable.header);
            } else {
                //				holder.productImage.setImageBitmap(getCircleBitmap(bitmap));
                holder.productImage.setImageBitmap(bitmap);
            }
            holder.quan_value.setTag(paramInt);
            holder.quan_value.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    System.out.println("ONtext changed " + new String(s.toString()));
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub
                    System.out.println("beforeTextChanged " + new String(s.toString()));
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                    System.out.println("afterTextChanged " + new String(s.toString()));
                    h.setProduct_quantity(new String(s.toString()));
                }
            });
            holder.btndelete.setTag(paramInt);
            holder.btndelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    int pos1 = (Integer) v.getTag();
                    Holder h1 = (Holder) list.get(pos1);

                    Product_id = h1.getProduct_id();
                    new DeleteProgress().execute(Product_id);
                }
            });

            holder.btnincrease.setTag(paramInt);
            holder.btnincrease.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    String a = holder.quan_value.getText().toString();

                    holder.quan_value.setText(String.valueOf(Integer.parseInt(a) + 1));

                    h.setProduct_quantity("" + (Integer.parseInt(a) + 1));
                }
            });


            holder.btndecrease.setTag(paramInt);
            holder.btndecrease.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String a = holder.quan_value.getText().toString();

                    holder.quan_value.setText(String.valueOf(Integer.parseInt(a) - 1));

                    h.setProduct_quantity("" + (Integer.parseInt(a) - 1));
                }
            });


            return paramView;
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnshop:
                fragment = new Product();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.frame, fragment);
                ft.replace(R.id.frame, fragment);
//                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                editor.putString("page", "Products");
                editor.commit();
//                MainActivity.add++;
//				MainActivity.navigationView.getMenu().getItem(3).setChecked(true);
                MainActivity.mSelectedItem = 1;
                MainActivity.mMenuAdapter.notifyDataSetChanged();
                break;
            case R.id.btnupdatecart:
                //http://www.puyangan.com/api/updatecart.php?user_id=67&product_id[]=703&p_qunty[]=5
                UrlarrayforUpdate.clear();
                for (int j = 0; j < length; j++) {

                    final Holder h = arraylist.get(j);
                    productquantity = h.getProduct_quantity();
                    productid = h.getProduct_id();

                    UrlarrayforUpdate.add("product_id[]=" + productid + "&p_qunty[]=" + productquantity);

                }
                arraylist.clear();
                //			ListUtils.setDynamicHeight(cartlist);

                new UpdateProgress().execute();


                break;
            case R.id.btncheckout:
                fragment = new CheckOut();
                Bundle b=new Bundle();
                b.putString("zipcode",edtzip.getText().toString());
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                fragment.setArguments(b);
                ft.add(R.id.frame, fragment);
                ft.addToBackStack("add" + MainActivity.add);
                ft.commit();
                MainActivity.add++;
                break;
            case R.id.btncheck:
                Zipcode = edtzip.getText().toString().trim();
                if (Zipcode.length() < 6) {
                    edtzip.requestFocus();
                    edtzip.setText("");
                    edtzip.setError("fill correct zipcode");
                } else {
                    new CheckAvailibality().execute();
                }
                break;
        }
    }

    public class CheckAvailibality extends AsyncTask<String, String, String> {
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
            //http://www.puyangan.com/api/cart.php?user_id=67//http://www.puyangan.com/api/checkout.php?user_id=67
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/validatezip.php?zipcode=" + Zipcode);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                availability = json.getJSONArray("result");
                for (int i = 0; i < availability.length(); i++) {
                    JSONObject c = availability.getJSONObject(i);
                    status = c.getBoolean("status");


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
            if (status) {
                btncheckout.setEnabled(true);
                btncheckout.setBackgroundColor(getResources().getColor(R.color.btnactiveColor));
                txtavailable.setText("Service available");
                txtavailable.setTextColor(getResources().getColor(R.color.darkgreen));
            } else {
                btncheckout.setEnabled(false);
                btncheckout.setBackgroundColor(getResources().getColor(R.color.btndeactiveColor));
                txtavailable.setText("Service not available in this area.");
                txtavailable.setTextColor(Color.RED);
            }
        }
    }

    public class UpdateProgress extends AsyncTask<String, String, String> {
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

            arraylist.clear();
            Totalprice = 0.0;
            Product_totalprice = 0.0;
            String a = UrlarrayforUpdate.toString().substring(1, UrlarrayforUpdate.toString().length() - 1).trim();
            UrlarrayupdateString = a.replace(",", "&").trim();
            UrlarrayupdateString = UrlarrayupdateString.replace(" ", "");

            Url = "/updatecart.php?user_id=52&" + UrlarrayupdateString;
            Log.e("updateurl", UrlarrayupdateString);
            JSONParser jParser = new JSONParser();
            //http://www.puyangan.com/api/cart.php?user_id=67
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/updatecart.php?user_id=" + LoginId + "&" + UrlarrayupdateString + "&device_id=" + Splash.device_id);
            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                productcart = json.getJSONArray("productcart");
                length = productcart.length();
                Order_Count = length;
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = productcart.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;

                    Product_id = c.getString("product_id");
                    Product_name = c.getString("product_name");
                    Product_price = c.getString("product_price");
                    Product_quantity = c.getString("product_quantity");
                    Product_image = c.getString("product_image");
                    Holder h = new Holder();
                    if (Product_image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Product_image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 4;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setProduct_id(Product_id);
                    h.setProduct_name(Product_name);
                    h.setProduct_price(Product_price);
                    h.setProduct_quantity(Product_quantity);
                    h.setProduct_image(Product_image);
                    Product_totalprice = Double.parseDouble(Product_quantity) * Double.parseDouble(Product_price);
                    h.setProduct_total_price(Product_totalprice);
                    arraylist.add(h);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            try {
                //http://www.puyangan.com/api/category.php
                // Getting Array of Employee
                cartinfo = json.getJSONArray("cartinfo");
                int lengths = cartinfo.length();

                for (int j = 0; j < lengths; j++) {
                    JSONObject cc = cartinfo.getJSONObject(j);
                    MainActivity.cart_item = cc.getString("totalitems");

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
            pdia.dismiss();
            pdia = null;
            MainActivity.txt_cart_item.setText(MainActivity.cart_item);
            cartlist.setAdapter(new MyCustomAdapter(getActivity(), arraylist));
            ListUtils.setDynamicHeight(cartlist);
            //			txttotalprice.setText("$"+Total_Price);
            txttotalprice.setText("$" + Totalprice);
            //			new OrderProgressBar().execute();
            if (Totalprice == 0.0) {
//                txttotalprice.setText("Total $"+Totalprice);
//                Total_Price=Totalprice;
                txttotalprice.setText("Total \u20B9" + String.format("%.2f", Totalprice));
                Total_Price = Double.parseDouble(String.format("%.2f", Totalprice));
            } else {
//                if (Totalprice.toString().substring(Totalprice.toString().indexOf("."), Totalprice.toString().length()).length() > 2) {
//                    txttotalprice.setText("Total $" + Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 3));
//
//                    Total_Price = Double.parseDouble(Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 3));
//                } else {
//                    txttotalprice.setText("Total $" + Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 2));
//                    Total_Price = Double.parseDouble(Totalprice.toString().substring(0, Totalprice.toString().indexOf(".") + 2));
//                }
                txttotalprice.setText("Total \u20B9" + String.format("%.2f", Totalprice));
                Total_Price = Double.parseDouble(String.format("%.2f", Totalprice));
            }

        }


    }

    public class DeleteProgress extends AsyncTask<String, String, String> {

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
            arraylist.clear();
            Totalprice = 0.0;
            Product_totalprice = 0.0;
            JSONParser jParser = new JSONParser();
            //http://www.puyangan.com/api/deletecart.php?user_id=29&product_id[]=703
//            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/deletecart.php?user_id="+LoginId+"&product_id[]="+Product_id+"&device_id="+Splash.device_id);
            JSONObject[] json = new JSONObject[2];
            json[0] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/deletecart.php?user_id=" + LoginId + "&product_id[]=" + Product_id + "&device_id=" + Splash.device_id);
            json[1] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=" + Splash.device_id);

            try {
                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
                productcart = json[0].getJSONArray("productcart");
                length = productcart.length();
                Order_Count = length;
                // looping of List
                for (int i = 0; i < length; i++) {
                    JSONObject c = productcart.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;

                    Product_id = c.getString("product_id");
                    Product_name = c.getString("product_name");
                    Product_price = c.getString("product_price");
                    Product_quantity = c.getString("product_quantity");
                    Product_image = c.getString("product_image");
                    Holder h = new Holder();
                    if (Product_image.equalsIgnoreCase("")) {

                    } else {

                        URL url = new URL(Product_image);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.setInstanceFollowRedirects(false);
                        connection.setRequestMethod("GET");
                        connection.connect();
                        input = connection.getInputStream();

                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        // opts.inJustDecodeBounds = true;
                        opts.inSampleSize = 4;
                        myBitmap = BitmapFactory.decodeStream(input, null, opts);
                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
                        //myBitmap.recycle();

                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        h.setBitmap(myBitmap);
                        connection.disconnect();
                    }
                    h.setProduct_id(Product_id);
                    h.setProduct_name(Product_name);
                    h.setProduct_price(Product_price);
                    h.setProduct_quantity(Product_quantity);
                    h.setProduct_image(Product_image);
                    Product_totalprice = Double.parseDouble(Product_quantity) * Double.parseDouble(Product_price);
                    h.setProduct_total_price(Product_totalprice);
                    arraylist.add(h);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {

            }
            try {
                cartinfo = json[1].getJSONArray("cartinfo");
                int lengths = cartinfo.length();

                for (int j = 0; j < lengths; j++) {
                    JSONObject cc = cartinfo.getJSONObject(j);
                    MainActivity.cart_item = cc.getString("totalitems");

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
            pdia.dismiss();
            pdia = null;
            MainActivity.txt_cart_item.setText(MainActivity.cart_item);
            cartlist.setAdapter(new MyCustomAdapter(getActivity(), arraylist));
            ListUtils.setDynamicHeight(cartlist);
            txttotalprice.setText("Total \u20B9" + String.format("%.2f", Totalprice));
            if (length == 0) {
                idcart.setVisibility(View.GONE);
                emptytext.setVisibility(View.VISIBLE);
                rlfirst.setVisibility(View.GONE);
                rltop.setVisibility(View.GONE);
            } else {
                idcart.setVisibility(View.VISIBLE);
                emptytext.setVisibility(View.GONE);
                rlfirst.setVisibility(View.VISIBLE);
                rltop.setVisibility(View.VISIBLE);
            }
        }
    }
}
