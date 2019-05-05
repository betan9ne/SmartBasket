package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import apps.betan9ne.smartbasket.R;
import helper.AppController;
import helper.ItemClickListener;
import objects.BasketItem;
import objects.ReceiptItem;

/**
 * Created by chisomo on 14-Dec-17.
 */

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {


    private Context mContext;
    private List<ReceiptItem> albumList;
    private ItemClickListener clickListener;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, descr, title;
        public ImageView image;
 public View viewBackground;
        public View viewForeground;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            descr = (TextView) view.findViewById(R.id.descr);
            title = (TextView) view.findViewById(R.id.title);
            image =  view.findViewById(R.id.img);
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

    public ReceiptAdapter(Context mContext, List<ReceiptItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_receipt_layout, parent, false);


        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        //holder.bind(albumList.get(position), listener);
        ReceiptItem album = albumList.get(position);
        holder.name.setText(album.getReceipt());
        holder.descr.setText(album.getDescr());
        holder.title.setText(album.getTitle());

        imageLoader.get(album.getReceipt(), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("image", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    holder.image.setImageBitmap(response.getBitmap());
                }
            }

        });
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
