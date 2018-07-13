package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.entry;
import com.example.jorgegonzalezcabrera.outgoing.models.entry.type;

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
        private ConstraintLayout background;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.textViewDayOfAction);
            category = itemView.findViewById(R.id.textViewCategoryOfAction);
            description = itemView.findViewById(R.id.textViewDescriptionOfAction);
            value = itemView.findViewById(R.id.textViewValueOfAction);
            background = itemView.findViewById(R.id.layoutActionItemBackground);
        }

        public void bind(entry entry) {
            DateFormat df = new SimpleDateFormat("dd");
            day.setText(df.format(entry.getDate()));
            category.setText(entry.getCategory());
            description.setText(entry.getDescription());
            if(entry.getType()== type.OUTGOING.ordinal()) {
                background.setBackgroundColor(Color.parseColor("#ea9999"));
                value.setText("-" + String.valueOf(entry.getValor()) + "€");
            }
            else {
                background.setBackgroundColor(Color.parseColor("#b6d7a8"));
                value.setText("+" + String.valueOf(entry.getValor()) + " €");
            }
        }
    }
}