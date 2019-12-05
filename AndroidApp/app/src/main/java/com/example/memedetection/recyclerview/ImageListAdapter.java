package com.example.memedetection.recyclerview;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.memedetection.R;
import com.example.memedetection.Room.Images;

import java.io.File;
import java.util.List;

import static com.example.memedetection.Utils.Constants.IMAGE_STATUS_MEME;


public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageViewHolder> {

    class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ItemView;
        private final ImageView memeIcon;

        private ImageViewHolder(View itemView) {
            super(itemView);
            ItemView = itemView.findViewById(R.id.iv_image);
            memeIcon = itemView.findViewById(R.id.is_it_meme);

        }
    }

    private final LayoutInflater mInflater;
    private List<Images> mImagePath; // Cached copy of words
    Context context;

    public ImageListAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        if (mImagePath != null) {

//            holder.empty_list.setVisibility(View.GONE);
            Images current = mImagePath.get(position);
            String imagePath = /*WHATSAPP_IMAGE_PATH+"/"+*/current.getImagePath();
            Uri imageUri = Uri.fromFile(new File(imagePath));

            Glide.with(context).load(imageUri)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(new RequestOptions().override(600, 200))
                    .skipMemoryCache(true)
                    .into(holder.ItemView);
//            holder.wordItemView.setText(current.getImagePath());

            if (current.getImageMemeStatus().equalsIgnoreCase(IMAGE_STATUS_MEME)) {
                holder.memeIcon.setImageResource(R.drawable.meme);
                holder.memeIcon.setVisibility(View.VISIBLE);

            } else {
                holder.memeIcon.setVisibility(View.GONE);

            }
        } else {
            Toast.makeText(context, "Empty list", Toast.LENGTH_SHORT).show();
        }

    }

    public void setImages(List<Images> imgs) {
        mImagePath = imgs;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mImagePath != null)
            return mImagePath.size();
        else return 0;
    }
}


