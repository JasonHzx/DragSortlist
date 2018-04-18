package com.jasonhzx.dragsortlist.component;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JasonHzx on 2018/4/17
 * 列表适配器 为了复用采用了抽象类的形式
 */

public abstract class DragSortListAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mList;

    private boolean isEdit;  //是否处于编辑状态
    private boolean hasSorted;  //是否有过排序
    private List<DragSortItemLayout> allItems = new ArrayList<>();
    private DragSortItemLayout mRightOpenItem;  //向右展开的删除项，只会存在一项

    public DragSortListAdapter(Context context, List<T> List) {
        this.mContext = context;
        this.mList = List;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateEditViewHolder(parent, viewType);
    }

    public abstract DragSortItemViewHolder onCreateEditViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final DragSortItemViewHolder viewHolder = (DragSortItemViewHolder) holder;
        final DragSortItemLayout editLayout = viewHolder.dragSortItemLayout;

        if (!allItems.contains(editLayout)) {
            allItems.add(editLayout);
        }

        editLayout.setEdit(isEdit);

        onBindEditViewHolder(viewHolder, position);

        viewHolder.vPreDelete.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (isRightDeleteState()) {
                            mRightOpenItem.openLeft();
                        } else {
                            editLayout.openRight();
                        }
                }
                return true;
            }
        });

        viewHolder.vDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRightClose()) {
                    return;
                }
                int position = viewHolder.getAdapterPosition();
                if (position >= 0) {
                    onItemRemove(mList.get(position));
                    mList.remove(position);
                    mRightOpenItem = null;
                    notifyItemRemoved(position);
                    if (position != mList.size()) {
                        notifyItemRangeChanged(position, mList.size() - position);
                    }
                }
            }
        });

        viewHolder.vSort.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (isRightDeleteState()) {
                    mRightOpenItem.openLeft();
                } else {
                    mOnItemSortListener.onStartDrags(viewHolder);
                }
                return false;
            }
        });

        editLayout.setOnDragStateChangeListener(new DragSortItemLayout.OnStateChangeListener() {

            @Override
            public void onLeftOpen(DragSortItemLayout layout) {
                if (mRightOpenItem == layout) {
                    mRightOpenItem = null;
                }
            }

            @Override
            public void onRightOpen(DragSortItemLayout layout) {
                if (mRightOpenItem != layout) {
                    mRightOpenItem = layout;
                }
            }

            @Override
            public void onClose(DragSortItemLayout layout) {
                if (mRightOpenItem == layout) {
                    mRightOpenItem = null;
                }
            }
        });

        editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRightDeleteState()) {
                    mRightOpenItem.openLeft();
                    return;
                }
                int position = viewHolder.getAdapterPosition();
                T item = mList.get(position);
                if (isEdit) {
                    onItemClickEdit(item);
                    return;
                }
                onItemClick(item);
            }
        });

    }

    public abstract void onItemClick(T item);

    public abstract void onItemClickEdit(T item);

    public abstract void onItemRemove(T item);

    public abstract void swapped(int fromPosition, int toPosition);

    private boolean isRightDeleteState() {
        return isEdit && isRightOpen();
    }

    private boolean isRightClose() {
        return mRightOpenItem == null;
    }

    private boolean isRightOpen() {
        return mRightOpenItem != null;
    }

    public abstract void onBindEditViewHolder(DragSortItemViewHolder holder, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    /**
     * 设置编辑状态
     *
     * @param isEdit 是否为编辑状态
     */
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        if (isEdit) {
            openLeftAll();
            setHasSorted(false);
        } else {
            closeAll();
        }
        for (DragSortItemLayout editLayout : allItems) {
            editLayout.setEdit(isEdit);
        }
    }

    private DragSortItemLayout.OnItemSortListener mOnItemSortListener;

    public void setOnItemSortListener(DragSortItemLayout.OnItemSortListener onItemSortListener) {
        mOnItemSortListener = onItemSortListener;
    }


    /**
     * 编辑状态改变监听器
     */
    public interface OnEditStateChangeListener {
        void onEditOpen();

        void onEditClose();
    }

    private OnEditStateChangeListener mOnEditStateChangeListener;

    public void setOnEditStateChangeListener(OnEditStateChangeListener onStateChangeListener) {
        mOnEditStateChangeListener = onStateChangeListener;
    }

    /**
     * 关闭所有 item
     */
    private void closeAll() {
        if (mOnEditStateChangeListener != null) {
            mOnEditStateChangeListener.onEditClose();
        }
        for (DragSortItemLayout editLayout : allItems) {
            editLayout.close();
        }
    }

    /**
     * 将所有 item 向左展开
     */
    private void openLeftAll() {
        if (mOnEditStateChangeListener != null) {
            mOnEditStateChangeListener.onEditOpen();
        }
        for (DragSortItemLayout editLayout : allItems) {
            editLayout.openLeft();
        }
    }

    /**
     * 获取编辑状态
     *
     * @return 是否为编辑状态
     */
    public boolean isEdit() {
        return isEdit;
    }

    /**
     * 获取排序状态
     * @return  是否有过排序
     */
    public boolean isHasSorted() {
        return hasSorted;
    }

    /**
     * 设置排序状态
     * @param hasSorted  是否有过排序
     */
    public void setHasSorted(boolean hasSorted) {
        this.hasSorted = hasSorted;
    }

    /**
     * 获取向右展开的 item
     *
     * @return 向右展开的 item
     */
    public DragSortItemLayout getRightOpenItem() {
        return mRightOpenItem;
    }

    public List<T> getList() {
        return mList;
    }
}
