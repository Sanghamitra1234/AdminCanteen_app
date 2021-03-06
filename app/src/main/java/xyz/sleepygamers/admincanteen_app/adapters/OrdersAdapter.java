package xyz.sleepygamers.admincanteen_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.sleepygamers.admincanteen_app.R;
import xyz.sleepygamers.admincanteen_app.SingleOrderActivity;
import xyz.sleepygamers.admincanteen_app.models.Order;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    Context mCtx;
    private ArrayList<Order> orderList;

    public OrdersAdapter(Context mCtx, ArrayList<Order> orderList) {
        this.mCtx = mCtx;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_row, parent, false);
        return new OrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Order order = orderList.get(position);
        holder.tvName.setText(order.getUser_name());
        holder.tvPrice.setText(Integer.toString(order.getPrice()));
        holder.tvOrderDetails.setText(order.getOrder_details());
        holder.tvOrderType.setText(order.getOrder_type());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mCtx,SingleOrderActivity.class);
                i.putExtra("singleOrder",order);
                mCtx.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvOrderDetails,tvOrderType;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvOrderDetails = itemView.findViewById(R.id.tv_order);
            tvOrderType = itemView.findViewById(R.id.tv_order_type);
        }
    }
}
