package com.example.mytraveldiary.interfaces;

import android.view.View;

import com.example.mytraveldiary.adepters.AlbumContentsAdapter;

public interface OnAlbumContentsItemClickListener {
    public void onItemClick(AlbumContentsAdapter.ViewHolder holder, View view, int position);
}
