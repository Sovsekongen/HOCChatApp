package p.vikpo.chatapp.session;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
{
    private static final String URL_PROFILE_PICTURE = "https://graph.facebook.com/";
    private static final String URL_PICTURE_TYPE = "/picture?type=small";

    public interface AsyncResponse
    {
        void ProcessFinish(Bitmap result);
    }

    private AsyncResponse delegate;

    public ImageDownloader(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... URL)
    {
        String imageURL = URL_PROFILE_PICTURE + URL[0] + URL_PICTURE_TYPE;

        Bitmap bitmap = null;
        try
        {
            // Download Image from URL
            InputStream input = new URL(imageURL).openStream();
            // Decode Bitmap
            bitmap = BitmapFactory.decodeStream(input);
        }
        catch (IOException e)
        {
            Log.e("ChatApp", "IOException in ImageDownloader", e);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);
        delegate.ProcessFinish(result);
    }
}
