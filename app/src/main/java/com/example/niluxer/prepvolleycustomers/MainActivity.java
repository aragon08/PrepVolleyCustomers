package com.example.niluxer.prepvolleycustomers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String URL_CUSTOMER_LIST = "http://ws.itcelaya.edu.mx:8080/curso/apirest/customers/listado";
    String URL_CUSTOMER_LIST_RFC = "http://ws.itcelaya.edu.mx:8080/curso/apirest/customers/misclientes/AACJ";

    public static String URL_CUSTOMER_ADD = "http://ws.itcelaya.edu.mx:8080/curso/apirest/customers/insertar";
    public static String URL_CUSTOMER_UPDATE = "http://ws.itcelaya.edu.mx:8080/curso/apirest/customers/actualizar";
    public static String URL_CUSTOMER_DELETE = "http://ws.itcelaya.edu.mx:8080/curso/apirest/customers/borrar/customer_id/AACJ";

    MenuItem mnuNew, mnuEdit, mnuDelete;

    RecyclerView rvCustomers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCustomers = (RecyclerView) findViewById(R.id.rvCustomers);

        loadCustomers();
        registerForContextMenu(rvCustomers);
    }

    List<Customer> listCustomers = new ArrayList<Customer>();
    private void loadCustomers()
    {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL_CUSTOMER_LIST_RFC,null,
        new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonResponse = response.getJSONArray("customers");
                    for (int i=0;i<jsonResponse.length(); i++)
                    {
                        JSONObject jsonChildNode = jsonResponse.getJSONObject(i);
                        String first_name  = jsonChildNode.getString("first_name");
                        String last_name   = jsonChildNode.getString("last_name");
                        String email       = jsonChildNode.getString("email");
                        String active      = jsonChildNode.getString("active");
                        String last_update = jsonChildNode.getString("last_update");
                        Double longitude   = jsonChildNode.getDouble("longitude");
                        Double latitude    = jsonChildNode.getDouble("latitude");
                        int customer_id    = jsonChildNode.getInt("customer_id");

                        Customer customer = new Customer(active, email, first_name, last_name, last_update, latitude, longitude);
                        customer.setId(customer_id);
                        listCustomers.add(customer);
                    }

                    rvCustomers.setAdapter(new CustomerAdapter(MainActivity.this, listCustomers));
                    rvCustomers.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
        new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }

        );

        queue.add(jsonObjectRequest);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnuNew) {
            Intent intent = new Intent(this, NewCustomerActivity.class);
            startActivity(intent);
        }else if(item.getItemId()==R.id.mnuCustomersMap){
            Intent intent = new Intent(this,CustomersMapsActivity.class);
            //Customer c= listCustomers.get(0);
            intent.putExtra("ListCustomers", (Serializable) listCustomers);


            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v == rvCustomers) {
            menu.setHeaderTitle("Opciones");
            //MenuInflater inflater=getMenuInflater();
            //inflater.inflate(R.menu.menu_contextual, menu);
            getMenuInflater().inflate(R.menu.menu_contextual, menu);
        }
    }*/

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Toast.makeText(this, "Hola" + info.position, Toast.LENGTH_SHORT).show();
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        rvCustomers.setAdapter(null);
        listCustomers.clear();
        loadCustomers();
    }
}
