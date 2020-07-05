package com.aditya.usergithub.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aditya.usergithub.R;
import com.aditya.usergithub.model.FavoritModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.FavoritViewHolder> {

    private List<FavoritModel> listFavorit = new ArrayList<>();

    void setData(List<FavoritModel> favoritModels){
        listFavorit.clear();
        listFavorit.addAll(favoritModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoritViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardview, parent, false);
        return new FavoritViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritViewHolder holder, int position) {
        FavoritModel favoritModel = listFavorit.get(position);
        holder.btn.setBackgroundResource(R.drawable.ic_delete);
        Glide.with(holder.itemView.getContext())
                .load(favoritModel.getAvatarUrl())
                .into(holder.imgAvatar);
        holder.tvNama.setText(favoritModel.getNama());
        holder.tvUrl.setText(favoritModel.getUrl());

    }

    @Override
    public int getItemCount() {
        if (listFavorit != null) {
            return listFavorit.size();
        } else {
            return 0;
        }
    }

    class FavoritViewHolder extends RecyclerView.ViewHolder {

        TextView tvNama;
        TextView tvUrl;
        ImageView imgAvatar;
        Button btn;

        FavoritViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.user_image);
            tvNama = itemView.findViewById(R.id.tv_user_name);
            tvUrl = itemView.findViewById(R.id.tv_user_url);
            btn = itemView.findViewById(R.id.btn_favorit);
        }
    }
}
