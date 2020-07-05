package com.aditya.githubconsumer;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import static com.aditya.githubconsumer.MainActivity.COLUMN_AVATAR;
import static com.aditya.githubconsumer.MainActivity.COLUMN_NAME;
import static com.aditya.githubconsumer.MainActivity.COLUMN_URL;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Cursor mCursor;

    void setUser(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_cardview, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (mCursor != null) {
            if (mCursor.moveToPosition(position)) {
                String urlImage = mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_AVATAR));
                holder.tvNama.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_NAME)));
                holder.tvUrl.setText(mCursor.getString(mCursor.getColumnIndexOrThrow(COLUMN_URL)));
                Glide.with(holder.itemView.getContext())
                        .load(urlImage)
                        .into(holder.imgAvatar);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView tvNama;
        TextView tvUrl;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.user_image);
            tvNama = itemView.findViewById(R.id.tv_user_name);
            tvUrl = itemView.findViewById(R.id.tv_user_url);
        }
    }
}
