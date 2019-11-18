package p.vikpo.chatapp.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity
{
    private static final int REQUEST_IMAGE_CAPTURE = 1808;
    private static final int REQUEST_RETURN_IMAGE = 2010;
    private static final String TAG = "ChatApp - CameraActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Intent resultIntent = new Intent();
            resultIntent.putExtra("messageImage", photo);

            setResult(REQUEST_RETURN_IMAGE, resultIntent);
            finish();
        }
    }
}
