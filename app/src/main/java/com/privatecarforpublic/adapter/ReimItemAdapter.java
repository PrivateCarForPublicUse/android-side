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
        ViewHoler viewHoler;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHoler = new ViewHoler();
            viewHoler.img = view.findViewById(R.id.reim_item_headImage);
            viewHoler.time = view.findViewById(R.id.reim_item_time);
            viewHoler.start = view.findViewById(R.id.reim_item_start);
            viewHoler.end = view.findViewById(R.id.reim_item_end);
            viewHoler.status = view.findViewById(R.id.reim_item_status);

            view.setTag(viewHoler);   //将viewHolder存在view中
        } else {
            view = convertView;  //进行了listView的性能优化，滑动的时候感觉不会那么卡
            viewHoler = (ViewHoler)view.getTag(); //获取
        }

        viewHoler.img.setImageResource(reimburse.getImgId());
        viewHoler.time.setText(reimburse.getTime());
        viewHoler.start.setText(reimburse.getStart());
        viewHoler.end.setText(reimburse.getEnd());
        viewHoler.status.setText(reimburse.getStatus());
        return view;
    }

    class ViewHoler {
        private CircleImageView img;
        private TextView time;
        private TextView start;
        private TextView end;
        private TextView status;
    }
}
