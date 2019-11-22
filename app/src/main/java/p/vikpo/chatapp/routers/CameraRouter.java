package p.vikpo.chatapp.routers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.util.Log;

import p.vikpo.chatapp.contracts.CameraContract;

public class CameraRouter implements CameraContract.Router
{
    private static final String TAG = "ChatApp - CameraRouter";
    private static final int REQUEST_IMAGE_CAPTURE = 1808;
    private static final int REQUEST_IMAGE_CHOOSE = 1809;
    private static final int REQUEST_RETURN_IMAGE = 2010;

    private Activity activity;

    public CameraRouter(Activity activity)
    {
        this.activity = activity;
    }

    @Override
    public void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null)
        {
            activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void dispatchChoosePictureIntent()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(Intent.createChooser(pickPhoto, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    /**
     * Returns the given bitmap as a result to the calling fragment.
     * @param bitmap the bitmap to be returned to the calling fragment
     */
    @Override
    public void setResultBitmap(Bitmap bitmap)
    {
        Log.e(TAG, "Sending Back Bitmap with size: " + bitmap.getByteCount());

        Intent resultIntent = new Intent();
        resultIntent.putExtra("messageImage", bitmap);

        activity.setResult(REQUEST_RETURN_IMAGE, resultIntent);
        activity.finish();
    }

    @Override
    public void setResultCancel()
    {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    @Override
    public void unregister()
    {
        activity = null;
    }
}
