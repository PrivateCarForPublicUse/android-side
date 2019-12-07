package com.privatecarforpublic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.privatecarforpublic.R;
import com.privatecarforpublic.model.Reimburse;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @date:2019/12/7
 * @author:zhongcz
 * @description: 报销管理页面的listview  Item的适配器
 */
public class ReimItemAdapter extends ArrayAdapter<Reimburse> {
    private int resourceId;

    public ReimItemAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<Reimburse> objects) {
        super(context, textViewResourceId, objects);
        this.resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reimburse reimburse = getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;  //进行了listView的性能优化，滑动的时候感觉不会那么卡
        }
        CircleImageView img = view.findViewById(R.id.reim_item_headImage);
        TextView time = view.findViewById(R.id.reim_item_time);
        TextView start = view.findViewById(R.id.reim_item_start);
        TextView end = view.findViewById(R.id.reim_item_end);
        TextView status = view.findViewById(R.id.reim_item_status);

        img.setImageResource(reimburse.getImgId());
        time.setText(reimburse.getTime());
        start.setText(reimburse.getStart());
        end.setText(reimburse.getEnd());
        status.setText(reimburse.getStatus());
        return view;
    }
}
