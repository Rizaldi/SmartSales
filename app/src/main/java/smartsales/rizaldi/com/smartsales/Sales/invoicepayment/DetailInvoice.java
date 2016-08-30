package smartsales.rizaldi.com.smartsales.Sales.invoicepayment;

import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Report.customeraging.*;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.Varglobal;
import smartsales.rizaldi.com.smartsales.customerorder.Payment;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class DetailInvoice extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rv;
    TextView customername, ref;
    RadioButton cash, cheque, transfer;
    RadioGroup checkfirst;
    LinearLayout showhide;
    EditText refnumber, payment;
    ImageView addPhoto, image;
    FloatingActionButton save;
    String idCustomer = "", salesId = "";
    private List<ModelDetail> list;
    SqliteHandler db;
    RVAadapterDetail adapter;
    Boolean check = false;
    LinearLayout placeImage;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Uri mUri;
    String filename = "";
    private String imageSend = "";
    private int SELECT_FILE = 200;
    private int paymentMethod = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle b = getIntent().getExtras();
        initialUI();
        payment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (check) {
                    if (!s.toString().isEmpty()) {
                        Varglobal.payment = String.valueOf(s);
                    }
                } else {
                    Toast.makeText(DetailInvoice.this, "choose payment Method", Toast.LENGTH_SHORT).show();
                    Varglobal.payment = "";
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!check)
                    return;
            }
        });
        db = new SqliteHandler(this);
        customername.setText(b.getString("customer"));
        idCustomer = b.getString("id");
        list = new ArrayList<>();
        HashMap<String, String> user = db.getUserDetails();
        salesId = user.get("employee_id");
        getDetailInvoice();
    }

    private void getDetailInvoice() {
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_detailinvoice + idCustomer + "&employeeId=" +
                salesId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseDataList(jsonArray);
                    } else {
                        Toast.makeText(DetailInvoice.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        "No Data Found!", Toast.LENGTH_LONG).show();
                list.clear();
                adapter = new RVAadapterDetail(list, DetailInvoice.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void parseDataList(JSONArray jsonArray) {
        int i;
        for (i = 0; i < jsonArray.length(); i++) {
            ModelDetail Items = new ModelDetail();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setPayment(jsonObject.getString("total_amount"));
                Items.setTime(jsonObject.getString("time_transaction"));
                Items.setDate(jsonObject.getString("date_transaction"));
                Items.setInvoice(jsonObject.getString("invoice_number"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && list.size() != 0) {
                list.clear();
                list.add(Items);
            } else if (i == 0) {
                list.add(Items);
            } else {
                list.add(Items);
            }
        }
        adapter = new RVAadapterDetail(list, this);
        rv.setAdapter(adapter);
    }

    private void initialUI() {
        rv = (RecyclerView) findViewById(R.id.rv);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        customername = (TextView) findViewById(R.id.customername);
        ref = (TextView) findViewById(R.id.ref);
        cash = (RadioButton) findViewById(R.id.cash);
        cheque = (RadioButton) findViewById(R.id.cheque);
        transfer = (RadioButton) findViewById(R.id.transfer);
        showhide = (LinearLayout) findViewById(R.id.showhide);
        refnumber = (EditText) findViewById(R.id.refnumber);
        payment = (EditText) findViewById(R.id.payment);
        addPhoto = (ImageView) findViewById(R.id.addPhoto);
        addPhoto.setOnClickListener(this);
        save = (FloatingActionButton) findViewById(R.id.save);
        save.setOnClickListener(this);
        checkfirst = (RadioGroup) findViewById(R.id.checkfirst);
        image = (ImageView) findViewById(R.id.image);
        placeImage = (LinearLayout) findViewById(R.id.placeImage);
    }

    @Override
    public void onClick(View v) {
        if (v == addPhoto) {
            selectImage();
        }
        if(v==save){
            String invoicepayment=Varglobal.arrPayment.substring(0,Varglobal.arrPayment.length()-1);
            saveInvoicePayment(invoicepayment);
        }
    }

    private void saveInvoicePayment(final String invoicepayment) {
        final ProgressDialog loading = ProgressDialog.show(this, "Saving...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLib.url_saveInvoicePayment, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(s);
                    String code = jObj.getString("code");
                    String msg = jObj.getString("message");
                    if (code.equals("1")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Toast.makeText(getApplicationContext(), volleyError.getMessage() + "a", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            protected Map<String, String> getParams() throws AuthFailureError {
                //Getting Image Name
                Map<String, String> params = new Hashtable<String, String>();
                params.put("customerId", idCustomer);
                params.put("paymentMethod", String.valueOf(paymentMethod));
                params.put("arrPayment", invoicepayment);
                if(paymentMethod!=1){
                    params.put("filename", filename);
                    params.put("image", imageSend);
                    params.put("referenceNo", refnumber.getText().toString());
                }
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DetailInvoice.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            placeImage.setVisibility(View.VISIBLE);
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                mUri = Uri.fromFile(destination);
                String path = mUri.getPath();
                filename = path.substring(path.lastIndexOf("/") + 1, path.length());
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageSend = getStringImage(thumbnail);
                image.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                        null);
                Cursor cursor = cursorLoader.loadInBackground();
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                imageSend = getStringImage(bm);
                filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1, selectedImagePath.length());
                image.setImageBitmap(bm);
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.cash:
                if (checked)
                    showhide.setVisibility(View.GONE);
                ref.setVisibility(View.GONE);
                setVariable(1);
                placeImage.setVisibility(View.GONE);
                break;
            case R.id.cheque:
                if (checked)
                    showhide.setVisibility(View.VISIBLE);
                ref.setVisibility(View.VISIBLE);
                setVariable(2);
                break;
            case R.id.transfer:
                if (checked)
                    showhide.setVisibility(View.VISIBLE);
                ref.setVisibility(View.VISIBLE);
                setVariable(3);
                break;
        }
    }

    private void setVariable(int b) {
        check = true;
        payment.setText("");
        Varglobal.payment = "";
        paymentMethod = b;
    }
}
