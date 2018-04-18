package com.jasonhzx.dragsortlist.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by JasonHzx on 2018/4/17
 * 自定义RecyclerView
 * 支持拖拽排序和侧滑编辑
 */

public class DragSortListLayout extends RecyclerView implements DragSortItemLayout.OnItemSortListener {

    private final ItemTouchHelperCallback dragSortCallback;
    private boolean isEdit;             //是否为编辑状态
    private DragSortItemLayout rightOpenItem;   //向右展开项
    private ItemTouchHelper mItemTouchHelper;

    public DragSortListLayout(Context context) {
        this(context, null);
    }

    public DragSortListLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragSortListLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setItemAnimator(new DefaultItemAnimator());
        dragSortCallback = new ItemTouchHelperCallback(this);
        mItemTouchHelper = new ItemTouchHelper(dragSortCallback);
        mItemTouchHelper.attachToRecyclerView(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        setLayoutManager(layoutManager);
    }

    @Override
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (getAdapter() instanceof DragSortListAdapter) {
                    DragSortListAdapter adapter = (DragSortListAdapter) getAdapter();
                    rightOpenItem = adapter.getRightOpenItem();
                    isEdit = adapter.isEdit();
                }
                if (isEdit && rightOpenItem != null) {
                    rightOpenItem.openLeft();
                }
        }
        return super.onTouchEvent(e);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        if (adapter instanceof DragSortListAdapter) {
            DragSortListAdapter listAdapter = (DragSortListAdapter) adapter;
            listAdapter.setOnItemSortListener(this);
            listAdapter.setOnEditStateChangeListener(new DragSortListAdapter.OnEditStateChangeListener() {
                @Override
                public void onEditOpen() {
                    dragSortCallback.setLongPressDragEnabled(true);
                }

                @Override
                public void onEditClose() {
                    dragSortCallback.setLongPressDragEnabled(false);
                }
            });
        }

    }

    @Override
    public void onStartDrags(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        DragSortListAdapter mAdapter = (DragSortListAdapter) getAdapter();
        List list = mAdapter.getList();
        Collections.swap(list, fromPosition, toPosition);
        mAdapter.swapped(fromPosition, toPosition);
        mAdapter.notifyItemMoved(fromPosition, toPosition);
        mAdapter.setHasSorted(true);

    }
}
