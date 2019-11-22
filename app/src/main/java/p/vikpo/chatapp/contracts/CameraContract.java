package p.vikpo.chatapp.contracts;

import android.graphics.Bitmap;

public interface CameraContract
{
    interface Router extends Contract.Router
    {
        void dispatchTakePictureIntent();
        void dispatchChoosePictureIntent();
        void setResultBitmap(Bitmap bitmap);
        void setResultCancel();
    }

    interface Presenter extends Contract.Presenter
    {
        void onDestroy();
    }
}
