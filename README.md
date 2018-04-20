[![](https://api.bintray.com/packages/jason98k/maven/DragSortList/images/download.svg)](https://bintray.com/jason98k/maven/DragSortList/_latestVersion)
[![](https://img.shields.io/badge/API-14+-green.svg?style=flat)](https://android-arsenal.com/api?level=14)


# DragSortList

List component that support drag and drop sorting and edit deleting.

![](https://github.com/JasonHzx/DragSortlist/blob/master/demo.gif)

# How to use

**Add the dependencies to your gradle file:**

```javascript
dependencies {
    compile 'com.jason98k:DragSortList:1.0.2'
}
```
**In the activity layout file, replace the RecyclerView with DragSortListLayout:**

```xml
<com.jasonhzx.dragsortlist.component.DragSortListLayout
    android:id="@+id/recycler_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

**Use DragSortItemLayout in the item layout file:**

```xml
<com.jasonhzx.dragsortlist.component.DragSortItemLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/item_layout"
    android:layout_width="match_parent"
    android:layout_height="@dimen/item_height">

    <!--Delete Button (删除按钮必须实现)-->

    <!--Text Content (内容视图必须实现)-->

    <!--PreDelete button (预删除按钮必须实现)-->

    <!--Sort Button (排序按钮必须实现)-->

    <!--mask（遮罩层可选）-->

</com.jasonhzx.dragsortlist.component.DragSortItemLayout>
```


**Extend DragSortListAdapter:**

```java
public class MyAdapter extends DragSortListAdapter<Entity> {

    MyAdapter(Context context, List<Entity> list) {
        super(context, list);
    }

    @Override
    public DragSortItemViewHolder onCreateDragSortItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindDragSortItemViewHolder(DragSortItemViewHolder holder, int position) {
        // bind holder.vContent
    }

    @Override
    public void onItemRemove(String item) {
        //获取被删除的Item
    }

    @Override
    public void onItemClick(String item) {
        //获取普通状态下被点击的Item
    }

    @Override
    public void onItemClickEdit(String item) {
        //获取编辑状态下被点击的Item
    }

    @Override
    public void swapped(int fromPosition, int toPosition) {
        //获取拖拽排序时，每一次列表item交换的上下两个position
    }


    private static class ViewHolder extends DragSortItemViewHolder
    {
       // ...
    @Override
    public void onItemSelected() {
       //显示遮罩层
    }

    @Override
    public void onItemClear() {
       //隐藏遮罩层
    }

    }
}
```






