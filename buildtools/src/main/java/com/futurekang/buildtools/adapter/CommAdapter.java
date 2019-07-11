package com.futurekang.buildtools.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommAdapter<T> extends BaseAdapter {
    private List<T> dataList;
    private int layoutId;
    private Context context;

    public CommAdapter(@NonNull List<T> dataList, @NonNull int layoutId) {
        this.dataList = dataList;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public T getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = ViewHolder.newsInstance(convertView, parent.getContext(), this.layoutId);

        setView(holder, position, parent.getContext());

        return holder.getConverView();
    }

    public abstract void setView(ViewHolder viewHolder, int position, Context context);

    public static class ViewHolder {
        /**
         * 保存所有itemview的集合
         */
        private SparseArray<View> mViews;

        private View mConvertView;

        private ViewHolder(Context context, int layoutId) {
            mConvertView = View.inflate(context, layoutId, null);
            mConvertView.setTag(this);
            mViews = new SparseArray<>();
        }

        public static ViewHolder newsInstance(View convertView, Context context, int layoutId) {
            if (convertView == null) {
                return new ViewHolder(context, layoutId);
            } else {
                return (ViewHolder) convertView.getTag();
            }
        }

        public View getConverView() {
            return mConvertView;
        }

        /**
         * 获取节点view
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getItemView(int id) {
            View view = mViews.get(id);
            if (view == null) {
                view = mConvertView.findViewById(id);
                mViews.append(id, view);
            }

            return (T) view;
        }
    }
}
