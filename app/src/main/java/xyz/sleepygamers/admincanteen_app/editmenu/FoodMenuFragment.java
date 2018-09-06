package xyz.sleepygamers.admincanteen_app.editmenu;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xyz.sleepygamers.admincanteen_app.AddMenuActivity;
import xyz.sleepygamers.admincanteen_app.R;
import xyz.sleepygamers.admincanteen_app.adapters.FoodmenuAdapter;
import xyz.sleepygamers.admincanteen_app.models.foodmenu;
import xyz.sleepygamers.admincanteen_app.utils.MySingleton;

import static xyz.sleepygamers.admincanteen_app.EndPoints.ADDMENU_URL;
import static xyz.sleepygamers.admincanteen_app.EndPoints.DELETE_FOODMENU;
import static xyz.sleepygamers.admincanteen_app.EndPoints.GET_FOODMENU;
import static xyz.sleepygamers.admincanteen_app.EndPoints.UPDATE_FOODMENU;

public class FoodMenuFragment extends Fragment {

    View view;
    ProgressBar progressBar;
    List<foodmenu> menuList;
    RecyclerView recyclerView;
    FoodmenuAdapter foodmenuAdapter;
    String type;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_menu, container, false);

        if (getArguments() != null) {
            type = getArguments().getString("params");
        }
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        menuList = new ArrayList<>();
        foodmenuAdapter = new FoodmenuAdapter(getContext(), menuList, FoodMenuFragment.this);
        recyclerView.setAdapter(foodmenuAdapter);
        loadfood();
        return view;
    }

    public void loadfood() {
        setVisibility(false);
        String url = GET_FOODMENU.concat(type);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setVisibility(true);
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("menus");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject food = array.getJSONObject(i);
                        menuList.add(new foodmenu(
                                food.getInt("id"),
                                food.getString("name"),
                                food.getString("price"),
                                food.getString("type"))
                        );
                    }
                    foodmenuAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setVisibility(true);
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    public void updatefood(int pos, final String menuitem_name, final String menuitem_price, final String menuitem_type) {
        String url = UPDATE_FOODMENU.concat(Integer.toString(menuList.get(pos).getId()));
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if (!responseObj.getBoolean("error")) {
                        Toast.makeText(getContext(), responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                        menuList.clear();
                        loadfood();
                    } else {
                        Toast.makeText(getContext(), responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", menuitem_name);
                params.put("price", menuitem_price);
                params.put("category", type);
                params.put("type", menuitem_type);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

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

    public void edit(final int pos) {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_menu);
        dialog.setTitle("Update...");
        final EditText menuitem_name = dialog.findViewById(R.id.editTextName);
        final EditText menuitem_price = dialog.findViewById(R.id.editTextPrice);
        final RadioGroup rg_type = dialog.findViewById(R.id.rg_type);
        menuitem_name.setText(menuList.get(pos).getName());
        menuitem_price.setText(menuList.get(pos).getPrice());
        dialog.findViewById(R.id.buttonUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String menuitem_type;
                if (rg_type.getCheckedRadioButtonId() == R.id.rb_veg) {
                    menuitem_type = "veg";
                } else {
                    menuitem_type = "nonveg";
                }
                updatefood(pos, menuitem_name.getText().toString(), menuitem_price.getText().toString(), menuitem_type);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void delete(final int pos) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are you sure,You wanted to delete");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteMenu(pos);
                    }
                });
        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteMenu(int pos) {
        setVisibility(false);
        String url = DELETE_FOODMENU.concat(Integer.toString(menuList.get(pos).getId()));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                setVisibility(true);
                try {
                    JSONObject object = new JSONObject(response);
                    if (!object.getBoolean("error")) {
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        menuList.clear();
                        loadfood();
                    } else {
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        setVisibility(true);
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }
}
