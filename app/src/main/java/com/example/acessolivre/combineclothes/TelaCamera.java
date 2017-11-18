package com.example.acessolivre.combineclothes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.VoicemailContract;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.acessolivre.combineclothes.model.Photo;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;


public class TelaCamera extends AppCompatActivity {
    private int countBackpress = 0;
    private static final int ACTIVITY_START_CAMERA_APP = 0;
    private Button btnVoltar;
    private PhotoView imageHolder;
    private final int requestCode = 20;
    static final int PICTURE_RESULT = 1;
    private Uri selectedImage;
    private int test = 0;
    private Uri imageUri;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private File mCurrenteFilePhoto;
    private static final String DEBUGTAG = "JWP";
    private Bitmap bitmap;
    private Path mPath;
    private CanvasView canvasView;
    private Cores cores;
    private Bitmap imagemTransp = null;


    private void takePicture(){

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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_camera);

        imageHolder = (PhotoView) findViewById(R.id.captured_photo);
        final CanvasView view = (CanvasView) findViewById(R.id.canvas);
        //final TextView corPred = (TextView) findViewById(R.id.cordenates);

        takePicture();

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


        Button btnZoom = (Button) findViewById(R.id.btn_zoom);
        btnZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageHolder.setScale(imageHolder.getScale()+0.2F);
            }
        });

        Button btnNewPic = (Button) findViewById(R.id.btn_new_picture);
        btnNewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrenteFilePhoto != null) {
                    mCurrenteFilePhoto.delete();
                }
                takePicture();
            }
        });
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void confirmarFoto(View view){
        final ProgressDialog progress = ProgressDialog.show(this,
                "Aguarde...",
                "Estamos carregando sua foto", true);
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] data = baos.toByteArray();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        database.setPersistenceEnabled(false);
        final DatabaseReference myRef = database.getReference("photos").push();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images/"+new Date().getTime()+".jpg");
        imageRef.putBytes(data)
        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                @SuppressWarnings("VisibleForTests")
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Photo photo = new Photo(null, downloadUrl.toString(), new Date());
                myRef.setValue(photo);
                photo.setId(myRef.getKey().toString());
                myRef.setValue(photo);

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                share.putExtra(Intent.EXTRA_SUBJECT, "CombineClothes");
                share.putExtra(Intent.EXTRA_TEXT, "CombineClothes, Vote na minha combinação: http://combineclothes.tk/vote/"+photo.getId());
                startActivity(Intent.createChooser(share, "Compartilhar"));
                progress.dismiss();
                finish();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e(CombineClothes.TAG, exception.getMessage());
                progress.dismiss();
                Toast.makeText(TelaCamera.this, "Problema no carregamento da Imagem.", Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if("".equals(mCurrentPhotoPath)){
            finish();
            return;
        }
        Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        imageHolder.setImageBitmap(imageBitmap);
        imagemTransp = imageBitmap;
        //textDomColor.setText(":)");
        //textDomColor.setText(getDominantColor());
        canvasView = (CanvasView) findViewById(R.id.canvas);

    }


    @Override
    public void onBackPressed() {
        if(imageHolder.getScale() > 1F){
            imageHolder.setScale(1F);
            return;
        }

        if(countBackpress >= 2){
            super.onBackPressed();
        }else {
            Toast.makeText(this, "Pressione voltar duas vezes para sair.", Toast.LENGTH_SHORT).show();
            countBackpress++;
            // zera contagem caso não seja executado o duplo toque em um segundo
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    countBackpress = 0;
                }
            }, 2000);
        }
    }

    private void addTouchListener(){
        final ImageView image = (ImageView)findViewById(R.id.captured_photo);

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
        mCurrenteFilePhoto = image;
        return image;
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




