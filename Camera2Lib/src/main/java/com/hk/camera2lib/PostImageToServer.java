package com.hk.camera2lib;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class PostImageToServer extends AsyncTask<String, String, String> {
    final static String TAG = "PostImageToServer";
    final static String URL = "https://apis-az-dev.vishwamcorp.com/v2/image_upload";
    private Context mContext;

    public PostImageToServer(final Context context) {
        mContext =context;
    }

    @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... str) {

            String res = null;
            try {
                String user_id = str[0], app_id = str[1], ImagePath = str[2];
                File sourceFile = new File(ImagePath);
                Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());
                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
                String filename = ImagePath.substring(ImagePath.lastIndexOf("/") + 1);
                //Creating server Request
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", filename, RequestBody.create(sourceFile, MEDIA_TYPE_PNG))
                       .addFormDataPart("user_id", user_id)
                       .addFormDataPart("app_id", app_id)
                        .build();

                Request request = new Request.Builder()
                        .url(URL)
                        .post(requestBody)
                        .build();

                OkHttpClient client = new OkHttpClient();
                okhttp3.Response response = client.newCall(request).execute();
                res = response.body().string();
                Log.e(TAG, "Response : " + res);
                return res;
            } catch (UnknownHostException | UnsupportedEncodingException e) {
                Log.e(TAG, "Error: " + e.getLocalizedMessage());
            } catch (Exception e) {
                Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
            }
            return res;

        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            //Server Response
            Toast.makeText(mContext, "Response: "+response, Toast.LENGTH_SHORT).show();
        }
    }