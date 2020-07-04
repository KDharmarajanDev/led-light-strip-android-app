package com.ledlightscheduler.uimanager.recyclerviewpsacer;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int lengthOfSpace;

    public HorizontalSpaceItemDecoration(int lengthOfSpace){
        this.lengthOfSpace = lengthOfSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = lengthOfSpace;
        outRect.left = lengthOfSpace;
    }
}
