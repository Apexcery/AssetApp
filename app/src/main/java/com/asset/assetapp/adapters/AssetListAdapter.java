package com.asset.assetapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asset.assetapp.AssetDetailsDialog;
import com.asset.assetapp.R;
import com.asset.assetapp.data.AssetDbAdapter;
import com.asset.assetapp.models.Asset;

import java.util.List;

public class AssetListAdapter extends RecyclerView.Adapter<AssetListAdapter.AssetViewHolder> {
    private final Context context;
    private List<Asset> assetList;
    private final AssetDbAdapter assetDbAdapter;
    private final FragmentManager fragmentManager;

    class AssetViewHolder extends RecyclerView.ViewHolder {
        ImageView assetImage;
        TextView assetTitle, assetSerialNumber;
        ImageButton viewAssetButton, deleteAssetButton;

        AssetViewHolder(View view) {
            super(view);

            assetImage = view.findViewById(R.id.img_asset_list_item_image);
            assetTitle = view.findViewById(R.id.txt_asset_list_item_title);
            assetSerialNumber = view.findViewById(R.id.txt_asset_list_item_serialNumber);

            viewAssetButton = view.findViewById(R.id.btn_asset_list_item_view);
            deleteAssetButton = view.findViewById(R.id.btn_asset_list_item_delete);
        }
    }

    public AssetListAdapter(Context context, List<Asset> assetList, AssetDbAdapter assetDbAdapter, FragmentManager fragmentManager) {
        this.context = context;
        this.assetList = assetList;
        this.assetDbAdapter = assetDbAdapter;
        this.fragmentManager = fragmentManager;
    }

    public void setValues(List<Asset> assetList) {
        this.assetList = assetList;
    }

    @NonNull
    @Override
    public AssetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.asset_list_item, parent, false);
        return new AssetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AssetViewHolder viewHolder, int position) {
        final Asset asset = assetList.get(position);

        viewHolder.assetImage.setImageBitmap(asset.getImage());
        viewHolder.assetTitle.setText(asset.getTitle());
        viewHolder.assetSerialNumber.setText(asset.getSerialNumber());

        viewHolder.viewAssetButton.setOnClickListener(view -> {
            AssetDetailsDialog dialog = new AssetDetailsDialog(asset);
            dialog.show(fragmentManager, null);
        });

        viewHolder.deleteAssetButton.setOnClickListener(view -> {
            int amountDeleted = assetDbAdapter.delete(asset.getId());
            if (amountDeleted == -1) {
                Toast.makeText(context, "Asset failed to be deleted.", Toast.LENGTH_SHORT).show();
                return;
            }
            assetList.remove(asset);
            this.notifyItemRemoved(position);
            Toast.makeText(context, "Asset successfully deleted.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if (assetList == null)
            return 0;
        return assetList.size();
    }
}
