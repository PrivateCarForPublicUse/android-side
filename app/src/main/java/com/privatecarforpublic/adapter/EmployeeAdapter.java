package com.privatecarforpublic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.User;

import java.util.List;

public class EmployeeAdapter extends BaseAdapter {
    private List<User> mList;
    private Context mContext;

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public User getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public EmployeeAdapter(Context _context, List<User> _list) {
        this.mContext = _context;
        this.mList=_list;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.employee_item, parent, false);
            holder.headImage = convertView.findViewById(R.id.headImage);
            holder.employee_name = convertView.findViewById(R.id.employee_name);
            holder.employee_star = convertView.findViewById(R.id.employee_star);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        User user = getItem(position);
        holder.headImage.setImageResource(R.drawable.head_image);
        holder.employee_name.setText(user.getName());
        holder.employee_star.setText(user.getStarLevel()+"");
        return convertView;
    }

    private class ViewHolder {
        private ImageView headImage;
        private TextView employee_name;
        private TextView employee_star;
    }

    public void updateView(List<User> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }
}