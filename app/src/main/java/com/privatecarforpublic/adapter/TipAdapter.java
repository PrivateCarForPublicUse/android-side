package com.privatecarforpublic.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.privatecarforpublic.R;

import java.util.List;

public class TipAdapter extends BaseAdapter {
    private List<Tip> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Tip getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public TipAdapter(Context _context, List<Tip> _list) {
        this.mContext = _context;
        this.mList=_list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.tip_item, parent, false);
        }
        Tip tip = getItem(position);
        TextView name = convertView.findViewById(R.id.name);
        name.setText(TextUtils.isEmpty(tip.getName()) ? "" : tip.getName());
        TextView address=convertView.findViewById(R.id.address);
        address.setText(TextUtils.isEmpty(tip.getAddress()) ? "" : tip.getAddress());
        return convertView;
    }

}