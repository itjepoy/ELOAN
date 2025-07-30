package com.cremcash.eloan.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class ImageResultHandler {
    public static Uri handleImageResult(Activity activity, Intent data, ImageView imageView, TextView pathTextView, String tag) {
        try {
            Uri uri = Objects.requireNonNull(data).getParcelableExtra("path");
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), uri);

            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }

            String imagePath = uri.getPath();
            if (pathTextView != null) {
                pathTextView.setText(imagePath);
            }

            Log.d(tag, "onActivityResult: " + imagePath);

            String filename = System.currentTimeMillis() + ".jpg";
            File imageFile = new File(activity.getExternalFilesDir(null), filename);

            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }

            Uri resultUri = Uri.parse(imageFile.getAbsolutePath());
            Log.d(tag, "Saved image: " + resultUri.toString());
            return resultUri;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
