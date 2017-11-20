package in.co.bubblewater;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by ADMIN on 06-Jul-17.
 */

public class MyAccount extends Fragment implements View.OnClickListener {
    View view;
    RelativeLayout rlpersiondetails, rladdress1, rladdress2, rlchangepassword;
    TextView txtname, txtemail, txtphone, txtdetails;
    TextView txtadd1_title, txtadd1_name, txtadd1_olddoor, txtadd1_newdoor, txtadd1_street, txtadd1_appartment, txtadd1_area, txtadd1_city, txtadd1_postcode, txtadd1_details;
    TextView txtadd2_title, txtadd2_name, txtadd2_olddoor, txtadd2_newdoor, txtadd2_street, txtadd2_appartment, txtadd2_area, txtadd2_city, txtadd2_postcode, txtadd2_details;
    TextView txtchangepass, txtlogout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.myaccount, container, false);
        txtname = (TextView) view.findViewById(R.id.txtname);
        txtemail = (TextView) view.findViewById(R.id.txtemail);
        txtphone = (TextView) view.findViewById(R.id.txtphone);
        txtdetails = (TextView) view.findViewById(R.id.txtdetails);

        txtadd1_title = (TextView) view.findViewById(R.id.txtadd1_title);
        txtadd1_name = (TextView) view.findViewById(R.id.txtadd1_name);
        txtadd1_olddoor = (TextView) view.findViewById(R.id.txtadd1_olddoor);
        txtadd1_newdoor = (TextView) view.findViewById(R.id.txtadd1_newdoor);
        txtadd1_street = (TextView) view.findViewById(R.id.txtadd1_street);
        txtadd1_appartment = (TextView) view.findViewById(R.id.txtadd1_appartment);
        txtadd1_area = (TextView) view.findViewById(R.id.txtadd1_area);
        txtadd1_city = (TextView) view.findViewById(R.id.txtadd1_city);
        txtadd1_postcode = (TextView) view.findViewById(R.id.txtadd1_postcode);
        txtadd1_details = (TextView) view.findViewById(R.id.txtadd1_details);

        txtadd2_title = (TextView) view.findViewById(R.id.txtadd2_title);
        txtadd2_name = (TextView) view.findViewById(R.id.txtadd2_name);
        txtadd2_olddoor = (TextView) view.findViewById(R.id.txtadd2_olddoor);
        txtadd2_newdoor = (TextView) view.findViewById(R.id.txtadd2_newdoor);
        txtadd2_street = (TextView) view.findViewById(R.id.txtadd2_street);
        txtadd2_appartment = (TextView) view.findViewById(R.id.txtadd2_appartment);
        txtadd2_area = (TextView) view.findViewById(R.id.txtadd2_area);
        txtadd2_city = (TextView) view.findViewById(R.id.txtadd2_city);
        txtadd2_postcode = (TextView) view.findViewById(R.id.txtadd2_postcode);
        txtadd2_details = (TextView) view.findViewById(R.id.txtadd2_details);

        txtchangepass = (TextView) view.findViewById(R.id.txtchangepass);
        txtlogout = (TextView) view.findViewById(R.id.txtlogout);

        rlpersiondetails = (RelativeLayout) view.findViewById(R.id.rlpersiondetails);
        rladdress1 = (RelativeLayout) view.findViewById(R.id.rladdress1);
        rladdress2 = (RelativeLayout) view.findViewById(R.id.rladdress2);
        rlchangepassword = (RelativeLayout) view.findViewById(R.id.rlchangepassword);

        txtdetails.setOnClickListener(this);
        txtadd1_details.setOnClickListener(this);
        txtadd2_details.setOnClickListener(this);
        txtchangepass.setOnClickListener(this);
        txtlogout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Intent in;
        switch (view.getId()) {
            case R.id.txtdetails:
                in = new Intent(getActivity(), AccountInfo.class);
                startActivity(in);
                break;
            case R.id.txtadd1_details:
                in = new Intent(getActivity(), EditAddress.class);
                startActivity(in);
                break;
            case R.id.txtadd2_details:
                in = new Intent(getActivity(), EditAddress.class);
                startActivity(in);
                break;
            case R.id.txtchangepass:
                in = new Intent(getActivity(), ChangePass.class);
                startActivity(in);
                break;
            case R.id.txtlogout:
                break;
        }
    }
}
