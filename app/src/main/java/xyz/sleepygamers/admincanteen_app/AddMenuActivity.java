package xyz.sleepygamers.admincanteen_app;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import xyz.sleepygamers.admincanteen_app.utils.MySingleton;

import static xyz.sleepygamers.admincanteen_app.EndPoints.ADDMENU_URL;

public class AddMenuActivity extends AppCompatActivity {
    private Button buttonAdd;
    private ImageView imageView;
    private EditText editText;
    private EditText price;
    private Spinner spinner1;
    private Bitmap bitmap;
    private RadioGroup rg_menutype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        buttonAdd = (Button) findViewById(R.id.buttonUpload);
        editText = (EditText) findViewById(R.id.editTextName);
        price = (EditText) findViewById(R.id.editTextPrice);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        rg_menutype = findViewById(R.id.rg_type);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validating data
                if (String.valueOf(spinner1.getSelectedItem()).equals(String.valueOf(spinner1.getItemAtPosition(0)))) {
                    Toast.makeText(AddMenuActivity.this, "Select the menu category", Toast.LENGTH_SHORT).show();
                    spinner1.requestFocus();
                    return;
                }
                if (editText.getText().toString().trim().isEmpty()) {
                    editText.setError("Enter Menu Item Name");
                    editText.requestFocus();
                    return;
                } else if (price.getText().toString().trim().isEmpty()) {
                    price.setError("Enter Price");
                    price.requestFocus();
                    return;
                } else if (rg_menutype.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(AddMenuActivity.this, "Select the menu type i.e veg or nonveg", Toast.LENGTH_SHORT).show();
                    return;
                }
                addMenu();
            }
        });
    }


    private void addMenu() {
        final String menuitem_name = editText.getText().toString().trim();
        final String menuitem_price = price.getText().toString().trim();
        final String menuitem_category = String.valueOf(spinner1.getSelectedItem());
        final String menuitem_type;
        if (rg_menutype.getCheckedRadioButtonId() == R.id.rb_veg) {
            menuitem_type = "veg";
        } else {
            menuitem_type = "nonveg";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDMENU_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject responseObj = new JSONObject(response);
                    if (!responseObj.getBoolean("error")) {
                        Toast.makeText(AddMenuActivity.this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddMenuActivity.this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddMenuActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", menuitem_name);
                params.put("category", menuitem_category);
                params.put("price", menuitem_price);
                params.put("type", menuitem_type);
                return params;
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

}
