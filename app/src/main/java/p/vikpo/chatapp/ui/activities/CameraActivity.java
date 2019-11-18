package p.vikpo.chatapp.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import p.vikpo.chatapp.R;

public class CameraActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        createDialog().show();
    }

    private static final int REQUEST_IMAGE_CAPTURE = 1808;
    private static final int REQUEST_IMAGE_CHOOSE = 1809;
    private static final int REQUEST_RETURN_IMAGE = 2010;
    private static final String TAG = "ChatApp - CameraActivity";

    public Dialog createDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle(R.string.dialog_title).setItems(R.array.dialog_options_array, (dialog, index) ->
        {
            switch(index)
            {
                case 0:
                    dispatchTakePictureIntent();
                    break;
                case 1:
                    dispatchChoosePictureIntent();
                    break;
            }
        });
        return builder.create();
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchChoosePictureIntent()
    {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(pickPhoto, "Select Picture"), REQUEST_IMAGE_CHOOSE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK)
        {
            Log.e(TAG, "Result is ok.");
            if(requestCode == REQUEST_IMAGE_CHOOSE)
            {
                Log.e(TAG, "IMAGE CHOOSE");
                try
                {
                    Uri selectedImage = data.getData();
                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    photo = scaleDown(photo, 380, true);

                    setResultBitmap(photo);
                }
                catch(IOException ioe)
                {
                    Log.e(TAG, "File not found.", ioe);
                }
            }

            if(requestCode == REQUEST_IMAGE_CAPTURE)
            {
                Log.e(TAG, "IMAGE CAPTURE");
                Bitmap photo = (Bitmap) data.getExtras().get("data");

                setResultBitmap(photo);
            }
        }
        else
        {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    private Bitmap scaleDown(Bitmap realImage, float maxImageSize, boolean filter)
    {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width, height, filter);
        return newBitmap;
    }

    /**
     * Returns the given bitmap as a result to the calling fragment.
     * @param bitmap
     */
    private void setResultBitmap(Bitmap bitmap)
    {
        Log.e(TAG, "Setting result of activity with a size of: " + bitmap.getByteCount());
        Intent resultIntent = new Intent();
        resultIntent.putExtra("messageImage", bitmap);

        setResult(REQUEST_RETURN_IMAGE, resultIntent);
        finish();
    }
}
