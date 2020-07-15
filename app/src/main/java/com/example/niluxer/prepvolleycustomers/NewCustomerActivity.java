package com.example.niluxer.prepvolleycustomers;

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

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewCustomerActivity extends AppCompatActivity {

    @BindView(R.id.etCustomerFirstName)
    EditText etCustomerFirstName;

    @BindView(R.id.etCustomerLastName)
    EditText etCustomerLastName;

    @BindView(R.id.etCustomerEmail)
    EditText etCustomerEmail;

    @BindView(R.id.etCustomerLatitude)
    EditText etCustomerLatitude;

    @BindView(R.id.etCustomerLongitude)
    EditText etCustomerLongitude;


    @OnClick(R.id.btnCustomerSave)
    public void onClickSave()
    {
        String first_name = etCustomerFirstName.getText().toString();
        String last_name = etCustomerLastName.getText().toString();
        String email = etCustomerEmail.getText().toString();
        String latitude = etCustomerLatitude.getText().toString();
        String longitude = etCustomerLongitude.getText().toString();

        String active = "1";
        String last_update = "2017/04/01";

        Customer customer = new Customer(
                active     , email,
                first_name , last_name,
                last_update,
                Double.parseDouble(latitude),
                Double.parseDouble(longitude));


        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(
                        Request.Method.POST,
                        MainActivity.URL_CUSTOMER_ADD,
                        customer.toJsonObject(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(
                                        NewCustomerActivity.this,
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        ButterKnife.bind(this);
    }
}
