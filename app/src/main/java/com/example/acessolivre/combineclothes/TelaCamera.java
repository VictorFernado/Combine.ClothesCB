package com.example.acessolivre.combineclothes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.graphics.Bitmap;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.*;
import android.view.*;


import java.io.*;
import java.net.URI;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TelaCamera extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private Button btnVoltar;
    private ImageView imageHolder;
    private final int requestCode = 20;
    static final int PICTURE_RESULT = 1;
    Uri selectedImage;
    private int test = 0;
    private Uri imageUri;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private static final String DEBUGTAG = "JWP";
    private Bitmap bitmap;
    private Path mPath;
    private CanvasView canvasView;
    private Cores cores;
    private Bitmap imagemTransp = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_camera);

        imageHolder = (ImageView) findViewById(R.id.captured_photo);
        final CanvasView view = (CanvasView) findViewById(R.id.canvas);
        //final TextView corPred = (TextView) findViewById(R.id.cordenates);

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        Uri photoURI = FileProvider.getUriForFile(TelaCamera.this,authorities,photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        //intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
        startActivityForResult(intent, ACTIVITY_START_CAMERA_APP);
        //dispatchTakePictureIntent();

        addTouchListener();

        if (null != view) {
            view.setOnUpCallback(new CanvasView.OnUpCallback() {


                @Override
                public void onRectFinished(final Rect rect) {
                      Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    //mCurrentPhotoPath = (Bitmap)findViewById(telaCamera.imagemTransp);
                    //int[] corPredominante = getDominantColor(mCurrentPhotoPath, mStartX,mStartY,mEndX, mEndY);
                    int[] corPredominante = getDominantColor(imageBitmap, rect);

                    //corPred.setText("Cor: {" + corPredominante[0]+","+ corPredominante[1]+","+corPredominante[2]+"}");
                    Toast.makeText(getApplicationContext(), "Rect is (" + rect.left + ", " + rect.top + ", " + rect.right + ", " + rect.bottom + ")",
                            Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        imageHolder.setImageBitmap(imageBitmap);
        imagemTransp = imageBitmap;
        //textDomColor.setText(":)");
        //textDomColor.setText(getDominantColor());
        canvasView = (CanvasView) findViewById(R.id.canvas);

    }

    public void createExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory.  Note that you should be careful about
        // what you place here, since the user often manages these files.  For
        // pictures and other media owned by the application, consider
        // Context.getExternalMediaDir().
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, "CombineClothes.jpg");

        try {
            // Make sure the Pictures directory exists.
            path.mkdirs();

            // Very simple code to copy a picture from the application's
            // resource into the external file.  Note that this code does
            // no error checking, and assumes the picture is small (does not
            // try to copy it in chunks).  Note that if external storage is
            // not currently mounted this will silently fail.
            InputStream is = getResources().openRawResource(R.raw.tela_2);
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(this,
                    new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        } catch (IOException e) {
            // Unable to create file, likely because external storage is
            // not currently mounted.
            Log.w("ExternalStorage", "Error writing " + file, e);
        }
    }

    private void addTouchListener(){
        final ImageView image = (ImageView)findViewById(R.id.captured_photo);
        //final TextView textCordenate = (TextView) findViewById(R.id.cordenates);


        image.setDrawingCacheEnabled(true);
        //image.buildDrawingCache(true);
        image.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){

                float x = event.getX();
                float y = event.getY();

                String message = String.format("Coordinates: (%.2f, %.2f)", x, y);
                Log.d(TelaCamera.DEBUGTAG, message);

                //textCordenate.setText(message);


                if(event.getAction() == event.ACTION_DOWN || event.getAction() == event.ACTION_MOVE || event.getAction() == event.ACTION_UP){
                    bitmap = image.getDrawingCache();
                    int pixel = bitmap.getPixel((int) event.getX(),(int) event.getY());

                    int r = Color.red(pixel);
                    int g = Color.green(pixel);
                    int b = Color.blue(pixel);


                }

                return true;
            }
        });

    }

    void deleteExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and delete the file.  If external
        // storage is not currently mounted this will fail.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, "CombineClothes.jpg");
        file.delete();
    }

    boolean hasExternalStoragePublicPicture() {
        // Create a path where we will place our picture in the user's
        // public pictures directory and check if the file exists.  If
        // external storage is not currently mounted this will think the
        // picture doesn't exist.
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path, "CombineClothes.jpg");
        return file.exists();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );



        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
         if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
             // Create the File where the photo should go
             File photoFile = null;
             try {
                 photoFile = createImageFile();
             } catch (IOException ex) {
                 // Error occurred while creating the File
                 Log.w("ExternalStorage", "Erro ao CRIAR o arquivo ");
             }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                String authorities = getApplicationContext().getPackageName() + ".fileprovider";
                Uri photoURI = FileProvider.getUriForFile(TelaCamera.this,
                        authorities,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public static int[] getDominantColor(Bitmap bitmap, Rect rect) {

        if (bitmap == null)
            throw new NullPointerException();

        int firstPixelX = rect.left;
        int firstPixelY = rect.top;
        int pixelToRows = (rect.right -  rect.left);
        int rows = (rect.bottom - rect.top);
        int size = pixelToRows * rows;
        int pixels[] = new int[size];

        Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);

        bitmap2.getPixels(pixels, 0, pixelToRows, firstPixelX, firstPixelY, pixelToRows, rows);

        final List<HashMap<Integer, Integer>> colorMap = new ArrayList<HashMap<Integer, Integer>>();
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());
        colorMap.add(new HashMap<Integer, Integer>());

        int color = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        Integer rC, gC, bC;
        for (int i = 0; i < pixels.length; i++) {
            color = pixels[i];

            r = Color.red(color);
            g = Color.green(color);
            b = Color.blue(color);

            rC = colorMap.get(0).get(r);
            if (rC == null)
                rC = 0;
            colorMap.get(0).put(r, ++rC);

            gC = colorMap.get(1).get(g);
            if (gC == null)
                gC = 0;
            colorMap.get(1).put(g, ++gC);

            bC = colorMap.get(2).get(b);
            if (bC == null)
                bC = 0;
            colorMap.get(2).put(b, ++bC);
        }

        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            int max = 0;
            int val = 0;
            for (Map.Entry<Integer, Integer> entry : colorMap.get(i).entrySet()) {
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    val = entry.getKey();
                }
            }
            rgb[i] = val;
        }

        int dominantColor = Color.rgb(rgb[0], rgb[1], rgb[2]);
        //String dominantColorString = String.format("Cor Predom.: {%d, %d, %d}",rgb[0], rgb[],rgb[2]);

        return rgb;
    }



}




