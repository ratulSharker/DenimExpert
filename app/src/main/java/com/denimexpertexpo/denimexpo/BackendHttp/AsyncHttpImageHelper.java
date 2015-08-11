package com.denimexpertexpo.denimexpo.BackendHttp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;



/**
 * Created by ratul on 8/7/2015.
 */
public class AsyncHttpImageHelper extends AsyncTask <String, String, String>{

    public static interface onAsyncHttpImageDownloadComplete
    {
        public void downloadedImagePath(String path);
    }

    private static final String IMAGE_FILE_NAME = "sitemap.jpg";
    private static final String IMAGE_TEMP_FILE = "sitemapTMP";
    public static final String SITEMAP_IMAGE_URL = "http://apps.bangladeshdenimexpo.com/img/eventmap/eventmap.jpg";

    private onAsyncHttpImageDownloadComplete handler;


    Context context;
    public AsyncHttpImageHelper(Context ctx, onAsyncHttpImageDownloadComplete handler)
    {
        this.handler = handler;
        this.context = ctx;
    }

    @Override
    protected String doInBackground(String... strings) {


        File outputDir =  context.getCacheDir();
        File previousImage = new File(outputDir.getAbsolutePath()+ "//" + IMAGE_FILE_NAME);

        try{
            //getting ready the streams
            URL url = new URL(strings[0]);
            InputStream is = url.openStream();


            File tmpFile = File.createTempFile(IMAGE_FILE_NAME, null, outputDir);
            OutputStream os = new FileOutputStream(tmpFile);

            //actual reading and saving it
            byte[] chunkData = new byte[2048];
            int length;


            while ((length = is.read(chunkData)) != -1) {
                os.write(chunkData, 0, length);
                publishProgress();
            }
            is.close();
            os.close();

            //check if previous image exists or not
            if(previousImage.exists())
            {
                previousImage.delete();
            }
            tmpFile.renameTo(previousImage);

            Log.e("tmp image ", tmpFile.getAbsolutePath());
            Log.e("previous image ", previousImage.getAbsolutePath());

            return previousImage.getAbsolutePath();
        }
        catch (MalformedURLException ex)
        {
            Log.e("AsyncHttpImageHelper", ex.toString());
            return previousImage.getAbsolutePath();
        }
        catch (IOException ex)
        {
            Log.e("AsyncHttpImageHelper", ex.toString());
            return previousImage.getAbsolutePath();
        }
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(this.handler != null)
        {
            this.handler.downloadedImagePath(s);
        }
    }
}
