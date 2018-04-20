package com.jasonhzx.dragsortlist.sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jasonhzx.dragsortlist.component.DragSortItemLayout;
import com.jasonhzx.dragsortlist.component.DragSortItemViewHolder;
import com.jasonhzx.dragsortlist.component.DragSortListAdapter;

import java.util.List;

/**
 * Created by JasonHzx on 2018/4/17
 * 主页面适配器
 */

public class MainListAdapter extends DragSortListAdapter<String> {

    private OnEditItemListener itemListener;

    public interface OnEditItemListener {
        void onItemRemove(String item);

        void onItemClick(String item);

        void onItemClickEdit(String item);
    }


    MainListAdapter(Context context, List<String> list) {
        super(context, list);
    }

    @Override
    public DragSortItemViewHolder onCreateDragSortItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindDragSortItemViewHolder(DragSortItemViewHolder holder, int position) {
        final TextView tvName = (TextView) holder.vContent;
        tvName.setText(mList.get(position));
    }

    @Override
    public void onItemRemove(String item) {
        if (itemListener == null) return;
        itemListener.onItemRemove(item);
    }

    @Override
    public void swapped(int fromPosition, int toPosition) {
        // 可处理拖拽排序时，item内部数据的交换，例子如下
//        Object itemA = mList.get(fromPosition);
//        Object itemB = mList.get(toPosition);
//        int orderNumTemp = itemA.getOrderNum();
//        itemA.setOrderNum(itemB.getOrderNum());
//        itemB.setOrderNum(orderNumTemp);
    }

    @Override
    public void onItemClick(String item) {
        if (itemListener == null) return;
        itemListener.onItemClick(item);
    }

    @Override
    public void onItemClickEdit(String item) {
        if (itemListener == null) return;
        itemListener.onItemClickEdit(item);
    }

    public void setOnEditItemListener(OnEditItemListener mRemoveListener) {
        this.itemListener = mRemoveListener;
    }

    private static class ViewHolder extends DragSortItemViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public DragSortItemLayout setItemLayout(View itemView) {
            return itemView.findViewById(R.id.item_layout);
        }

        @Override
        public View setContent(View itemView) {
            return itemView.findViewById(R.id.tv_name);
        }

        @Override
        public View setPreDelete(View itemView) {
            return itemView.findViewById(R.id.fl_pre_delete);
        }

        @Override
        public View setDelete(View itemView) {
            return itemView.findViewById(R.id.fl_delete);
        }

        @Override
        public View setSort(View itemView) {
            return itemView.findViewById(R.id.fl_sort);
        }

        @Override
        public void onItemSelected() {
            View mask = itemView.findViewById(R.id.mask);
            mask.setVisibility(View.VISIBLE);
        }

        @Override
        public void onItemClear() {
            View mask = itemView.findViewById(R.id.mask);
            mask.setVisibility(View.GONE);
        }
    }
}
