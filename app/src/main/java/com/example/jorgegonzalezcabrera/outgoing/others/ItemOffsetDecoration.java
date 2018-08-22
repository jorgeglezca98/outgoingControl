package com.example.jorgegonzalezcabrera.outgoing.others;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static com.example.jorgegonzalezcabrera.outgoing.utilities.utils.dpToPixels;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int itemOffset;
    private Context context;

    public ItemOffsetDecoration(Context context, int itemOffset) {
        this.itemOffset = itemOffset;
        this.context = context;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int pxOffset = dpToPixels(context,itemOffset);
        outRect.set(pxOffset, pxOffset, pxOffset, pxOffset);
    }
}