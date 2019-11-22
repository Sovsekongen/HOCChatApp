package p.vikpo.chatapp.views.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import p.vikpo.chatapp.R;
import p.vikpo.chatapp.presenters.CameraPresenter;

public class CameraActivity extends AppCompatActivity
{
    private static final String TAG = "ChatApp - CameraActivity";

    private CameraPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        createDialog().show();
        presenter = new CameraPresenter(this);
    }

    public Dialog createDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog_Alert);
        builder.setTitle(R.string.dialog_title).setItems(R.array.dialog_options_array, (dialog, index) ->
        {
            switch(index)
            {
                case 0:
                    presenter.takePicture();
                    break;
                case 1:
                    presenter.choosePicture();
                    break;
            }
        });
        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            presenter.onActivityResult(requestCode, data);
        }
        else
        {
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy();
        super.onDestroy();
    }
}
