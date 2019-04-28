package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;


import apps.betan9ne.smartbasket.R;
import helper.ItemClickListener;

import objects.BasketItem;

/**
 * Created by chisomo on 14-Dec-17.
 */

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.MyViewHolder> {


    private Context mContext;
    private List<BasketItem> albumList;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price, cost, q;
 public View viewBackground;
        public View viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            cost = (TextView) view.findViewById(R.id.cost);
            q = (TextView) view.findViewById(R.id.q);
             viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public BasketAdapter(Context mContext, List<BasketItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.baskets, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //holder.bind(albumList.get(position), listener);
        BasketItem album = albumList.get(position);
        holder.name.setText(album.getName()+ " (" +  album.getQ()+")");
        holder.price.setText("K"+album.getPrice());
        holder.q.setText(album.getAddedBy());
        holder.cost.setText("Total: "+Float.parseFloat( album.getPrice() * album.getQ()+""));

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
