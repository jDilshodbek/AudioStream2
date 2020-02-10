package com.viconajz.audiostream.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;

import com.viconajz.audiostream.R;

public class Progress {
    public static Progress s_m_oCShowProgress;
    public static Context m_Context;
    public Dialog m_Dialog;

    private Progress(Context m_Context) {
        this.m_Context = m_Context;
    }

    public static Progress getInstance() {
        if (s_m_oCShowProgress == null) {
            s_m_oCShowProgress = new Progress(m_Context);
        }
        return s_m_oCShowProgress;
    }

    public void showProgress(Context m_Context) {
        m_Dialog = new Dialog(m_Context);
        m_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_Dialog.setContentView(R.layout.progress_layout);
        m_Dialog.findViewById(R.id.progress_bar).setVisibility(View.VISIBLE);
        m_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        m_Dialog.setCancelable(false);
        m_Dialog.setCanceledOnTouchOutside(false);
        m_Dialog.show();
    }

    public void hideProgress() {
        if (m_Dialog != null) {
            m_Dialog.dismiss();
            m_Dialog = null;
        }
    }
}
