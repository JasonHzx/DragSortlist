package com.jasonhzx.dragsortlist.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JasonHzx on 2018/4/17
 * 列表编辑项的 ViewHolder
 */

public abstract class DragSortItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    public DragSortItemLayout dragSortItemLayout;
    public View vContent;
    public View vPreDelete;
    public View vDelete;
    public View vSort;

    public DragSortItemViewHolder(View itemView) {
        super(itemView);
        dragSortItemLayout = setItemLayout(itemView);
        vContent = setContent(itemView);
        vPreDelete = setPreDelete(itemView);
        vDelete = setDelete(itemView);
        vSort = setSort(itemView);
    }

    public abstract DragSortItemLayout setItemLayout(View itemView);

    public abstract View setContent(View itemView);

    public abstract View setPreDelete(View itemView);

    public abstract View setDelete(View itemView);

    public abstract View setSort(View itemView);

}
