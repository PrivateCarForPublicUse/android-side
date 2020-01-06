package com.privatecarforpublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Master;

import java.util.List;

public class AdminAdapter extends BaseAdapter {
    private List<Master> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Master getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public AdminAdapter(Context _context, List<Master> _list) {
        this.mContext = _context;
        this.mList = _list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.admin_item, parent, false);
            holder.admin_name = convertView.findViewById(R.id.admin_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Master master = getItem(position);
        holder.admin_name.setText(master.getMasterName());

        return convertView;
    }

    private class ViewHolder {
        private TextView admin_name;
    }

    public void updateView(List<Master> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }
}
