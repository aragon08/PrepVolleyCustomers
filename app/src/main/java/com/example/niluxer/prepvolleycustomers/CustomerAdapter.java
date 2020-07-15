package com.example.niluxer.prepvolleycustomers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by niluxer on 3/31/17.
 */

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder>
{

    Context context;
    List<Customer> customers;

    public CustomerAdapter(Context context, List<Customer> customers) {
        this.context = context;
        this.customers = customers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_item2, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Customer c = customers.get(position);
        holder.tvCustomerName.setText(c.getFirst_name() + " " + c.getLast_name());
        holder.tvCustomerEmail.setText(c.getEmail());
    }

    @Override
    public int getItemCount() {
        return customers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener
    {
        TextView tvCustomerName;
        TextView tvCustomerEmail;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCustomerName = (TextView) itemView.findViewById(R.id.tvCustomerName);
            tvCustomerEmail = (TextView) itemView.findViewById(R.id.tvCustomerEmail);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem edit = menu.add(Menu.NONE, 1, 1, "Edit");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            MenuItem location = menu.add(Menu.NONE, 3, 3, "Location");
            edit.setOnMenuItemClickListener(listenerOnEditMenu);
            delete.setOnMenuItemClickListener(listenerOnDeleteMenu);
            location.setOnMenuItemClickListener(listenerOnLocationMenu);
        }

        MenuItem.OnMenuItemClickListener listenerOnEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Customer c = customers.get(getAdapterPosition());
                //Toast.makeText(context, c.getFirst_name() + c.getId() + "", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, EditCustomerActivity.class);

                Bundle bundle=new Bundle();

                bundle.putInt("customer_id", c.getId());
                bundle.putString("first_name" , c.getFirst_name());
                bundle.putString("last_name"  , c.getLast_name());
                bundle.putString("email"      , c.getEmail());
                bundle.putDouble("latitude"   , c.getLatitude());
                bundle.putDouble("longitude"  , c.getLongitude());

                intent.putExtras(bundle);

                context.startActivity(intent);

                return true;
            }
        };

        MenuItem.OnMenuItemClickListener listenerOnDeleteMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final Customer c = customers.get(getAdapterPosition());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setTitle("Delete Customer");
                builder.setMessage("Se eliminará el cliente: " + c.getFirst_name() + " " + c.getLast_name());
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteCustomer(c.getId());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Cancel", null);

                Dialog d = builder.create();
                d.show();

                return true;
            }
        };

        MenuItem.OnMenuItemClickListener listenerOnLocationMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Customer c = customers.get(getAdapterPosition());

                Intent intent = new Intent(context, ClientLocationMapsActivity.class);

                Bundle bundle= new Bundle();
                bundle.putDouble("latitude",c.getLatitude());
                bundle.putDouble("longitude",c.getLongitude());

                intent.putExtras(bundle);

                context.startActivity(intent);

                return true;
            }
        };

        private void deleteCustomer(int customer_id)
        {
            RequestQueue queue = Volley.newRequestQueue(context);
            JsonObjectRequest jsonObjectRequest =
                    new JsonObjectRequest(
                            Request.Method.DELETE,
                            MainActivity
                                    .URL_CUSTOMER_DELETE
                                    .replace(
                                            "customer_id",
                                            String.valueOf(customer_id)),
                            null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Toast.makeText(context, "Successfully Deleted...", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
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

    }

}
