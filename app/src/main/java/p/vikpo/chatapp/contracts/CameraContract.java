package p.vikpo.chatapp.contracts;

import android.graphics.Bitmap;

public interface CameraContract
{
    interface Router
    {
        void dispatchTakePictureIntent();
        void dispatchChoosePictureIntent();
        void setResultBitmap(Bitmap bitmap);
        void setResultCancel();
        void unregister();
    }

    interface Presenter
    {
        void onDestroy();
    }
}
