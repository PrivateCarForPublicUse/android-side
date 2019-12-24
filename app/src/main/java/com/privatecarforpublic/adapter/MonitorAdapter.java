package com.privatecarforpublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Monitor;

import java.util.List;

public class MonitorAdapter extends BaseAdapter {
    private List<Monitor> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Monitor getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public MonitorAdapter(Context _context, List<Monitor> _list) {
        this.mContext = _context;
        this.mList = _list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.monitor_item, parent, false);
            holder.picture = convertView.findViewById(R.id.picture);
            holder.license = convertView.findViewById(R.id.license);
            holder.type = convertView.findViewById(R.id.type);
            holder.star = convertView.findViewById(R.id.star);
            holder.user = convertView.findViewById(R.id.user_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Monitor monitor = getItem(position);
        //Picasso.get().load(car.getPicture()).into(holder.picture);

        holder.license.setText(monitor.getCar().getLicense());
        holder.type.setText(monitor.getCar().getType());
        holder.star.setText(monitor.getCar().getStarOfCar() + "");
        holder.user.setText(monitor.getUser().getName() + "正在出车中");
        return convertView;
    }

    private class ViewHolder {
        private ImageView picture;
        private TextView license;
        private TextView type;
        private TextView star;
        private TextView user;
    }
}
