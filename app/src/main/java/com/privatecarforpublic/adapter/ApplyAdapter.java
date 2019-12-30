package com.privatecarforpublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Apply;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ApplyAdapter extends BaseAdapter {
    private List<Apply> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Apply getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ApplyAdapter(Context _context, List<Apply> _list) {
        this.mContext = _context;
        this.mList = _list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.apply_item, parent, false);
            holder.picture = convertView.findViewById(R.id.picture);
            holder.license = convertView.findViewById(R.id.license);
            holder.apply_user = convertView.findViewById(R.id.apply_user);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Apply apply = getItem(position);
        //Picasso.get().load(apply.getUser().getHeadPhotoUrl()).into(holder.picture);

        holder.license.setText(apply.getCar().getLicense());
        holder.apply_user.setText(apply.getUser().getName());
        return convertView;
    }

    private class ViewHolder {
        private ImageView picture;
        private TextView license;
        private TextView apply_user;
    }

    public void updateView(List<Apply> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }
}
