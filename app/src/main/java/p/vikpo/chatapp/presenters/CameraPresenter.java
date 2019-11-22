package p.vikpo.chatapp.presenters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import p.vikpo.chatapp.contracts.CameraContract;
import p.vikpo.chatapp.routers.CameraRouter;

public class CameraPresenter implements CameraContract.Presenter
{
    private CameraRouter router;
    private Activity activity;
    private static final String TAG = "ChatApp - CameraPresenter";
    private static final int REQUEST_IMAGE_CAPTURE = 1808;
    private static final int REQUEST_IMAGE_CHOOSE = 1809;

    public CameraPresenter(AppCompatActivity activity)
    {
        router = new CameraRouter(activity);
        this.activity = activity;
    }

    private Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter)
    {
        float ratio = Math.min(maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        return Bitmap.createScaledBitmap(realImage, width, height, filter);
    }

    public void takePicture()
    {
        router.dispatchTakePictureIntent();
    }

    public void choosePicture()
    {
        router.dispatchChoosePictureIntent();
    }

    public void onActivityResult(int requestCode, Intent data)
    {
        if (requestCode == REQUEST_IMAGE_CHOOSE)
        {
            try
            {
                Uri selectedImage = data.getData();
                Bitmap photo = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
                photo = scaleDown(photo, 380, true);

                router.setResultBitmap(photo);
            }
            catch (IOException ioe)
            {
                Log.e(TAG, "File not found.", ioe);
                router.setResultCancel();
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            if(photo != null)
            {
                router.setResultBitmap(photo);
            }
        }
    }

    @Override
    public void onDestroy()
    {
        router.unregister();
        router = null;
        activity = null;
    }
}
