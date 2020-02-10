package com.viconajz.audiostream.ui.fragments.stream;

import android.util.Log;

import com.viconajz.audiostream.api.AudioApi;
import com.viconajz.audiostream.api.pojo.Stream;
import com.viconajz.audiostream.ui.infrastructures.BasePresenterImp;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class StreamPresenter extends BasePresenterImp<IStreamContract.View> implements IStreamContract.Presenter, IStreamSelectListener {

    private AudioApi audioApi;
    private StreamAdapter adapter;
    private List<Stream> streams;
    private String currentStreamUrl;

    public StreamPresenter(IStreamContract.View view, AudioApi audioApi) {
        super(view, audioApi);
        this.audioApi = audioApi;
    }

    @Override
    public void loadStreams(String ip) {
        String url = "http://" + ip + "/getstreams";
//        String url = "https://eventlab.uz//getstreams";
        mCompositeDisposable.add(audioApi.getStreamList(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(streams -> {
                    this.streams = streams;
                    adapter = new StreamAdapter(streams, this);
                    mView.setAdapter(adapter);
                }, throwable -> {
                    mView.setText(throwable.getMessage());
                }));
    }

    @Override
    public void updateStream(String action) {
        for (Stream stream : streams) {
            if (stream.getUrl().equals(currentStreamUrl)) {
                stream.setPlaying(action.equals(StreamingService.ACTION_PLAY));
                adapter.notifyItemChanged(streams.indexOf(stream));
            }
        }
    }

    @Override
    public void playStream(Stream stream) {
        currentStreamUrl = stream.getUrl();
        for (Stream currentStream : streams) {
            currentStream.setPlaying(currentStream == stream);
        }
        adapter.notifyDataSetChanged();
        mView.play(stream.getUrl(), stream.getName());

//        SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(mView.getContext());
//        // Produces DataSource instances through which media data is loaded.
//        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mView.getContext(),
//                Util.getUserAgent(mView.getContext(), "Audio Stream"));
//        // This is the MediaSource representing the media to be played.
//        MediaSource audioSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(Uri.parse(stream.getUrl()));
//
//        // Prepare the player with the source.
//        player.prepare(audioSource);
//        player.setPlayWhenReady(true);
    }

    @Override
    public void pauseStream(Stream stream) {


        mView.pause();
    }
}
