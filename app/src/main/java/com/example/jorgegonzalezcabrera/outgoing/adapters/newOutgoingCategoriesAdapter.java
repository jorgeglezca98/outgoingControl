package com.example.jorgegonzalezcabrera.outgoing.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jorgegonzalezcabrera.outgoing.R;
import com.example.jorgegonzalezcabrera.outgoing.models.outgoingCategory;

import io.realm.RealmList;

public class newOutgoingCategoriesAdapter extends RecyclerView.Adapter<newOutgoingCategoriesAdapter.ViewHolder> {

    private Context context;
    private int layout;
    private RealmList<outgoingCategory> newCategories;

    public newOutgoingCategoriesAdapter(Context context) {
        this.context = context;
        layout = R.layout.new_outgoing_category;
        newCategories = new RealmList<>();
        newCategories.add(new outgoingCategory());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layout,viewGroup,false);
        return new newOutgoingCategoriesAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind();
    }

    @Override
    public int getItemCount() {
        return newCategories.size();
    }

    public void addOne(){
        newCategories.add(new outgoingCategory());
        notifyDataSetChanged();
    }

    public void deleteLast(){
        if(newCategories.size()>1){
            newCategories.remove(newCategories.size()-1);
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public EditText name;
        public EditText max;
        public ImageButton imageButtonAddSubcategory;
        public ImageButton imageButtonDeleteSubcategory;
        public RecyclerView recyclerViewSubcategories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.editTextCategoryName);
            max = itemView.findViewById(R.id.editTextMaxValue);
            recyclerViewSubcategories = itemView.findViewById(R.id.recyclerViewSubcategories);
            imageButtonAddSubcategory = itemView.findViewById(R.id.imageButtonAddSubcategory);
            imageButtonDeleteSubcategory = itemView.findViewById(R.id.imageButtonDeleteSubcategory);
        }

        public void bind(){
            final newSubcategoriesAdapter adapter = new newSubcategoriesAdapter();
            recyclerViewSubcategories.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerViewSubcategories.setLayoutManager(layoutManager);

            imageButtonAddSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.addOne();
                }
            });

            imageButtonDeleteSubcategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.deleteLast();
                }
            });
        }
    }
}
