package in.co.bubblewater;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Defining Variables
    public static Toolbar toolbar;
    NavigationView navigationView;
    public static DrawerLayout drawerLayout;

    String[] title;
    String[] subtitle;
    int[] icon;
    public static ListView mDrawerList;
    public static MenuListAdapter mMenuAdapter;
    public static int mSelectedItem = 0;
    public static int add = 0;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    boolean doubleBackToExitPressedOnce = false;
    String currentpage = "";
    Fragment fragment;
    FragmentTransaction ft;

    Home home = new Home();
    Product product = new Product();
    Cart cart = new Cart();
    OrderHistory orderhistory = new OrderHistory();
    Setting myaccount = new Setting();
    int shoplength;
    private Menu menu;
    SharedPreferences.Editor editor;
    public static TextView txt_cart_item;
    public static String cart_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {

            setSupportActionBar(toolbar);
        }
        title = new String[]{"Home", "Products", "Cart", "Order History", "My Account", "Logout"};

        getSupportActionBar().setTitle("EzeWater");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setIcon(R.drawable.ic_menu_camera);
//        // Generate icon
        icon = new int[]{R.mipmap.home, R.mipmap.product, R.mipmap.cartmenu, R.mipmap.orderhistory, R.mipmap.myaccount, R.mipmap.logout};


        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerList = (ListView) findViewById(R.id.lst_menu_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ft = getSupportFragmentManager().beginTransaction();
        mMenuAdapter = new MenuListAdapter(MainActivity.this, title, icon);
        mDrawerList.setAdapter(mMenuAdapter);
        ft.replace(R.id.frame, product);
        ft.commit();
        mSelectedItem = 1;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();
        editor.putString("page", "Product");
        editor.commit();

//        drawerLayout.openDrawer(Gravity.START);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        mDrawerList.setBackgroundColor(Color.BLACK);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Locate Position
                editor = sharedpreferences.edit();
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                currentpage = sharedpreferences.getString("page", "");
                ft = getSupportFragmentManager().beginTransaction();
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                switch (position) {
                    case 0:

                        if (!cd.isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();
                        } else {
                            if (currentpage.equalsIgnoreCase("Home")) {

                            } else {
                                ft.replace(R.id.frame, home);
                                editor.putString("page", "Home");
                                editor.commit();
                            }
                        }
                        break;
                    case 1:
                        if (!cd.isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (currentpage.equalsIgnoreCase("Products")) {

                            } else {
                                ft.replace(R.id.frame, product);
                                editor.putString("page", "Products");
                                editor.commit();
                            }
                        }

                        break;
                    case 2:
                        if (!cd.isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (currentpage.equalsIgnoreCase("Cart")) {

                            } else {
                                ft.replace(R.id.frame, cart);
                                editor.putString("page", "Cart");
                                editor.commit();
                            }
                        }

                        break;

                    case 3:
                        if (!cd.isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (currentpage.equalsIgnoreCase("OrderHistory")) {

                            } else {
                                ft.replace(R.id.frame, orderhistory);
                                editor.putString("page", "OrderHistory");
                                editor.commit();
                            }
                        }

                        break;
                    case 4:
                        if (!cd.isConnectingToInternet()) {
                            Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                        } else {
                            if (currentpage.equalsIgnoreCase("MyAccount")) {

                            } else {
                                ft.replace(R.id.frame, myaccount);
                                editor.putString("page", "MyAccount");
                                editor.commit();
                            }
                        }

                        break;


                    case 5:
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(MainActivity.this);
                        }
                        builder.setTitle("Logout")
                                .setMessage("Are you sure want to Logout?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor = sharedpreferences.edit();
                                        editor.putInt("LoginId", 0);
                                        editor.putInt("fblogin", 0);
                                        editor.putString("page", "");
                                        editor.commit();
                                        Intent in = new Intent(MainActivity.this, LoginActivity.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(in);
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert).show();

                        break;
                }

                ft.commit();
//                mDrawerList.setBackgroundColor(getResources().getColor(R.color.SelectedColor));
                if (position != 5)
                    mSelectedItem = position;
                mMenuAdapter.notifyDataSetChanged();
//                mDrawerList.setItemChecked(position, true);
//                for (int i = 0; i < mDrawerList.getChildCount(); i++) {
//                    if (position == i) {
//                        mDrawerList.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.SelectedColor));
//                    } else {
//                        mDrawerList.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
//                    }
//                }
                // Get the title followed by the position
//                getSupportActionBar().setTitle("Emerald Jewellers");
                // Close drawer
                drawerLayout.closeDrawers();
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
//                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//                LoginId = sharedpreferences.getInt("LoginId", 0);
//                if (LoginId == 0) {
//                    title = new String[]{"Signin", "Cart", "Order", "Categories", "Recents", "Features", "All", "Settings"};
//                    mMenuAdapter.notifyDataSetChanged();
//                    mMenuAdapter = new MenuListAdapter(MainActivity.this, title);
//                    mDrawerList.setAdapter(mMenuAdapter);
//                } else {
//                    title = new String[]{"Signin", "Cart", "Order", "Categories", "Recents", "Features", "All", "Settings", "Logout"};
//                    mMenuAdapter.notifyDataSetChanged();
//                    mMenuAdapter = new MenuListAdapter(MainActivity.this, title);
//                    mDrawerList.setAdapter(mMenuAdapter);
//                }
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0) {
            // If there are back-stack entries, leave the FragmentActivity
            // implementation take care of them.
            manager.popBackStack();
//            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//            page = sharedpreferences.getString("page", "");
//            if (page.equalsIgnoreCase("Categories")) {
//                Categories.txtstore.setText(Categories.store);
//            } else if (page.equalsIgnoreCase("Recents")) {
//                Recents.txtstore.setText(Categories.store);
//            } else if (page.equalsIgnoreCase("Features")) {
//                Features.txtstore.setText(Categories.store);
//            } else if (page.equalsIgnoreCase("All")) {
//                All.txtstore.setText(Categories.store);
//            } else {
//            }

        } else {
            // Otherwise, ask user if he wants to leave :)
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    public class MenuListAdapter extends BaseAdapter {

        // Declare Variables
        Context context;
        String[] mTitle;
        //    String[] mSubTitle;
        int[] mIcon;
        LayoutInflater inflater;
        View itemView;

        public MenuListAdapter(Context context, String[] title, int[] icon) {
            this.context = context;
            this.mTitle = title;
//        this.mSubTitle = subtitle;
            this.mIcon = icon;
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            // Declare Variables
            TextView txtTitle;
            TextView txtSubTitle;
            ImageView imgIcon;

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.drawer_list_item, parent,
                    false);

            // Locate the TextViews in drawer_list_item.xml
            txtTitle = (TextView) itemView.findViewById(R.id.title);
            txtSubTitle = (TextView) itemView.findViewById(R.id.subtitle);

            // Locate the ImageView in drawer_list_item.xml
            imgIcon = (ImageView) itemView.findViewById(R.id.icon);

            // Set the results into TextViews
            txtTitle.setText(mTitle[position]);
//        txtSubTitle.setText(mSubTitle[position]);

            // Set the results into ImageView
            imgIcon.setImageResource(mIcon[position]);
//            if (position == 0)
//                itemView.setBackgroundColor(context.getResources().getColor(R.color.SelectedColor));
            if (mSelectedItem != 5) {
                if (position == mSelectedItem) {
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.SelectedColor));
                } else {
//                itemView.setBackgroundColor(Color.BLACK);
                    itemView.setBackgroundColor(context.getResources().getColor(R.color.UnSelectedColor));
                }
            }
            return itemView;
        }
    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_button, menu);
        this.menu = menu;
        MenuItem item = menu.findItem(R.id.options_menu_main_cart);
        MenuItemCompat.setActionView(item, R.layout.actionbar_badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);

        txt_cart_item = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
//        txt_cart_item.setText("12");
        ImageView img = (ImageView) notifCount.findViewById(R.id.img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                currentpage = sharedpreferences.getString("page", "");
                ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
                if (!cd.isConnectingToInternet()) {
                    Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                } else {
                    if (!currentpage.equalsIgnoreCase("Cart")) {

                        fragment = new Cart();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame, fragment);
//                ft.add(R.id.frame, fragment);
//			      ft.addToBackStack("add" + MainActivity.add);
                        ft.commit();
                        mSelectedItem = 2;
                        mMenuAdapter.notifyDataSetChanged();
                        editor.putString("page", "Cart");
                        editor.commit();
                    }
                }

            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void updateMenuTitles() {
        MenuItem MenuItem = menu.findItem(R.id.options_menu_main_locator);
        MenuItem.setTitle("" + shoplength);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        currentpage = sharedpreferences.getString("page", "");
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
        switch (item.getItemId()) {

            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.options_menu_main_cart: {
//                Toast.makeText(MainActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
                if (!cd.isConnectingToInternet()) {
                    Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                } else {
                    if (currentpage.equalsIgnoreCase("Cart")) {

                    } else {
                        fragment = new Cart();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame, fragment);
//                    ft.add(R.id.frame, fragment);
//                    ft.addToBackStack("add" + MainActivity.add);
                        ft.commit();
                        MainActivity.mSelectedItem = 2;
                        MainActivity.mMenuAdapter.notifyDataSetChanged();
                        editor.putString("page", "Cart");
                        editor.commit();
                    }
                }

//                MainActivity.add++;
                return true;
            }

            case R.id.options_menu_main_locator: {
//                Toast.makeText(MainActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
                if (!cd.isConnectingToInternet()) {
                    Toast.makeText(MainActivity.this, "Please connect to working internet connection", Toast.LENGTH_LONG).show();

                } else {
                    if (currentpage.equalsIgnoreCase("nearby")) {

                    } else {
                        fragment = new Cart();
                        ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.frame, fragment);
//                    ft.add(R.id.frame, fragment);
//                    ft.addToBackStack("add" + MainActivity.add);
                        ft.commit();
                        editor.putString("page", "nearby");
                        editor.commit();
                    }
                }

                return true;
            }
            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    public static View getToolbarLogoIcon(Toolbar toolbar) {
        //check if contentDescription previously was set
        boolean hadContentDescription = android.text.TextUtils.isEmpty(toolbar.getLogoDescription());
        String contentDescription = String.valueOf(!hadContentDescription ? toolbar.getLogoDescription() : "logoContentDescription");
        toolbar.setLogoDescription(contentDescription);
        ArrayList<View> potentialViews = new ArrayList<View>();
        //find the view based on it's content description, set programatically or with android:contentDescription
        toolbar.findViewsWithText(potentialViews, contentDescription, View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION);
        //Nav icon is always instantiated at this point because calling setLogoDescription ensures its existence
        View logoIcon = null;
        if (potentialViews.size() > 0) {
            logoIcon = potentialViews.get(0);
        }
        //Clear content description if not previously present
        if (hadContentDescription)
            toolbar.setLogoDescription(null);
        return logoIcon;
    }
}