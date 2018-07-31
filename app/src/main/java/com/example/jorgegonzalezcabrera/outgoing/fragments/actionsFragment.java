package com.example.jorgegonzalezcabrera.outgoing.fragments;

import android.content.Context;
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
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.adapters.allEntriesAdapter;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration;
import com.example.jorgegonzalezcabrera.outgoing.others.HeaderItemDecoration.StickyHeaderInterface;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;

public class actionsFragment extends Fragment implements StickyHeaderInterface {

    RecyclerView recyclerViewAllTheActions;
    allEntriesAdapter adapter;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.actions_fragment, container, false);

        recyclerViewAllTheActions = view.findViewById(R.id.recyclerViewAllTheActions);
        RealmList<entry> allTheActions = new RealmList<>();
        allTheActions.addAll(Realm.getDefaultInstance().where(entry.class).findAll());
        adapter = new allEntriesAdapter(allTheActions);
        recyclerViewAllTheActions.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerViewAllTheActions.setLayoutManager(layoutManager);
        recyclerViewAllTheActions.addItemDecoration(new HeaderItemDecoration(R.layout.entries_by_month, this));
        recyclerViewAllTheActions.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));

        return view;
    }

    public void updateData(entry newEntry) {
        adapter.newEntryAdded(newEntry);
    }

    @Override
    public int getHeaderPositionForItem(int itemPosition) {
        return itemPosition;
    }

    @Override
    public void bindHeaderData(View header, int headerPosition) {
        TextView date = header.findViewById(R.id.textViewMonth);
        DateFormat df = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("es", "ES"));
        if(adapter.get(headerPosition)==null){
            date.setText(df.format(adapter.get(headerPosition + 1).getCreationDate()));
        } else{
            date.setText(df.format(adapter.get(headerPosition).getCreationDate()));
        }
    }
}
