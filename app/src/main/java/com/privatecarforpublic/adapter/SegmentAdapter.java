package com.privatecarforpublic.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.activity.RegimeActivity;
import com.privatecarforpublic.activity.SearchPlaceActivity;
import com.privatecarforpublic.model.Segment;

import java.util.List;

public class SegmentAdapter extends BaseAdapter{
    private List<Segment> mList;
    private Context mContext;
    private Activity activity;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Segment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SegmentAdapter(Context _context,Activity _activity, List<Segment> _list) {
        this.activity=_activity;
        this.mContext = _context;
        this.mList=_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.segment_item, parent, false);
            holder.departure = convertView.findViewById(R.id.departure);
            holder.destination = convertView.findViewById(R.id.destination);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Segment segment = getItem(position);
        holder.departure.setText(segment.getDeparture().getName());
        holder.departure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchPlaceActivity.class);
                intent.putExtra("position",position);
                activity.startActivityForResult(intent, RegimeActivity.TO_SEARCH_DEPARTURE);
            }
        });
        holder.destination.setText(segment.getDestination().getName());
        holder.destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SearchPlaceActivity.class);
                intent.putExtra("position",position);
                activity.startActivityForResult(intent, RegimeActivity.TO_SEARCH_DESTINATION);
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView departure;
        private TextView destination;
    }
}
