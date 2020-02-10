package com.viconajz.audiostream.ui.fragments.stream;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viconajz.audiostream.R;
import com.viconajz.audiostream.api.pojo.Stream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StreamHolder extends RecyclerView.ViewHolder {

    private IStreamSelectListener listener;

    @BindView(R.id.btn_stream)
    Button btnStream;

    @BindView(R.id.iv_play)
    ImageView ivPlay;

    @BindView(R.id.iv_stop)
    ImageView ivStop;

    StreamHolder(@NonNull View itemView, IStreamSelectListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    void bind(Stream stream) {
        if (stream.isPlaying()) {
            ivPlay.setVisibility(View.GONE);
            ivStop.setVisibility(View.VISIBLE);
        }
        else {
            ivStop.setVisibility(View.GONE);
            ivPlay.setVisibility(View.VISIBLE);
        }
        btnStream.setText(stream.getName());
        ivPlay.setOnClickListener((v) -> {
            ivPlay.setVisibility(View.GONE);
            ivStop.setVisibility(View.VISIBLE);
            listener.playStream(stream);
        });
        ivStop.setOnClickListener((v) -> {
            ivStop.setVisibility(View.GONE);
            ivPlay.setVisibility(View.VISIBLE);
            listener.pauseStream(stream);
        });
    }
}
