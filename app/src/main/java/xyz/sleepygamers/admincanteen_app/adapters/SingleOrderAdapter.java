package xyz.sleepygamers.admincanteen_app.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.sleepygamers.admincanteen_app.R;
import xyz.sleepygamers.admincanteen_app.models.SingleOrderMenuItem;

public class SingleOrderAdapter extends RecyclerView.Adapter<SingleOrderAdapter.ViewHolder> {
    Context mCtx;
    private ArrayList<SingleOrderMenuItem> menuList;

    public SingleOrderAdapter(Context mCtx, ArrayList<SingleOrderMenuItem> menuList) {
        this.mCtx = mCtx;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public SingleOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.single_order_row, parent, false);
        return new SingleOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleOrderAdapter.ViewHolder holder, int position) {

        final SingleOrderMenuItem menuItem = menuList.get(position);
        holder.tvName.setText(menuItem.getName());
        holder.tvPrice.setText(menuItem.getPrice());
        holder.tvQuantity.setText(menuItem.getQuantity());
      }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvPrice, tvQuantity;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
        }
    }
}
