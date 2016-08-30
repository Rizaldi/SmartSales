package smartsales.rizaldi.com.smartsales.stock;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class CustomerStock extends AppCompatActivity implements View.OnClickListener {
    LinearLayout placeimage, place;
    ImageView img, submit;
    Spinner customerspinner;
    RelativeLayout startdate;
    RecyclerView rv;
    static final int Dialog_Id = 0;
    int year_x, month_x, day_x;
    TextView tvstartdate;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    Uri mUri;
    String filename = "";
    private int SELECT_FILE = 200;
    int posisi = 0;
    List<Modelcustomername> listcustomer;
    SqliteHandler db;
    static String salesId = "", id_customer = "",start = "",image="";
    List<Modelcustomerstock> liststock;
    RVAadapterStock adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_stock);
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
        Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        salesId = user.get("employee_id");
        initial();
        listcustomer = new ArrayList<>();
        liststock = new ArrayList<>();
        showDialogStartDate();
        getDataCustomer();
        selectCustomername();
    }

    private void getDataStock(String customerid, String date) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_getstockcustomer + salesId + "&customerId=" +
                customerid + "&date=" + date, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        String data = jsonObject.getString("data");
                        jsonArray = new JSONArray(data);
                        parseDataList(jsonArray);
                    } else {
                        Toast.makeText(CustomerStock.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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
                liststock.clear();

                adapter = new RVAadapterStock(liststock, CustomerStock.this);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void parseDataList(JSONArray jsonArray) {
        int i;
        for (i = 0; i < jsonArray.length(); i++) {
            Modelcustomerstock Items = new Modelcustomerstock();
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                Items.setCustomerId(jsonObject.getString("customer_id"));
                Items.setCustomername(jsonObject.getString("customer_name"));
                Items.setProductId(jsonObject.getString("product_id"));
                Items.setProductname(jsonObject.getString("product_name"));
                Items.setQtyrack(jsonObject.getString("qtyRack"));
                Items.setQtywarehouse(jsonObject.getString("qtyWarehouse"));
                Items.setUrlimage(jsonObject.getString("urlImage"));
                Items.setSt(jsonObject.getString("st"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && liststock.size() != 0) {
                liststock.clear();
                liststock.add(Items);
            } else if (i == 0) {
                liststock.add(Items);
            } else {
                liststock.add(Items);
            }
        }
//        adapter = new RVAadapterStock(liststock, this);
        adapter = new RVAadapterStock(this, liststock, new RVAadapterStock.BtnClickListener() {

            @Override
            public void onBtnClick(int position) {
                // TODO Auto-generated method stub
                // Call your function which creates and shows the dialog here
                posisi = position;
                Toast.makeText(CustomerStock.this, liststock.get(position).getProductname(), Toast.LENGTH_SHORT).show();
                selectImage();
            }

        });
        rv.setAdapter(adapter);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            place.setVisibility(View.GONE);
            placeimage.setVisibility(View.VISIBLE);
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
                image = getStringImage(thumbnail);
                img.setImageBitmap(thumbnail);
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
                image = getStringImage(bm);
                filename = selectedImagePath.substring(selectedImagePath.lastIndexOf("/") + 1, selectedImagePath.length());
                img.setImageBitmap(bm);
            }
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerStock.this);
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


    private void selectCustomername() {
        customerspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                id_customer = ((TextView) view.findViewById(R.id.id_category)).getText().toString();

                if (!id_customer.equals("-1")) {
//                    adapter.customerId=id_customer;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDataCustomer() {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customer + salesId, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    jsonArray = new JSONArray(data);
                    parseData(jsonArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
    }

    private void placeholderSpinnerSales() {
        Modelcustomername placeholder = new Modelcustomername();
        placeholder.setName("Choose Customer");
        placeholder.setId("-1");
        listcustomer.add(placeholder);
    }

    private void parseData(JSONArray array) {
        placeholderSpinnerSales();
        for (int i = 0; i < array.length(); i++) {
            Modelcustomername Items = new Modelcustomername();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setName(json.getString("text"));
                Items.setId(json.getString("id"));
                Items.setBrn(json.getString("brn"));
                Items.setGstregdate(json.getString("gst_reg_date"));
                Items.setGstnumber(json.getString("gst_number"));
                Items.setVisitig(json.getString("visiting"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            listcustomer.add(Items);
        }
        customerspinner.setAdapter(new CustomerAdapter(CustomerStock.this, R.layout.spinnercustomername,
                listcustomer));
    }

    @Override
    public void onClick(View v) {
        if (v == submit) {
            place.setVisibility(View.VISIBLE);
            placeimage.setVisibility(View.GONE);
            liststock.get(posisi).setFilename(filename);
            adapter.notifyDataSetChanged();
        }
    }

    public class CustomerAdapter extends ArrayAdapter {
        List<Modelcustomername> listcustomer;

        public CustomerAdapter(Context context, int resource, List<Modelcustomername> listcustomer) {
            super(context, resource, listcustomer);
            this.listcustomer = listcustomer;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.spinnercustomername, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_category);
            id.setText(listcustomer.get(position).getId());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.nama_category);
            tvcustomer.setText(listcustomer.get(position).getName());
            TextView brn = (TextView) layout
                    .findViewById(R.id.brn);
            brn.setText(listcustomer.get(position).getBrn());
            TextView gst_reg_date = (TextView) layout
                    .findViewById(R.id.gst_reg_date);
            gst_reg_date.setText(listcustomer.get(position).getGstregdate());
            TextView gst_number = (TextView) layout
                    .findViewById(R.id.gst_number);
            gst_number.setText(listcustomer.get(position).getGstnumber());
            TextView visiting = (TextView) layout
                    .findViewById(R.id.visiting);
            visiting.setText(listcustomer.get(position).getVisitig());
            return layout;

        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }

    private void initial() {
        tvstartdate = (TextView) findViewById(R.id.date);
        customerspinner = (Spinner) findViewById(R.id.customerspinner);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        rv.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        rv.setFocusable(false);
        placeimage = (LinearLayout) findViewById(R.id.placeimage);
        place = (LinearLayout) findViewById(R.id.place);
        img = (ImageView) findViewById(R.id.img);
        submit = (ImageView) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    private void showDialogStartDate() {
        startdate = (RelativeLayout) findViewById(R.id.startdate);
        startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(Dialog_Id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == Dialog_Id) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            String bulan, tanggal;
            if (month_x < 10) {
                bulan = "0" + month_x;
            } else {
                bulan = String.valueOf(month_x);
            }
            if (day_x < 10) {
                tanggal = "0" + day_x;
            } else {
                tanggal = String.valueOf(day_x);
            }
            start = year_x + "-" + bulan + "-" + tanggal;
            tvstartdate.setText(start);
            if (!id_customer.equals("-1")) {
                getDataStock(id_customer, start);
            } else if (start.isEmpty() || id_customer.equals("1")) {
                liststock.clear();
                adapter = new RVAadapterStock(liststock, CustomerStock.this);
                rv.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
            }
        }
    };


}
