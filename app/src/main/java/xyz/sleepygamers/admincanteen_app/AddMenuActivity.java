package xyz.sleepygamers.admincanteen_app;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
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
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import xyz.sleepygamers.admincanteen_app.utils.MySingleton;

import static xyz.sleepygamers.admincanteen_app.EndPoints.UPLOAD_URL;

public class AddMenuActivity extends AppCompatActivity {
    private Button buttonUpload, buttonAddImage;
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

        buttonAddImage = (Button) findViewById(R.id.buttonAddImage);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);
        imageView = (ImageView) findViewById(R.id.imageView);
        editText = (EditText) findViewById(R.id.editTextName);
        price = (EditText) findViewById(R.id.editTextPrice);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        rg_menutype = findViewById(R.id.rg_type);

        //ADDING IMAGE
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions();
                } else {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 100);
                }
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
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

                if (bitmap != null)
                    uploadBitmap(bitmap);
                else
                    addMenu();
            }
        });
    }

    void checkPermissions() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 100);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                }).check();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                //displaying selected image to imageview
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(final Bitmap bitmap) {

        final String menuitem_name = editText.getText().toString().trim();
        final String menuitem_price = price.getText().toString().trim();
        final String menuitem_category = String.valueOf(spinner1.getSelectedItem());
        final String menuitem_type;
        if (rg_menutype.getCheckedRadioButtonId() == 0) {
            menuitem_type = "veg";
        } else {
            menuitem_type = "novnveg";
        }
        VolleyMultiPartRequest volleyMultipartRequest = new VolleyMultiPartRequest(Request.Method.POST, EndPoints.UPLOADPIC_URL,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject responseObj = new JSONObject(new String(response.data));
                            if (!responseObj.getBoolean("error")) {
                                Toast.makeText(AddMenuActivity.this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(AddMenuActivity.this, responseObj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddMenuActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("pic", new DataPart("http://sleepygamers.xyz/tatapower/uploads/" + imagename + ".png", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(volleyMultipartRequest);

    }

    private void addMenu() {
        final String menuitem_name = editText.getText().toString().trim();
        final String menuitem_price = price.getText().toString().trim();
        final String menuitem_category = String.valueOf(spinner1.getSelectedItem());
        final String menuitem_type;
        if (rg_menutype.getCheckedRadioButtonId() == 0) {
            menuitem_type = "veg";
        } else {
            menuitem_type = "novnveg";
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
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
