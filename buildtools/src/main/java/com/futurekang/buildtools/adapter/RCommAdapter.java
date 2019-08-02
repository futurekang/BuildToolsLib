package com.futurekang.buildtools.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class RCommAdapter<T> extends RecyclerView.Adapter<RCommAdapter.RCViewHolder> {
    private static final String TAG = "RCommAdapter";
    private List<T> dataList;
    public int layoutId;
    public int headerViewId = -1;
    public final int TYPE_HEADER = 0;
    public final int TYPE_NORMAL = 1;
    private Map<Integer, Boolean> selectedMap;


    public RCommAdapter(List<T> dataList, int layoutId) {
        this.layoutId = layoutId;
        this.dataList = dataList;
        selectedMap = new HashMap<>();
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
        if (viewType == TYPE_NORMAL) {
            View v = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            return new RCViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(headerViewId, parent, false);
            return new RCViewHolder(v);
        }

    }

    /**
     * 保存上一次选中的下标
     */
    int lastIndex = -1;

    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        if (null == getItemSelectStatus(position)) {
            this.selectedMap.put(position, false);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastIndex = getSeletedIndex();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, position);
                }
                setItemSelected(position, !getItemSelectStatus(position));
            }
        });
        if (headerViewId != -1 && position != 0) {
            setView(holder, position, dataList.get(position - 1));
        } else if (headerViewId != -1 && position == 0) {
            setView(holder, position, null);
        } else {
            setView(holder, position, dataList.get(position));
        }
    }

    public int getLastSelectedIndex() {
        return lastIndex;
    }

    public abstract void setView(RCViewHolder viewHolder, int position, T itemData);

    @Override
    public int getItemCount() {
        return headerViewId == -1 ? dataList.size() : dataList.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return headerViewId == -1 ? super.getItemId(position) : super.getItemId(position - 1);
    }


    @Override
    public int getItemViewType(int position) {
        if (headerViewId != -1 && position == 0) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    /**
     * 通过布局的ID 设置header
     *
     * @param headerViewId
     */
    public void setHeaderViewId(int headerViewId) {
        this.headerViewId = headerViewId;
    }

    /**
     * 单选模式
     */
    public static final int MODE_RADIO = 1000;
    /**
     * 多选模式
     */
    public static final int MODE_MULTIPLE = 1001;

    @IntDef({MODE_RADIO, MODE_MULTIPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    private int mode = MODE_MULTIPLE;

    /**
     * 设置选中状态
     */
    public void setItemSelected(int position, boolean select) {
        if (mode == MODE_RADIO) {
            Iterator<Integer> iterator = this.selectedMap.keySet().iterator();
            if (select) {
                while (iterator.hasNext()) {
                    int temp = iterator.next();
                    if (this.selectedMap.get(temp)) {
                        this.selectedMap.put(temp, false);
                        notifyItemChanged(temp);
                    }
                }
                this.selectedMap.put(position, true);
                notifyItemChanged(position);
            }
        } else if (mode == MODE_MULTIPLE) {
            this.selectedMap.put(position, select);
            notifyItemChanged(position);
        }
    }

    /**
     * 获取到单选模式下选中项
     *
     * @return
     */
    public Integer getSeletedIndex() {
        if (mode == MODE_RADIO) {
            Iterator<Integer> iterator = this.selectedMap.keySet().iterator();
            while (iterator.hasNext()) {
                int index = iterator.next();
                if (this.selectedMap.get(index)) {
                    return index;
                }
            }
        }
        return -1;
    }

    /**
     * 设置多选或单选的模式
     *
     * @param mode
     */
    public void setMode(@Mode int mode) {
        this.mode = mode;
    }

    /**
     * 传入position 返回该下标的条目的选中状态
     *
     * @param position
     * @return
     */
    public Boolean getItemSelectStatus(int position) {
        return selectedMap.get(position);
    }

    /**
     * 条目单击监听器
     */
    OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        public void onItemClick(RCViewHolder viewHolder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
