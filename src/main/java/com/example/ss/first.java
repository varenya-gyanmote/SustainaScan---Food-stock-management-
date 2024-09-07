package com.example.ss;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.media.MediaPlayer;


public class first extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        // Find the VideoView by its ID
        VideoView videoView = findViewById(R.id.videoView);

        // Get the path of the video file
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.videopage;

        // Parse the video path into a Uri
        Uri uri = Uri.parse(videoPath);

        // Set the Uri as the data source for the VideoView
        videoView.setVideoURI(uri);

        // Start playing the video
        videoView.start();

        // Set a completion listener to the VideoView
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                // Video playback is complete, start MainActivity
                Intent intent = new Intent(first.this, MainActivity.class);
                startActivity(intent);

                // Finish current activity to prevent going back to it when pressing back button
                finish();
            }
        });
    }
}
