package com.mirka.app.naimi.utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.boxes.Container;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
import com.mirka.app.naimi.MainActivity;
import com.mirka.app.naimi.R;
import com.mirka.app.naimi.RecordUtil;
import com.mirka.app.naimi.TestActivity;
import com.mirka.app.naimi.VideoTestActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.mirka.app.naimi.data.AppData.base_filename;
import static com.mirka.app.naimi.data.AppData.base_filename_subs;
import static com.mirka.app.naimi.data.AppData.base_path;

/**
 * Created by Miras on 9/17/2017.
 */

public class VideoEditingUtils {

    public static final String TAG = "VideoEditingUtils";



    public static String convertToSrtFormat(List<String> text, List<Long> timings) {
        String result = "";
        for (int i = 0; i < text.size(); i++) {
            long mili = timings.get(i) % 1000;
            long sec = timings.get(i) / 1000;
            long min = sec / 60;
            sec %= 60;

            long sec2 = timings.get(i+1) / 1000;
            long min2 = sec2 / 60;
            sec2 = (sec2 % 60) - 1;
            String time1 = "00:"+((min<10)?"0":"")+min+":"+((sec<10)?"0":"")+sec+","+((mili<10)?"00":((mili<100)?"0":""))+mili;
            String time2 = "00:"+((min2<10)?"0":"")+min2+":"+((sec2<10)?"0":"")+sec2+",000";
            result += (i+1)+"\n"+time1+" --> "+time2+"\n" + text.get(i) + "\n\n";
        }
        return result;
    }

    public static Boolean buildSRTFile(List<String> text, List<Long> timings){
        String subtitles = convertToSrtFormat(text, timings);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(new File(base_path + base_filename_subs+".srt"), false);
            outputStream.write(subtitles.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static void embedSubtitlesToVideo(final Context  context, String videoFullPath, String subs) {
        final FFmpeg ffmpeg = FFmpeg.getInstance(context);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onFailure() {}

                @Override
                public void onSuccess() {}

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegNotSupportedException e) {
            // Handle if FFmpeg is not supported by device
        }
        try {

            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(("-i "+videoFullPath+" -i "+base_path+subs+".srt -c copy -c:s mov_text "+base_path+base_filename+"final.mp4").split(" "), new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {}

                @Override
                public void onProgress(String message) {}

                @Override
                public void onFailure(String message) {
                    Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, message);

                }

                @Override
                public void onSuccess(String message) {
                    Log.i(TAG, message);
//                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFinish() {}
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }


    /**
     * @param
     * @return 	return combine video path string
     *
     * appendVideo( new String[]{"filename0.mp4", "filename2.mp4"}  );
     * */
    public static String appendVideo(String[] videos, Context context) throws IOException {
        Log.v(TAG, "in appendVideo() videos length is " + videos.length);
        Movie[] inMovies = new Movie[videos.length];
        int index = 0;
        for(String video: videos)
        {
            Log.i(TAG, "    in appendVideo one video path = " + video);
            inMovies[index] = MovieCreator.build(base_path+video);
            index++;
        }
        List<Track> videoTracks = new LinkedList<Track>();
        List<Track> audioTracks = new LinkedList<Track>();
        for (Movie m : inMovies) {
            for (Track t : m.getTracks()) {
                if (t.getHandler().equals("soun")) {
                    audioTracks.add(t);
                }
                if (t.getHandler().equals("vide")) {
                    videoTracks.add(t);
                }
            }
        }

        Movie result = new Movie();
        Log.v(TAG, "audioTracks size = " + audioTracks.size()
                + " videoTracks size = " + videoTracks.size());
        if (audioTracks.size() > 0) {
            result.addTrack(new AppendTrack(audioTracks.toArray(new Track[audioTracks.size()])));
        }
        if (videoTracks.size() > 0) {
            result.addTrack(new AppendTrack(videoTracks.toArray(new Track[videoTracks.size()])));
        }
        String videoCombinePath = RecordUtil.createFinalPath(context, MainActivity.getName());
        Container out = new DefaultMp4Builder().build(result);
        FileChannel fc = new RandomAccessFile(videoCombinePath, "rw").getChannel();
        out.writeContainer(fc);
        fc.close();
        Log.v(TAG, "after combine videoCombinepath = " + videoCombinePath);
        return videoCombinePath;
    }

    public static final List<Long> getSubtitlesTiming (Context context, String[] names){
        List<Long> result = new ArrayList<>();
        result.add(0, 0L);
        for (int i = 0; i<names.length; i++){

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(context, Uri.fromFile(new File(base_path + names[i])));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time );
            result.add(i+1, result.get(i) + timeInMillisec);

            retriever.release();
        }

        return result;
    }

    public static final void cleanTheFiles() {
        File dir = new File(base_path);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
