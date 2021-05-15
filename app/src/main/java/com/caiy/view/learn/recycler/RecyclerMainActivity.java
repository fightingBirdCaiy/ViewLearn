package com.caiy.view.learn.recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caiy.view.learn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * created by caiyong at 2020/6/14
 */
public class RecyclerMainActivity extends Activity {

    private static final String TAG = "Learn/RecyclerMainActivity";

    private RecyclerView mRecyclerView;
    private MyRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_main);
        initView();
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.v_recycler);

        mRecyclerView.getItemAnimator().setAddDuration(1000L);
        mRecyclerView.getItemAnimator().setChangeDuration(1000L);
        mRecyclerView.getItemAnimator().setMoveDuration(1000L);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000L);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MyRecyclerViewAdapter(this, getInitDataList());
        mAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecyclerMainActivity.this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    private List<String> getInitDataList() {
        List<java.lang.String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");
        animalNames.add("张三");
        animalNames.add("李四");
        animalNames.add("王五");
        animalNames.add("赵六");
        return animalNames;
    }

    public void triggerRequestLayout(View view) {
        mRecyclerView.requestLayout();
    }

    public void triggerNotifyDataSetChange(View view) {
        mAdapter.setDataList(getInitDataList());
        mAdapter.notifyDataSetChanged();
    }

    public void triggerNotifyItemInserted(View view) {
        mAdapter.addData("插入数据", 1);
        mAdapter.notifyItemInserted(1);
    }

    public void triggerNotifyItemRemoved(View view) {
        mAdapter.removeData(1);
        mAdapter.notifyItemRemoved(1);
    }

    public void triggerNotifyItemChanged(View view) {
        mAdapter.changeData("改变数据",1);
        mAdapter.notifyItemChanged(1);
    }

    public void triggerNotifyItemMoved(View view) {
        mAdapter.moveData(0,2);
        mAdapter.notifyItemMoved(0, 2);
    }

    public static class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

        private List<String> mDataList;
        private LayoutInflater mInflater;
        private ItemClickListener mClickListener;

        // data is passed into the constructor
        MyRecyclerViewAdapter(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.mDataList = data;
        }

        // inflates the row layout from xml when needed
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recycler_view_row, parent, false);
            view.setFocusable(true);
            view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    v.setBackgroundColor(hasFocus ? v.getContext().getResources().getColor(R.color.colorBlue) : v.getContext().getResources().getColor(R.color.colorGray));
                }
            });
            return new ViewHolder(view);
        }

        // binds the data to the TextView in each row
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String animal = mDataList.get(position);
            holder.itemView.setBackgroundColor(holder.itemView.hasFocus() ? holder.itemView.getContext().getResources().getColor(R.color.colorBlue) : holder.itemView.getContext().getResources().getColor(R.color.colorGray));

            holder.myTextView.setText(animal);
        }

        // total number of rows
        @Override
        public int getItemCount() {
            return mDataList.size();
        }


        // stores and recycles views as they are scrolled off screen
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView myTextView;

            ViewHolder(View itemView) {
                super(itemView);
                myTextView = itemView.findViewById(R.id.tvAnimalName);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        // convenience method for getting data at click position
        String getItem(int id) {
            return mDataList.get(id);
        }

        // allows clicks events to be caught
        void setClickListener(ItemClickListener itemClickListener) {
            this.mClickListener = itemClickListener;
        }

        public void setDataList(List<String> dataList) {
            mDataList.clear();
            if (dataList != null) {
                mDataList.addAll(dataList);
            }
        }

        public void addData(String data, int index) {
            mDataList.add(index, data);
        }

        public void removeData(int index) {
            mDataList.remove(index);
        }

        public void changeData(String data, int index) {
            mDataList.set(index, data);
        }

        public void moveData(int fromIndex, int toIndex) {
            if (fromIndex < toIndex) {
                String fromValue = mDataList.remove(fromIndex);
                mDataList.add(toIndex -1, fromValue);
            } else {
                String fromValue = mDataList.remove(fromIndex);
                mDataList.add(toIndex, fromValue);
            }
        }

        // parent activity will implement this method to respond to click events
        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }
}
