package com.example.startproject2;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {
    EditText editText2;
    EditText editText;
    EditText editText3;
    EditText editText4;
    ImageView imageView;
    RadioButton radioButton;
    File file;
    File signature;
    LinearLayout drawlinear;
    RadioButton radioButton2;
    String v1;
    String v2;
    String v3;
    String v4;
  PaintBoard m;
    Bitmap bitmap;
    Bitmap bitmap2;
    @Override
    public void onStop() {
        super.onStop();
        File signatureFile = new File(getActivity().getExternalFilesDir(null),
                "signature.png");

        bitmap2 = m.mBitmap;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap2.compress(Bitmap.CompressFormat.PNG, 0, bos);
        try {
            FileOutputStream fos = new FileOutputStream(signatureFile);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prfs = getContext().getSharedPreferences("Mablang", Context.MODE_PRIVATE);
        if (prfs != null) {
            v1 = prfs.getString("name", "");
            v2 = prfs.getString("name2", "");
            v3 = prfs.getString("name3", "");
            v4 = prfs.getString("name4", "");

            boolean q = prfs.getBoolean("man", false);
            boolean q1 = prfs.getBoolean("girl", false);
            editText3.setText(v3);
            editText4.setText(v4);
            radioButton.setChecked(q);
            radioButton2.setChecked(q1);
            editText.setText(v1);
            editText2.setText(v2);



        }
        file = createFile();
        if (file.exists()) {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), null);
            imageView.setImageBitmap(bitmap);
        }
        File signatureFile = new File(getActivity().getExternalFilesDir(null),
                "signature.png");
        if(signatureFile.exists()){
            bitmap2 = BitmapFactory.decodeFile(signatureFile.getAbsolutePath(), null);
            m.changeBitmap(bitmap2);
        }


    }

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences prfs = getContext().getSharedPreferences("Mablang", Context.MODE_PRIVATE);

        SharedPreferences.Editor edit = prfs.edit();
        edit.putString("name2", editText2.getText().toString());
        edit.putString("name", editText.getText().toString());
        edit.putString("name3", editText3.getText().toString());
        edit.putBoolean("man", radioButton.isChecked());
        edit.putBoolean("girl", radioButton2.isChecked());
        edit.putString("name4", editText4.getText().toString());
        edit.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);
        editText2 = rootView.findViewById(R.id.editText2);
        Button button =rootView.findViewById(R.id.button);
        imageView = rootView.findViewById(R.id.imageView4);
        editText3 = rootView.findViewById(R.id.editText3);
        editText = rootView.findViewById(R.id.editText);
        editText4 = rootView.findViewById(R.id.editText4);
        radioButton = rootView.findViewById(R.id.radioButton);
        radioButton2 = rootView.findViewById(R.id.radioButton2);
        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DATE);
        drawlinear =(LinearLayout)rootView.findViewById(R.id.drawliner);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
        m = new PaintBoard(getContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m.clear();
            }
        });
        editText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getContext(), listener2, year, month, day);
                dialog.show();
            }
        });

        drawlinear.addView(m);
        return rootView;
    }

    private DatePickerDialog.OnDateSetListener listener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            int month = monthOfYear + 1;
            editText2.setText(year + "년" + month + "월" + dayOfMonth + "일");
        }
    };

    private void takePicture() {
        if (file == null) {
            file = createFile();
        }
        Uri fileUri = FileProvider.getUriForFile(getContext(), "com.example.samplecaptureintent.fileprovider", file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, 101);
        }
    }


    private File createFile() {
        String filename = "capture.jpg";
        File storageDir = getContext().getExternalFilesDir(null);
        File outFile = new File(storageDir, filename);
        return outFile;
    }


    @Override
   public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }
}




