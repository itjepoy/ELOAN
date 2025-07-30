package com.cremcash.eloan.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cremcash.eloan.ClientInformation;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OfflineDataHelper {
    private static final String TAG = "OfflineDataHelper";

    public static void saveOfflineData(Context context) {
        ClientInformation ci = (ClientInformation) context;

        boolean isSingle = ci.status_exposed_dropdown.getText().toString().equals("Single") ||
                ci.status_exposed_dropdown.getText().toString().equals("Widowed") ||
                ci.status_exposed_dropdown.getText().toString().equals("Separated");

        File mainFile = new File(context.getExternalFilesDir(null),
                ci.lastname_edit_text.getText().toString() + "_" +
                        ci.firstname_edit_text.getText().toString() + "_2x2.jpg");

        try (FileOutputStream fos = new FileOutputStream(mainFile)) {
            ci.bitmapApp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String idStr = getOrDefault(ci.textID.getText().toString());
        String idStr1 = getOrDefault(ci.textID1.getText().toString());
        String idStr2 = getOrDefault(ci.textID2.getText().toString());
        String idStr3 = getOrDefault(ci.textID3.getText().toString());

        String strID = (!idStr.equals(idStr2) || !idStr.equals(idStr3)) ? idStr : idStr + "_1";
        String strID1 = (!idStr1.equals(idStr2) || !idStr1.equals(idStr3)) ? idStr1 : idStr1 + "_1";
        String strID2 = (!idStr2.equals(idStr) && !idStr2.equals(idStr1)) ? idStr2 : idStr2 + "_1";
        String strID3 = (!idStr3.equals(idStr) && !idStr3.equals(idStr1)) ? idStr3 : idStr3 + "_1";

        // Save ID images based on logic
        saveIDImages(context, ci, isSingle, idStr, idStr1, idStr2, idStr3, strID, strID1, strID2, strID3);

        // Save Excel
        saveExcelFile(context, ci.lastname_edit_text.getText().toString() + ", " +
                ci.firstname_edit_text.getText().toString() + " " +
                ci.middlename_edit_text.getText().toString() + ".xls");

        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(context, "Client Successfully Uploaded!", Toast.LENGTH_LONG).show()
        );

    }

    private static void saveIDImages(Context context, ClientInformation ci, boolean isSingle,
                                     String idStr, String idStr1, String idStr2, String idStr3,
                                     String strID, String strID1, String strID2, String strID3) {

        try {
            if (isSingle) {
                if (!idStr.equals("empty String") && !idStr1.equals("empty String")) {
                    writeImage(ci.bitmap, new File(context.getExternalFilesDir(null), ci.lastname_edit_text.getText() + "_" + strID + ".jpg"));
                    writeImage(ci.bitmap1, new File(context.getExternalFilesDir(null), ci.lastname_edit_text.getText() + "_" + strID1 + ".jpg"));
                } else if (!idStr.equals("empty String")) {
                    writeImage(ci.bitmap, new File(context.getExternalFilesDir(null), ci.lastname_edit_text.getText() + "_" + strID + ".jpg"));
                }
            } else {
                writeImage(ci.bitmapSpo, new File(context.getExternalFilesDir(null), ci.lastnameS_edit_text.getText() + "_" + ci.firstnameS_edit_text.getText() + "_S_2x2.jpg"));

                if (!idStr.equals("empty String")) writeImage(ci.bitmap, new File(context.getExternalFilesDir(null), ci.lastname_edit_text.getText() + "_" + idStr + ".jpg"));
                if (!idStr1.equals("empty String")) writeImage(ci.bitmap1, new File(context.getExternalFilesDir(null), ci.lastname_edit_text.getText() + "_" + idStr1 + ".jpg"));
                if (!idStr2.equals("empty String")) writeImage(ci.bitmap2, new File(context.getExternalFilesDir(null), ci.lastnameS_edit_text.getText() + "_" + idStr2 + ".jpg"));
                if (!idStr3.equals("empty String")) writeImage(ci.bitmap3, new File(context.getExternalFilesDir(null), ci.lastnameS_edit_text.getText() + "_" + idStr3 + ".jpg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeImage(Bitmap bitmap, File file) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        }
    }

    private static String getOrDefault(String value) {
        return (value == null || value.trim().isEmpty()) ? "empty String" : value;
    }

    public static boolean saveExcelFile(Context context, String fileName) {
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            Log.e(TAG, "Storage not available or read only");
            return false;
        }

        ClientInformation ci = (ClientInformation) context;
        boolean success;
        Workbook wb = new HSSFWorkbook();

        Sheet sheet1 = wb.createSheet("clientinformation");
        Sheet sheet2 = wb.createSheet("attachments");

        Row header = sheet1.createRow(0);
        Row data = sheet1.createRow(1);

        String[] headers = {
                "lastname", "firstname", "middlename", "status", "age", "contactNo", "bplace", "gender",
                "email", "slastname", "sfirstname", "smiddlename", "bldgno", "street", "brgy", "citymun",
                "province", "ClientType", "MarketingMode", "MMOthers", "Term", "Rate", "DateEntry",
                "bdate", "loanpurpose", "amountapplied", "modepayment", "mannerpayment"
        };

        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }

        String[] values = {
                ci.lastname_edit_text.getText().toString(),
                ci.firstname_edit_text.getText().toString(),
                ci.middlename_edit_text.getText().toString(),
                ci.status_exposed_dropdown.getText().toString(),
                ci.age_edit_text.getText().toString(),
                ci.contact_edit_text.getText().toString(),
                ci.bplace_edit_text.getText().toString(),
                ci.gender_exposed_dropdown.getText().toString(),
                ci.email_edit_text.getText().toString(),
                ci.lastnameS_edit_text.getText().toString(),
                ci.firstnameS_edit_text.getText().toString(),
                ci.middlenameS_edit_text.getText().toString(),
                ci.bldgNo_edit_text.getText().toString(),
                ci.street_edit_text.getText().toString(),
                ci.brgy_edit_text.getText().toString(),
                ci.cityMun_edit_text.getText().toString(),
                ci.province_exposed_dropdown.getText().toString(),
                ci.clientType_exposed_dropdown.getText().toString(),
                ci.mMode_exposed_dropdown.getText().toString(),
                ci.mmOthers_edit_text.getText().toString(),
                ci.term_edit_text.getText().toString(),
                ci.rate_edit_text.getText().toString(),
                (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) ? LocalDate.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy")) : "",
                ci.bDate_edit_text.getText().toString(),
                ci.loan_purpose_exposed_dropdown.getText().toString(),
                ci.amount_edit_text.getText().toString(),
                ci.mode_exposed_dropdown.getText().toString(),
                "Monthly Interest"
        };

        for (int i = 0; i < values.length; i++) {
            data.createCell(i).setCellValue(values[i]);
            sheet1.setColumnWidth(i, 15 * 500);
        }

        Row s1 = sheet2.createRow(0);
        String[] attachHeaders = {"idtype", "idno", "map", "sig"};
        for (int i = 0; i < attachHeaders.length; i++) {
            s1.createCell(i).setCellValue(attachHeaders[i]);
        }

        String[][] attachData = {
                {ci.textID.getText().toString(), ci.textIDno.getText().toString(), ci.coordinates.getText().toString(), ci.appPath.getText().toString()},
                {ci.textID1.getText().toString(), ci.textIDno1.getText().toString(), "", ci.spoPath.getText().toString()},
                {ci.textID2.getText().toString(), ci.textIDno2.getText().toString(), "", ""},
                {ci.textID3.getText().toString(), ci.textIDno3.getText().toString(), "", ""}
        };

        for (int i = 0; i < attachData.length; i++) {
            Row r = sheet2.createRow(i + 1);
            for (int j = 0; j < attachData[i].length; j++) {
                r.createCell(j).setCellValue(attachData[i][j]);
            }
        }

        File file = new File(context.getExternalFilesDir(null), fileName);
        try (FileOutputStream os = new FileOutputStream(file)) {
            wb.write(os);
            success = true;
        } catch (IOException e) {
            Log.w(TAG, "Error writing file: " + file, e);
            success = false;
        }
        return success;
    }

    private static boolean isExternalStorageAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    private static boolean isExternalStorageReadOnly() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED_READ_ONLY);
    }
}
