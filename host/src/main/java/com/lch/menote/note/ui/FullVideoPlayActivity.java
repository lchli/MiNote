package com.lch.menote.note.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ToastUtils;
import com.lch.menote.R;
import com.lch.menote.utils.VideoPlayUtil;
import com.lch.netkit.common.base.BaseCompatActivity;
import com.lch.video_player.LchVideoPlayer;
import com.lch.video_player.VideoPlayer;
import com.lch.video_player.helper.VideoHelper;
import com.lch.video_player.model.BAFTimedText;
import com.lch.video_player.ui.SimpleVideoView;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class FullVideoPlayActivity extends BaseCompatActivity {

    private SimpleVideoView videoView;
    private VideoPlayer videoPlayer;

    public static void launch(Context context, String url) {
        Intent it = new Intent(context, FullVideoPlayActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        it.putExtra("url", url);
        context.startActivity(it);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_full_video_play);

        videoView = f(R.id.videoView);
        videoView.getPlayerController().ivNext.setVisibility(View.GONE);
        videoView.getPlayerController().ivPre.setVisibility(View.GONE);
        videoView.getPlayerController().ivBackward.setVisibility(View.GONE);
        videoView.getPlayerController().ivForward.setVisibility(View.GONE);

        videoView.getPlayerController().ivPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPlayer.isPlaying()) {
                    videoPlayer.pause();
                    videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                } else if (videoPlayer.isPrepared()) {
                    videoPlayer.start();
                    videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    videoPlayer.prepareAsync();
                }
            }
        });
        videoView.getPlayerController().seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (videoPlayer.isPrepared()) {
                    float f = ((float) seekBar.getProgress()) / seekBar.getMax();
                    videoPlayer.seekTo((int) (f * videoPlayer.getDuration()));
                }
            }
        });
        videoView.getPlayerController().ivFullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        videoPlayer = LchVideoPlayer.newPlayer(this);

        String url = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(url)) {
            ToastUtils.showShort("视频地址为空！");
            finish();
            return;
        }

        videoPlayer.setStateListener(new VideoPlayer.StateListener() {
            @Override
            public void onError(int what, int extra) {
                super.onError(what, extra);
                videoView.hideLoading();
            }

            @Override
            public void onCompletion() {
                super.onCompletion();
                VideoHelper.releaseAudioFocus(FullVideoPlayActivity.this);
                videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
            }

            @Override
            public void onBufferingUpdate(int percent) {
                super.onBufferingUpdate(percent);
                float per = ((float) videoPlayer.getBufferPercent()) / 100;
                videoView.getPlayerController().setSecondProgress((int) (videoView.getPlayerController().getMaxProgress() * per));
            }

            @Override
            public void onProgress(long currentPositionMillsecs, long totalMillsecs) {
                super.onProgress(currentPositionMillsecs, totalMillsecs);
                float f = ((float) videoPlayer.getCurrentPosition()) / videoPlayer.getDuration();
                int p = (int) (videoView.getPlayerController().getMaxProgress() * f);

                videoView.getPlayerController().setProgress(p);
                videoView.getPlayerController().setCurrentTime(VideoHelper.formatVideoTime((int) videoPlayer.getCurrentPosition()));
            }

            @Override
            public void onVideoSizeChanged(int width, int height) {
                super.onVideoSizeChanged(width, height);
                if (VideoHelper.isAcceptVideoSize(videoPlayer.getVideoWidth(), videoPlayer.getVideoHeight())) {
                    videoView.setVideoSize(videoPlayer.getVideoWidth(), videoPlayer.getVideoHeight());
                }
            }

            @Override
            public void onTimedText(BAFTimedText text) {
                super.onTimedText(text);
            }

            @Override
            public void onPrepared() {
                super.onPrepared();
                VideoHelper.requestAudioFocus(FullVideoPlayActivity.this);
                videoPlayer.start();
                videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                videoView.getPlayerController().setTotalTime(VideoHelper.formatVideoTime((int) videoPlayer.getDuration()));
            }

            @Override
            public void onInfo(int what, int extra) {
                super.onInfo(what, extra);
                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        videoView.setVideoRotation(extra);
                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        videoView.showLoading();
                        break;

                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        videoView.hideLoading();
                        break;
                }
            }

            @Override
            public void onSeekComplete() {
                super.onSeekComplete();
            }
        });

        videoPlayer.setDataSource(this, Uri.parse(url), VideoPlayUtil.isShouldCache(url));

        videoView.getRenderView().setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                videoPlayer.setSurface(new Surface(surface));

                videoPlayer.prepareAsync();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                videoPlayer.setSurface(null);
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayer.release();
    }
}
