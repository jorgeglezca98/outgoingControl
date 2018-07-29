package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.utils.firstDateOfTheMonth;

public class allActionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int layout;
    private int secondLayout;
    private Vector<RealmList<entry>> entries;

    public allActionsAdapter(RealmList<entry> allEntries) {
        this.layout = R.layout.action_item;
        this.secondLayout = R.layout.actions_by_month;

        Date firstDayOfMonth = firstDateOfTheMonth(new Date());
        entries = new Vector<>();
        entries.add(new RealmList<entry>());
        entries.lastElement().add(null);
        for (int i = allEntries.size() - 1; i >= 0; i--) {
            if (firstDayOfMonth.before(allEntries.get(i).getDate())) {
                entries.lastElement().add(allEntries.get(i));
            } else {
                entries.add(new RealmList<entry>());
                entries.lastElement().add(null); //It is the way I know that there is item using secondLayout. Maybe it is not the best option.
                entries.lastElement().add(allEntries.get(i));
                firstDayOfMonth = firstDateOfTheMonth(allEntries.get(i).getDate());
            }
        }
    }

    public void newEntryAdded(entry newEntry) {
        GregorianCalendar dateOfNewEntry = new GregorianCalendar();
        dateOfNewEntry.setTime(newEntry.getDate());
        GregorianCalendar dateOfLastEntry = new GregorianCalendar();
        dateOfLastEntry.setTime(entries.firstElement().get(1).getDate());
        if(dateOfNewEntry.get(Calendar.MONTH)==dateOfLastEntry.get(Calendar.MONTH) && dateOfNewEntry.get(Calendar.YEAR)==dateOfLastEntry.get(Calendar.YEAR)){
            entries.firstElement().add(1, newEntry);
            notifyItemInserted(1);
        } else{
            entries.add(0,new RealmList<entry>());
            entries.firstElement().add(0,null);
            notifyItemInserted(0);
            entries.firstElement().add(1,newEntry);
            notifyItemInserted(1);
            //TODO: check this new method
        }
    }

    public entry get(int position) {
        int i = 0;
        while (position >= entries.get(i).size()) {
            position -= entries.get(i).size();
            i++;
        }
        return entries.get(i).get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return (get(position) == null) ? 0 : 1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate((i == 1) ? layout : secondLayout, viewGroup, false);
        return (i == 1) ? new actionViewHolder(v) : new dateViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == 0) {
            ((dateViewHolder) viewHolder).bind(get(i + 1).getDate());
        } else if (getItemViewType(i) == 1) {
            ((actionViewHolder) viewHolder).bind(get(i));
        }
    }

    @Override
    public int getItemCount() {
        int result = 0;
        for (int i = 0; i < entries.size(); i++) {
            result += entries.get(i).size();
        }
        return result;
    }

    class actionViewHolder extends RecyclerView.ViewHolder {

        private TextView day;
        private TextView category;
        private TextView description;
        private TextView value;

        actionViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.textViewDayOfAction);
            category = itemView.findViewById(R.id.textViewCategoryOfAction);
            description = itemView.findViewById(R.id.textViewDescriptionOfAction);
            value = itemView.findViewById(R.id.textViewValueOfAction);
        }

        void bind(entry entry) {
            DateFormat df = new SimpleDateFormat("dd", new Locale("es", "ES"));
            day.setText(df.format(entry.getDate()));
            category.setText(entry.getCategory());
            description.setText(entry.getDescription());

            String formattedValue;
            if (entry.getType() == com.example.jorgegonzalezcabrera.outgoing.models.entry.type.OUTGOING.ordinal()) {
                formattedValue = "-" + String.valueOf(entry.getValor()) + "€";
                value.setText(formattedValue);
                value.setTextColor(Color.parseColor("#ea9999"));
            } else {
                formattedValue = "+" + String.valueOf(entry.getValor()) + " €";
                value.setText(formattedValue);
                value.setTextColor(Color.parseColor("#b6d7a8"));
            }
        }
    }

    class dateViewHolder extends RecyclerView.ViewHolder {
        private TextView date;

        dateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.textViewMonth);
        }

        void bind(Date dateOfMonth) {
            DateFormat df = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("es", "ES"));
            date.setText(df.format(dateOfMonth));
        }

    }
}
