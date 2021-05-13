package com.caiy.view.learn.recycler;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caiy.view.learn.R;
import com.caiy.view.learn.view.custom.MyFocusView;

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

        // data to populate the RecyclerView with
        List<String> animalNames = new ArrayList<>();
        animalNames.add("Horse");
        animalNames.add("Cow");
        animalNames.add("Camel");
        animalNames.add("Sheep");
        animalNames.add("Goat");
        animalNames.add("张三");
        animalNames.add("李四");
        animalNames.add("王五");
        animalNames.add("赵六");

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyRecyclerViewAdapter(this, animalNames);
        mAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecyclerMainActivity.this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
    }

    public void triggerRequestLayout(View view) {
        mRecyclerView.requestLayout();
    }

    public void triggerNotifyDataSetChange(View view) {
        mAdapter.notifyDataSetChanged();
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

        // parent activity will implement this method to respond to click events
        public interface ItemClickListener {
            void onItemClick(View view, int position);
        }
    }
}
