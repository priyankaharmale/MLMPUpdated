package hnwebproject.com.mlmp.Activity;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import hnwebproject.com.mlmp.R;


public class VideoViewActivity extends AppCompatActivity {
    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    String VideoURL;
    TextView tv_noti_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Find your VideoView in your video_main.xml layout

        videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            VideoURL = bundle.getString("VIDEOurl");
            Log.e("VideoURL", VideoURL);
        }

        // Create a progressbar
        pDialog = new ProgressDialog(VideoViewActivity.this);
        // Set progressbar title
        pDialog.setTitle("Video Streaming....");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(VideoViewActivity.this);
            mediacontroller.setAnchorView(videoview);
            // Get the URL from String VideoURL
            Uri video = Uri.parse(VideoURL);
            videoview.setMediaController(mediacontroller);
            videoview.setVideoURI(video);

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        videoview.requestFocus();
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                videoview.start();
            }
        });
    }
}
