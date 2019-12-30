package com.privatecarforpublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.SecRoute;

import java.util.List;

public class SecRouteAdapter extends BaseAdapter {
    private List<SecRoute> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public SecRoute getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public SecRouteAdapter(Context _context, List<SecRoute> _list) {
        this.mContext = _context;
        this.mList=_list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sec_route_item, parent, false);
            holder.departure = convertView.findViewById(R.id.departure);
            holder.destination = convertView.findViewById(R.id.destination);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        SecRoute secRoute = getItem(position);
        holder.departure.setText(secRoute.getOrigin());
        holder.destination.setText(secRoute.getDestination());
        return convertView;
    }

    private class ViewHolder {
        private TextView departure;
        private TextView destination;
    }
}