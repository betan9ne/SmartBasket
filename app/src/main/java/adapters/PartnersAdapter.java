package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import apps.betan9ne.smartbasket.R;
import helper.ItemClickListener;
import objects.BasketItem;
import objects.PartnersItem;

public class PartnersAdapter  extends RecyclerView.Adapter<PartnersAdapter.MyViewHolder> {


    private Context mContext;
    private List<PartnersItem> albumList;
    private ItemClickListener clickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price;


        public MyViewHolder(View view) {
            super(view);
            name =   view.findViewById(R.id.textView11);

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
        //holder.bind(albumList.get(position), listener);
        PartnersItem album = albumList.get(position);
        holder.name.setText(Html.fromHtml("<b>" + album.getF_name() + "</b>") + " has invited you to join their "+ album.getName());



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
