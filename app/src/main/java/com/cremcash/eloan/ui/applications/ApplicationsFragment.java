package com.cremcash.eloan.ui.applications;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cremcash.eloan.ApplicationsAdapter;
import com.cremcash.eloan.ConnectionClass;
import com.cremcash.eloan.GridItem;
import com.cremcash.eloan.GridViewAdapter;
import com.cremcash.eloan.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ApplicationsFragment extends Fragment {

    private ArrayList<GridItem> mGridData = new ArrayList<>();
    private GridView gridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;

    private RecyclerView recyclerView;
    private ApplicationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applications, container, false);

//        gridView = view.findViewById(R.id.gridView);
        recyclerView = view.findViewById(R.id.recyclerView);
        mProgressBar = view.findViewById(R.id.progressBar);

//        mGridAdapter = new GridViewAdapter(getContext(), R.layout.grid_item_layout, mGridData);
//        gridView.setAdapter(mGridAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ApplicationsAdapter(getContext(), mGridData);
        recyclerView.setAdapter(adapter);

//        MainActivity activity = (MainActivity) getActivity();
//        if (activity != null && activity.getBottomAppBar() != null) {
//            activity.getBottomAppBar().post(() -> {
//                int bottomPadding = activity.getBottomAppBar().getHeight();
//                gridView.setPadding(0, 0, 0, bottomPadding);
//            });
//        }

//        gridView.setOnItemClickListener((parent, v, position, id) -> {
//            GridItem item = (GridItem) parent.getItemAtPosition(position);
//            Toast.makeText(getContext(), item.getId() + " " + item.getTitle(), Toast.LENGTH_LONG).show();
//        });

        new LoadApplicationsTask().execute();

        return view;
    }

    private class LoadApplicationsTask extends AsyncTask<Void, Void, ArrayList<GridItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mGridData.clear();
            adapter.notifyDataSetChanged(); // optional, ensures UI reflects cleared data
        }

        @Override
        protected ArrayList<GridItem> doInBackground(Void... voids) {
            ArrayList<GridItem> results = new ArrayList<>();

            try (Connection con = getConnection()) {
                if (con != null) {
                    String query = "SELECT SeqID, Firstname, Lastname, AppImage FROM EloanApp";
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) {
                        GridItem item = new GridItem();
                        item.setId(rs.getString("SeqID"));
                        item.setTitle(rs.getString("Firstname") + " " + rs.getString("Lastname"));

                        byte[] imageBytes = rs.getBytes("AppImage");
                        if (imageBytes != null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            item.setImageBitmap(bmp);
                        }

                        results.add(item);
                    }
                } else {
                    Log.e("DB", "Connection failed.");
                }
            } catch (Exception e) {
                Log.e("LoadApplicationsTask", "Error loading data", e);
            }

            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<GridItem> result) {
//            mGridData.addAll(result);
//            mGridAdapter.notifyDataSetChanged();
//            mProgressBar.setVisibility(View.GONE);
            mGridData.addAll(result);
            adapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }
    }

    private Connection getConnection() {
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String url = "jdbc:jtds:sqlserver://" + ConnectionClass.ip + "/" + ConnectionClass.db +
                    ";user=" + ConnectionClass.un + ";password=" + ConnectionClass.pass + ";";
            return DriverManager.getConnection(url);
        } catch (Exception e) {
            Log.e("SQL Connection Error", e.getMessage());
            return null;
        }
    }
}
