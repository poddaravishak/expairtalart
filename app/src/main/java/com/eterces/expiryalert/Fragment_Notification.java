package com.eterces.expiryalert;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;


public class Fragment_Notification extends Fragment {

    private Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment__notification, container, false);
        toolbar = view.findViewById(R.id.mytoolbar);

        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("");

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
    }
}