package com.asset.assetapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.asset.assetapp.models.Asset;

public class AssetDetailsDialog extends DialogFragment {

    private static Asset asset;

    ImageView imgAsset;
    TextView txtId;
    TextView txtTitle;
    TextView txtDescription;
    TextView txtSerialNumber;
    TextView txtManufacturer;
    TextView txtModelNumber;
    TextView txtLatitude;
    TextView txtLongitude;

    public AssetDetailsDialog(Asset asset) {
        AssetDetailsDialog.asset = asset;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.asset_details_view, container, false);

        imgAsset = view.findViewById(R.id.img_assetDetailsImg);
        if (asset.getImage() != null)
            imgAsset.setImageBitmap(asset.getImage());

        txtId = view.findViewById(R.id.txt_assetDetailsId);
        txtId.setText(String.format("ID: %s", asset.getId()));

        txtTitle = view.findViewById(R.id.txt_assetDetailsTitle);
        txtTitle.setText(String.format("Title: %s", asset.getTitle()));

        txtDescription = view.findViewById(R.id.txt_assetDetailsDescription);
        txtDescription.setText(String.format("Description: %s", asset.getDescription()));

        txtSerialNumber = view.findViewById(R.id.txt_assetDetailsSerialNum);
        txtSerialNumber.setText(String.format("Serial Number: %s", asset.getSerialNumber()));

        txtManufacturer = view.findViewById(R.id.txt_assetDetailsManufacturer);
        txtManufacturer.setText(String.format("Manufacturer: %s", asset.getManufacturer()));

        txtModelNumber = view.findViewById(R.id.txt_assetDetailsModelNum);
        txtModelNumber.setText(String.format("Model Number: %s", asset.getModelNumber()));

        txtLatitude = view.findViewById(R.id.txt_assetDetailsLatitude);
        txtLatitude.setText(String.format("Latitude: %s", asset.getLocation().getLatitude()));

        txtLongitude = view.findViewById(R.id.txt_assetDetailsLongitude);
        txtLongitude.setText(String.format("Longitude: %s", asset.getLocation().getLongitude()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
