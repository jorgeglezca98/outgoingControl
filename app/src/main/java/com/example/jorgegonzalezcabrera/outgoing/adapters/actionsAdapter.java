package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.realm.RealmList;

public class actionsAdapter extends RecyclerView.Adapter<actionsAdapter.ViewHolder>{

    private int layout;
    private RealmList<entry> entries;

    public actionsAdapter(RealmList<entry> entries) {
        this.layout = R.layout.action_item;
        this.entries = entries;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new actionsAdapter.ViewHolder(v);
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

        private TextView day;
        private TextView category;
        private TextView description;
        private TextView value;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.textViewDayOfAction);
            category = itemView.findViewById(R.id.textViewCategoryOfAction);
            description = itemView.findViewById(R.id.textViewDescriptionOfAction);
            value = itemView.findViewById(R.id.textViewValueOfAction);
        }

        public void bind(entry entry) {
            DateFormat df = new SimpleDateFormat("dd");
            day.setText(df.format(entry.getDate()));
            category.setText(entry.getCategory());
            description.setText(entry.getDescription());
            value.setText(String.valueOf(entry.getValor()));
        }
    }
}
