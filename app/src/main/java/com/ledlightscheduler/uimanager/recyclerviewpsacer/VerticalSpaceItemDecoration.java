package com.ledlightscheduler.uimanager.recyclerviewpsacer;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int lengthOfSpace;

    public VerticalSpaceItemDecoration(int lengthOfSpace){
        this.lengthOfSpace = lengthOfSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(parent.getChildAdapterPosition(view) == 0) {
            outRect.top = lengthOfSpace;
        }
        outRect.bottom = lengthOfSpace;
    }
}
