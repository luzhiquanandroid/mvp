package com.example.admin.myapplication.Utils;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.text.TextUtils;

import com.example.admin.myapplication.ui.dialog.EasyProgressDialog;


public class LoadingDialogUtil {

    private static EasyProgressDialog progressDialog;

    public static EasyProgressDialog showProgressDialog(Context context, String message) {
        return showProgressDialog(context, message, false, null);
    }

    public static EasyProgressDialog showProgressDialog(Context context, String message, boolean cancelable) {
        return showProgressDialog(context, message, cancelable, null);
    }

    public static EasyProgressDialog showProgressDialog(Context context, String message, boolean canCancelable, OnCancelListener listener) {

        if (progressDialog == null) {
            progressDialog = new EasyProgressDialog(context, message);
        } else if (progressDialog.getContext() != context) {
            // maybe existing dialog is running in a destroyed activity cotext
            // we should recreate one
            dismissProgressDialog();
            progressDialog = new EasyProgressDialog(context, message);
        }

        progressDialog.setCancelable(canCancelable);

        if (listener != null) {
            progressDialog.setOnCancelListener(listener);
        }

        progressDialog.show();

        return progressDialog;
    }

    public static void dismissProgressDialog() {

        if (null == progressDialog) {
            return;
        }
        if (progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                // maybe we catch IllegalArgumentException here.
            }

        }

    }

    public static void setMessage(String message) {
        if (null != progressDialog && progressDialog.isShowing()
                && !TextUtils.isEmpty(message)) {
            progressDialog.setMessage(message);
        }
    }

    public static void updateLoadingMessage(String message) {
        if (null != progressDialog && progressDialog.isShowing()
                && !TextUtils.isEmpty(message)) {
            progressDialog.updateLoadingMessage(message);
        }
    }

    public static boolean isShowing() {
        return (progressDialog != null && progressDialog.isShowing());
    }

}
