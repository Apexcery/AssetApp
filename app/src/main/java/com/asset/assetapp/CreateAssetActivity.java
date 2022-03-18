package com.asset.assetapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.asset.assetapp.data.AssetDbAdapter;
import com.asset.assetapp.databinding.ActivityCreateAssetBinding;
import com.asset.assetapp.models.Asset;
import com.google.android.material.textfield.TextInputEditText;

public class CreateAssetActivity extends AppCompatActivity implements TextWatcher {

    AssetDbAdapter assetDbAdapter;
    ActivityResultLauncher<Intent> imageUploadResultLauncher;

    Bitmap AssetImageBitmap = null;
    TextInputEditText txtId;
    TextInputEditText txtTitle;
    TextInputEditText txtDescription;
    TextInputEditText txtSerialNum;
    TextInputEditText txtManufacturer;
    TextInputEditText txtModelNum;
    TextInputEditText txtLatitude;
    TextInputEditText txtLongitude;

    Button addAssetButton;

    public CreateAssetActivity() { }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_asset);

        ActivityCreateAssetBinding binding = ActivityCreateAssetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        assetDbAdapter = new AssetDbAdapter(this);

        addAssetButton = (Button) findViewById(R.id.btn_addAsset);
        addAssetButton.setEnabled(false);
        addAssetButton.setOnClickListener(this::addAsset);

        ImageView uploadImage = (ImageView) findViewById(R.id.img_assetImg);
        uploadImage.setOnClickListener(this::uploadImage);

        txtId = (TextInputEditText) findViewById(R.id.txt_assetId);
        txtId.addTextChangedListener(this);

        txtTitle = (TextInputEditText) findViewById(R.id.txt_assetTitle);
        txtTitle.addTextChangedListener(this);

        txtDescription = (TextInputEditText) findViewById(R.id.txt_assetDescription);
        txtDescription.addTextChangedListener(this);

        txtSerialNum = (TextInputEditText) findViewById(R.id.txt_assetSerialNum);
        txtSerialNum.addTextChangedListener(this);

        txtManufacturer = (TextInputEditText) findViewById(R.id.txt_assetManufacturer);
        txtManufacturer.addTextChangedListener(this);

        txtModelNum = (TextInputEditText) findViewById(R.id.txt_assetModelNum);
        txtModelNum.addTextChangedListener(this);

        txtLatitude = (TextInputEditText) findViewById(R.id.txt_assetLatitude);
        txtLatitude.addTextChangedListener(this);

        txtLongitude = (TextInputEditText) findViewById(R.id.txt_assetLongitude);
        txtLongitude.addTextChangedListener(this);

        imageUploadResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data == null)
                            return;
                        Uri imageUri = data.getData();
                        try {
                            AssetImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                            uploadImage.setImageBitmap(AssetImageBitmap);
                        } catch (Exception e) {
                            Log.e("Upload Image", e.getMessage());
                        }
                    }
                });
    }



    private void uploadImage(View view) {
        imageUploadResultLauncher.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addAsset(View view) {
        String assetId = "";
        if (txtId.getEditableText() != null)
            assetId = txtId.getEditableText().toString();

        String assetTitle = "";
        if (txtTitle.getEditableText() != null)
            assetTitle = txtTitle.getEditableText().toString();

        String assetDescription = "";
        if (txtDescription.getEditableText() != null)
            assetDescription = txtDescription.getEditableText().toString();

        String assetSerialNumber = "";
        if (txtSerialNum.getEditableText() != null)
            assetSerialNumber = txtSerialNum.getEditableText().toString();

        String assetManufacturer = "";
        if (txtManufacturer.getEditableText() != null)
            assetManufacturer = txtManufacturer.getEditableText().toString();

        String assetModelNumber = "";
        if (txtModelNum.getEditableText() != null)
            assetModelNumber = txtModelNum.getEditableText().toString();

        double assetLatitude = 0.0;
        if (txtLatitude.getEditableText() != null)
            assetLatitude = Double.parseDouble(txtLatitude.getEditableText().toString());

        double assetLongitude = 0.0;
        if (txtLongitude.getEditableText() != null)
            assetLongitude = Double.parseDouble(txtLongitude.getEditableText().toString());

        Asset asset = new Asset(assetId, AssetImageBitmap, assetTitle, assetDescription, assetSerialNumber, assetManufacturer, assetModelNumber, new Asset.Location(assetLatitude, assetLongitude));

        long id = assetDbAdapter.insertAsset(asset);
        if (id == -1) {
            Toast.makeText(this, "An Asset with that ID already exists.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Asset successfully added.", Toast.LENGTH_SHORT).show();
            txtId.setText("");
            txtTitle.setText("");
            txtDescription.setText("");
            txtSerialNum.setText("");
            txtManufacturer.setText("");
            txtModelNum.setText("");
            txtLatitude.setText("");
            txtLongitude.setText("");
            txtId.requestFocus();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        addAssetButton.setEnabled(!txtId.getEditableText().toString().trim().equals("") &&
                !txtTitle.getEditableText().toString().trim().equals("") &&
                !txtDescription.getEditableText().toString().trim().equals("") &&
                !txtSerialNum.getEditableText().toString().trim().equals("") &&
                !txtManufacturer.getEditableText().toString().trim().equals("") &&
                !txtModelNum.getEditableText().toString().trim().equals("") &&
                !txtLatitude.getEditableText().toString().trim().equals("") &&
                !txtLongitude.getEditableText().toString().trim().equals(""));
    }

    @Override
    public void afterTextChanged(Editable editable) { }
}