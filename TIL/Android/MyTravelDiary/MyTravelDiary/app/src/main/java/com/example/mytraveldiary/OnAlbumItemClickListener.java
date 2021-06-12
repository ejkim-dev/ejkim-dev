package com.example.mytraveldiary;

import android.view.View;

import com.example.mytraveldiary.adepters.AlbumContentsAdapter;

public interface OnAlbumItemClickListener {
    public void onItemClick(MyAlbumAdapter.ViewHolder holder, View view, int position);
}

