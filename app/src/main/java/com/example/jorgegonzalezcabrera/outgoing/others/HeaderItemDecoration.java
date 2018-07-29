package com.example.jorgegonzalezcabrera.outgoing.others;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HeaderItemDecoration extends RecyclerView.ItemDecoration {

    private StickyHeaderInterface mListener;
    private int headerLayout;

    public HeaderItemDecoration(int headerLayout, StickyHeaderInterface mListener) {
        this.mListener = mListener;
        this.headerLayout = headerLayout;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        View topChild = parent.getChildAt(0);
        if (topChild != null) {
            int topChildPosition = parent.getChildAdapterPosition(topChild);
            if (topChildPosition != RecyclerView.NO_POSITION) {
                View currentHeader = getHeaderViewForItem(topChildPosition, parent);
                fixLayoutSize(parent, currentHeader);

                drawHeader(c, currentHeader);
            }
        }
    }

    private View getHeaderViewForItem(int itemPosition, RecyclerView parent) {
        int headerPosition = mListener.getHeaderPositionForItem(itemPosition);
        View header = LayoutInflater.from(parent.getContext()).inflate(headerLayout, parent, false);
        mListener.bindHeaderData(header, headerPosition);
        return header;
    }

    private void drawHeader(Canvas c, View header) {
        c.save();
        c.translate(0, 0);
        header.draw(c);
        c.restore();
    }

    private void fixLayoutSize(ViewGroup parent, View view) {

        int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);

        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);

        view.measure(childWidthSpec, childHeightSpec);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public interface StickyHeaderInterface {
        int getHeaderPositionForItem(int itemPosition);
        void bindHeaderData(View header, int headerPosition);
    }
}

//Found in https://stackoverflow.com/questions/32949971/how-can-i-make-sticky-headers-in-recyclerview-without-external-lib