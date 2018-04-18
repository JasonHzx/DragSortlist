package com.jasonhzx.dragsortlist.component;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by JasonHzx on 2018/4/17
 * 拖拽排序回调
 */

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private DragSortItemLayout.OnItemSortListener mOnItemSortListener;
    private boolean isLongPressDrag;

    public ItemTouchHelperCallback(DragSortItemLayout.OnItemSortListener onItemSortListener) {
        this.mOnItemSortListener = onItemSortListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // 允许上下拖拽
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        // 禁止左右拖拽
        int swipeFlags = 0;
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        mOnItemSortListener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }


    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // We only want the active item to change
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof ItemTouchHelperViewHolder) {
                // Let the view holder know that this item is being moved or dragged
                ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            // Tell the view holder it's time to restore the idle state
            ItemTouchHelperViewHolder itemViewHolder = (ItemTouchHelperViewHolder) viewHolder;
            itemViewHolder.onItemClear();
        }
    }

    @Override
    public boolean isLongPressDragEnabled() {
        //是否允许长按触发拖拽
        return isLongPressDrag;
    }

    public void setLongPressDragEnabled(boolean isLongPressDrag) {
        this.isLongPressDrag = isLongPressDrag;
    }
}
