package no.ntnu.diverslogbook.tasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

import no.ntnu.diverslogbook.utils.ImageCache;

public class DownloadProfileImageTask extends AsyncTask<String, Void, Bitmap> {

    /**
     * A reference to the activity where the ImageView is present.
     * Uses weakReference to avoid memory leak by leaking the context
     */
    //private WeakReference<Activity> profileActivity;

    private WeakReference<ImageView> imageView;

    private String imageName = "";

    /**
     * Constructor
     * @param profileActivity The activity where the ImageView is located
     */
    public DownloadProfileImageTask(ImageView profileActivity, String imageName) {
        //this.profileActivity = new WeakReference<>(profileActivity);
        this.imageView = new WeakReference<>(profileActivity);
        this.imageName = imageName;
    }

    /**
     * {@inheritDoc}
     * @param strings
     * @return
     */
    @Override
    protected Bitmap doInBackground(String... strings) {

        try {
            URL url = new URL(strings[0]);
            return BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            return null;
        }

    }

    /**
     * {@inheritDoc}
     * @param bitmap
     */
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //ImageView image = this.profileActivity.get().findViewById(R.id.iv_profile_image);
        if(bitmap != null) {
            this.imageView.get().setImageBitmap(bitmap);
            //ImageCache.put(this.profileActivity.get(), this.imageName, bitmap);
            ImageCache.put(this.imageView.get().getContext(), this.imageName, bitmap);
        }
    }
}
