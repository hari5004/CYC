package com.example.harikrishnan.cyc10;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.MultipartBuilder;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.Request;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;
import com.squareup.okhttp.*;

import id.zelory.compressor.Compressor;
import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

//import static com.example.harikrishnan.cyc10.R.id.imgPreview;
import static com.example.harikrishnan.cyc10.R.id.progressBar;

public class UploadActivity extends AppCompatActivity {
    String filePath;
    Context context=this;
    //private ImageView imgPreview;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Intent i = getIntent();
        filePath = i.getStringExtra("filePath");
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
      //      previewMedia(isImage);

        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }


    }

/*    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);

        }
    }*/
    public void upload(View view)
    {  // Snackbar.make(view, "Please check your Internet Connection", Snackbar.LENGTH_LONG).show();
        Toast toast=Toast.makeText(this,filePath,Toast.LENGTH_LONG);
        toast.show();
        UploadImageToServer serverimage=new UploadImageToServer(context,filePath);
        serverimage.execute(new String[]{filePath});
    }
}
     class UploadImageToServer extends AsyncTask<String, String, String> {
         String json;
         private String credentials;
         ProgressDialog pDialog;
         private Context mcontext;
         String file="sample";
         public UploadImageToServer(Context context,String filepath) {
             mcontext = context;
             file=filepath;
         }

         @Override
         protected void onPreExecute() {
             // setting progress bar to zero
             //progressBar.setProgress(0);
           //  super.onPreExecute();
             pDialog = ProgressDialog.show(mcontext, "", "Please Wait", false);
         }

         @Override
         protected String doInBackground(String... params) {
             //return uploadImage();

             try {
                 File sourceFile = new File(file);

            //     Toast t=Toast.makeText(mcontext,sourceFile.getName(),Toast.LENGTH_LONG);
              //   t.show();
                 File csourceFile = Compressor.getDefault(mcontext).compressToFile(sourceFile);

                 Log.d("FILENAME",csourceFile.getName() );
                 OkHttpClient client = new OkHttpClient();
                 this.credentials = com.squareup.okhttp.Credentials.basic("admin", "admin");
                 final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

                 RequestBody req = new MultipartBody.Builder()
                         .setType(MultipartBody.FORM)
                         .addFormDataPart("userID", "5")
                        .addFormDataPart("image",csourceFile.getName(), RequestBody.create(MEDIA_TYPE_PNG, csourceFile))
                         .addFormDataPart("landMark","xyx")
                         .addFormDataPart("location","234124351245sdfgsdfb")
                         .build();

                 Request request = new Request.Builder()
                         .url("https://cyc-new.herokuapp.com/uploads/img_upload/")
                        // .url("http://images.google.com/searchbyimage/upload")
                         .post(req)
                         .header("Authorization",credentials)
                         .header("Content-Type", "application/x-www-form-urlencoded; charset=utf-8")
                         .build();

                 Response response = client.newCall(request).execute();
                json=response.body().string();

             } catch (Exception e) {
                 //Log.e(TAG, "Other Error: " + e.getLocalizedMessage());
             }
             return json;
         }
         @Override
         protected void onPostExecute(String result) {
          //   String loginid = "";
             pDialog.dismiss();
             Log.d("response", "uploadImage:" + json);

             Toast toast=Toast.makeText(mcontext,json,Toast.LENGTH_LONG);
             toast.show();
         }
         }


        //public static JSONObject uploadImage() {



