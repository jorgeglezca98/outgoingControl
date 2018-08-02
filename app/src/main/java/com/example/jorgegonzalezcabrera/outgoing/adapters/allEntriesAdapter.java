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
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import javax.annotation.Nonnull;

import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.utils.firstDateOfTheMonth;

public class allEntriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private enum viewType {ACTION, HEADER}

    private int actionLayout;
    private int headerLayout;
    private Vector<RealmList<entry>> entries;

    public allEntriesAdapter(@Nonnull RealmList<entry> allEntries) {
        this.actionLayout = R.layout.entry_item;
        this.headerLayout = R.layout.entries_by_month;

        Date firstDayOfMonth = new Date();
        entries = new Vector<>();
        for (int i = allEntries.size() - 1; i >= 0; i--) {
            entry entry = allEntries.get(i);
            if (entry != null) {
                if (firstDayOfMonth.before(entry.getCreationDate())) {
                    entries.lastElement().add(allEntries.get(i));
                } else {
                    entries.add(new RealmList<entry>());
                    entries.lastElement().add(null); //It is the way I know that there is a header in this position.
                    entries.lastElement().add(allEntries.get(i));
                    firstDayOfMonth = firstDateOfTheMonth(entry.getCreationDate());
                }
            }
        }
    }

    public void newEntryAdded(@Nonnull entry newEntry) {
        GregorianCalendar dateOfNewEntry = new GregorianCalendar();
        dateOfNewEntry.setTime(newEntry.getCreationDate());
        GregorianCalendar dateOfLastEntry = new GregorianCalendar();
        if(!entries.isEmpty()){
            entry entry = entries.firstElement().last();
            if(entry!=null){
                dateOfLastEntry.setTime(entry.getCreationDate());
                if (dateOfNewEntry.get(Calendar.MONTH) == dateOfLastEntry.get(Calendar.MONTH)) {
                    if (dateOfNewEntry.get(Calendar.YEAR) == dateOfLastEntry.get(Calendar.YEAR)) {
                        entries.firstElement().add(1, newEntry);
                        notifyItemInserted(1);
                    }
                } else {
                    entries.add(0, new RealmList<entry>());
                    entries.firstElement().add(0, null);
                    notifyItemInserted(0);
                    entries.firstElement().add(1, newEntry);
                    notifyItemInserted(1);
                }
            }
        } else{
            entries.add(0, new RealmList<entry>());
            entries.firstElement().add(0, null);
            notifyItemInserted(0);
            entries.firstElement().add(1, newEntry);
            notifyItemInserted(1);
        }
    }

    public entry get(int position) {
        int i = 0, j = position;
        while (j >= entries.get(i).size()) {
            j -= entries.get(i).size();
            i++;
        }
        return entries.get(i).get(j);
    }

    @Override
    public int getItemViewType(int position) {
        return (get(position) == null) ? viewType.HEADER.ordinal() : viewType.ACTION.ordinal();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == viewType.ACTION.ordinal()) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(actionLayout, viewGroup, false);
            return new actionViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(headerLayout, viewGroup, false);
            return new dateViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == viewType.HEADER.ordinal()) {
            ((dateViewHolder) viewHolder).bind(get(i + 1).getCreationDate());
        } else if (getItemViewType(i) == viewType.ACTION.ordinal()) {
            ((actionViewHolder) viewHolder).bind(get(i));
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < entries.size(); i++) {
            count += entries.get(i).size();
        }
        return count;
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

        void bind(@Nonnull entry entry) {
            DateFormat df = new SimpleDateFormat("dd", new Locale("es", "ES"));
            day.setText(df.format(entry.getCreationDate()));
            category.setText(entry.getCategory());
            description.setText(entry.getDescription());

            String formattedValue;
            if (entry.getType() == type.OUTGOING) {
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

        void bind(@Nonnull Date dateOfMonth) {
            DateFormat df = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("es", "ES"));
            date.setText(df.format(dateOfMonth));
        }

    }
}
