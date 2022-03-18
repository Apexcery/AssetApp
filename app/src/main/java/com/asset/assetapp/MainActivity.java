package com.asset.assetapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asset.assetapp.adapters.AssetListAdapter;
import com.asset.assetapp.data.AssetDbAdapter;
import com.asset.assetapp.databinding.ActivityMainBinding;
import com.asset.assetapp.models.Asset;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AssetDbAdapter assetDbAdapter;

    private AppBarConfiguration appBarConfiguration;
    private ArrayList<Asset> assets;

    AssetListAdapter assetListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fabCreate.setOnClickListener(view -> switchToCreateAssetActivity());

        TooltipCompat.setTooltipText(binding.fabCreate, "Add Asset");

        assetDbAdapter = new AssetDbAdapter(this);

        if (assetDbAdapter.databaseExists())
            assets = assetDbAdapter.getData();

        RecyclerView assetRecycler = findViewById(R.id.asset_list_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        assetRecycler.setLayoutManager(layoutManager);
        assetRecycler.setItemAnimator(new DefaultItemAnimator());

        assetListAdapter = new AssetListAdapter(getBaseContext(), assets, assetDbAdapter, getSupportFragmentManager());

        assetRecycler.setAdapter(assetListAdapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (assetDbAdapter.databaseExists()) {
            assets = assetDbAdapter.getData();
            assetListAdapter.setValues(assets);
            assetListAdapter.notifyDataSetChanged();
        }
    }

    private void switchToCreateAssetActivity(){
        Intent intent = new Intent(MainActivity.this, CreateAssetActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}