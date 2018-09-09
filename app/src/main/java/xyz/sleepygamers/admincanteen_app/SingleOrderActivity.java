package xyz.sleepygamers.admincanteen_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import xyz.sleepygamers.admincanteen_app.adapters.OrdersAdapter;
import xyz.sleepygamers.admincanteen_app.adapters.SingleOrderAdapter;
import xyz.sleepygamers.admincanteen_app.models.Order;
import xyz.sleepygamers.admincanteen_app.models.SingleOrderMenuItem;

public class SingleOrderActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ArrayList<SingleOrderMenuItem> menuList;
    SingleOrderAdapter orderAdapter;

    TextView tvName, tvId, tvRoomno, tvPrice, tvDate, tvType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_order);

        Order order = (Order) getIntent().getSerializableExtra("singleOrder");

        tvName = findViewById(R.id.tv_name);
        tvId = findViewById(R.id.tv_user_uid);
        tvDate = findViewById(R.id.tv_order_date);
        tvRoomno = findViewById(R.id.tv_roomno);
        tvPrice = findViewById(R.id.tv_price);
        tvType = findViewById(R.id.tv_order_type);

        tvName.setText(order.getUser_name());
        tvId.setText(Integer.toString(order.getUser_id()));
        tvDate.setText(order.getOrder_date());
        tvRoomno.setText(Integer.toString(order.getUser_roomno()));
        tvPrice.setText(Integer.toString(order.getPrice()));
        tvType.setText(order.getOrder_type());


        recyclerView = findViewById(R.id.recylcer_view_orders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        menuList = new ArrayList<>();
        orderAdapter = new SingleOrderAdapter(this, menuList);
        recyclerView.setAdapter(orderAdapter);
        loadItems(order.getOrder_details());
    }

    private void loadItems(String orderDetails) {
        String items[] = orderDetails.split(",");
        for (int i = 0; i < items.length; i++) {
            String itemParts[] = items[i].split("\\s+");
            SingleOrderMenuItem orderMenuItem = new SingleOrderMenuItem(itemParts[0], itemParts[1], itemParts[2]);
            menuList.add(orderMenuItem);
        }
        orderAdapter.notifyDataSetChanged();
    }
}
