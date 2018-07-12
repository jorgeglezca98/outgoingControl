package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.allActionsAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;
import static android.support.v7.widget.RecyclerView.VERTICAL;

public class actionsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actions_fragment,container,false);

        RecyclerView recyclerViewAllTheActions = view.findViewById(R.id.recyclerViewAllTheActions);
        RealmList<entry> parameter = new RealmList<>();
        parameter.addAll(Realm.getDefaultInstance().where(entry.class).findAll());
        recyclerViewAllTheActions.setAdapter(new allActionsAdapter(getContext(),parameter));
        recyclerViewAllTheActions.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
