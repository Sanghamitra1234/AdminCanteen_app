package xyz.sleepygamers.admincanteen_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import xyz.sleepygamers.admincanteen_app.adapters.FoodmenuAdapter;
import xyz.sleepygamers.admincanteen_app.adapters.OrdersAdapter;
import xyz.sleepygamers.admincanteen_app.editmenu.FoodMenuFragment;
import xyz.sleepygamers.admincanteen_app.models.Order;
import xyz.sleepygamers.admincanteen_app.models.foodmenu;
import xyz.sleepygamers.admincanteen_app.utils.MySingleton;

import static xyz.sleepygamers.admincanteen_app.EndPoints.GET_FOODMENU;
import static xyz.sleepygamers.admincanteen_app.EndPoints.ORDERS_PENDING;

public class OrdersActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<Order> orderList;
    OrdersAdapter ordersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        recyclerView = findViewById(R.id.recylcer_view_orders);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        orderList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, orderList);
        recyclerView.setAdapter(ordersAdapter);
        loadOrders();
    }

    void loadOrders() {
        setVisibility(false);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ORDERS_PENDING, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setVisibility(true);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean("error")) {
                        JSONArray array = object.getJSONArray("orders");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject order = array.getJSONObject(i);
                            orderList.add(new Order(
                                            order.getInt("id"),
                                            order.getString("order_details"),
                                            order.getString("order_date"),
                                            order.getInt("price"),
                                            order.getString("order_type"),
                                            order.getString("delivery_type"),
                                            order.getString("order_status"),
                                            order.getInt("user_id"),
                                            order.getString("user_name"),
                                            order.getInt("user_roomno"),
                                            order.getInt("user_uid")
                                    )
                            );
                        }
                        ordersAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setVisibility(true);
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    void setVisibility(boolean visibility) {
        if (visibility) {
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        orderList.clear();
        loadOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        orderList.clear();
        loadOrders();
    }
}
