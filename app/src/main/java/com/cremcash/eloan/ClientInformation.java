package com.cremcash.eloan;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cremcash.eloan.databinding.ActivityClientInformationBinding;
import com.cremcash.eloan.helper.ClientDataUploader;
import com.cremcash.eloan.helper.ImageResultHandler;
import com.cremcash.eloan.helper.OfflineDataHelper;
import com.cremcash.eloan.helper.PermissionHelper;
import com.cremcash.eloan.helper.TextWatcherHelper;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientInformation extends AppCompatActivity {
    private ActivityClientInformationBinding binding;

    public static TextInputEditText lastname_edit_text,firstname_edit_text,middlename_edit_text,age_edit_text,contact_edit_text,bplace_edit_text,email_edit_text,
    lastnameS_edit_text,firstnameS_edit_text,middlenameS_edit_text,bldgNo_edit_text,street_edit_text,brgy_edit_text,cityMun_edit_text, mmOthers_edit_text, term_edit_text,
            rate_edit_text, bDate_edit_text, amount_edit_text;

    final Calendar calendar = Calendar.getInstance();

    public static AutoCompleteTextView status_exposed_dropdown,gender_exposed_dropdown,province_exposed_dropdown, clientType_exposed_dropdown, mMode_exposed_dropdown, loan_purpose_exposed_dropdown, mode_exposed_dropdown, manner_exposed_dropdown;

    public static TextView textview;

    Connection con;

    String[] GENDER = new String[] {"Male", "Female"};
    String[] STATUS = new String[] {"Single", "Married", "Common Law", "Separated", "Widowed"};
    String[] PROVINCE = new String[] {"Abra", "Agusan del Norte", "Agusan del Sur", "Aklan", "Albay", "Antique", "Apayao"
            , "Aurora", "Basilan", "Bataan", "Batanes", "Batangas", "Benguet", "Biliran"
            , "Bohol", "Bukidnon", "Bulacan", "Cagayan", "Camarines Norte", "Camarines Sur"
            , "Camiguin", "Capiz", "Catanduanes", "Cavite", "Cebu", "Compostela Valley"
            , "Cotabato", "Davao del Norte", "Davao del Sur", "Davao Oriental", "Dinagat Islands"
            , "Eastern Samar", "Guimaras", "Ifugao", "Ilocos Norte", "Ilocos Sur", "Iloilo"
            , "Isabela", "Kalinga", "La Union", "Laguna", "Lanao del Norte", "Lanao del Sur"
            , "Leyte", "Maguindanao", "Marinduque", "Masbate", "Metro Manila", "Misamis Occidental"
            , "Misamis Oriental", "Mountain Province", "Negros Occidental", "Negros Oriental"
            , "Northern Samar", "Nueva Ecija", "Nueva Vizcaya", "Occidental Mindoro", "Oriental Mindoro"
            , "Palawan", "Pampanga", "Pangasinan", "Quezon", "Quirino", "Rizal", "Romblon", "Samar"
            , "Sarangani", "Shariff Kabunsuan", "Siquijor", "Sorsogon", "South Cotabato", "Southern Leyte"
            , "Sultan Kudarat", "Sulu", "Surigao del Norte", "Surigao del Sur", "Tarlac", "Tawi-Tawi"
            , "Zambales", "Zamboanga del Norte", "Zamboanga del Sur", "Zamboanga Sibugay"};

    String[] CTYPE = new String[]{"Individual", "Corporate"};
    String[] MMODE = new String[]{"Leaflets", "Tarpaulin", "Agent", "Referral", "LARC", "Social Media", "Solicit", "Others"};
    String[] LPURPOSE = new String[]{"Take Out","For Travel","Medical","Education","Land Prep.","Finance Truck","Purchase of Vehicles","Acquiring Assets","Home Improvement","Initial Capital","Additional Capital"};
    String[] MODEP = new String []{"Single", "Daily", "Weekly", "Semi-Monthly", "Monthly", "Quarterly", "Semestrally", "Annually"};

    String[] MANNER_BALLOON = new String[] {
            "Balloon Payment", "Monthly Interest"
    };

    String[] MANNER_SINGLE_DAILY = new String[] {
            "-"  // auto-selected
    };

    String formattedDate;

    @SuppressLint("StaticFieldLeak")
    public static ImageView imgID, imgID1, imgID2, imgID3;
    @SuppressLint("StaticFieldLeak")
    public static TextView textID,textID1,textID2, textID3, textIDno, textIDno1, textIDno2, textIDno3, appPic, spoPic, spoLabel, spo1;

    @SuppressLint("StaticFieldLeak")
    public static ImageView imgProfile, imgSpouseProfile;

    public static final int REQUEST_IMAGE = 100;
    public static final int REQUEST_IMAGE1 = 101;
    public static final int REQUEST_IMAGE2 = 102;
    public static final int REQUEST_IMAGE3 = 103;

    public static final int REQUEST_IMAGE_APP = 104;
    public static final int REQUEST_IMAGE_SPO = 105;

    public static Bitmap bitmap, bitmap1, bitmap2, bitmap3, bitmapApp, bitmapSpo;

    public static String uriString, uriString1, uriString2, uriString3;
    public static Uri uri, uri1, uri2, uri3, uriApp, uriSpo;

    public static Uri uriAppImg, uriSpoImg, uriID1, uriID2, uriID3, uriID4;

    public static String idChecker1, idChecker2, idChecker3, idChecker4;

    public static String imgChecker, imgCheckerSpo;

    public static String uriStringApp, uriStringSpouse;

    public static TextView appPath, spoPath;

    public static TextView coordinates;

    public static Button mapBtn, sigBtn, spoSigBtn;

    private static final String TAG = ClientInformation.class.getSimpleName();

    private CoordinatorLayout coordinatorLayout;

    private Uri filePath, filePath1, filePath2, filePath3, filePathSpouse, filePath0;

    public static String path, path0, path1, path2, path3, pathSpouse;

    String conStatus;

    private AlertDialog loadingDialog;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityClientInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle("Client Information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ArrayAdapter<String> adapter1= new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,GENDER);
        ArrayAdapter<String> adapter2= new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,STATUS);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,PROVINCE);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,CTYPE);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,MMODE);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,LPURPOSE);
        ArrayAdapter<String> adapter7 = new ArrayAdapter<>(getApplicationContext(),
                R.layout.dropdown_menu_popup_item,MODEP);

        lastname_edit_text = findViewById(R.id.lastname_edit_text);
        firstname_edit_text = findViewById(R.id.firstname_edit_text);
        middlename_edit_text = findViewById(R.id.middlename_edit_text);
        age_edit_text = findViewById(R.id.age_edit_text);
        contact_edit_text = findViewById(R.id.contact_edit_text);
        bplace_edit_text = findViewById(R.id.bplace_edit_text);
        email_edit_text = findViewById(R.id.email_edit_text);
        lastnameS_edit_text = findViewById(R.id.lastnameS_edit_text);
        firstnameS_edit_text = findViewById(R.id.firstnameS_edit_text);
        middlenameS_edit_text = findViewById(R.id.middlenameS_edit_text);
        bldgNo_edit_text = findViewById(R.id.bldgNo_edit_text);
        street_edit_text = findViewById(R.id.street_edit_text);
        brgy_edit_text = findViewById(R.id.brgy_edit_text);
        cityMun_edit_text = findViewById(R.id.cityMun_edit_text);
        status_exposed_dropdown = findViewById(R.id.status_exposed_dropdown);
        status_exposed_dropdown.setAdapter(adapter2);
        gender_exposed_dropdown = findViewById(R.id.gender_exposed_dropdown);
        gender_exposed_dropdown.setAdapter(adapter1);
        province_exposed_dropdown = findViewById(R.id.province_exposed_dropdown);
        province_exposed_dropdown.setAdapter(adapter3);
        clientType_exposed_dropdown = findViewById(R.id.clientType_exposed_dropdown);
        clientType_exposed_dropdown.setAdapter(adapter4);
        mMode_exposed_dropdown = findViewById(R.id.mMode_exposed_dropdown);
        mMode_exposed_dropdown.setAdapter(adapter5);
        mmOthers_edit_text = findViewById(R.id.mmOthers_edit_text);
        term_edit_text = findViewById(R.id.term_edit_text);
        rate_edit_text = findViewById(R.id.rate_edit_text);
        textview = findViewById(R.id.textView);
        bDate_edit_text = findViewById(R.id.bDate_edit_text);
        loan_purpose_exposed_dropdown = findViewById(R.id.loan_purpose_exposed_dropdown);
        loan_purpose_exposed_dropdown.setAdapter(adapter6);
        mode_exposed_dropdown = findViewById(R.id.mode_exposed_dropdown);
        mode_exposed_dropdown.setAdapter(adapter7);
        manner_exposed_dropdown = findViewById(R.id.manner_exposed_dropdown);
        amount_edit_text = findViewById(R.id.amount_edit_text);
        spoLabel = findViewById(R.id.spoLabel);
        spo1 = findViewById(R.id.spo1);

        mode_exposed_dropdown.setOnItemClickListener((parent, view, position, id) -> {
            String selected = (String) parent.getItemAtPosition(position);
            ArrayAdapter<String> mannerAdapter;

            if (selected.equals("Monthly") || selected.equals("Quarterly") ||
                    selected.equals("Semestrally") || selected.equals("Annually")){
                mannerAdapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        MANNER_BALLOON
                );

                manner_exposed_dropdown.setAdapter(mannerAdapter);
                manner_exposed_dropdown.setText("", false);
            } else if (selected.equals("Single") || selected.equals("Daily")) {
                mannerAdapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        MANNER_SINGLE_DAILY
                );

                manner_exposed_dropdown.setAdapter(mannerAdapter);
                manner_exposed_dropdown.setText("", false);
            } else {
                // Default: clear or empty
                mannerAdapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        R.layout.dropdown_menu_popup_item,
                        new String[] {}
                );

                manner_exposed_dropdown.setAdapter(mannerAdapter);
                manner_exposed_dropdown.setText("", false);
            }
        });

        imgID = findViewById(R.id.img_id);
        imgID1 = findViewById(R.id.img_id1);
        imgID2 = findViewById(R.id.img_id2);
        imgID3 = findViewById(R.id.img_id3);

        textID = findViewById(R.id.id1);
        textID1 = findViewById(R.id.id2);
        textID2 = findViewById(R.id.id3);
        textID3 = findViewById(R.id.id4);

        textIDno = findViewById(R.id.idNo1);
        textIDno1 = findViewById(R.id.idNo2);
        textIDno2 = findViewById(R.id.idNo3);
        textIDno3 = findViewById(R.id.idNo4);

        appPic = findViewById(R.id.app);
        spoPic = findViewById(R.id.spo);

        imgProfile = findViewById(R.id.img_profile);
        imgSpouseProfile = findViewById(R.id.img_spouse_profile);

        appPath = findViewById(R.id.appPath);
        spoPath = findViewById(R.id.spoPath);

        coordinates = findViewById(R.id.coordinates);

        mapBtn = findViewById(R.id.map_button);
        sigBtn = findViewById(R.id.sig_button);
        spoSigBtn = findViewById(R.id.sig_button_spo);

        FloatingActionButton fab = binding.fab;

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        LinearLayout spoInfo = findViewById(R.id.spoInfo);

        String mode_status = getIntent().getStringExtra("status");
        conStatus = mode_status;

        if ("offline".equals(mode_status)) {
            new AlertDialog.Builder(this)
                    .setTitle("Network Error")
                    .setIcon(R.drawable.baseline_error_24)
                    .setMessage("You are not connected to the network server.\nOffline Mode will activate...")
                    .setCancelable(true)
                    .setPositiveButton("OK", (dialog, id) -> {
                        Toast.makeText(getApplicationContext(), "Offline Mode Activated...", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    })
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "You are connected to the network...", Toast.LENGTH_LONG).show();
            // You can now safely call connectionClass() here if needed
            con = ConnectionClass.getConnection(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.db, ConnectionClass.ip);
            check_seq_id();
        }

        bDate_edit_text.setOnClickListener(v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ClientInformation.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Set selected date
                        String date = (selectedMonth + 1) + "/" + selectedDay + "/" + selectedYear;
                        bDate_edit_text.setText(date);

                        // Calculate and display age
                        int age = calculateAge(selectedYear, selectedMonth, selectedDay);
                        age_edit_text.setText(String.valueOf(age));
                    }, year, month, day);

            datePickerDialog.show();
        });

        fab.setOnClickListener(v -> {
            String status = ClientInformation.status_exposed_dropdown.getText().toString().trim();
            String appSignaturePath = ClientInformation.appPath.getText().toString().trim();
            String spouseSignaturePath = ClientInformation.spoPath.getText().toString().trim();

            boolean isSingle = isSingleStatus(status);
            boolean hasAppSignature = !appSignaturePath.isEmpty();
            boolean hasSpouseSignature = !spouseSignaturePath.isEmpty();

            if (isSingle) {
                if (!hasAppSignature) {
                    showAlert("Missing Signature", "Please provide the applicant's signature.");
                    return;
                }
            } else {
                if (!hasAppSignature && !hasSpouseSignature) {
                    showAlert("Missing Signatures", "Please provide signatures for both the applicant and the spouse.");
                    return;
                } else if (!hasAppSignature) {
                    showAlert("Missing Signature", "Please provide the applicant's signature.");
                    return;
                } else if (!hasSpouseSignature) {
                    showAlert("Missing Signature", "Please provide the spouse's signature.");
                    return;
                }
            }

            if (!areFieldsValid()) {
                return;
            }

            showLoadingDialog();

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler mainHandler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                try {
                    if ("offline".equalsIgnoreCase(conStatus)){
                        OfflineDataHelper.saveOfflineData(ClientInformation.this);
                    }else {
                        extractUriPaths();
                        ClientDataUploader uploader = new ClientDataUploader(this, this);
                        uploader.checkRecord(textview.getText().toString());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    mainHandler.post(() ->{
                        hideLoadingDialog();
                        showAlert("Error","Am error occurred: " + e.getMessage());
                    });
                    return;
                }

                mainHandler.post(() -> {
                    hideLoadingDialog();
                    Toast.makeText(ClientInformation.this, "Data saved successfully.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ClientInformation.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });

            });
        });

        imgID.setOnClickListener(view -> imagePermission());

        imgID1.setOnClickListener(view -> imagePermission1());

        imgID2.setOnClickListener(view -> imagePermission2());

        imgID3.setOnClickListener(view -> imagePermission3());

        imgProfile.setOnClickListener(view -> imagePermissionApp());

        imgSpouseProfile.setOnClickListener(view -> imagePermissionSpo());

        TextWatcherHelper.setRequiredValidation(clientType_exposed_dropdown, "Select Client Type");
        TextWatcherHelper.setRequiredValidation(mMode_exposed_dropdown, "Select Marketing Mode");
        TextWatcherHelper.setRequiredValidation(lastname_edit_text, "Enter Lastname");
        TextWatcherHelper.setRequiredValidation(firstname_edit_text, "Enter Firstname");
        TextWatcherHelper.setRequiredValidation(age_edit_text, "Enter Age");
        TextWatcherHelper.setRequiredValidation(contact_edit_text, "Enter Contact No.");
        TextWatcherHelper.setRequiredValidation(bplace_edit_text, "Enter Birthplace");
        TextWatcherHelper.setRequiredValidation(bDate_edit_text, "Enter Birthdate");

        status_exposed_dropdown.setOnItemClickListener((parent, view, position, id) -> {
            String status = ClientInformation.status_exposed_dropdown.getText().toString().trim();

            boolean isSingle = isSingleStatus(status);

            if (isSingle) {
                lastnameS_edit_text.setVisibility(View.GONE);
                firstnameS_edit_text.setVisibility(View.GONE);
                middlenameS_edit_text.setVisibility(View.GONE);
                spoInfo.setVisibility(View.GONE);
                spoLabel.setVisibility(View.GONE);
                imgSpouseProfile.setVisibility(View.GONE);
                spo1.setVisibility(View.GONE);
                spoSigBtn.setVisibility(View.GONE);

            }else{
                lastnameS_edit_text.setVisibility(View.VISIBLE);
                firstnameS_edit_text.setVisibility(View.VISIBLE);
                middlenameS_edit_text.setVisibility(View.VISIBLE);
                spoInfo.setVisibility(View.VISIBLE);
                spoLabel.setVisibility(View.VISIBLE);
                imgSpouseProfile.setVisibility(View.VISIBLE);
                spo1.setVisibility(View.VISIBLE);
                spoSigBtn.setVisibility(View.VISIBLE);
                TextWatcherHelper.setRequiredValidation(lastnameS_edit_text,"Enter Lastname");
                TextWatcherHelper.setRequiredValidation(firstnameS_edit_text,"Enter Firstname");
            }
        });

        TextWatcherHelper.setRequiredValidation(bldgNo_edit_text, "Enter Bldg / No.");
        TextWatcherHelper.setRequiredValidation(street_edit_text, "Enter Street");
        TextWatcherHelper.setRequiredValidation(brgy_edit_text, "Enter Brgy.");
        TextWatcherHelper.setRequiredValidation(cityMun_edit_text, "Enter City/Municipality");

        TextWatcherHelper.setRequiredValidation(status_exposed_dropdown, "Select Status");
        TextWatcherHelper.setRequiredValidation(gender_exposed_dropdown, "Select Gender");
        TextWatcherHelper.setRequiredValidation(province_exposed_dropdown, "Select Province");
        TextWatcherHelper.setRequiredValidation(loan_purpose_exposed_dropdown, "Select Loan Purpose");

        TextWatcherHelper.setAmountFormatter(amount_edit_text,"Enter Amount");

        TextWatcherHelper.setRequiredValidation(term_edit_text, "Enter Term");
        TextWatcherHelper.setRequiredValidation(rate_edit_text, "Enter Rate");
        TextWatcherHelper.setRequiredValidation(mode_exposed_dropdown, "Select Mode of Payment");
//        TextWatcherHelper.setRequiredValidation(manner_exposed_dropdown, "Select Manner of Payment");

        sigBtn.setOnClickListener(v -> {
            Intent i = new Intent(this,
                    ESignature.class);
            startActivity(i);
        });

        spoSigBtn.setOnClickListener(v -> {
            Intent i = new Intent(this,
                    SESignature.class);
            startActivity(i);
        });

        mapBtn.setOnClickListener(v -> {
            if (haveNetwork()){
                Intent i = new Intent(this,
                        MapsActivity.class);
                startActivity(i);
            } else if (!haveNetwork()) {
                Toast.makeText(this, "Network connection is not available", Toast.LENGTH_SHORT).show();
            }
        });

        Bundle bundle = this.getIntent().getExtras();

        Intent intent = this.getIntent();
        String mode = intent.getStringExtra("mode");

        if ("load".equals(mode) || "view".equals(mode)){
            String lastname = bundle.getString("lastname");
            String firstname = bundle.getString("firstname");
            String middlename = bundle.getString("middlename");
            String status = bundle.getString("status");
            String age = bundle.getString("age");
            String contact = bundle.getString("contact");
            String bplace = bundle.getString("bplace");
            String gender = bundle.getString("gender");
            String email = bundle.getString("email");
            String slastname = bundle.getString("slastname");
            String sfirstname = bundle.getString("sfirstname");
            String smiddlename = bundle.getString("smiddlename");
            String bldgno = bundle.getString("bldgno");
            String street = bundle.getString("street");
            String brgy = bundle.getString("brgy");
            String city = bundle.getString("city");
            String prov = bundle.getString("prov");
            String ctype = bundle.getString("ctype");
            String mmode = bundle.getString("mmode");
            String mmothers = bundle.getString("mmothers");
            String term = bundle.getString("term");
            String rate = bundle.getString("rate");
            String dateentry = bundle.getString("dateentry");
            String bdate = bundle.getString("bdate");
            String loanpurpose = bundle.getString("loanpurpose");
            String amountapplied = bundle.getString("amountapplied");
            String modepayment = bundle.getString("modepayment");
            String mannerpayment = bundle.getString("mannerpayment");

            lastname_edit_text.setText(lastname);
            firstname_edit_text.setText(firstname);
            middlename_edit_text.setText(middlename);
            status_exposed_dropdown.setText(status);
            age_edit_text.setText(age);
            contact_edit_text.setText(contact);
            bplace_edit_text.setText(bplace);
            gender_exposed_dropdown.setText(gender);
            email_edit_text.setText(email);
            lastnameS_edit_text.setText(slastname);
            firstnameS_edit_text.setText(sfirstname);
            middlenameS_edit_text.setText(smiddlename);
            bldgNo_edit_text.setText(bldgno);
            street_edit_text.setText(street);
            brgy_edit_text.setText(brgy);
            cityMun_edit_text.setText(city);
            province_exposed_dropdown.setText(prov);
            clientType_exposed_dropdown.setText(ctype);
            mMode_exposed_dropdown.setText(mmode);
            mmOthers_edit_text.setText(mmothers);
            term_edit_text.setText(term);
            rate_edit_text.setText(rate);
            bDate_edit_text.setText(bdate);
            loan_purpose_exposed_dropdown.setText(loanpurpose);
            amount_edit_text.setText(amountapplied);
            mode_exposed_dropdown.setText(modepayment);
            manner_exposed_dropdown.setText(mannerpayment);

            String load_idtype = bundle.getString("idtype");
            String load_idno = bundle.getString("idno");
            String load_idtype1 = bundle.getString("idtype1");
            String load_idno1 = bundle.getString("idno1");
            String load_idtype2 = bundle.getString("idtype2");
            String load_idno2 = bundle.getString("idno2");
            String load_idtype3 = bundle.getString("idtype3");
            String load_idno3 = bundle.getString("idno3");
            String load_map = bundle.getString("map");
            String load_sig = bundle.getString("sig");
            String load_sig_spo = bundle.getString("sigSpo");


            textID.setText(load_idtype);
            textIDno.setText(load_idno);
            textID1.setText(load_idtype1);
            textIDno1.setText(load_idno1);
            textID2.setText(load_idtype2);
            textIDno2.setText(load_idno2);
            textID3.setText(load_idtype3);
            textIDno3.setText(load_idno3);
            coordinates.setText(load_map);
            appPath.setText(load_sig);
            spoPath.setText(load_sig_spo);

            ExecutorService executor = Executors.newFixedThreadPool(3);
            Handler handler = new Handler(Looper.getMainLooper());

            if (!lastname_edit_text.getText().toString().isEmpty()){
                executor.execute(() -> {
                    File imgFile = new File(this.getExternalFilesDir(null), lastname_edit_text.getText().toString().concat("_").concat(firstname_edit_text.getText().toString().concat("_2x2.jpg")));

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        handler.post(() ->{
                            imgProfile.setImageBitmap(myBitmap);
                            uriApp = Uri.parse(imgFile.getAbsolutePath());
                            loadProfile(uriApp.toString());
                            uriStringApp = uriApp.toString();
                            appPic.setText(uriStringApp);
                            imgChecker = "new";
                        });
                    }
                });

            }

            if (!lastnameS_edit_text.getText().toString().isEmpty()){
                executor.execute(() ->{
                    File imgFile = new File(this.getExternalFilesDir(null), lastnameS_edit_text.getText().toString().concat("_").concat(firstnameS_edit_text.getText().toString().concat("_S_2x2.jpg")));

                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        handler.post(() -> {
                            imgSpouseProfile.setImageBitmap(myBitmap);
                            uriSpo = Uri.parse(imgFile.getAbsolutePath());
                            loadProfileSpo(uriSpo.toString());
                            uriStringSpouse = uriSpo.toString();
                            spoPic.setText(uriStringSpouse);
                            imgCheckerSpo = "new";
                        });
                    }
                });

            }

            if (!textID.getText().toString().isEmpty()){
                if (textID.getText().toString().equals(textID2.getText().toString()) || textID.getText().toString().equals(textID3.getText().toString())){
                    executor.execute(() -> {
                        File imgFile = new File(this.getExternalFilesDir(null), lastname_edit_text.getText().toString().concat("_").concat(textID.getText().toString().concat("1.jpg")));

                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                            handler.post(() -> {
                                imgID.setImageBitmap(myBitmap);
                                uri = Uri.parse(imgFile.getAbsolutePath());
                                uriString = uri.toString();
                                idChecker1 = "new";
                            });

                        }
                    });

                }else{
                    executor.execute(() -> {
                        File imgFile = new File(this.getExternalFilesDir(null), lastname_edit_text.getText().toString().concat("_").concat(textID.getText().toString().concat(".jpg")));

                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                            handler.post(() -> {
                                imgID.setImageBitmap(myBitmap);
                                uri = Uri.parse(imgFile.getAbsolutePath());
                                uriString = uri.toString();
                                idChecker1 = "new";
                            });

                        }
                    });

                }

            }

            if (!textID1.getText().toString().isEmpty()){
                if (textID1.getText().toString().equals(textID2.getText().toString()) || textID1.getText().toString().equals(textID3.getText().toString())){
                    executor.execute(() -> {
                        File imgFile1 = new File(this.getExternalFilesDir(null), lastname_edit_text.getText().toString().concat("_").concat(textID1.getText().toString().concat("1.jpg")));

                        if(imgFile1.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());

                            handler.post(() -> {
                                imgID1.setImageBitmap(myBitmap);
                                uri1 = Uri.parse(imgFile1.getAbsolutePath());
                                uriString1 = uri1.toString();
                                idChecker2 = "new";
                            });
                        }
                    });

                }else{
                    executor.execute(() -> {
                        File imgFile1 = new File(this.getExternalFilesDir(null), lastname_edit_text.getText().toString().concat("_").concat(textID1.getText().toString().concat(".jpg")));

                        if(imgFile1.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile1.getAbsolutePath());

                            handler.post(() -> {
                                imgID1.setImageBitmap(myBitmap);
                                uri1 = Uri.parse(imgFile1.getAbsolutePath());
                                uriString1 = uri1.toString();
                                idChecker2 = "new";
                            });
                        }
                    });
                }
            }

            if (!textID2.getText().toString().isEmpty()){
                if (textID2.getText().toString().equals(textID.getText().toString()) || textID2.getText().toString().equals(textID1.getText().toString())){
                    executor.execute(() -> {
                        File imgFile2 = new File(this.getExternalFilesDir(null), lastnameS_edit_text.getText().toString().concat("_").concat(textID2.getText().toString().concat("1.jpg")));

                        if(imgFile2.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());

                            handler.post(() -> {
                                imgID2.setImageBitmap(myBitmap);
                                uri2 = Uri.parse(imgFile2.getAbsolutePath());
                                uriString2 = uri2.toString();
                                idChecker3 = "new";
                            });

                        }
                    });

                }else{
                    executor.execute(() -> {
                        File imgFile2 = new File(this.getExternalFilesDir(null), lastnameS_edit_text.getText().toString().concat("_").concat(textID2.getText().toString().concat(".jpg")));

                        if(imgFile2.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile2.getAbsolutePath());

                            handler.post(() -> {
                                imgID2.setImageBitmap(myBitmap);
                                uri2 = Uri.parse(imgFile2.getAbsolutePath());
                                uriString2 = uri2.toString();
                                idChecker3 = "new";
                            });
                        }
                    });
                }
            }

            if (!textID3.getText().toString().isEmpty()){
                if (textID3.getText().toString().equals(textID.getText().toString()) || textID3.getText().toString().equals(textID1.getText().toString())){
                    executor.execute(() -> {
                        File imgFile3 = new File(this.getExternalFilesDir(null), lastnameS_edit_text.getText().toString().concat("_").concat(textID3.getText().toString().concat("1.jpg")));

                        if(imgFile3.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());

                            handler.post(() -> {
                                imgID3.setImageBitmap(myBitmap);
                                uri3 = Uri.parse(imgFile3.getAbsolutePath());
                                uriString3 = uri3.toString();
                                idChecker4 = "new";
                            });
                        }
                    });
                }else{
                    executor.execute(() -> {
                        File imgFile3 = new File(this.getExternalFilesDir(null), lastnameS_edit_text.getText().toString().concat("_").concat(textID3.getText().toString().concat(".jpg")));

                        if(imgFile3.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile3.getAbsolutePath());

                            handler.post(() -> {
                                imgID3.setImageBitmap(myBitmap);
                                uri3 = Uri.parse(imgFile3.getAbsolutePath());
                                uriString3 = uri3.toString();
                                idChecker4 = "new";
                            });
                        }
                    });
                }
            }
        }else {
            lastname_edit_text.setText("");
            firstname_edit_text.setText("");
            middlename_edit_text.setText("");
            status_exposed_dropdown.setText("");
            age_edit_text.setText("");
            contact_edit_text.setText("");
            bplace_edit_text.setText("");
            gender_exposed_dropdown.setText("");
            email_edit_text.setText("");
            lastnameS_edit_text.setText("");
            firstnameS_edit_text.setText("");
            middlenameS_edit_text.setText("");
            bldgNo_edit_text.setText("");
            street_edit_text.setText("");
            brgy_edit_text.setText("");
            cityMun_edit_text.setText("");
            province_exposed_dropdown.setText("");
            clientType_exposed_dropdown.setText("");
            mMode_exposed_dropdown.setText("");
            mmOthers_edit_text.setText("");
            term_edit_text.setText("");
            rate_edit_text.setText("");

            bDate_edit_text.setText("");
            loan_purpose_exposed_dropdown.setText("");
            amount_edit_text.setText("");
            mode_exposed_dropdown.setText("");
            manner_exposed_dropdown.setText("");

            textID.setText("");
            textIDno.setText("");
            textID1.setText("");
            textIDno1.setText("");
            textID2.setText("");
            textIDno2.setText("");
            textID3.setText("");
            textIDno3.setText("");

            imgProfile.setImageResource(R.drawable.baseline_account_circle_24);
            imgSpouseProfile.setImageResource(R.drawable.baseline_account_circle_24);

            imgID.setImageResource(R.drawable.baseline_camera_24);
            imgID1.setImageResource(R.drawable.baseline_camera_24);
            imgID2.setImageResource(R.drawable.baseline_camera_24);
            imgID3.setImageResource(R.drawable.baseline_camera_24);
        }

    }

    private int calculateAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        dob.set(year, month, day);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        return age;
    }

    private void showLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_loading, null);
        builder.setView(dialogView);

        loadingDialog = builder.create();
        loadingDialog.show();
    }

    private void hideLoadingDialog(){
        if (loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }

    private boolean areFieldsValid() {
        if (lastname_edit_text.getText().toString().trim().isEmpty() ||
                firstname_edit_text.getText().toString().trim().isEmpty() ||
                age_edit_text.getText().toString().trim().isEmpty() ||
                contact_edit_text.getText().toString().trim().isEmpty() ||
                bplace_edit_text.getText().toString().trim().isEmpty() ||
                bldgNo_edit_text.getText().toString().trim().isEmpty() ||
                street_edit_text.getText().toString().trim().isEmpty() ||
                brgy_edit_text.getText().toString().trim().isEmpty() ||
                cityMun_edit_text.getText().toString().trim().isEmpty() ||
                term_edit_text.getText().toString().trim().isEmpty() ||
                rate_edit_text.getText().toString().trim().isEmpty() ||

                status_exposed_dropdown.getText().toString().trim().isEmpty() ||
                gender_exposed_dropdown.getText().toString().trim().isEmpty() ||
                province_exposed_dropdown.getText().toString().trim().isEmpty() ||
                clientType_exposed_dropdown.getText().toString().trim().isEmpty()) {

            Toast.makeText(this, "Please fill in all required fields.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean isSingleStatus(String status) {
        return status.equals("Single") || status.equals("Widowed") || status.equals("Separated");
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> {});
        builder.create().show();
    }

    private void extractUriPaths() {
        filePath = parseUri(ClientInformation.uriStringApp, "App Path");
        path = filePath != null ? filePath.toString() : "";

        filePathSpouse = parseUri(ClientInformation.uriStringSpouse, "Spo Path");
        pathSpouse = filePathSpouse != null ? filePathSpouse.toString() : "";

        filePath0 = parseUri(ClientInformation.uriString, "ID1 Path");
        path0 = filePath0 != null ? filePath0.toString() : "";

        filePath1 = parseUri(ClientInformation.uriString1, "ID2 Path");
        path1 = filePath1 != null ? filePath1.toString() : "";

        filePath2 = parseUri(ClientInformation.uriString2, "ID3 Path");
        path2 = filePath2 != null ? filePath2.toString() : "";

        filePath3 = parseUri(ClientInformation.uriString3, "ID4 Path");
        path3 = filePath3 != null ? filePath3.toString() : "";
    }

    private Uri parseUri(String uriString, String logTag) {
        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            String finalPath = uri.toString();
            if (!finalPath.contains("/storage/")) {
                finalPath = finalPath.substring(1);
            }
            Log.d(TAG, "onCreate: " + logTag + " " + finalPath);
            return uri;
        }
        return null;
    }


    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Glide.with(this).load(url)
                    .into(imgProfile);
        }
        imgProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
        imgChecker = "new";
    }

    private void loadProfileSpo(String url) {
        Log.d(TAG, "Image cache path: " + url);

        Glide.with(this).load(url)
                .into(imgSpouseProfile);
        imgSpouseProfile.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
        imgCheckerSpo = "new";
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    private boolean haveNetwork(){
        boolean have_WIFI= false;
        boolean have_MobileData = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))if (info.isConnected())have_WIFI=true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))if (info.isConnected())have_MobileData=true;
        }
        return have_WIFI||have_MobileData;
    }

    void imagePermission(){
        PermissionHelper.showImagePermissionDialog(
                this,
                textID,
                textIDno,
                this::showImagePickerOptions,
                this::showSettingsDialog
        );
    }

    void imagePermission1() {
        PermissionHelper.showImagePermissionDialog(
                this,
                textID1,
                textIDno1,
                this::showImagePickerOptions1,
                this::showSettingsDialog1
        );
    }

    void imagePermission2() {
        PermissionHelper.showImagePermissionDialog(
                this,
                textID2,
                textIDno2,
                this::showImagePickerOptions2,
                this::showSettingsDialog2
        );
    }

    void imagePermission3() {
        PermissionHelper.showImagePermissionDialog(
                this,
                textID3,
                textIDno3,
                this::showImagePickerOptions3,
                this::showSettingsDialog3
        );
    }

    void imagePermissionApp() {
        PermissionHelper.requestCameraPermission(
                this,
                this::showImagePickerOptionsApp,
                this::showSettingsDialogApp
        );
    }

    void imagePermissionSpo() {
        PermissionHelper.requestCameraPermission(
                this,
                this::showImagePickerOptionsSpo,
                this::showSettingsDialogSpo
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) return;

        switch (requestCode) {
            case REQUEST_IMAGE:
                uriID1 = ImageResultHandler.handleImageResult(this, data, imgID, null, TAG);
                idChecker1 = "new";
                uriString = uriID1.toString();
                break;

            case REQUEST_IMAGE1:
                uriID2 = ImageResultHandler.handleImageResult(this, data, imgID1, null, TAG);
                idChecker2 = "new";
                uriString1 = uriID2.toString();
                break;

            case REQUEST_IMAGE2:
                uriID3 = ImageResultHandler.handleImageResult(this, data, imgID2, null, TAG);
                idChecker3 = "new";
                uriString2 = uriID3.toString();
                break;

            case REQUEST_IMAGE3:
                uriID4 = ImageResultHandler.handleImageResult(this, data, imgID3, null, TAG);
                idChecker4 = "new";
                uriString3 = uriID4.toString();
                break;

            case REQUEST_IMAGE_APP:
                uriAppImg = ImageResultHandler.handleImageResult(this, data, imgProfile, appPic, TAG);
                imgChecker = "new";
                uriStringApp = uriAppImg.toString();
                break;

            case REQUEST_IMAGE_SPO:
                uriSpoImg = ImageResultHandler.handleImageResult(this, data, imgSpouseProfile, spoPic, TAG);
                imgCheckerSpo = "new";
                uriStringSpouse = uriSpoImg.toString();
                break;
        }
    }

    private void showSettingsDialog(){
        PermissionHelper.showSettingsDialog(this, this::openSettings);
    }

    private void showSettingsDialog1() {
        PermissionHelper.showSettingsDialog(this, this::openSettings1);
    }

    private void showSettingsDialog2() {
        PermissionHelper.showSettingsDialog(this, this::openSettings2);
    }

    private void showSettingsDialog3() {
        PermissionHelper.showSettingsDialog(this, this::openSettings3);
    }

    private void showSettingsDialogApp() {
        PermissionHelper.showSettingsDialog(this, this::openSettingsApp);
    }

    private void showSettingsDialogSpo() {
        PermissionHelper.showSettingsDialog(this, this::openSettingsSpo);
    }

    private void openSettings1() {
        PermissionHelper.openAppSettings(this, REQUEST_IMAGE1);
    }

    private void openSettings2() {
        PermissionHelper.openAppSettings(this, REQUEST_IMAGE2);
    }

    private void openSettings3() {
        PermissionHelper.openAppSettings(this, REQUEST_IMAGE3);
    }

    private void openSettingsApp() {
        PermissionHelper.openAppSettings(this, REQUEST_IMAGE_APP);
    }

    private void openSettingsSpo() {
        PermissionHelper.openAppSettings(this, REQUEST_IMAGE_SPO);
    }

    private void openSettings() {
        PermissionHelper.openAppSettings(this, REQUEST_IMAGE);
    }

    private void showImagePickerOptions() {
        PermissionHelper.showImagePickerOptions(
                this,
                this::launchCameraIntent,
                this::launchGalleryIntent
        );
    }

    private void showImagePickerOptions1() {
        PermissionHelper.showImagePickerOptions(
                this,
                this::launchCameraIntent1,
                this::launchGalleryIntent1
        );
    }

    private void showImagePickerOptions2() {
        PermissionHelper.showImagePickerOptions(
                this,
                this::launchCameraIntent2,
                this::launchGalleryIntent2
        );
    }

    private void showImagePickerOptions3() {
        PermissionHelper.showImagePickerOptions(
                this,
                this::launchCameraIntent3,
                this::launchGalleryIntent3
        );
    }

    private void showImagePickerOptionsApp() {
        PermissionHelper.showImagePickerOptions(
                this,
                this::launchCameraIntentApp,
                this::launchGalleryIntentApp
        );
    }

    private void showImagePickerOptionsSpo() {
        PermissionHelper.showImagePickerOptions(
                this,
                this::launchCameraIntentSpo,
                this::launchGalleryIntentSpo
        );
    }

    private void launchCameraIntent1() {
        PermissionHelper.launchCameraIntent(this, REQUEST_IMAGE1);
    }

    private void launchGalleryIntent1() {
        PermissionHelper.launchGalleryIntent(this, REQUEST_IMAGE1);
    }

    private void launchCameraIntent2() {
        PermissionHelper.launchCameraIntent(this, REQUEST_IMAGE2);
    }

    private void launchGalleryIntent2() {
        PermissionHelper.launchGalleryIntent(this, REQUEST_IMAGE2);
    }

    private void launchCameraIntent3() {
        PermissionHelper.launchCameraIntent(this, REQUEST_IMAGE3);
    }

    private void launchGalleryIntent3() {
        PermissionHelper.launchGalleryIntent(this, REQUEST_IMAGE3);
    }

    private void launchCameraIntentApp() {
        PermissionHelper.launchCameraIntent(this, REQUEST_IMAGE_APP);
    }

    private void launchGalleryIntentApp() {
        PermissionHelper.launchGalleryIntent(this, REQUEST_IMAGE_APP);
    }

    private void launchCameraIntentSpo() {
        PermissionHelper.launchCameraIntent(this, REQUEST_IMAGE_SPO);
    }

    private void launchGalleryIntentSpo() {
        PermissionHelper.launchGalleryIntent(this, REQUEST_IMAGE_SPO);
    }

    private void launchCameraIntent() {
        PermissionHelper.launchCameraIntent(this, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        PermissionHelper.launchGalleryIntent(this, REQUEST_IMAGE);
    }

    public void check_seq_id() {
        if (!"online".equalsIgnoreCase(conStatus)) {
            Log.d("CheckSeqID", "Offline mode: Skipping check_seq_id()");
            return;
        }
        // Run on background thread
        new Thread(() -> {
            try {
                con = ConnectionClass.getConnection(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.db, ConnectionClass.ip);
                if (con == null) {
                    runOnUiThread(() -> {
                        Toast.makeText(ClientInformation.this, "Check Internet Connection", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                String sql = "SELECT COUNT(SeqID) AS seq FROM EloanApp";
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                String resultDate;
                Date c = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyMMdd");

                if (rs.next()) {
                    int intSeq = rs.getInt("seq");
                    intSeq += 1;
                    resultDate = df.format(c).concat(String.valueOf(intSeq));
                } else {
                    resultDate = df.format(c).concat("1");
                }
                // Update UI
                String finalResultDate = resultDate;
                runOnUiThread(() -> {
                    formattedDate = finalResultDate;
                    textview.setText(formattedDate);
                });
            } catch (Exception e) {
                Log.e("SQL Error", "check_seq_id: " + e.getMessage());
                runOnUiThread(() -> {
                    Toast.makeText(ClientInformation.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }
}