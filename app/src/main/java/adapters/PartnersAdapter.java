package adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
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
import objects.PartnersItem;

public class PartnersAdapter  extends RecyclerView.Adapter<PartnersAdapter.MyViewHolder> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private Context mContext;
    private List<PartnersItem> albumList;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price;
        public ImageView photo;

        public MyViewHolder(View view) {
            super(view);
            name =   view.findViewById(R.id.textView11);
            photo =   view.findViewById(R.id.photo);

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

    public PartnersAdapter(Context mContext, List<PartnersItem> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;

    }

    @Override
    public PartnersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invite_item, parent, false);


        return new PartnersAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(final PartnersAdapter.MyViewHolder holder, int position) {
            PartnersItem album = albumList.get(position);
        if (imageLoader == null)
        {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        holder.name.setText(Html.fromHtml("<b>" + album.getF_name() + "</b>") + " has invited you to join their "+ album.getName());

      imageLoader.get(album.getPhoto(), new ImageLoader.ImageListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("image", "Image Load Error: " + error.getMessage());
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                if (response.getBitmap() != null) {
                    // load image into imageview
                    holder.photo.setImageBitmap(response.getBitmap());
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
