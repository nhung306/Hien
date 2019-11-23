package org.tttnhung.hien;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterSearch extends RecyclerView.Adapter<AdapterSearch.ViewHolder> {

        Context context;
        ArrayList<ImageInfo> list;

    public AdapterSearch(Context context, ArrayList<ImageInfo> list) {

        this.list = list;
        this.context = context;
    }

    public void filterList(ArrayList<ImageInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        return new ViewHolder(v);
        }

    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {
        holder.imageNameTextView.setText("Name : " + list.get(position).getName());
        Glide.with(context).load(list.get(position).getImage()).into(holder.imageView);

        if (position <= (list.size() - 1)) {
            holder.btn_buy.setVisibility(Button.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
        }


    class ViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView imageNameTextView;
    public Button btn_buy;

        public ViewHolder(View itemView) {
        super(itemView);
        btn_buy = (Button) itemView.findViewById(R.id.btn_buy);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        imageNameTextView = (TextView) itemView.findViewById(R.id.name_text);
        }
    }
}