package com.cremcash.eloan.ui.info;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cremcash.eloan.R;
import com.cremcash.eloan.SQLiteHandler;
import com.cremcash.eloan.SessionManager;
import com.cremcash.eloan.UserLoginActivity;

public class InfoFragment extends Fragment {

    private InfoViewModel mViewModel;

    private SessionManager session;

    private SQLiteHandler db;

    Button btnLogout;

    public static InfoFragment newInstance() {
        return new InfoFragment();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        btnLogout = view.findViewById(R.id.btn_logout);

        session = new SessionManager(this.getContext());

        db = new SQLiteHandler(this.getContext());

        btnLogout.setOnClickListener(view1 -> logoutUser());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        // TODO: Use the ViewModel
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(InfoFragment.this.getContext(), UserLoginActivity.class);
        startActivity(intent);
        this.requireActivity().finish();
    }

}