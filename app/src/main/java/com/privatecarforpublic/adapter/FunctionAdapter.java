package com.privatecarforpublic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Function;

import java.util.List;

public class FunctionAdapter extends BaseAdapter {
    private List<Function> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Function getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public FunctionAdapter(Context _context, List<Function> _list) {
        this.mContext = _context;
        this.mList=_list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.function_item, parent, false);
            holder.function_icon = convertView.findViewById(R.id.function_icon);
            holder.function_name = convertView.findViewById(R.id.function_name);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Function function = getItem(position);
        holder.function_icon.setImageResource(R.drawable.star);
        holder.function_name.setText(function.getFunctionName());
        return convertView;
    }

    private class ViewHolder {
        private ImageView function_icon;
        private TextView function_name;
    }
}