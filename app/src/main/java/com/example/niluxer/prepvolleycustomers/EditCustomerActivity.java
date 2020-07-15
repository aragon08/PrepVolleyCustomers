package com.example.niluxer.prepvolleycustomers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditCustomerActivity extends AppCompatActivity {

    @BindView(R.id.etCustomerEditFirstName)
    EditText etCustomerEditFirstName;

    @BindView(R.id.etCustomerEditLastName)
    EditText etCustomerEditLastName;

    @BindView(R.id.etCustomerEditEmail)
    EditText etCustomerEditEmail;

    @BindView(R.id.etCustomerEditLatitude)
    EditText etCustomerEditLatitude;

    @BindView(R.id.etCustomerEditLongitude)
    EditText etCustomerEditLongitude;

    Integer customer_id;

    @OnClick(R.id.btnCustomerSaveEdit)
    public void onSaveEdit()
    {
        String first_name = etCustomerEditFirstName.getText().toString();
        String last_name  = etCustomerEditLastName.getText().toString();
        String email      = etCustomerEditEmail.getText().toString();
        String latitude   = etCustomerEditLatitude.getText().toString();
        String longitude  = etCustomerEditLongitude.getText().toString();

        String active = "1";
        String last_update = "2017/04/01";

        Customer customer = new Customer(
                active     , email,
                first_name , last_name,
                last_update,
                Double.parseDouble(latitude),
                Double.parseDouble(longitude));

        customer.setId(customer_id);


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.PUT,
                        MainActivity.URL_CUSTOMER_UPDATE,
                        customer.toJsonObject(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(
                                        EditCustomerActivity.this,
                                        "Customer saved successfully.",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                );

        queue.add(jsonObjectRequest);

    }

    int PLACE_PICKER_REQUEST = 1;

    @OnClick(R.id.btnCustomerGetLocation)
    public void customerGetLocation(){

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this),PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==PLACE_PICKER_REQUEST){
            if (resultCode==RESULT_OK){
                Place place= PlacePicker.getPlace(this,data);
                LatLng latlng=place.getLatLng();
                etCustomerEditLatitude.setText(latlng.latitude +"");
                etCustomerEditLongitude.setText(latlng.longitude +"");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        Bundle bundle=intent.getExtras();

        //System.out.println(intent.getIntExtra("customer_id", 0));
        customer_id = bundle.getInt("customer_id");

        etCustomerEditFirstName.setText(
                bundle.getString("first_name"));

        etCustomerEditLastName.setText(
                bundle.getString("last_name"));

        etCustomerEditEmail.setText(
                bundle.getString("email"));

        etCustomerEditLatitude.setText(
                bundle.getDouble("latitude") + " ");

        etCustomerEditLongitude.setText(
                bundle.getDouble("longitude") + " ");

    }
}
