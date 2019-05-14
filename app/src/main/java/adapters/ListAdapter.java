package adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;


import apps.betan9ne.smartbasket.R;
import helper.AppConfig;
import helper.AppController;
import helper.ItemClickListener;
import objects.ProductItem;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ProductItem item);
    }

   // ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    private Context mContext;
    private List<ProductItem> albumList;
    private ItemClickListener clickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemcount, name,partners ;
        public LinearLayout bck;

        public MyViewHolder(View view) {
            super(view);
            itemcount = view.findViewById(R.id._count);
            name = view.findViewById(R.id.name);
            partners =  view.findViewById(R.id.partners);
            bck = view.findViewById(R.id.bck);
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

    public ListAdapter(Context mContext, List<ProductItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ProductItem album = albumList.get(position);

      if(position % 3 == 0)
        {
            holder.bck.setBackgroundColor(Color.parseColor("#28267A"));
        }
        else if (position % 2 == 0)
        {
            holder.bck.setBackgroundColor(Color.parseColor("#3D9AD5"));
        }
        holder.name.setText(album.getName());
        holder.itemcount.setText(album.getItem_count()+" Items");
        holder.partners.setText(album.getF_name());

    }




    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
