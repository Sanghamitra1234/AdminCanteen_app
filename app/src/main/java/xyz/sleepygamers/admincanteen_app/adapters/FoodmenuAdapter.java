package xyz.sleepygamers.admincanteen_app.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.sleepygamers.admincanteen_app.AddMenuActivity;
import xyz.sleepygamers.admincanteen_app.R;
import xyz.sleepygamers.admincanteen_app.editmenu.FoodMenuFragment;
import xyz.sleepygamers.admincanteen_app.models.foodmenu;


public class FoodmenuAdapter extends RecyclerView.Adapter<FoodmenuAdapter.ViewHolder> {
    Context mCtx;
    private List<foodmenu> menuList;
    FoodMenuFragment foodMenuFragment;

    public FoodmenuAdapter(Context mCtx, List<foodmenu> menuList, FoodMenuFragment foodMenuFragment) {
        this.mCtx = mCtx;
        this.menuList = menuList;
        this.foodMenuFragment = foodMenuFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.foodmenu_row, parent, false);
        return new FoodmenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final foodmenu foodmenu = menuList.get(position);
        holder.name.setText(foodmenu.getName());
        holder.price.setText((CharSequence) foodmenu.getPrice());
        holder.menutype.setText(foodmenu.getType());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodMenuFragment.edit(position);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodMenuFragment.delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, menutype;
        private ImageButton edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.menu_name);
            price = itemView.findViewById(R.id.menu_price);
            menutype = itemView.findViewById(R.id.menu_type);
            edit = itemView.findViewById(R.id.edit_menu);
            delete = itemView.findViewById(R.id.delete_menu);
        }

    }
}
