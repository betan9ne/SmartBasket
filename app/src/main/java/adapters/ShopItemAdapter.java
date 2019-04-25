package adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.betan9ne.smartbasket.R;
import helper.ItemClickListener;
import objects.BasketItem;

public class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.MyViewHolder> {


    private Context mContext;
    private List<BasketItem> albumList;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price;


        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);

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

    public ShopItemAdapter(Context mContext, List<BasketItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public ShopItemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shop_item, parent, false);


        return new ShopItemAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final ShopItemAdapter.MyViewHolder holder, int position) {
        //holder.bind(albumList.get(position), listener);
        BasketItem album = albumList.get(position);
        holder.name.setText(album.getName());
        holder.price.setText("K"+album.getPrice());


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
