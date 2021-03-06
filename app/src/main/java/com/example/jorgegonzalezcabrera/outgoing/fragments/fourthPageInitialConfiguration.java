package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.newMoneyControllersAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.category;
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;

import java.util.Vector;

import io.realm.RealmList;

public class fourthPageInitialConfiguration extends Fragment {

    private newMoneyControllersAdapter moneyControllerAdapter;
    private LinearLayoutManager moneyControllerLayoutManager;
    private RealmList<category> categories;
    private ScrollView scrollViewContainer;

    public fourthPageInitialConfiguration() {
        categories = new RealmList<>();
        moneyControllerAdapter = new newMoneyControllersAdapter(getContext(), categories);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fourth_fragment_initial_configuration, container, false);

        RecyclerView moneyControllerRecyclerView = view.findViewById(R.id.recyclerViewMoneyControllersRequest);
        moneyControllerRecyclerView.setAdapter(moneyControllerAdapter);
        moneyControllerLayoutManager = new LinearLayoutManager(getContext());
        moneyControllerRecyclerView.setLayoutManager(moneyControllerLayoutManager);

        scrollViewContainer = view.findViewById(R.id.scrollViewContainer);

        return view;
    }

    public void setCategories(RealmList<category> categories) {
        this.categories = categories;
        this.moneyControllerAdapter.setCategories(this.categories);
    }

    public void addOne() {
        moneyControllerAdapter.addOne();
        if (scrollViewContainer != null)
            scrollViewContainer.post(new Runnable() {
                public void run() {
                    scrollViewContainer.fullScroll(View.FOCUS_DOWN);
                }
            });
    }

    public boolean checkData() {
        Vector<moneyController> adapterItems = moneyControllerAdapter.getData();
        int i = 0;
        while (i < adapterItems.size()) {
            if (!adapterItems.get(i).check()) {
                return false;
            }
            i++;
        }
        return true;
    }

    public RealmList<moneyController> getData() {
        RealmList<moneyController> data = new RealmList<>();
        data.addAll(moneyControllerAdapter.getData());
        return data;
    }
}
