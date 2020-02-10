package com.viconajz.audiostream.ui.fragments.stream;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.viconajz.audiostream.R;
import com.viconajz.audiostream.api.pojo.Stream;

import java.util.List;

public class StreamAdapter extends RecyclerView.Adapter<StreamHolder>{

    private List<Stream> streams;
    private IStreamSelectListener listener;

    StreamAdapter(List<Stream> streams, IStreamSelectListener listener) {
        this.streams = streams;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StreamHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;
        view = inflater.inflate(R.layout.item_stream, viewGroup, false);
        return new StreamHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamHolder streamHolder, int i) {
        Stream stream = streams.get(i);
        streamHolder.bind(stream);
    }

    @Override
    public int getItemCount() {
        if (streams != null) {
            return streams.size();
        } else return 0;
    }
}
