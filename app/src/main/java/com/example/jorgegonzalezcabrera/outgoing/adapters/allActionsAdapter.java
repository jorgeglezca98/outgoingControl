package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;

import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.utils.firstDateOfTheMonth;

public class allActionsAdapter extends RecyclerView.Adapter<allActionsAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private Vector<RealmList<entry>> entries;

    public allActionsAdapter(Context context, RealmList<entry> allEntries) {
        this.context = context;
        this.layout = R.layout.actions_by_month;

        Date firstDayOfMonth = firstDateOfTheMonth(new Date());
        entries = new Vector<>();
        entries.add(new RealmList<entry>());
        for(int i=allEntries.size()-1;i>=0;i--){
            if(firstDayOfMonth.before(allEntries.get(i).getDate())){
                entries.lastElement().add(allEntries.get(i));
            } else{
                entries.add(new RealmList<entry>());
                entries.lastElement().add(allEntries.get(i));
                firstDayOfMonth = firstDateOfTheMonth(allEntries.get(i).getDate());
            }
        }
    }

    public void newEntryAdded(entry newEntry){
        entries.firstElement().add(0,newEntry);
        notifyItemChanged(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new allActionsAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(entries.get(i));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private RecyclerView entriesByMonth;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.textViewMonth);
            entriesByMonth = itemView.findViewById(R.id.recyclerViewActionsOfTheMonth);
        }

        public void bind(RealmList<entry> entriesOfTheMonth) {
            DateFormat df = new SimpleDateFormat("MMMM 'de' yyyy",  new Locale("es", "ES"));
            if(entriesOfTheMonth.size()>0) {
                //TODO: look for another option to solve this problem
                date.setText(df.format(entriesOfTheMonth.first().getDate()));
                entriesByMonth.setAdapter(new actionsAdapter(entriesOfTheMonth));
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                entriesByMonth.setLayoutManager(layoutManager);
                entriesByMonth.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
            }
        }
    }
}
