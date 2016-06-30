package com.dc.wibome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.wibome.R;
import com.dc.wibome.entity.response.UserItem;
import com.dc.wibome.utils.ToastUtils;

import java.util.List;

/**
 * 用户的列表适配器
 */
public class UserItemAdapter extends BaseAdapter {

    private Context context;
    private List<UserItem> datas;

    public UserItemAdapter(Context context, List<UserItem> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public UserItem getItem(int position) {

        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_user, null);

            holder.v_divider = convertView.findViewById(R.id.v_divider);
            //除隐藏布局之外的总布局
            holder.ll_content = convertView.findViewById(R.id.ll_content);
            holder.iv_left = (ImageView) convertView.findViewById(R.id.iv_left);
            holder.tv_subhead = (TextView) convertView.findViewById(R.id.tv_subhead);
            holder.tv_caption = (TextView) convertView.findViewById(R.id.tv_caption);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // set data
        UserItem item = getItem(position);
        holder.iv_left.setImageResource(item.getLeftImg());
        holder.tv_subhead.setText(item.getSubhead());
        holder.tv_caption.setText(item.getCaption());
        //true 显示 分割栏
        holder.v_divider.setVisibility(item.isShowTopDivider() ?
                View.VISIBLE : View.GONE);
        //不想点击分隔栏 进行事件，所以不能使用列表得itemOnclick事件，需要除分隔栏的布局点击事件
        holder.ll_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context, "item click position = " + position, Toast.LENGTH_SHORT);
            }
        });

        return convertView;
    }

    public static class ViewHolder{
        public View v_divider;
        public View ll_content;
        public ImageView iv_left;
        public TextView tv_subhead;
        public TextView tv_caption;
    }


}
