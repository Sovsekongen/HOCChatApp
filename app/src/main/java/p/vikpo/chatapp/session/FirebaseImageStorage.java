package p.vikpo.chatapp.session;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class FirebaseImageStorage
{
    public interface OnDownloadResult
    {
        void downloadResult(Bitmap result);
    }

    private FirebaseStorage storage;
    private StorageReference imageRef;
    private static final long ONE_MEGABYTE = 1024 * 1024;


    private static final String TAG = "ChatApp - FirebaseImageStorage";

    public FirebaseImageStorage()
    {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadImage(String title, Bitmap image)
    {
        imageRef = storage.getReference(title);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] data = baos.toByteArray();

        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> Log.e(TAG, "Encountered Error", exception))
                .addOnSuccessListener(taskSnapshot -> Log.e(TAG, "Uploaded Image " + title));
    }

    public void getImage(String title, OnDownloadResult onDownloadResult)
    {
        Log.e(TAG, "Downloading " + title);
        imageRef = storage.getReference(title);
        imageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes ->
                        {
                            Log.e(TAG, "Successfully downloaded chat image");
                            onDownloadResult.downloadResult(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        })
                .addOnFailureListener(exception ->
                        Log.e(TAG, "Encountered Error While Downloading Image", exception));
    }
}
