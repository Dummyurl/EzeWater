package in.co.bubblewater;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Product extends Fragment {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    GridView gridview;
    JSONArray productslist = null;
    JSONArray Totalproduct = null;
    int totalproductlength, addtolist;
    String totalproduct;
    String ProductId, Name, Image, Regular_price, Sale_price, Price;
    String P_Id, P_name, P_regular_price, P_sale_price, P_image;
    //	public static String Product_Id;
    ArrayList<Holder> list = new ArrayList<Holder>();
    int LoginId;
    //	public static String str_product;
    JSONArray cartinfo = null;
    int pageno = 1;
    Fragment fragment;
    FragmentTransaction ft;
    RelativeLayout idall;
    TextView emptylist;
    int p_length;
    boolean flag_loading = false;
    SwipyRefreshLayout mSwipyRefreshLayout;
    private ProgressDialog pDialog;
    ProgressBar circular_progress;
    String URL = "http://vantagewebtech.com/puyangan/api/products.php?show=all";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.home, container, false);
        //img=(ImageView)view.findViewById(R.id.banner);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LoginId = sharedpreferences.getInt("LoginId", 0);
        gridview = (GridView) view.findViewById(R.id.productlist);
        mSwipyRefreshLayout = (SwipyRefreshLayout) view.findViewById(R.id.swipyrefreshlayout);
        emptylist = (TextView) view.findViewById(R.id.emptylist);
        emptylist.setVisibility(View.GONE);
        circular_progress = (ProgressBar) view.findViewById(R.id.circular_progress);
        initpDialog();

//        new ProductProgressBar().execute();
        ProductProgress();
//        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh(SwipyRefreshLayoutDirection direction) {
//                Log.d("MainActivity", "Refresh triggered at "
//                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
//                flag_loading=true;
//                pageno = pageno + 1;
//                if (addtolist == Integer.parseInt(totalproduct)) {
//
//                } else {
//                    mSwipyRefreshLayout.setRefreshing(true);
//                    new ProductProgressBar().execute();
//                }
//
//            }
//        });
        mSwipyRefreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                Log.d("MainActivity", "Refresh triggered at "
                        + (direction == SwipyRefreshLayoutDirection.TOP ? "top" : "bottom"));
                Toast.makeText(getActivity(), "End of the slide", Toast.LENGTH_LONG).show();
                mSwipyRefreshLayout.setRefreshing(false);
            }
        });
        gridview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                    if (flag_loading == false) {
                        flag_loading = true;

                        if (addtolist == Integer.parseInt(totalproduct)) {
                            mSwipyRefreshLayout.setRefreshing(false);
                        } else {
                            mSwipyRefreshLayout.setRefreshing(true);
//                            pageno = pageno + 1;
//                            new ProductProgressBar().execute();
                            ProductProgress();
                        }
                    }

                }
            }
        });
        return view;
    }

    public class ProductProgressBar extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (!flag_loading)
                showpDialog();
//                circular_progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            //			list.clear();

            JSONParser jParser = new JSONParser();
            // http://www.puyangan.com/api/products.php?show=featured
//			JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/products.php?show=all&page="+pageno);

            JSONObject[] json = new JSONObject[2];
            json[0] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/products.php?show=all&page=" + pageno);
            json[1] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=" + Splash.device_id);


            try {
                Totalproduct = json[0].getJSONArray("totalproducts");
                cartinfo = json[1].getJSONArray("cartinfo");
                totalproductlength = Totalproduct.length();
                int lengths = cartinfo.length();

                //http://www.puyangan.com/api/category.php?cid=178
                // Getting Array of Employee
//				Totalproduct = json.getJSONArray("totalproducts");
                totalproductlength = Totalproduct.length();
                for (int i = 0; i < totalproductlength; i++) {
                    JSONObject ob = Totalproduct.getJSONObject(i);
                    totalproduct = ob.getString("total");
                }


                productslist = json[0].getJSONArray("products");
                p_length = productslist.length();
                // looping of List
                for (int i = 0; i < p_length; i++) {

                    addtolist = addtolist + 1;
                    JSONObject c = productslist.getJSONObject(i);
                    //					Bitmap myBitmap = null;
                    InputStream input = null;
                    ProductId = c.getString("id");
                    Name = c.getString("name");
                    Image = c.getString("image");
                    Regular_price = c.getString("regular_price");
                    Sale_price = c.getString("sale_price");
                    Price = c.getString("price");
                    Holder h = new Holder();
//                    if (Image.equalsIgnoreCase("")) {
//
//                    } else {
//
//                        URL url = new URL(Image);
//                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                        connection.setDoInput(true);
//                        connection.setInstanceFollowRedirects(false);
//                        connection.setRequestMethod("GET");
//                        connection.connect();
//                        input = connection.getInputStream();
//
//                        BitmapFactory.Options opts = new BitmapFactory.Options();
//                        // opts.inJustDecodeBounds = true;
//                        opts.inSampleSize = 4;
//                        Bitmap myBitmap = BitmapFactory.decodeStream(input, null, opts);
//                        //						Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, 200, 200, true);
//                        //myBitmap.recycle();
//
//                        //						Bitmap myBitmap = BitmapFactory.decodeStream(input);
//                        h.setBitmap(myBitmap);
//                        connection.disconnect();
//                    }


                    h.setId(ProductId);
                    h.setName(Name);
                    h.setRegular_price(Regular_price);
                    h.setSale_price(Sale_price);
                    h.setPrice(Price);
                    h.setImage(Image);

                    list.add(h);

                }
                for (int j = 0; j < lengths; j++) {
                    JSONObject cc = cartinfo.getJSONObject(j);

                    MainActivity.cart_item = cc.getString("totalitems");

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            catch (IOException e) {
//
//            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            hidepDialog();
//            circular_progress.setVisibility(View.GONE);
            int currentPosition = gridview.getFirstVisiblePosition();
            gridview.setAdapter(new MyCustomAdapter(getActivity(), list));
//			gridview.setSelectionFromTop(currentPosition + 1, 0);
//			gridview.smoothScrollToPosition(currentPosition);
            MainActivity.txt_cart_item.setText(MainActivity.cart_item);
            if (p_length == 0) {
                emptylist.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
            } else {
                emptylist.setVisibility(View.GONE);
                gridview.setVisibility(View.VISIBLE);
            }

            gridview.setSelection(currentPosition);
            mSwipyRefreshLayout.setRefreshing(false);
            flag_loading = false;
        }

    }


    public static class ListUtils {
        public static void setDynamicHeight(GridView gridview) {
            ListAdapter mListAdapter = gridview.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(gridview.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, gridview);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = gridview.getLayoutParams();
            params.height = height + (gridview.getHeight() * (mListAdapter.getCount() - 1));
            gridview.setLayoutParams(params);
            gridview.requestLayout();
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
            TextView name, oldprice, price;
            ImageView productImage, btnAddtocart;
            RelativeLayout rl;
        }

        @Override
        public long getItemId(int paramInt) {
            return paramInt;
        }

        @Override
        public View getView(final int paramInt, View paramView, ViewGroup paramViewGroup) {

            ViewHolder holder;
            if (paramView == null) {
                paramView = inflater.inflate(R.layout.product_list_item, paramViewGroup, false);
                holder = new ViewHolder();

                holder.name = (TextView) paramView.findViewById(R.id.txtname);
                holder.oldprice = (TextView) paramView.findViewById(R.id.txtoldprice);
                holder.price = (TextView) paramView.findViewById(R.id.txtprice);
                holder.btnAddtocart = (ImageView) paramView.findViewById(R.id.addtocart);
                holder.productImage = (ImageView) paramView.findViewById(R.id.product_img);
                holder.rl = (RelativeLayout) paramView.findViewById(R.id.rl);
                paramView.setTag(holder);
            } else {
                holder = (ViewHolder) paramView.getTag();
            }

            Holder h = list.get(paramInt);
            String name = h.getName();
            holder.name.setText(name);
            String price = h.getPrice();
//            holder.oldprice.setText("\u20B9" + (Double.parseDouble(price) + 10));
            holder.oldprice.setText("\u20B9" + h.getRegular_price());
            holder.price.setText("\u20B9" + price);
            //  use ₹ for hindi ruppes   and $ for dollers

            holder.oldprice.setPaintFlags(holder.oldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            Bitmap bitmap = h.getBitmap();
            if (h.getImage().equalsIgnoreCase("")) {
//                holder.productImage.setBackgroundResource(R.drawable.ic_menu_camera);
            } else {
//                holder.productImage.setImageBitmap(bitmap);
                Picasso.with(getActivity())
                        .load(h.getImage())
//                        .placeholder(R.drawable.ic_menu_camera)   // optional
//                        .error(R.drawable.ic_menu_camera)      // optional
                        .resize(200, 200)                        // optional
                        .into(holder.productImage);

            }


            holder.btnAddtocart.setTag(paramInt);
            holder.btnAddtocart.setOnClickListener(new OnClickListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    int pos1 = (Integer) vv.getTag();
                    Holder h1 = (Holder) list.get(pos1);

                    P_Id = h1.getId();
                    sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                    LoginId = sharedpreferences.getInt("LoginId", 0);
                    new AddToCartProgress().execute();
                }
            });
            holder.productImage.setTag(paramInt);
            holder.productImage.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    int pos1 = (Integer) vv.getTag();
                    Holder h1 = (Holder) list.get(pos1);
                    P_Id = h1.getId();
                    P_name = h1.getName();
                    P_image = h1.getImage();
                    P_regular_price = h1.getRegular_price();
                    P_sale_price = h1.getSale_price();

                    Bundle b = new Bundle();
                    b.putString("id", P_Id);
                    b.putString("name", P_name);
                    b.putString("image", P_image);
                    b.putString("r_price", P_regular_price);
                    b.putString("s_price", P_sale_price);
                    fragment = new ProductDetails();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
            });
            holder.rl.setTag(paramInt);
            holder.rl.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View vv) {
                    // TODO Auto-generated method stub
                    int pos1 = (Integer) vv.getTag();
                    Holder h1 = (Holder) list.get(pos1);
                    P_Id = h1.getId();
                    P_name = h1.getName();
                    P_image = h1.getImage();
                    P_regular_price = h1.getRegular_price();
                    P_sale_price = h1.getSale_price();

                    Bundle b = new Bundle();
                    b.putString("id", P_Id);
                    b.putString("name", P_name);
                    b.putString("image", P_image);
                    b.putString("r_price", P_regular_price);
                    b.putString("s_price", P_sale_price);
                    fragment = new ProductDetails();
                    fragment.setArguments(b);
                    ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.frame, fragment);
                    ft.addToBackStack("add" + MainActivity.add);
                    ft.commit();
                    MainActivity.add++;
                }
            });
            return paramView;
        }
    }

    public class AddToCartProgress extends AsyncTask<String, String, String> {


        private ProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdia = new ProgressDialog(getActivity());
            pdia.setMessage("Please wait...");
            pdia.show();
            pdia.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub


            JSONParser jParser = new JSONParser();

//            JSONObject[] json = new JSONObject[2];
//            json[0] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/addtocart.php?user_id=" + LoginId + "&product_id=" + P_Id + "&qunt=1" + "&device_id=" + Splash.device_id);
//            json[1] = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/cart.php?user_id=" + LoginId + "&device_id=" + Splash.device_id);
            JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url) + "/api/addtocart.php?user_id=" + LoginId + "&product_id=" + P_Id + "&qunt=1" + "&device_id=" + Splash.device_id);


            // getting JSON string from URL//checkuserforgotpwd=1,user_login=emailid
            //			JSONObject json = jParser.getJSONFromUrl(getResources().getString(R.string.url)+"/api/addtocart.php?user_id="+LoginId+"&product_id="+Product_Id+"&qunt=1");
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
        }
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setIcon(R.mipmap.ic_launcher);
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public void ProductProgress() {
        if (!flag_loading)
            showpDialog();
        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Totalproduct = response.getJSONArray("totalproducts");
                            cartinfo = response.getJSONArray("cartinfo");
                            totalproductlength = Totalproduct.length();
                            int lengths = cartinfo.length();

                            //http://www.puyangan.com/api/category.php?cid=178
                            // Getting Array of Employee
//				Totalproduct = json.getJSONArray("totalproducts");
                            totalproductlength = Totalproduct.length();
                            for (int i = 0; i < totalproductlength; i++) {
                                JSONObject ob = Totalproduct.getJSONObject(i);
                                totalproduct = ob.getString("total");
                            }


                            productslist = response.getJSONArray("products");
                            p_length = productslist.length();
                            // looping of List
                            for (int i = 0; i < p_length; i++) {

                                addtolist = addtolist + 1;
                                JSONObject c = productslist.getJSONObject(i);
                                //					Bitmap myBitmap = null;
                                InputStream input = null;
                                ProductId = c.getString("id");
                                Name = c.getString("name");
                                Image = c.getString("image");
                                Regular_price = c.getString("regular_price");
                                Sale_price = c.getString("sale_price");
                                Price = c.getString("price");
                                Holder h = new Holder();
                                h.setId(ProductId);
                                h.setName(Name);
                                h.setRegular_price(Regular_price);
                                h.setSale_price(Sale_price);
                                h.setPrice(Price);
                                h.setImage(Image);

                                list.add(h);

                            }
                            for (int j = 0; j < lengths; j++) {
                                JSONObject cc = cartinfo.getJSONObject(j);

                                MainActivity.cart_item = cc.getString("totalitems");

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            loadingComplete();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("page", String.valueOf(pageno));
                parameters.put("user_id",String.valueOf(LoginId));
                parameters.put("device_id", Splash.device_id);
                return parameters;
            }
        };

        jsonReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(getActivity());

        rQueue.add(jsonReq);
    }

    public void loadingComplete() {
        hidepDialog();
        pageno = pageno + 1;
//            circular_progress.setVisibility(View.GONE);
        int currentPosition = gridview.getFirstVisiblePosition();
        gridview.setAdapter(new MyCustomAdapter(getActivity(), list));
//			gridview.setSelectionFromTop(currentPosition + 1, 0);
//			gridview.smoothScrollToPosition(currentPosition);
        MainActivity.txt_cart_item.setText(MainActivity.cart_item);
        if (p_length == 0) {
            emptylist.setVisibility(View.VISIBLE);
            gridview.setVisibility(View.GONE);
        } else {
            emptylist.setVisibility(View.GONE);
            gridview.setVisibility(View.VISIBLE);
        }

        gridview.setSelection(currentPosition);
        mSwipyRefreshLayout.setRefreshing(false);
        flag_loading = false;
    }
}

