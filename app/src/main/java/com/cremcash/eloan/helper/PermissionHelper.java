package com.cremcash.eloan.helper;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cremcash.eloan.ImagePickerActivity;
import com.cremcash.eloan.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class PermissionHelper {
    public static void showImagePermissionDialog(
            Activity activity,
            TextView textIDView,
            TextView textIDnoView,
            Runnable onPermissionGranted,
            Runnable onPermissionDenied
    ) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialogView);

        final EditText input1 = dialogView.findViewById(R.id.etUserInput);
        final EditText input2 = dialogView.findViewById(R.id.etUserInput1);

        builder.setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    textIDView.setText(input1.getText().toString());
                    textIDnoView.setText(input2.getText().toString());

                    Dexter.withContext(activity)
                            .withPermissions(Manifest.permission.CAMERA)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    if (report.areAllPermissionsGranted() && onPermissionGranted != null) {
                                        onPermissionGranted.run();
                                    }

                                    if (report.isAnyPermissionPermanentlyDenied() && onPermissionDenied != null) {
                                        onPermissionDenied.run();
                                    }
                                }

                                @Override
                                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                                    token.continuePermissionRequest();
                                }
                            }).check();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static void requestCameraPermission(
            Activity activity,
            Runnable onPermissionGranted,
            Runnable onPermissionDenied
    ) {
        Dexter.withContext(activity)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted() && onPermissionGranted != null) {
                            onPermissionGranted.run();
                        }

                        if (report.isAnyPermissionPermanentlyDenied() && onPermissionDenied != null) {
                            onPermissionDenied.run();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    public static void showSettingsDialog(Activity activity, Runnable openSettingsAction) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getString(R.string.dialog_permission_title));
        builder.setMessage(activity.getString(R.string.dialog_permission_message));
        builder.setPositiveButton(activity.getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            if (openSettingsAction != null) {
                openSettingsAction.run();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    public static void openAppSettings(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        intent.setData(uri);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void showImagePickerOptions(
            Activity activity,
            Runnable onCameraSelected,
            Runnable onGallerySelected
    ) {
        ImagePickerActivity.showImagePickerOptions(activity, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                if (onCameraSelected != null) onCameraSelected.run();
            }

            @Override
            public void onChooseGallerySelected() {
                if (onGallerySelected != null) onGallerySelected.run();
            }
        });
    }

    public static void launchCameraIntent(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void launchGalleryIntent(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        activity.startActivityForResult(intent, requestCode);
    }




}
