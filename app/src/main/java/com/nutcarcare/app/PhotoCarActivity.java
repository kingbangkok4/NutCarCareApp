package com.nutcarcare.app;

/**
 * Created by Administrator on 4/17/2016.
 */
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class PhotoCarActivity extends Activity{

    private String photoCarArray[] = new String[5];
    private String[] namePhotoSplite;
    private ArrayList<HashMap<String, String>> MyArrList = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> tmpMyArrList = new ArrayList<HashMap<String, String>>();
    private Double sumTotal = 0.00;
    private String strService = "";

    ImageView imgView;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    Button btnUploadFront, btnUploadLeft, btnUploadRight, btnUploadBehide, btnUploadTop;

    static String strSDCardPathName = Environment.getExternalStorageDirectory() + "/temp_picture" + "/";
    static String strURLUpload = "http://www.nutcarcare.com/api/uploadFile.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_car);

        Bundle extras = getIntent().getExtras();
        // เช็คว่ามีค่าที่ส่งมาจากหน้าอื่นหรือไม่ถ้ามีจะไม่เท่ากับ null
        if (extras != null) {
            tmpMyArrList = (ArrayList<HashMap<String, String>>) extras
                    .getSerializable("MyArrList");
            if (tmpMyArrList != null) {
                MyArrList = tmpMyArrList;
            }

            sumTotal = extras.getDouble("sumTotal");
            strService = extras.getString("strService");
        }

        // Permission StrictMode
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        // *** Create Folder
        createFolder();

        // *** ImageView
        imgView = (ImageView) findViewById(R.id.imgView);

        // *** Take Photo
        final Button btnTakePhoto = (Button) findViewById(R.id.btnTakePhoto);
        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CarActivity.class);
                i.putExtra("photoCarArray", photoCarArray);
                startActivity(i);
            }
        });
        // Perform action on click
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                    }
                }

            }
        });

        // *** Upload Photo
        btnUploadFront = (Button) findViewById(R.id.btnUploadFront);
        btnUploadLeft = (Button) findViewById(R.id.btnUploadLeft);
        btnUploadRight = (Button) findViewById(R.id.btnUploadRight);
        btnUploadBehide = (Button) findViewById(R.id.btnUploadBehide);
        btnUploadTop = (Button) findViewById(R.id.btnUploadTop);
        // Perform action on click
        btnUploadFront.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // *** Upload file to Server
                boolean status = uploadFiletoServer(mCurrentPhotoPath, strURLUpload);
                if(status){
                    msgShow("Upload Photo สำเร็จ");
                    btnUploadFront.setBackgroundColor(Color.GREEN);
                    namePhotoSplite = mCurrentPhotoPath.split("/");
                    photoCarArray[0] = namePhotoSplite[namePhotoSplite.length - 1];
                }else {
                    msgShow("Upload Photo ไม่สำเร็จ!!");
                }
            }
        });
        btnUploadLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // *** Upload file to Server
                boolean status = uploadFiletoServer(mCurrentPhotoPath, strURLUpload);
                if(status){
                    msgShow("Upload Photo สำเร็จ");
                    btnUploadLeft.setBackgroundColor(Color.GREEN);
                    namePhotoSplite = mCurrentPhotoPath.split("/");
                    photoCarArray[1] = namePhotoSplite[namePhotoSplite.length - 1];
                }else {
                    msgShow("Upload Photo ไม่สำเร็จ!!");
                }
            }
        });
        btnUploadRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // *** Upload file to Server
                boolean status = uploadFiletoServer(mCurrentPhotoPath, strURLUpload);
                if(status){
                    msgShow("Upload Photo สำเร็จ");
                    btnUploadRight.setBackgroundColor(Color.GREEN);
                    namePhotoSplite = mCurrentPhotoPath.split("/");
                    photoCarArray[2] = namePhotoSplite[namePhotoSplite.length - 1];
                }else {
                    msgShow("Upload Photo ไม่สำเร็จ!!");
                }
            }
        });
        btnUploadBehide.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // *** Upload file to Server
                boolean status = uploadFiletoServer(mCurrentPhotoPath, strURLUpload);
                if(status){
                    msgShow("Upload Photo สำเร็จ");
                    btnUploadBehide.setBackgroundColor(Color.GREEN);
                    namePhotoSplite = mCurrentPhotoPath.split("/");
                    photoCarArray[3] = namePhotoSplite[namePhotoSplite.length - 1];
                }else {
                    msgShow("Upload Photo ไม่สำเร็จ!!");
                }
            }
        });
        btnUploadTop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // *** Upload file to Server
                boolean status = uploadFiletoServer(mCurrentPhotoPath, strURLUpload);
                if(status){
                    msgShow("Upload Photo สำเร็จ");
                    btnUploadTop.setBackgroundColor(Color.GREEN);
                    namePhotoSplite = mCurrentPhotoPath.split("/");
                    photoCarArray[4] = namePhotoSplite[namePhotoSplite.length - 1];
                }else {
                    msgShow("Upload Photo ไม่สำเร็จ!!");
                }
            }
        });

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(strSDCardPathName);
        File image = File.createTempFile(imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            imgView.setImageBitmap(bitmap);
        }
    }

    public static boolean uploadFiletoServer(String strSDPath, String strUrlServer) {

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        int resCode = 0;
        String resMessage = "";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        try {
            File file = new File(strSDPath);
            if (!file.exists()) {
                return false;
            }

            FileInputStream fileInputStream = new FileInputStream(new File(strSDPath));

            URL url = new URL(strUrlServer);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes(
                    "Content-Disposition: form-data; name=\"filUpload\";filename=\"" + strSDPath + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Response Code and Message
            resCode = conn.getResponseCode();
            if (resCode == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int read = 0;
                while ((read = is.read()) != -1) {
                    bos.write(read);
                }
                byte[] result = bos.toByteArray();
                bos.close();

                resMessage = new String(result);

            }

            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

            return true;

        } catch (Exception ex) {
            // Exception handling
            return false;
        }
    }

    public static void createFolder() {
        File folder = new File(strSDCardPathName);
        try {
            // Create folder
            if (!folder.exists()) {
                folder.mkdir();
            }
        } catch (Exception ex) {
        }

    }

    private void msgShow(String strMsg){
        Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_SHORT).show();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

}
