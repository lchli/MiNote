package com.lch.menote.note.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lch.audio_player.AudioPlayer;
import com.lch.audio_player.ui.SimpleAudioView;
import com.lch.menote.R;
import com.lch.menote.note.model.NoteElement;
import com.lch.video_player.VideoPlayer;
import com.lch.video_player.helper.VideoHelper;
import com.lch.video_player.model.BAFTimedText;
import com.lch.video_player.ui.SimpleVideoView;
import com.lchli.utils.base.AbsAdapter;
import com.lchli.utils.tool.VF;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import tv.danmaku.ijk.media.player.IMediaPlayer;


/**
 * Created by lichenghang on 2018/5/20.
 */

public class NoteElementAdapter extends AbsAdapter<NoteElement> {
    private static final int ITEM_TEXT = 0;
    private static final int ITEM_IMG = 1;
    private static final int ITEM_AUDIO = 2;
    private static final int ITEM_VIDEO = 3;
    private final Activity activity;
    private final AudioPlayer mAudioPlayer;
    private final VideoPlayer mVideoPlayer;
    private Callback callback;
    private int currentPlayingAudioPosition = -1;
    private int currentPlayingVideoPosition = -1;

    private boolean isShowLoading = false;
    private boolean isVideoSizeChanged = false;
    private boolean isVideoRotationChanged = false;

    private int videoRotation = -1;
    private HashMap<Integer, SurfaceTexture> textureSparseArray = new HashMap<>();

    public interface Callback {
        void showOperation(int position, boolean isPlayingVideo, boolean isPlayingAudio);
    }

    public NoteElementAdapter(Callback cb, final Activity activity, AudioPlayer audioPlayer, VideoPlayer videoPlayer) {
        this.callback = cb;
        this.activity = activity;
        this.mAudioPlayer = audioPlayer;
        this.mVideoPlayer = videoPlayer;

        audioPlayer.setStateListener(new AudioPlayer.StateListener() {

            @Override
            public void onError(int what, int extra) {
                ToastUtils.showLong("播放失败！");
                notifyDataSetChanged();
            }

            @Override
            public void onCompletion() {
                super.onCompletion();
                notifyDataSetChanged();
            }

            @Override
            public void onProgress(long currentPositionMillsecs, long totalMillsecs) {
                super.onProgress(currentPositionMillsecs, totalMillsecs);

                notifyDataSetChanged();
            }

            @Override
            public void onPrepared() {
                mAudioPlayer.start();

                notifyDataSetChanged();
            }

        });

        videoPlayer.setStateListener(new VideoPlayer.StateListener() {
            @Override
            public void onError(int what, int extra) {
                isShowLoading = false;

                notifyDataSetChanged();
            }

            @Override
            public void onCompletion() {
                VideoHelper.releaseAudioFocus(activity);

                notifyDataSetChanged();
            }

            @Override
            public void onBufferingUpdate(int percent) {
                notifyDataSetChanged();
            }

            @Override
            public void onProgress(long currentPositionMillsecs, long totalMillsecs) {
                notifyDataSetChanged();
            }

            @Override
            public void onVideoSizeChanged(int width, int height) {

                isVideoSizeChanged = true;

                notifyDataSetChanged();
            }

            @Override
            public void onTimedText(BAFTimedText text) {

            }

            @Override
            public void onPrepared() {
                isShowLoading = false;
                isVideoSizeChanged = true;

                VideoHelper.requestAudioFocus(activity);

                mVideoPlayer.start();

                notifyDataSetChanged();

            }

            @Override
            public void onInfo(int what, int extra) {

                switch (what) {
                    case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                        videoRotation = extra;
                        isVideoRotationChanged = true;

                        notifyDataSetChanged();

                        break;
                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
                        isShowLoading = true;

                        notifyDataSetChanged();
                        break;

                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
                        isShowLoading = false;

                        notifyDataSetChanged();
                        break;
                }


            }

            @Override
            public void onSeekComplete() {

                notifyDataSetChanged();
            }
        });


    }

    @NotNull
    @Override
    public AbsViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {


        switch (viewType) {
            case ITEM_TEXT:

                return new HText(viewType, parent.getContext());
            case ITEM_IMG:

                return new HImg(viewType, parent.getContext());
            case ITEM_AUDIO:
                return new HAudio(viewType, parent.getContext());
            case ITEM_VIDEO:
                return new HVideo(viewType, parent.getContext());
            default:
                return new HText(viewType, parent.getContext());

        }

    }

    @Override
    public void onBindViewHolder(@NotNull AbsViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        final NoteElement data = getItem(position);
        if (data == null) {
            return;
        }

        switch (viewType) {
            case ITEM_TEXT: {
                final HText h = (HText) holder;
                h.note_edittext.requestFocus();
                h.note_edittext.setText(data.text);
                h.note_edittext.setSelection(data.text != null ? data.text.length() : 0);

                h.operationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.showOperation(position, false, false);

                    }
                });

                h.note_edittext.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        data.text = h.note_edittext.getText().toString();


                    }
                });
            }
            break;
            case ITEM_IMG: {
                HImg h = (HImg) holder;
                if (data.path != null) {
                    RequestOptions opt = RequestOptions
                            .overrideOf(ScreenUtils.getScreenWidth(), ScreenUtils.getScreenHeight());

                    Glide.with(activity)
                            .load(data.path)
                            .apply(opt)
                            .into(h.note_imageview);

                    h.note_imageview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> imgPaths = new ArrayList<>();
                            imgPaths.add(data.path);

                            ImageGalleryActivity.Companion.startSelf(activity, imgPaths, 0);
                        }
                    });
                }

                h.operationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.showOperation(position, false, false);
                    }
                });
            }
            break;
            case ITEM_AUDIO: {
                final HAudio h = (HAudio) holder;

                if (currentPlayingAudioPosition == position) {

                    if (mAudioPlayer.isPlaying()) {
                        h.simpleAudioView.ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        h.simpleAudioView.ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    }
                    float f = ((float) mAudioPlayer.getCurrentPosition()) / mAudioPlayer.getDuration();
                    h.simpleAudioView.seekBar.setProgress((int) (h.simpleAudioView.seekBar.getMax() * f));

                    h.simpleAudioView.endText.setText(VideoHelper.formatVideoTime((int) mAudioPlayer.getDuration()));
                    h.simpleAudioView.startText.setText(VideoHelper.formatVideoTime((int) mAudioPlayer.getCurrentPosition()));

                } else {
                    h.simpleAudioView.ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    h.simpleAudioView.seekBar.setProgress(0);
                    h.simpleAudioView.endText.setText("00:00");
                    h.simpleAudioView.startText.setText("00:00");
                }

                h.simpleAudioView.ivPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (currentPlayingAudioPosition == position) {
                            if (mAudioPlayer.isPlaying()) {
                                mAudioPlayer.pause();
                                h.simpleAudioView.ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                            } else if (mAudioPlayer.isPrepared()) {
                                mAudioPlayer.start();
                                h.simpleAudioView.ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                            } else {
                                mAudioPlayer.prepareAsync();
                            }

                        } else {
                            mAudioPlayer.reset();
                            mAudioPlayer.setDataSource(activity, Uri.parse(data.path));

                            currentPlayingAudioPosition = position;

                            mAudioPlayer.prepareAsync();
                        }

                    }
                });

                h.simpleAudioView.seekBar.setEnabled(currentPlayingAudioPosition == position);

                h.simpleAudioView.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (seekBar.isEnabled()) {
                            float f = ((float) seekBar.getProgress()) / seekBar.getMax();
                            mAudioPlayer.seekTo((int) (f * mAudioPlayer.getDuration()));
                        }

                    }
                });


                h.operationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.showOperation(position, false, position == currentPlayingAudioPosition);
                    }
                });
            }
            break;

            case ITEM_VIDEO: {
                final HVideo h = (HVideo) holder;

                h.operationButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.showOperation(position, position == currentPlayingVideoPosition, false);
                    }
                });

                h.videoView.getPlayerController().seekBar.setEnabled(currentPlayingVideoPosition == position);

                h.videoView.getPlayerController().seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        if (seekBar.isEnabled()) {
                            float f = ((float) seekBar.getProgress()) / seekBar.getMax();
                            mVideoPlayer.seekTo((int) (f * mVideoPlayer.getDuration()));
                        }

                    }
                });

                h.videoView.getRenderView().setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                    @Override
                    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                        textureSparseArray.put(position, surface);

                        if (position == currentPlayingVideoPosition) {
                            mVideoPlayer.setSurface(new Surface(surface));
                            isVideoSizeChanged = true;
                            isVideoRotationChanged = true;

                            notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

                    }

                    @Override
                    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                        textureSparseArray.remove(position);
                        if (position == currentPlayingVideoPosition) {
                            mVideoPlayer.setSurface(null);
                        }

                        return false;
                    }

                    @Override
                    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

                    }
                });


                if (position == currentPlayingVideoPosition) {

                    if (mVideoPlayer.isPlaying()) {
                        h.videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                    } else {
                        h.videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                    }

                    float f = ((float) mVideoPlayer.getCurrentPosition()) / mVideoPlayer.getDuration();
                    int p = (int) (h.videoView.getPlayerController().getMaxProgress() * f);
                    h.videoView.getPlayerController().setProgress(p);
                    h.videoView.getPlayerController().setCurrentTime(VideoHelper.formatVideoTime((int) mVideoPlayer.getCurrentPosition()));

                    if (isShowLoading) {
                        isShowLoading = false;
                        h.videoView.showLoading();
                    } else {
                        h.videoView.hideLoading();
                    }

                    float per = ((float) mVideoPlayer.getBufferPercent()) / 100;
                    h.videoView.getPlayerController().setSecondProgress((int) (h.videoView.getPlayerController().getMaxProgress() * per));

                    if (isVideoSizeChanged) {
                        isVideoSizeChanged = false;
                        if (VideoHelper.isAcceptVideoSize(mVideoPlayer.getVideoWidth(), mVideoPlayer.getVideoHeight())) {
                            h.videoView.setVideoSize(mVideoPlayer.getVideoWidth(), mVideoPlayer.getVideoHeight());
                        }
                    }


                    if (isVideoRotationChanged) {
                        isVideoRotationChanged = false;
                        h.videoView.setVideoRotation(videoRotation);
                    }

                    h.videoView.getPlayerController().setTotalTime(VideoHelper.formatVideoTime((int) mVideoPlayer.getDuration()));


                } else {
                    h.videoView.getPlayerController().resetUI();
                    h.videoView.getPlayerController().setVisibility(View.VISIBLE);
                }


                h.videoView.getPlayerController().ivPlayPause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == currentPlayingVideoPosition) {
                            if (mVideoPlayer.isPlaying()) {
                                mVideoPlayer.pause();
                                h.videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_play);
                            } else if (mVideoPlayer.isPrepared()) {
                                mVideoPlayer.start();
                                h.videoView.getPlayerController().ivPlayPause.setImageResource(android.R.drawable.ic_media_pause);
                            } else {
                                mVideoPlayer.prepareAsync();
                            }
                        } else {
                            SurfaceTexture texture = textureSparseArray.get(position);
                            if (texture == null) {
                                ToastUtils.showShort("texture未准备好！");
                                return;
                            }
                            if (data.path == null) {
                                ToastUtils.showShort("播放地址为空！");
                                return;
                            }

                            boolean useCache = false;
                            if (data.path.startsWith("http://") || data.path.startsWith("https://")) {
                                useCache = true;
                            }

                            mVideoPlayer.reset();
                            mVideoPlayer.setDataSource(activity, Uri.parse(data.path), useCache);

                            currentPlayingVideoPosition = position;


                            mVideoPlayer.setSurface(new Surface(texture));

                            mVideoPlayer.prepareAsync();

                        }

                    }
                });

                h.videoView.getPlayerController().ivFullscreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(data.path)) {
                            mVideoPlayer.pause();

                            FullVideoPlayActivity.launch(v.getContext(), data.path);
                        }
                    }
                });
            }


        }


    }

    @Override
    public int getItemViewType(int position) {
        final int def = ITEM_TEXT;

        NoteElement data = getItem(position);
        if (data == null || data.type == null) {
            return def;
        }

        switch (data.type) {
            case NoteElement.TYPE_TEXT:
                return ITEM_TEXT;
            case NoteElement.TYPE_IMG:
                return ITEM_IMG;
            case NoteElement.TYPE_AUDIO:
                return ITEM_AUDIO;
            case NoteElement.TYPE_VIDEO:
                return ITEM_VIDEO;
            default:
                return def;
        }

    }

    private class HText extends AbsViewHolder {
        private EditText note_edittext;
        private Button operationButton;
        private View itemView;

        public HText(int viewType, Context context) {
            super(viewType);
            itemView = View.inflate(context, R.layout.list_item_note_element_text, null);
            note_edittext = VF.f(itemView, R.id.note_edittext);
            operationButton = VF.f(itemView, R.id.note_radioButton);

        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }

    private class HImg extends AbsViewHolder {
        private ImageView note_imageview;
        private Button operationButton;
        private View itemView;

        public HImg(int viewType, Context context) {
            super(viewType);
            itemView = View.inflate(context, R.layout.list_item_note_element_img, null);
            note_imageview = VF.f(itemView, R.id.note_imageview);
            operationButton = VF.f(itemView, R.id.note_radioButton);

        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }

    private class HAudio extends AbsViewHolder {

        private SimpleAudioView simpleAudioView;
        private Button operationButton;
        private View itemView;

        public HAudio(int viewType, Context context) {
            super(viewType);
            itemView = View.inflate(context, R.layout.list_item_note_element_audio, null);

            simpleAudioView = VF.f(itemView, R.id.simpleAudioView);
            operationButton = VF.f(itemView, R.id.note_radioButton);
            simpleAudioView.prev.setVisibility(View.INVISIBLE);
            simpleAudioView.next.setVisibility(View.INVISIBLE);
        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }

    private class HVideo extends AbsViewHolder {
        private Button operationButton;
        private SimpleVideoView videoView;
        private View itemView;

        public HVideo(int viewType, Context context) {
            super(viewType);
            itemView = View.inflate(context, R.layout.list_item_note_element_video, null);

            videoView = VF.f(itemView, R.id.videoView);
            videoView.getPlayerController().ivNext.setVisibility(View.GONE);
            videoView.getPlayerController().ivPre.setVisibility(View.GONE);
            videoView.getPlayerController().ivBackward.setVisibility(View.GONE);
            videoView.getPlayerController().ivForward.setVisibility(View.GONE);
            operationButton = VF.f(itemView, R.id.note_radioButton);

        }

        @Override
        protected View getItemView() {
            return itemView;
        }
    }
}
