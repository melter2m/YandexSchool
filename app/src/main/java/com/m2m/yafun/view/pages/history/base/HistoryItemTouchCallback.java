package com.m2m.yafun.view.pages.history.base;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.m2m.yafun.view.pages.history.base.HistoryItemViewHolder;
import com.m2m.yafun.view.pages.history.helper.ItemTouchHelperAdapter;
import com.m2m.yafun.view.pages.history.helper.SimpleItemTouchHelperCallback;

class HistoryItemTouchCallback extends SimpleItemTouchHelperCallback {

    HistoryItemTouchCallback(ItemTouchHelperAdapter adapter) {
        super(adapter);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (!(viewHolder instanceof HistoryItemViewHolder))
            return super.getMovementFlags(recyclerView, viewHolder);
        final int dragFlags = 0;
        int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }
}
