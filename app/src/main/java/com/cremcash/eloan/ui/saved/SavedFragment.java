package com.cremcash.eloan.ui.saved;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.cremcash.eloan.ClientInformation;
import com.cremcash.eloan.ConnectionClass;
import com.cremcash.eloan.R;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class SavedFragment extends Fragment {

    Connection con;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_saved, container, false);

        ListView fileNameList = view.findViewById(R.id.listViewFiles);

        ArrayList<String> filenames = new ArrayList<>();

        File directory = new File(getActivity().getExternalFilesDir(null), "");
        File[] files = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".xls"));

        for (int i = 0; i < files.length; i++)
        {

            String file_name = files[i].getName();

            filenames.add(file_name);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, filenames);

        fileNameList.setAdapter(arrayAdapter);

        fileNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedFile = filenames.get(i);

                con = connectionClass(ConnectionClass.un, ConnectionClass.pass, ConnectionClass.db, ConnectionClass.ip);
                if (con != null){
                    Toast.makeText(getActivity(),"Connected", Toast.LENGTH_LONG)
                            .show();
                    try{
                        File file = new File(requireContext().getExternalFilesDir(null), selectedFile);
                        FileInputStream myInput = new FileInputStream(file);

                        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

                        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

                        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                        HSSFSheet mySheetAtt = myWorkBook.getSheetAt(1);

                        String load_lastname = mySheet.getRow(1).getCell(0).toString();
                        String load_firstname = mySheet.getRow(1).getCell(1).toString();
                        String load_middlename = mySheet.getRow(1).getCell(2).toString();
                        String load_status = mySheet.getRow(1).getCell(3).toString();
                        String load_age = mySheet.getRow(1).getCell(4).toString();
                        String load_contact = mySheet.getRow(1).getCell(5).toString();
                        String load_bplace = mySheet.getRow(1).getCell(6).toString();
                        String load_gender = mySheet.getRow(1).getCell(7).toString();
                        String load_email = mySheet.getRow(1).getCell(8).toString();
                        String load_slastname = mySheet.getRow(1).getCell(9).toString();
                        String load_sfirstname = mySheet.getRow(1).getCell(10).toString();
                        String load_smiddlename = mySheet.getRow(1).getCell(11).toString();
                        String load_bldgno = mySheet.getRow(1).getCell(12).toString();
                        String load_street = mySheet.getRow(1).getCell(13).toString();
                        String load_brgy = mySheet.getRow(1).getCell(14).toString();
                        String load_city = mySheet.getRow(1).getCell(15).toString();
                        String load_prov = mySheet.getRow(1).getCell(16).toString();

                        String load_idtype = mySheetAtt.getRow(1).getCell(0).toString();
                        String load_idno = mySheetAtt.getRow(1).getCell(1).toString();
                        String load_idtype1 = mySheetAtt.getRow(2).getCell(0).toString();
                        String load_idno1 = mySheetAtt.getRow(2).getCell(1).toString();
                        String load_idtype2 = mySheetAtt.getRow(3).getCell(0).toString();
                        String load_idno2 = mySheetAtt.getRow(3).getCell(1).toString();
                        String load_idtype3 = mySheetAtt.getRow(4).getCell(0).toString();
                        String load_idno3 = mySheetAtt.getRow(4).getCell(1).toString();

                        String load_map = mySheetAtt.getRow(1).getCell(1).toString();

                        String load_sig = mySheetAtt.getRow(1).getCell(2).toString();
                        String load_sig_spo = mySheetAtt.getRow(2).getCell(2).toString();

                        Intent intent = new Intent(SavedFragment.this.getContext(), ClientInformation.class);
                        Bundle bundle = new Bundle();

                        String btn_save = "enable";

                        bundle.putString("lastname", load_lastname);
                        bundle.putString("firstname", load_firstname);
                        bundle.putString("middlename", load_middlename);
                        bundle.putString("status", load_status);
                        bundle.putString("age", load_age);
                        bundle.putString("contact", load_contact);
                        bundle.putString("bplace", load_bplace);
                        bundle.putString("gender", load_gender);
                        bundle.putString("email", load_email);
                        bundle.putString("slastname", load_slastname);
                        bundle.putString("sfirstname", load_sfirstname);
                        bundle.putString("smiddlename", load_smiddlename);
                        bundle.putString("bldgno", load_bldgno);
                        bundle.putString("street", load_street);
                        bundle.putString("brgy", load_brgy);
                        bundle.putString("city", load_city);
                        bundle.putString("prov", load_prov);

                        bundle.putString("idtype", load_idtype);
                        bundle.putString("idno", load_idno);
                        bundle.putString("idtype1", load_idtype1);
                        bundle.putString("idno1", load_idno1);
                        bundle.putString("idtype2", load_idtype2);
                        bundle.putString("idno2", load_idno2);
                        bundle.putString("idtype3", load_idtype3);
                        bundle.putString("idno3", load_idno3);

                        bundle.putString("map", load_map);

                        bundle.putString("sig", load_sig);
                        bundle.putString("sigSpo", load_sig_spo);

                        bundle.putString("enable", btn_save);

                        intent.putExtras(bundle);

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                        builder1.setTitle("Reminder");
                        builder1.setIcon(R.drawable.baseline_info_24);
                        builder1.setMessage("Please double check the information before saving.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                (dialog, id2) -> {
                                    intent.putExtra("mode", "load");
                                    startActivity(intent);
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getActivity(),"Not Connected", Toast.LENGTH_LONG)
                            .show();

                    try{
                        File file = new File(requireContext().getExternalFilesDir(null), selectedFile);
                        FileInputStream myInput = new FileInputStream(file);

                        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

                        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

                        HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                        HSSFSheet mySheetAtt = myWorkBook.getSheetAt(1);

                        String load_lastname = mySheet.getRow(1).getCell(0).toString();
                        String load_firstname = mySheet.getRow(1).getCell(1).toString();
                        String load_middlename = mySheet.getRow(1).getCell(2).toString();
                        String load_status = mySheet.getRow(1).getCell(3).toString();
                        String load_age = mySheet.getRow(1).getCell(4).toString();
                        String load_contact = mySheet.getRow(1).getCell(5).toString();
                        String load_bplace = mySheet.getRow(1).getCell(6).toString();
                        String load_gender = mySheet.getRow(1).getCell(7).toString();
                        String load_email = mySheet.getRow(1).getCell(8).toString();
                        String load_slastname = mySheet.getRow(1).getCell(9).toString();
                        String load_sfirstname = mySheet.getRow(1).getCell(10).toString();
                        String load_smiddlename = mySheet.getRow(1).getCell(11).toString();
                        String load_bldgno = mySheet.getRow(1).getCell(12).toString();
                        String load_street = mySheet.getRow(1).getCell(13).toString();
                        String load_brgy = mySheet.getRow(1).getCell(14).toString();
                        String load_city = mySheet.getRow(1).getCell(15).toString();
                        String load_prov = mySheet.getRow(1).getCell(16).toString();
                        String load_ctype = mySheet.getRow(1).getCell(17).toString();
                        String load_mmode = mySheet.getRow(1).getCell(18).toString();
                        String load_mmothers = mySheet.getRow(1).getCell(19).toString();
                        String load_term = mySheet.getRow(1).getCell(20).toString();
                        String load_rate = mySheet.getRow(1).getCell(21).toString();
                        String load_dateentry = mySheet.getRow(1).getCell(22).toString();
                        String load_bdate = mySheet.getRow(1).getCell(23).toString();
                        String load_loanpurpose = mySheet.getRow(1).getCell(24).toString();
                        String load_amountapplied = mySheet.getRow(1).getCell(25).toString();
                        String load_modepayment = mySheet.getRow(1).getCell(26).toString();
                        String load_mannerpayment = mySheet.getRow(1).getCell(27).toString();

                        String load_idtype = mySheetAtt.getRow(1).getCell(0).toString();
                        String load_idno = mySheetAtt.getRow(1).getCell(1).toString();
                        String load_idtype1 = mySheetAtt.getRow(2).getCell(0).toString();
                        String load_idno1 = mySheetAtt.getRow(2).getCell(1).toString();
                        String load_idtype2 = mySheetAtt.getRow(3).getCell(0).toString();
                        String load_idno2 = mySheetAtt.getRow(3).getCell(1).toString();
                        String load_idtype3 = mySheetAtt.getRow(4).getCell(0).toString();
                        String load_idno3 = mySheetAtt.getRow(4).getCell(1).toString();

                        String load_map = mySheetAtt.getRow(1).getCell(1).toString();

                        String load_sig = mySheetAtt.getRow(1).getCell(2).toString();
                        String load_sig_spo = mySheetAtt.getRow(2).getCell(2).toString();

                        Intent intent = new Intent(SavedFragment.this.getContext(), ClientInformation.class);
                        Bundle bundle = new Bundle();

                        String btn_save = "disable";

                        bundle.putString("lastname", load_lastname);
                        bundle.putString("firstname", load_firstname);
                        bundle.putString("middlename", load_middlename);
                        bundle.putString("status", load_status);
                        bundle.putString("age", load_age);
                        bundle.putString("contact", load_contact);
                        bundle.putString("bplace", load_bplace);
                        bundle.putString("gender", load_gender);
                        bundle.putString("email", load_email);
                        bundle.putString("slastname", load_slastname);
                        bundle.putString("sfirstname", load_sfirstname);
                        bundle.putString("smiddlename", load_smiddlename);
                        bundle.putString("bldgno", load_bldgno);
                        bundle.putString("street", load_street);
                        bundle.putString("brgy", load_brgy);
                        bundle.putString("city", load_city);
                        bundle.putString("prov", load_prov);
                        bundle.putString("ctype", load_ctype);
                        bundle.putString("mmode", load_mmode);
                        bundle.putString("mmothers", load_mmothers);
                        bundle.putString("term", load_term);
                        bundle.putString("rate", load_rate);
                        bundle.putString("dateentry", load_dateentry);
                        bundle.putString("bdate", load_bdate);
                        bundle.putString("loanpurpose", load_loanpurpose);
                        bundle.putString("amountapplied", load_amountapplied);
                        bundle.putString("modepayment", load_modepayment);
                        bundle.putString("mannerpayment", load_mannerpayment);

                        bundle.putString("idtype", load_idtype);
                        bundle.putString("idno", load_idno);
                        bundle.putString("idtype1", load_idtype1);
                        bundle.putString("idno1", load_idno1);
                        bundle.putString("idtype2", load_idtype2);
                        bundle.putString("idno2", load_idno2);
                        bundle.putString("idtype3", load_idtype3);
                        bundle.putString("idno3", load_idno3);

                        bundle.putString("map", load_map);

                        bundle.putString("sig", load_sig);
                        bundle.putString("sigSpo", load_sig_spo);

                        bundle.putString("disable", btn_save);

                        intent.putExtras(bundle);

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(requireContext());
                        builder1.setTitle("Reminder");
                        builder1.setIcon(R.drawable.baseline_info_24);
                        builder1.setMessage("Please double check the information before saving.");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "OK",
                                (dialog, id2) -> {
                                    intent.putExtra("mode", "view");
                                    startActivity(intent);
                                });
                        AlertDialog alert11 = builder1.create();
                        alert11.show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
        return view;

    }

    @SuppressLint("NewApi")
    public Connection connectionClass(String user, String password, String database, String server){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String connectionURL = null;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connectionURL = "jdbc:jtds:sqlserver://" + server+"/" + database + ";user=" + user + ";password=" + password + ";";
            connection = DriverManager.getConnection(connectionURL);
        }catch (Exception e){
            Log.e("SQL Connection Error : ", e.getMessage());
        }

        return connection;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}