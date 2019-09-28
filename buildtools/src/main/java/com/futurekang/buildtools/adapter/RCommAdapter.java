package com.futurekang.buildtools.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
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
    public int itemlayoutId = -1;
    public int headerViewId = -1;
    public int footerViewId = -1;

    public final int TYPE_HEADER = 0;//头布局
    public final int TYPE_NORMAL = 1;//条目
    public final int TYPE_FOOTER = 2;//底部布局

    /**
     * 保存选中状态
     */
    private Map<Integer, Boolean> selectedMap;

    /**
     * 条目选中
     */
    private Boolean seleteEnable = false;

    /**
     * 是否缓存条目（关闭后可以解决包含radiobutton/checkbox时出错的情况）
     */
    protected static boolean cacheViewEnable = true;

    /**
     * 保存上一次选中的下标（用于选中/取消的情况）
     */
    int lastIndex = -1;

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


    // 当前加载状态，默认为加载完成
    private int loadState = 2;
    // 正在加载
    public static final int LOADING = 1;
    // 加载完成
    public static final int LOADING_COMPLETE = 2;
    // 加载到底
    public static final int LOADING_END = 3;


    @IntDef({LOADING, LOADING_COMPLETE, LOADING_END})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LoadState {
    }


    public RCommAdapter(List<T> dataList, int layoutId) {
        this.itemlayoutId = layoutId;
        this.dataList = dataList;
        selectedMap = new HashMap<>();
    }


    public static class RCViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View item;

        public RCViewHolder(@NonNull View itemView) {
            super(itemView);
            mViews = new SparseArray<>();
            item = itemView;
        }

        /**
         * 获取节点view
         */
        @SuppressWarnings("unchecked")
        public <T extends View> T getItemView(@IdRes int id) {
            if (cacheViewEnable) {
                View view = mViews.get(id);
                if (view == null) {
                    view = itemView.findViewById(id);
                    mViews.append(id, view);
                }
                return (T) view;
            } else {
                return item.findViewById(id);
            }
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
            View v = LayoutInflater.from(parent.getContext()).inflate(itemlayoutId, parent, false);
            return new RCViewHolder(v);
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(headerViewId, parent, false);
            return new RCViewHolder(v);
        } else if (viewType == TYPE_FOOTER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(footerViewId, parent, false);
            return new RCViewHolder(v);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        if (null == getItemSelectStatus(position)) {
            this.selectedMap.put(position, false);
        }
        //只给条目设置选中事件
        if (seleteEnable && getItemViewType(position) == TYPE_NORMAL) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setItemSelected(position, !getItemSelectStatus(position));
                    lastIndex = getSelectIndex();
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(holder, position);
                    }
                }
            });
        }
        if (getItemViewType(position) == TYPE_NORMAL && headerViewId != -1) {
            setView(holder, position, dataList.get(position - 1));
        } else if (getItemViewType(position) == TYPE_NORMAL) {
            setView(holder, position, dataList.get(position));
        } else {
            setView(holder, position, null);
        }

        if (getItemViewType(position) == TYPE_FOOTER && loadState == LOADING) {
            holder.itemView.setVisibility(View.VISIBLE);
        } else if (getItemViewType(position) == TYPE_FOOTER && (loadState == LOADING_COMPLETE || loadState == LOADING_END)) {
            holder.itemView.setVisibility(View.GONE);
        }
    }

    public int getLastSelectedIndex() {
        return lastIndex;
    }

    public abstract void setView(RCViewHolder viewHolder, int position, T itemData);

    @Override
    public int getItemCount() {
        int itemCount = -1;
        if (headerViewId == -1 && footerViewId == -1) {
            itemCount = dataList.size();
        } else if (headerViewId != -1 && footerViewId != -1) {
            itemCount = dataList.size() + 2;
        } else if (headerViewId == -1 || footerViewId == -1) {
            itemCount = dataList.size() + 1;
        }
        return itemCount;
    }

    public T getItem(int postion) {
        int itemCount = -1;
        if (getItemViewType(postion) == TYPE_NORMAL) {
            return dataList.get(postion);
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (headerViewId != -1 && position == 0) {
            return TYPE_HEADER;
        } else if (footerViewId != -1 && position == getItemCount() - 1) {
            return TYPE_FOOTER;
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
                this.selectedMap.put(getLastSelectedIndex(), false);
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
    public Integer getSelectIndex() {
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
     * 获取到是否有选中项
     *
     * @return
     */
    public boolean isExisSelectedItem() {
        Iterator<Integer> iterator = this.selectedMap.keySet().iterator();
        while (iterator.hasNext()) {
            int index = iterator.next();
            if (this.selectedMap.get(index)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 设置多选或单选的模式
     *
     * @param mode
     */
    public void setSelectMode(@Mode int mode) {
        this.mode = mode;
    }

    public int getSelectMode() {
        return mode;
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

    public void setSeletcEnable(Boolean seleteEnable) {
        this.seleteEnable = seleteEnable;
    }


    /**
     * 设置上拉加载状态
     *
     * @param loadState 0.正在加载 1.加载完成 2.加载到底
     */
    public void setLoadState(@LoadState int loadState) {
        if (footerViewId != -1) {
            this.loadState = loadState;
            notifyDataSetChanged();
        }
    }

    public void setFooterViewId(int footerViewId) {
        this.footerViewId = footerViewId;
    }

    public void setCacheViewEnable(boolean cacheVieweEnable) {
        cacheViewEnable = cacheVieweEnable;
    }
}
