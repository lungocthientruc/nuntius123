public class VideoCallActivity extends AppCompatActivity  {
 
    private RtcEngine mRtcEngine;
    private IRtcEngineEventHandler mRtcEventHandler;
    private Button btnendcall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videocall_screen);
        mRtcEventHandler = new IRtcEngineEventHandler() {
           
 
            @Override
            public void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed) {
                Log.i("uid video",uid+"");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRemoteVideo(uid);
                    }
                });
            }
 
           
        };
        initializeAgoraEngine();
        btnendcall.setOnClickListener(new View.OnClickListener() { //ending call
            @Override
            public void onClick(View view) {
                leaveChannel();
                Intent intent = new Intent(VideoCallActivity.this, ChatScreen.class); //return to chat screen
                startActivity(intent);
            }
    }
 
    private void initializeAgoraEngine() { 
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
            joinChannel();
            setupLocalVideo();
            setupVideoProfile();
        } catch (Exception e) {
           e.printStackTrace();
         }
    }
 
    private void setupVideoProfile() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoProfile(Constants.VIDEO_PROFILE_360P, false);
    }
 
    private void setupLocalVideo() {
        FrameLayout container = (FrameLayout) findViewById(R.id.local_video_view_container);
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        surfaceView.setZOrderMediaOverlay(true);
        container.addView(surfaceView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, 0));
    }
 
    private void joinChannel() {
        mRtcEngine.joinChannel(null, "aye", "Extra Optional Data", new Random().nextInt(10000000)+1); // if you do not specify the uid, Agora will assign one.
    }
 
    private void setupRemoteVideo(int uid) {
        FrameLayout container = (FrameLayout) findViewById(R.id.remote_video_view_container);
 
        if (container.getChildCount() >= 1) {
            return;
        }
 
        SurfaceView surfaceView = RtcEngine.CreateRendererView(getBaseContext());
        container.addView(surfaceView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_ADAPTIVE, uid));
        surfaceView.setTag(uid);
 
    }
 
    private void leaveChannel() {
        mRtcEngine.leaveChannel();
    }
}
