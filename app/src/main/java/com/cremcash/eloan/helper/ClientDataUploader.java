package com.cremcash.eloan.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.cremcash.eloan.ClientInformation;
import com.cremcash.eloan.ConnectionClass;
import com.cremcash.eloan.SessionManager;

import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientDataUploader {
    private final Context context;
    private final Connection con;
    private final ClientInformation ci;

    public ClientDataUploader(Context context, ClientInformation ci) {
        this.context = context;
        this.ci = ci;
        this.con = ConnectionClass.getConnection(
                ConnectionClass.un,
                ConnectionClass.pass,
                ConnectionClass.db,
                ConnectionClass.ip
        );
    }

    public void checkRecord(String seqID) {
        if (con == null) {
            showToast("Check Internet Connection");
            return;
        }

        try {
            String sql = "SELECT * FROM EloanApp WHERE Lastname = ? AND Firstname = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, ci.lastname_edit_text.getText().toString());
            stmt.setString(2, ci.firstname_edit_text.getText().toString());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                showToast("Client Exist...");
            } else {
                insertData(seqID);
            }
        } catch (Exception e) {
            Log.e("SQL Error", e.getMessage());
        }
    }

    public void insertData(String seqID) {
        if (con == null) {
            showToast("Check Internet Connection");
            return;
        }

        try {
            byte[] appImg = loadImageBytes(ci.appPic.getText().toString());
            byte[] spoImg = loadImageBytes(ci.spoPic.getText().toString());
            byte[] id0 = loadImageBytes(ci.path0);
            byte[] id1 = loadImageBytes(ci.path1);
            byte[] id2 = loadImageBytes(ci.path2);
            byte[] id3 = loadImageBytes(ci.path3);
            byte[] sigA = loadImageBytes(ci.appPath.getText().toString());
            byte[] sigS = loadImageBytes(ci.spoPath.getText().toString());

            String uid = new SessionManager(context).getUserId();
            String branchCode = getBranchCode(uid);

            String manufacturer = Build.MANUFACTURER;
            String model = Build.MODEL;
            String device = Build.DEVICE;

            String userDeviceName = Settings.Secure.getString(context.getContentResolver(), "bluetooth_name");
            if (userDeviceName == null || userDeviceName.isEmpty()) {
                userDeviceName = model;
            }

            String technicalDetails = manufacturer + " " + model + " (" + device + ")";

            String deviceDetails = userDeviceName + " - " + technicalDetails;

            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO EloanApp(" +
                            "SeqID, ClientType, MarketingMode, MMOthers, Lastname, Firstname, Middlename, " +
                            "Status, Age, ContactNo, Birthplace, Birthdate, Gender, Email, " +
                            "SLastname, SFirstname, SMiddlename, BldgNo, Street, Brgy, CityMun, Province, " +
                            "LoanPurpose, AmountApplied, Term, Rate, ModePayment, MannerPayment, AppImage, " +
                            "SpoImage, AppSig, SpoSig, IDType1, IDType2, IDNo1, IDNo2, AppID1, AppID2, " +
                            "SIDType1, SIDType2, SIDNo1, SIDNo2, SpoID1, SpoID2, Map, AppStatus, " +
                            "CClassification, DateEntry, Staff, Device) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );

            ps.setString(1, seqID);
            ps.setString(2, ci.clientType_exposed_dropdown.getText().toString());
            ps.setString(3, ci.mMode_exposed_dropdown.getText().toString());
            ps.setString(4, ci.mmOthers_edit_text.getText().toString());
            ps.setString(5, ci.lastname_edit_text.getText().toString());
            ps.setString(6, ci.firstname_edit_text.getText().toString());
            ps.setString(7, ci.middlename_edit_text.getText().toString());
            ps.setString(8, ci.status_exposed_dropdown.getText().toString());
            ps.setString(9, ci.age_edit_text.getText().toString());
            ps.setString(10, ci.contact_edit_text.getText().toString());
            ps.setString(11, ci.bplace_edit_text.getText().toString());
            ps.setString(12, ci.bDate_edit_text.getText().toString());
            ps.setString(13, ci.gender_exposed_dropdown.getText().toString());
            ps.setString(14, ci.email_edit_text.getText().toString());
            ps.setString(15, ci.lastnameS_edit_text.getText().toString());
            ps.setString(16, ci.firstnameS_edit_text.getText().toString());
            ps.setString(17, ci.middlenameS_edit_text.getText().toString());
            ps.setString(18, ci.bldgNo_edit_text.getText().toString());
            ps.setString(19, ci.street_edit_text.getText().toString());
            ps.setString(20, ci.brgy_edit_text.getText().toString());
            ps.setString(21, ci.cityMun_edit_text.getText().toString());
            ps.setString(22, ci.province_exposed_dropdown.getText().toString());
            ps.setString(23, ci.loan_purpose_exposed_dropdown.getText().toString());
            ps.setString(24, ci.amount_edit_text.getText().toString().replace(",", ""));
            ps.setString(25, ci.term_edit_text.getText().toString());
            ps.setString(26, ci.rate_edit_text.getText().toString());
            ps.setString(27, ci.mode_exposed_dropdown.getText().toString());
            ps.setString(28, "Monthly Interest");
            ps.setBytes(29, appImg);
            ps.setBytes(30, spoImg);
            ps.setBytes(31, sigA);
            ps.setBytes(32, sigS);
            ps.setString(33, ci.textID.getText().toString());
            ps.setString(34, ci.textID1.getText().toString());
            ps.setString(35, ci.textIDno.getText().toString());
            ps.setString(36, ci.textIDno1.getText().toString());
            ps.setBytes(37, id0);
            ps.setBytes(38, id1);
            ps.setString(39, ci.textID2.getText().toString());
            ps.setString(40, ci.textID3.getText().toString());
            ps.setString(41, ci.textIDno2.getText().toString());
            ps.setString(42, ci.textIDno3.getText().toString());
            ps.setBytes(43, id2);
            ps.setBytes(44, id3);
            ps.setString(45, ci.coordinates.getText().toString());
            ps.setString(46, null);
            ps.setString(47, branchCode);
            ps.setDate(48, currentDate);
            ps.setString(49, uid);
            ps.setString(50, deviceDetails);

            ps.execute();
            showToast("Client Successfully Uploaded!");
        } catch (Exception e) {
            Log.e("InsertData Error", e.getMessage());
        }
    }

    private byte[] loadImageBytes(String path) {
        if (path == null || path.isEmpty()) return null;
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            Log.e("Image Error", "Failed to load image from " + path);
            return null;
        }
    }

    private String getBranchCode(String uid) throws SQLException {
        String branch = null;
        PreparedStatement stmt = con.prepareStatement("SELECT Staff_BranchCode FROM StaffMember WHERE Staff_IDNo = ?");
        stmt.setString(1, uid);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            branch = rs.getString("Staff_BranchCode");
        }
        return branch;
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        );
    }


}
