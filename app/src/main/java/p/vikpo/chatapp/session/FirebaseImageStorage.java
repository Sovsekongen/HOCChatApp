package p.vikpo.chatapp.session;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class FirebaseImageStorage
{
    public interface OnDownloadResult
    {
        void downloadResult(Bitmap result);
    }

    private FirebaseStorage storage;

    private static final String TAG = "ChatApp - FirebaseImageStorage";

    public FirebaseImageStorage()
    {
        storage = FirebaseStorage.getInstance();
    }

    public void uploadImage(String title, Bitmap image)
    {
        StorageReference storageRef = storage.getReference("images/" + title);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(exception -> Log.e(TAG, "Encountered Error", exception))
                .addOnSuccessListener(taskSnapshot -> Log.e(TAG, "Uploaded Image " + title));
    }

    public void getImage(String title, OnDownloadResult onDownloadResult)
    {
        StorageReference storageRef = storage.getReference("images/" + title);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageRef.getBytes(ONE_MEGABYTE)
                .addOnSuccessListener(bytes ->
                        {
                            Log.e(TAG, "Successfully downloaded chat image");
                            onDownloadResult.downloadResult(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                        })
                .addOnFailureListener(exception ->
                        Log.e(TAG, "Encountered Error While Downloading Image", exception));
    }
}
