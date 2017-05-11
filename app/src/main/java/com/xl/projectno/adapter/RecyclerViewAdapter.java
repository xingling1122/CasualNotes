package com.xl.projectno.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.xl.projectno.R;
import com.xl.projectno.base.DataManager;
import com.xl.projectno.model.BaseBean;
import com.xl.projectno.model.ItemBean;
import com.xl.projectno.widget.ItemThouchHelperAdapterCallback;
import com.xl.projectno.widget.onDragStartListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Administrator on 2017/3/22.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemThouchHelperAdapterCallback {
    private onDragStartListener startdraglistener;
    Context context;
    ArrayList<BaseBean> data;
    LayoutInflater layoutInflater;
    public ArrayList<BaseBean> bufferData = new ArrayList<>();
    private ArrayList<BaseBean> sortData = new ArrayList<>();

    private final int VIEW_INDEX = 0;
    private final int VIEW_ITEM = 1;

    public int ndatasize = 0;
    private int ydatesize = 0;

    private boolean deleteEnable = false;

    public RecyclerViewAdapter(Context context, ArrayList<BaseBean> data) {
        this.context = context;
        this.data = data;
        sortData(data);
    }

    public void setDeleteEnable(boolean deleteEnable) {
        this.deleteEnable = deleteEnable;
        notifyDataSetChanged();
    }

    private void sortData(ArrayList<BaseBean> data) {
        bufferData.clear();
        sortData.clear();
        ndatasize = 0;
        ydatesize = 0;
        ItemBean item = new ItemBean();
        item.sortNo = 1;
        item.time = "今天";
        sortData.add(item);
        for (BaseBean baseBean : data) {
            if (!((ItemBean) baseBean).isFinish) {
                sortData.add(baseBean);
                bufferData.add(baseBean);
                ndatasize++;
            }
        }
        ItemBean item1 = new ItemBean();
        item1.sortNo = ndatasize + 2;
        item1.time = "已完成";
        sortData.add(item1);
        for (BaseBean baseBean : data) {
            if (((ItemBean) baseBean).isFinish) {
                sortData.add(baseBean);
                bufferData.add(baseBean);
                ydatesize++;
            }
        }
    }

    public void setStartdraglistener(onDragStartListener startdraglistener) {
        this.startdraglistener = startdraglistener;
    }

    public void setData(ArrayList<BaseBean> data) {
        this.data = data;
        sortData(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layoutInflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case VIEW_INDEX:
                viewHolder = new IndexViewHolder(layoutInflater.inflate(R.layout.index, null));
                break;
            case VIEW_ITEM:
                viewHolder = new ItemViewHolder(layoutInflater.inflate(R.layout.list_main, null));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        ItemBean itemBean = (ItemBean) sortData.get(i);
        if (i == 0 || i == ndatasize + 1) {
            ((IndexViewHolder) holder).index.setText(itemBean.time);
            if ((ndatasize == 0 && i == 0) || (ydatesize == 0 && i == ndatasize + 1)) {
                ((IndexViewHolder) holder).index.setVisibility(View.GONE);
            } else {
                ((IndexViewHolder) holder).index.setVisibility(View.VISIBLE);
            }
        } else {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            fillItem(viewHolder, itemBean);
        }
    }

    private void fillItem(final ItemViewHolder viewHolder, final ItemBean itemBean) {
//        viewHolder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if (b) {
//                    itemBean.isFinish = true;
//                } else {
//                    itemBean.isFinish = false;
//                }
//                itemBean.time = itemBean.getTime(itemBean.finishTime);
//                DataManager.getInstance().updateItem(itemBean, itemBean);
//            }
//        });
        viewHolder.check.setChecked(itemBean.isFinish);
        viewHolder.title.setText(itemBean.title);
        viewHolder.time.setText(itemBean.time);
        if (itemBean.isFinish) {
            setBackground(viewHolder, R.drawable.checkbox, context.getResources().getColor(R.color.hide), R.drawable.fdelete);
        } else {
            setBackground(viewHolder, R.drawable.box, context.getResources().getColor(R.color.black), R.drawable.delete);
        }
        if (!deleteEnable) {
            viewHolder.delete.setVisibility(View.GONE);
        } else {
            viewHolder.delete.setVisibility(View.VISIBLE);
        }

        viewHolder.title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                long down_time = event.getDownTime();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        long up_time = event.getEventTime();
                        if ((up_time-down_time)>2000) {
                            startdraglistener.onStartDrag(viewHolder);
                        }
                        break;
                }
                return true;
            }
        });
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("是否要删除?");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataManager.getInstance().delItem(itemBean);
                        notifyDataSetChanged();
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
    }

    private void setBackground(ItemViewHolder viewHolder, int checkbox, int color, int delete) {
        viewHolder.check.setButtonDrawable(checkbox);
        viewHolder.title.setTextColor(color);
        viewHolder.time.setTextColor(color);
        viewHolder.collectboxname.setTextColor(color);
        viewHolder.delete.setBackground(context.getResources().getDrawable(delete));
    }

    @Override
    public int getItemCount() {
        return data == null || data.size() == 0 ? 0 : sortData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == ndatasize + 1) {
            return VIEW_INDEX;
        } else {
            return VIEW_ITEM;
        }
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        BaseBean baseBean = sortData.get(fromPosition);

        Collections.swap(sortData, fromPosition, toPosition);
        //        刷新数据
        if (toPosition != 0 && toPosition != ndatasize + 1) {
            if ((fromPosition <= ndatasize && toPosition <= ndatasize) || (fromPosition > ndatasize + 1 && toPosition > ndatasize + 1)) {
                notifyItemMoved(fromPosition, toPosition);
                ArrayList<Integer> last = new ArrayList<>();
                for (BaseBean itemBean1 : bufferData) {
                    last.add(((ItemBean) itemBean1).sortNo);
                }
                if (fromPosition <= ndatasize && toPosition <= ndatasize) {
                    bufferData.remove(fromPosition - 1);
                    bufferData.add(toPosition - 1, baseBean);
                } else {
                    bufferData.remove(fromPosition - 2);
                    bufferData.add(toPosition - 2, baseBean);
                }
                for (int i = 0; i < bufferData.size(); i++) {
                    ((ItemBean) bufferData.get(i)).sortNo = last.get(i);
                }
                DataManager.getInstance().updaterItems(bufferData);
            }
        }
        return false;
    }

    @Override
    public void onItemSwiped(int adapterPosition) {
        ItemBean itemBean = (ItemBean) sortData.get(adapterPosition);
        if (!itemBean.isFinish) {
            itemBean.isFinish = true;
        } else {
            itemBean.isFinish = false;
        }
        itemBean.time = itemBean.getTime(itemBean.finishTime);
        DataManager.getInstance().updateItem(itemBean, itemBean);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox check;
        TextView title;
        TextView time;
        TextView collectboxname;
        ImageView delete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            check = (CheckBox) itemView.findViewById(R.id.check);
            title = (TextView) itemView.findViewById(R.id.title);
            time = (TextView) itemView.findViewById(R.id.time);
            collectboxname = (TextView) itemView.findViewById(R.id.collectboxname);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    class IndexViewHolder extends RecyclerView.ViewHolder {
        TextView index;

        public IndexViewHolder(View itemView) {
            super(itemView);
            index = (TextView) itemView.findViewById(R.id.index);
        }
    }


}
