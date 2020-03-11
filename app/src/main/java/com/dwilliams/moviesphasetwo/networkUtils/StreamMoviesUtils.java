package com.dwilliams.moviesphasetwo.networkUtils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.dwilliams.moviesphasetwo.constants.Constants;

import androidx.annotation.NonNull;

public class StreamMoviesUtils {

         // Create Action View Intent: Launches Youtube App
        public static void launchTrailerVideoInYoutubeApp(@NonNull Context context, String videoID) {

            Intent launchTrailerVideoInYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse(("vnd.youtube://" + videoID)));
            context.startActivity(launchTrailerVideoInYoutube);
        }


        //  Create Activity to Launch Browers and play trailer
        public static void launchTrailerVideoInYoutubeBrowser(@NonNull Context context, String videoID) {

            Intent launchTrailerVideoInYoutube = new Intent(Intent.ACTION_VIEW, Uri.parse((Constants.YOUTUBE_URL + videoID)));
            context.startActivity(launchTrailerVideoInYoutube);
        }
    }

