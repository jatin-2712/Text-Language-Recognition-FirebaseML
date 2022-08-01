package com.masterandroid.textrecognition;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView resultTv;
    Button choose;
    ActivityResultLauncher<String> mTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        resultTv = findViewById(R.id.textView);
        choose = findViewById(R.id.button);


        mTakePhoto = registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        imageView.setImageURI(result);
                        FirebaseVisionImage image;
                        try {
                            image = FirebaseVisionImage.fromFilePath(getApplicationContext(),result);
                            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                                    .getOnDeviceTextRecognizer();
                            textRecognizer.processImage(image)
                                    .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                        @Override
                                        public void onSuccess(FirebaseVisionText result) {
                                            // Task completed successfully
                                            // ...
                                            resultTv.setText(result.getText().toString());
                                        }
                                    })
                                    .addOnFailureListener(
                                            new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Task failed with an exception
                                                    // ...
                                                }
                                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

        );

                choose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mTakePhoto.launch("image/*");
                    }
                });
    }
}