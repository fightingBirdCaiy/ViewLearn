package com.caiy.view.learn;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.caiy.view.learn.view.custom.MyScrollView;
import com.caiy.view.learn.view.list.ListAdapter;
import com.caiy.view.learn.view.list.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * created by caiyong at 2020/6/14
 */
public class ListActivity extends Activity {

    private static final String TAG = "Learn/ListActivity";
    private String[] COLORS = {"#008577", "#D81B60", "#FF0000"};

    private ListView mListView;
    private TestAdaper mAdapter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        triggerListViewRequestLayout();
//        triggerListViewForceLayout();
        triggerDataSetChange();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.v_list);
    }

    private void initData() {
        mAdapter = new TestAdaper();
        mAdapter.setData(buildDataList(0, 3));
        mListView.setAdaper(mAdapter);
    }

    private List<Data> buildDataList(int start, int count) {
        List<Data> result = new ArrayList<>();
        for (int i = start; i < count; i++) {
            Data data = new Data();
            data.title = "第" + i + "条数据";
            data.bgColor = COLORS[i % COLORS.length];
            result.add(data);
        }
        return result;
    }

    private void triggerListViewRequestLayout() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.requestLayout();//会触发ListView的onLayout方法
            }
        },3000L);
    }

    private void triggerListViewForceLayout() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.forceLayout();//不会触发ListView的onLayout方法
            }
        },3000L);
    }

    private void triggerDataSetChange() {
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setData(buildDataList(4, 8));
                mAdapter.notifyDataSetChanged();
            }
        },3000L);
    }

    private class TestAdaper extends ListAdapter<ListActivity.Data> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ListActivity.this).inflate(R.layout.item_list, parent, false);
                holder = new ViewHolder();
                holder.backgroudView = (FrameLayout) convertView.findViewById(R.id.fl_bg);
                holder.titleView = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Data data = getItem(position);
            holder.titleView.setText(data.title);
            holder.backgroudView.setBackgroundColor(Color.parseColor(data.bgColor));
            Log.d(TAG, "getView: position=" + position + " convertView=" + convertView);
            return convertView;
        }
    }

    private class ViewHolder {
        FrameLayout backgroudView;
        TextView titleView;
    }

    private class Data {
        String title;
        String bgColor;
    }
}
