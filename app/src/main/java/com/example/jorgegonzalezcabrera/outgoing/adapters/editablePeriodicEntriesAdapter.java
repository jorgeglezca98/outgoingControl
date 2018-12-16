package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.periodicEntry;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_ADD_MONEY_CONTROLLER;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_PERIODIC_ENTRY;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_NEW_PERIODIC_ENTRY;

public class editablePeriodicEntriesAdapter extends RecyclerView.Adapter<editablePeriodicEntriesAdapter.ViewHolder> {

    private int layout;
    private RealmList<periodicEntry> periodicEntries;
    private localUtils.changePeriodicEntriesInterface changePeriodicEntriesInterface;
    private boolean lastIsEmpty;
    private boolean showingLast;

    public editablePeriodicEntriesAdapter(@NonNull localUtils.changePeriodicEntriesInterface changePeriodicEntriesInterface) {
        this.periodicEntries = new RealmList<>();
        this.periodicEntries.addAll(Realm.getDefaultInstance().where(periodicEntry.class).findAll());
        this.layout = R.layout.erasable_item;
        this.changePeriodicEntriesInterface = changePeriodicEntriesInterface;
        this.lastIsEmpty = false;
        this.showingLast = false;
    }

    @NonNull
    @Override
    public editablePeriodicEntriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new editablePeriodicEntriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editablePeriodicEntriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(periodicEntries.get(i));
    }

    @Override
    public int getItemCount() {
        return periodicEntries.size();
    }

    public void addOne() {
        if (!lastIsEmpty) {
            periodicEntries.add(new periodicEntry());
            notifyItemInserted(getItemCount() - 1);
            lastIsEmpty = true;
        }
    }

    public void confirmLast(periodicEntry addedPeriodicEntry) {
        if (lastIsEmpty) {
            periodicEntries.remove(getItemCount() - 1);
            periodicEntries.add(addedPeriodicEntry);
            notifyItemChanged(getItemCount() - 1);
            lastIsEmpty = false;
            showingLast = false;
        }
    }

    public void cancelNewCategory() {
        if (lastIsEmpty) {
            periodicEntries.remove(getItemCount() - 1);
            notifyItemRemoved(getItemCount());
            lastIsEmpty = false;
            showingLast = false;
        }
    }

    public void modify(periodicEntry changedPeriodicEntry) {
        for (int i = 0; i < periodicEntries.size(); i++) {
            if (changedPeriodicEntry.getId() == periodicEntries.get(i).getId()) {
                notifyItemChanged(i);
                return;
            }
        }
    }

    public void add(periodicEntry addedPeriodicEntry) {
        periodicEntries.add(addedPeriodicEntry);
        notifyItemInserted(periodicEntries.size() - 1);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull editablePeriodicEntriesAdapter.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if ((holder.getAdapterPosition() == (getItemCount() - 1)) && lastIsEmpty && !showingLast) {
            changePeriodicEntriesInterface.edit(periodicEntries.get(holder.getAdapterPosition()), holder.container, REQUEST_NEW_PERIODIC_ENTRY);
            showingLast = true;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView description;
        ImageButton removeButton;

        private final static String CONTAINER_TRANSITION_NAME = "container";

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.constraintLayout);
            description = itemView.findViewById(R.id.textViewCategoryName);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveItem);
        }

        void bind(final periodicEntry periodicEntryToBind) {
            container.setTransitionName(CONTAINER_TRANSITION_NAME + periodicEntryToBind.getId());

            description.setText(periodicEntryToBind.getDescription());
            description.setFocusable(false);
            description.setFocusableInTouchMode(false);
            description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changePeriodicEntriesInterface.edit(periodicEntryToBind, container, REQUEST_EDIT_PERIODIC_ENTRY);
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    periodicEntries.remove(pos);
                    notifyItemRemoved(pos);
                    changePeriodicEntriesInterface.remove(periodicEntryToBind);
                }
            });
        }
    }

}
