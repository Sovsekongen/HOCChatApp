package p.vikpo.chatapp.interactors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Image downloader class for downloading the avatar-images of each user. These should later be stored
 * smarter, perhaps as a ViewModel so each user shouldnt be downloaded each time.
 * Works as an AyncTask with the URL as a input and the Bitmap downloaded as the output.
 */
public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
{
    private static final String TAG = "ChatApp - ImageDownlaoder";

    /**
     * Interface for accessing the bitmap when the AsyncTask has finished.
     */
    public interface AsyncResponse
    {
        void ProcessFinish(Bitmap result);
    }

    private AsyncResponse delegate;


    ImageDownloader(AsyncResponse delegate)
    {
        this.delegate = delegate;
    }

    /**
     * Function for handling the download operation in the background.
     * @param URL the url to be downloaded
     * @return the Bitmap downloaded from the URL.
     */
    @Override
    protected Bitmap doInBackground(String... URL)
    {
        String imageURL = URL[0];

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

    /**
     * When the download operation is finished this function is passing the Bitmap to the interface
     * for further usage.
     * @param result the downloaded Bitmap.
     */
    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);

        Log.e(TAG, "Downloaded Bitmap With the size of: "+ result.getByteCount());
        delegate.ProcessFinish(result);
    }
}
