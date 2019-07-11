package com.futurekang.buildtools.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class RCommAdapter<T> extends RecyclerView.Adapter<RCommAdapter.RCViewHolder> {
    private List<T> dataList;
    public int layoutId;

    public RCommAdapter(List<T> dataList, int layoutId) {
        this.layoutId = layoutId;
        this.dataList = dataList;
    }

    public static class RCViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;

        public RCViewHolder(@NonNull View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
        }

        /**
         * 获取节点view
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getItemView(int id) {
            View view = mViews.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViews.append(id, view);
            }
            return (T) view;
        }

        public void setText(String text, int id) {
            TextView textView = getItemView(id);
            textView.setText(text);
        }

        public void setOnClickListener(int id, View.OnClickListener listener) {
            getItemView(id).setOnClickListener(listener);
        }
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new RCViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        setView(holder, position, dataList.get(position));
    }

    public abstract void setView(RCViewHolder viewHolder, int position, T itemData);

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
