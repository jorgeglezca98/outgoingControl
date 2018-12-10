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
import com.example.jorgegonzalezcabrera.outgoing.models.moneyController;
import com.example.jorgegonzalezcabrera.outgoing.utilities.localUtils;

import io.realm.Realm;
import io.realm.RealmList;

import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_ADD_MONEY_CONTROLLER;
import static com.example.jorgegonzalezcabrera.outgoing.activities.MainActivity.REQUEST_EDIT_MONEY_CONTROLLER;

public class editableOutgoingCategoriesAdapter extends RecyclerView.Adapter<editableOutgoingCategoriesAdapter.ViewHolder> {

    private int layout;
    private RealmList<moneyController> controllers;
    private localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface;
    private editOutgoingCategoryInterface editOutgoingCategoryInterface;
    private boolean lastIsEmpty;
    private boolean showingLast;

    public interface editOutgoingCategoryInterface {
        void editMoneyController(moneyController moneyController, ConstraintLayout container, int requestCode);
    }

    public editableOutgoingCategoriesAdapter(@NonNull localUtils.OnCategoriesChangeInterface onCategoriesChangeInterface,
                                             editOutgoingCategoryInterface editOutgoingCategoryInterface) {
        this.controllers = new RealmList<>();
        this.controllers.addAll(Realm.getDefaultInstance().where(moneyController.class).findAll());
        this.layout = R.layout.erasable_item;
        this.onCategoriesChangeInterface = onCategoriesChangeInterface;
        this.editOutgoingCategoryInterface = editOutgoingCategoryInterface;
        this.lastIsEmpty = false;
        this.showingLast = false;
    }

    @NonNull
    @Override
    public editableOutgoingCategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new editableOutgoingCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull editableOutgoingCategoriesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.bind(controllers.get(i));
    }

    @Override
    public int getItemCount() {
        return controllers.size();
    }

    public void addOne() {
        if (!lastIsEmpty) {
            controllers.add(new moneyController());
            notifyItemInserted(getItemCount() - 1);
            lastIsEmpty = true;
        }
    }

    public void confirmLast(moneyController storedMoneyController) {
        if (lastIsEmpty) {
            controllers.remove(getItemCount() - 1);
            controllers.add(storedMoneyController);
            notifyItemChanged(getItemCount() - 1);
            lastIsEmpty = false;
            showingLast = false;
        }
    }

    public void cancelNewCategory() {
        if (lastIsEmpty) {
            controllers.remove(getItemCount() - 1);
            notifyItemRemoved(getItemCount());
            lastIsEmpty = false;
            showingLast = false;
        }
    }

    public void modify(moneyController modifiedMoneyController) {
        for (int i = 0; i < controllers.size(); i++) {
            if (modifiedMoneyController.getId() == controllers.get(i).getId()) {
                notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if ((holder.getAdapterPosition() == (getItemCount() - 1)) && lastIsEmpty && !showingLast) {
            editOutgoingCategoryInterface.editMoneyController(controllers.get(holder.getAdapterPosition()), holder.container, REQUEST_ADD_MONEY_CONTROLLER);
            showingLast = true;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView categoryName;
        ImageButton removeButton;

        private final static String CONTAINER_TRANSITION_NAME = "container";
        private final static String CATEGORY_NAME_TRANSITION_NAME = "categoryNameTextView";

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            container = itemView.findViewById(R.id.constraintLayout);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            removeButton = itemView.findViewById(R.id.imageButtonRemoveItem);
        }

        void bind(final moneyController controllerToBind) {
            container.setTransitionName(CONTAINER_TRANSITION_NAME + controllerToBind.getId());
            categoryName.setTransitionName(CATEGORY_NAME_TRANSITION_NAME + controllerToBind.getId());

            categoryName.setText(controllerToBind.getName());
            categoryName.setFocusable(false);
            categoryName.setFocusableInTouchMode(false);
            categoryName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editOutgoingCategoryInterface.editMoneyController(controllerToBind, container, REQUEST_EDIT_MONEY_CONTROLLER);
                }
            });

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    controllers.remove(pos);
                    notifyItemRemoved(pos);
                    onCategoriesChangeInterface.removeMoneyController(controllerToBind);
                }
            });
        }
    }

}
