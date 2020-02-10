package com.viconajz.audiostream.ui.fragments.stream;

import com.viconajz.audiostream.api.pojo.Stream;

public interface IStreamSelectListener {
    void playStream(Stream stream);
    void pauseStream(Stream stream);
}
