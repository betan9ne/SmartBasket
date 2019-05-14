package adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import apps.betan9ne.smartbasket.R;
import fragments.ShopFrament;
import helper.AppController;
import helper.ItemClickListener;
import helper.ItemClickListenerB;
import objects.PartnersItem;
import objects.TagItem;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.MyViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Context mContext;
    private List<TagItem> albumList;
    private ItemClickListenerB clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public View asd;

        public MyViewHolder(View view) {
            super(view);
            name =   view.findViewById(R.id.textView16);
            asd = view.findViewById(R.id.asd);
             itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListenerB itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public CatAdapter(Context mContext, List<TagItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public CatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cat_item, parent, false);


        return new CatAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final CatAdapter.MyViewHolder holder, int position) {
        final TagItem album = albumList.get(position);
        holder.asd.setBackgroundColor(Color.parseColor("#35479D"));
        holder.name.setText(album.getName());
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void removeItem(int position) {
        albumList.remove(position);
        notifyItemRemoved(position);
    }
}
