package com.mobila.project.today.control;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.mobila.project.today.control.utils.FileUtils;
import com.mobila.project.today.model.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShareContentManager {

    private static final String TAG = ShareContentManager.class.getSimpleName();
    private final Context context;

    public ShareContentManager(Context context) {
        this.context = context;
    }

    public void sendSpannable(Spannable spannable, String fileName) {
        this.testFileCreation();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        File shareFile = this.createFileFromSpannable(spannable, fileName);
        Uri uriShareFile = FileProvider.getUriForFile(this.context,
                this.context.getPackageName() + ".fileprovider", shareFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uriShareFile);
        shareIntent.setType("today/*");
        context.startActivity(Intent.createChooser(shareIntent, "Sending: " + fileName));
    }

    private File createFileFromSpannable(Spannable spannable, String fileName) {
        String spanAsHtml = Html.toHtml(spannable, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);

        File filePath = new File(this.context.getFilesDir(), "shared");
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        File shareFile = null;
        try {
            shareFile = new File(filePath, fileName + ".today");
            FileWriter writer = new FileWriter(shareFile);
            writer.write(spanAsHtml);
            writer.flush();
            writer.close();
            Toast.makeText(this.context, "Sending your note", Toast.LENGTH_LONG).show();
            Log.d(TAG, "created new file: " + shareFile.getAbsolutePath() + ", exists: " + shareFile.exists());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return shareFile;
    }

    private void testFileCreation() {
        File filePath = new File(this.context.getFilesDir(), "shared");
        if (!filePath.exists()) {
            filePath.mkdir();
        }
        try {
            File file = new File(filePath, "sample");
            FileWriter writer = new FileWriter(file);
            writer.append("ciao");
            writer.flush();
            writer.close();
            Toast.makeText(this.context, "Send your note", Toast.LENGTH_LONG).show();
            Log.d(TAG, "created new file: " + file.getAbsolutePath() + ", exists: " + file.exists());
        } catch (Exception e) {
        }
    }

    private static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if(firstLine){
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        return sb.toString();
    }

    public Note getNoteFromIntent(Intent intent) {
        Note receivedNote = new Note();

        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            Uri uri = intent.getData();

            InputStream fileInputStream;
            try {
                fileInputStream = this.context.getContentResolver().openInputStream(uri);
                String fileString = ShareContentManager.convertStreamToString(fileInputStream);
                Spannable spannable = new SpannableString(Html.fromHtml(fileString, Html.FROM_HTML_MODE_LEGACY));
                receivedNote = new Note();
                receivedNote.setTitle(FileUtils.getFileNameWOExtension(this.context, uri));
                receivedNote.setContent(spannable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return receivedNote;
    }
}