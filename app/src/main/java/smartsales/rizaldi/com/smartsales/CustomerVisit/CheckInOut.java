package smartsales.rizaldi.com.smartsales.CustomerVisit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.text.format.Time;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smartsales.rizaldi.com.smartsales.DateToday;
import smartsales.rizaldi.com.smartsales.GPSTracker;
import smartsales.rizaldi.com.smartsales.R;
import smartsales.rizaldi.com.smartsales.Sales.ModelCustomerLocation;
import smartsales.rizaldi.com.smartsales.UrlLib;
import smartsales.rizaldi.com.smartsales.session.AppController;
import smartsales.rizaldi.com.smartsales.session.SqliteHandler;

public class CheckInOut extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener, CropImageView.OnGetCroppedImageCompleteListener {
    String schedule_id = "", customer_id = "", salesId = "", image, asal = "";
    static String filename = "", deskripsi = "";
    ImageButton addcamera, ibsave;
    public static final int MEDIA_TYPE_IMAGE = 1;
    ImageView imageCheck;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private static final String IMAGE_DIRECTORY_NAME = "Smart Sales camera";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private Uri filePath;
    TextView location;
    Geocoder geocoder;
    GPSTracker gps;
    double latitude;
    double longitude;
    EditText description;
    SqliteHandler db;
    private List<ModelCustomerLocation> mlocation; //getlokasi in vansales
    Spinner spinlokasicustomer;
    //    edit
    FloatingActionButton fabcrop, fab;
    LinearLayout placeresult, cropplace, spinlokasi, lokasi;
    private CropImageView mCropImageView;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 100;
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;
    Bitmap croppedImage, bitmapsresult;
    private Uri mCropImageUri;
    private Uri fileUri;
    String date;
    String timestamp = "";
    String salesposition = "", customerLocationId = "-1";
    ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        inisial();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        db = new SqliteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        salesposition = user.get("position_status");
        Time today = new Time(Time.getCurrentTimezone());
        today.setToNow();
        timestamp = today.format("%k:%M:%S");
        date = DateToday.dateNow();
        if (!isDeviceSuppportCamera()) {
            Toast.makeText(getApplicationContext(), "Your Device Not support Camera", Toast.LENGTH_LONG).show();
            finish();
        }

        fabcrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeresult.setVisibility(View.GONE);
                cropplace.setVisibility(View.VISIBLE);
                fabcrop.setVisibility(View.GONE);
                mCropImageView.getCroppedImageAsync(mCropImageView.getCropShape(), 0, 0);
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
//                captureImage();
            }
        });
        try {
            Bundle b = getIntent().getExtras();
            schedule_id = b.getString("scheduleId");
            salesId = b.getString("salesId");
            customer_id = b.getString("customerId");
            asal = b.getString("asal");
        } catch (Exception e) {

        }
        if (salesposition.equals("2")) {
            lokasi.setVisibility(View.GONE);
            spinlokasi.setVisibility(View.VISIBLE);
            getDataCustomerLocation(customer_id);
        }
        if (asal.equals("out") && salesposition.equals("2")) {
            spinlokasi.setVisibility(View.GONE);
            lokasi.setVisibility(View.VISIBLE);
        }
        ibsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deskripsi = description.getText().toString();
                if (salesposition.equals("2")) {
                    deskripsi = "description";
                }
                if (!deskripsi.isEmpty() && !filename.isEmpty()) {
                    if (salesposition.equals("1")) {
                        if (asal.equals("in")) {
                            UpdateImage(schedule_id, salesId, customer_id, deskripsi, filename, UrlLib.saveCheckIn);
                            Ischekin.chekin = true;
                        } else {
                            UpdateImage(schedule_id, salesId, customer_id, deskripsi, filename, UrlLib.saveCheckOut);
                            Ischekin.chekin = false;
                        }

                    } else if (salesposition.equals("2")) {
                        if (asal.equals("in")) {
                            if (!customerLocationId.equals("-1")) {
                                CheckinVansales(salesId, customer_id, customerLocationId, deskripsi, filename, UrlLib.saveCheckInVan);
                                Ischekin.checkinvansales = true;
                            } else {
                                Snackbar.make(v, "Chose Location", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            CheckOutVanSales(schedule_id, salesId, customer_id, filename, UrlLib.saveCheckOutVan);
                            Ischekin.checkinvansales = false;
                        }
                    }

                } else {
                    Snackbar.make(v, "Lengkapi Data", Snackbar.LENGTH_LONG).show();
                }
            }


        });
        latitude = gps.getLatitude();
        longitude = gps.getLongitude();
        gps = new GPSTracker(this);
        geocoder = new Geocoder(this, Locale.getDefault());

        GPS();
    }

    private void CheckOutVanSales(final String scheduleidparam, final String salesIdparam, final String customer_idparam, final String filenameparam, String url) {
        final ProgressDialog loading = ProgressDialog.show(this, "Saving...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(s);
                    String code = jObj.getString("code");
                    String msg = jObj.getString("message");
                    Log.e("cek", code);
                    if (code.equals("1")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CheckInOut.this, VisitVanSeles.class));

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
                image = getStringImage(bitmapsresult);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("schedule_id", scheduleidparam);
                params.put("salesId", salesIdparam);
                params.put("customerId", customer_idparam);
                params.put("filename", filenameparam);
                params.put("image", image);
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));
                params.put("tglImage", date);
                params.put("wktImage", timestamp);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void CheckinVansales(final String salesIdparam, final String customer_idparam, final String customerLocationIdparam, final String deskripsiparam, final String filenameparam, String url) {
        final ProgressDialog loading = ProgressDialog.show(this, "Saving...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(s);
                    String code = jObj.getString("code");
                    String msg = jObj.getString("message");
                    Log.e("cek", code);
                    if (code.equals("1")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CheckInOut.this, VisitVanSeles.class));

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
                image = getStringImage(bitmapsresult);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("salesId", salesIdparam);
                params.put("customerId", customer_idparam);
                params.put("customerLocationId", customerLocationIdparam);
                params.put("description", deskripsiparam);
                params.put("filename", filenameparam);
                params.put("image", image);
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));
                params.put("tglImage", date);
                params.put("wktImage", timestamp);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckInOut.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    startActivityForResult(getPickImageChooserIntent("camera"), 200);
                } else if (items[item].equals("Choose from Library")) {
                    startActivityForResult(getPickImageChooserIntent("gallery"), 200);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    //    setlokasi
    public class CustomerLocation extends ArrayAdapter {
        List<ModelCustomerLocation> listCustomer;

        public CustomerLocation(Context context, int resource, List<ModelCustomerLocation> listCustomer) {
            super(context, resource, listCustomer);
            this.listCustomer = listCustomer;
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.customerlocation_spinner, parent, false);
            TextView id = (TextView) layout.findViewById(R.id.id_customerlocation);
            id.setText(listCustomer.get(position).getIdlokasi());
            TextView tvcustomer = (TextView) layout
                    .findViewById(R.id.customerlocation);
            tvcustomer.setText(listCustomer.get(position).getLokasi());
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

    private void inisial() {
        imageCheck = (ImageView) findViewById(R.id.imageCheck);
        location = (TextView) findViewById(R.id.location);
        fabcrop = (FloatingActionButton) findViewById(R.id.savecrop);
        fab = (FloatingActionButton) findViewById(R.id.addcamera);
        description = (EditText) findViewById(R.id.etcatatan);
        ibsave = (ImageButton) findViewById(R.id.save);
        gps = new GPSTracker(this);
        geocoder = new Geocoder(this, Locale.getDefault());
        placeresult = (LinearLayout) findViewById(R.id.imgresult);
        cropplace = (LinearLayout) findViewById(R.id.imgplace);
        mCropImageView = (CropImageView) findViewById(R.id.CropImageView);//1
        mCropImageView.setFixedAspectRatio(true);
        mCropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);
        spinlokasi = (LinearLayout) findViewById(R.id.spinlokasi);
        lokasi = (LinearLayout) findViewById(R.id.lokasi);
        mlocation = new ArrayList<>();
        spinlokasicustomer = (Spinner) findViewById(R.id.customerlocation);
        spinlokasicustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    customerLocationId = ((TextView) view.findViewById(R.id.id_customerlocation)).getText().toString();
                } catch (Exception e) {
                    Log.e("cek id", "tidak ada data");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnGetCroppedImageCompleteListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mCropImageView.setOnSetImageUriCompleteListener(null);
        mCropImageView.setOnGetCroppedImageCompleteListener(null);
    }

    //    start cropimage
    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            placeresult.setVisibility(View.VISIBLE);
            cropplace.setVisibility(View.GONE);
            fabcrop.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(this, "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
        if (error == null) {
            croppedImage = bitmap;
            @SuppressLint("WrongViewCast")
            ImageView croppedImageView = (ImageView) findViewById(R.id.imageCheck);
            croppedImageView.setImageBitmap(croppedImage);
            Uri outputFileUri = getImageUri(getApplicationContext(), croppedImage);
            String path = getRealPathFromURI(getApplicationContext(), outputFileUri);
            filename = path.substring(path.lastIndexOf("/") + 1);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            bitmapsresult = BitmapFactory.decodeFile(path, options);
            if (outputFileUri != null) {
                Intent intent = new Intent();

                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }

        } else {
            Log.e("AIC", "Failed to crop image", error);
            Toast.makeText(this, "Image crop failed: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    private void getDataCustomerLocation(final String idcustomer) {
        final ProgressDialog loading = new ProgressDialog(this);
        loading.setMessage("please wait...");
        loading.setCancelable(false);
        loading.show();
        final StringRequest strReq = new StringRequest(Request.Method.GET, UrlLib.url_customerlocation + idcustomer, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                Log.e("JSON", s);
                JSONArray jsonArray = null;
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String data = jsonObject.getString("data");
                    String code = jsonObject.getString("code");
                    if (code.equals("1")) {
                        jsonArray = new JSONArray(data);
                        parseDataCustomerLocation(jsonArray);
                    } else {
                        String msg = jsonObject.getString("message");
                    }

                } catch (JSONException e) {
                    Log.e("error GC", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                NetworkResponse networkResponse = volleyError.networkResponse;
                Toast.makeText(getApplicationContext(),
                        volleyError.getMessage(), Toast.LENGTH_LONG).show();
                mlocation.clear();
                CustomerLocation adapter = new CustomerLocation(CheckInOut.this, R.layout.customerlocation_spinner,
                        mlocation);
                spinlokasicustomer.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                loading.dismiss();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(strReq);
        Log.e("e", strReq.toString());
    }

    private void parseDataCustomerLocation(JSONArray array) {
        int i;
        for (i = 0; i < array.length(); i++) {
            ModelCustomerLocation Items = new ModelCustomerLocation();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                Items.setIdlokasi(json.getString("id"));
                Items.setLokasi(json.getString("address"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (i == 0 && mlocation.size() != 0) {
                mlocation.clear();
                placeholderLocation();
                mlocation.add(Items);
            } else if (i == 0) {
                placeholderLocation();
                mlocation.add(Items);
            } else {
                mlocation.add(Items);
            }
        }
        CustomerLocation adapter = new CustomerLocation(CheckInOut.this, R.layout.customerlocation_spinner,
                mlocation);
        spinlokasicustomer.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void placeholderLocation() {
        ModelCustomerLocation Placeholder = new ModelCustomerLocation();
        Placeholder.setLokasi("Pilih Lokasi");
        Placeholder.setIdlokasi("-1");
        mlocation.add(Placeholder);
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    public boolean isUriRequiresPermissions(Uri uri) {
        try {
            ContentResolver resolver = getContentResolver();
            InputStream stream = resolver.openInputStream(uri);
            stream.close();
            return false;
        } catch (FileNotFoundException e) {
            if (e.getCause() instanceof ErrnoException) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = getPickImageResultUri(data);
            boolean requirePermissions = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    isUriRequiresPermissions(imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                requirePermissions = true;
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (!requirePermissions) {
                ((CropImageView) findViewById(R.id.CropImageView)).setImageUriAsync(imageUri);
            }
        }
    }

    private boolean isDeviceSuppportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    private Intent getPickImageChooserIntent(String source) {
        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();
        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();
        // collect all gallery intents

        if (source.equals("gallery")) {
            // collect all gallery intents
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
            for (ResolveInfo res : listGallery) {
                Intent intent = new Intent(galleryIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                allIntents.add(intent);
            }

        } else if (source.equals("camera")) {
            // collect all camera intents
            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
            for (ResolveInfo res : listCam) {
                Intent intent = new Intent(captureIntent);
                intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                intent.setPackage(res.activityInfo.packageName);
                if (outputFileUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
                allIntents.add(intent);
            }
        }
        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ((CropImageView) findViewById(R.id.CropImageView)).setImageUriAsync(mCropImageUri);
        } else
            Toast.makeText(this, "Required permissions are not granted", Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
        outState.putInt(ASPECT_RATIO_X, mAspectRatioX);
        outState.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fileUri = savedInstanceState.getParcelable("file_uri");
        mAspectRatioX = savedInstanceState.getInt(ASPECT_RATIO_X);
        mAspectRatioY = savedInstanceState.getInt(ASPECT_RATIO_Y);
    }

    //    coba


    private void UpdateImage(final String schedule_idparam, final String salesIdparam, final String customer_idparam, final String deskripsiparam, final String filenameparam, String url) {
        final ProgressDialog loading = ProgressDialog.show(this, "Saving...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                loading.dismiss();
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(s);
                    String code = jObj.getString("code");
                    String msg = jObj.getString("message");
                    Log.e("cek", code);
                    if (code.equals("1")) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        startActivity(new Intent(CheckInOut.this, CustomerVisit.class));

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
                image = getStringImage(bitmapsresult);
                Map<String, String> params = new Hashtable<String, String>();
                params.put("schedule_id", schedule_idparam);
                params.put("salesId", salesIdparam);
                params.put("customerId", customer_idparam);
                params.put("description", deskripsiparam);
                params.put("filename", filenameparam);
                params.put("image", image);
                params.put("lat", String.valueOf(latitude));
                params.put("lng", String.valueOf(longitude));
                params.put("tglImage", date);
                params.put("wktImage", timestamp);
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void GPS() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, UrlLib.MapsURL(latitude, longitude),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            JSONObject jsonArray = jsonObject.getJSONArray("results").getJSONObject(0);
                            String formatted_address = jsonArray.getString("formatted_address");

                            location.setText(formatted_address);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        filePath = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);

        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            filename = "IMG_" + timeStamp + ".jpg";
        } else {
            return null;
        }

        return mediaFile;
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                filePath = data.getData();
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                    imageCheck.setImageBitmap(bitmap);
//                } catch (IOException e) {
//
//                }
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        } else if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                previewCapturedImage();
//            } else if (resultCode == RESULT_CANCELED) {
//                Toast.makeText(getApplicationContext(),
//                        "User cancelled image capture", Toast.LENGTH_SHORT)
//                        .show();
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
//                        .show();
//            }
//        } else {
//
//        }
//    }

    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            bitmap = BitmapFactory.decodeFile(filePath.getPath(),
                    options);
            imageCheck.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
