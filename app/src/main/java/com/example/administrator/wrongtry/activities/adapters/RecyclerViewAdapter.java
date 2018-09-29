package com.example.administrator.wrongtry.activities.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.wrongtry.R;
import com.example.administrator.wrongtry.activities.activities.ChartActivity;
import com.example.administrator.wrongtry.activities.data.Item;

import java.util.List;

/**
 * Created by QiWangming on 2015/6/13.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.NewsViewHolder>{

    private List<Item> itemlist;
    private Context context;

    public RecyclerViewAdapter(List<Item> itemlist,Context context) {
        this.itemlist = itemlist;
        this.context=context;
    }

    //自定义ViewHolder类
    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        public CardView cardView;
        public TextView date;
        public TextView time;

        public NewsViewHolder(View itemView) {
            super(itemView);
            cardView= (CardView) itemView.findViewById(R.id.card_view);
            date= (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            //设置TextView背景为半透明
            //news_title.setBackgroundColor(Color.argb(20, 0, 0, 0));
        }
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(context).inflate(R.layout.item,viewGroup,false);
        final NewsViewHolder holder = new NewsViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        final Item item = itemlist.get(position);
        holder.date.setText(itemlist.get(position).getDate());
        holder.time.setText(itemlist.get(position).getTime());

        //为btn_share btn_readMore cardView设置点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ChartActivity.class);
                intent.putExtra("time",itemlist.get(position).getRecord());
                context.startActivity(intent);
                int position = holder.getAdapterPosition();
                Item item = itemlist.get(position);
                Toast.makeText(v.getContext(),item.getDate()+" "+item.getTime(),Toast.LENGTH_SHORT).show();
            }
        });

        /*personViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                intent.putExtra(Intent.EXTRA_TEXT, newses.get(j).getDesc());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(Intent.createChooser(intent, newses.get(j).getTitle()));
            }
        });

        personViewHolder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,NewsActivity.class);
                intent.putExtra("News",newses.get(j));
                context.startActivity(intent);
            }
        });*/


    }

    @Override
    public int getItemCount() {
        return itemlist.size();
    }
}
