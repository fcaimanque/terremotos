package cl.ucn.disc.dsm.fcaimanque.terremotos.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.TileSystem;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import java.util.List;

import cl.ucn.disc.dsm.fcaimanque.terremotos.R;
import cl.ucn.disc.dsm.fcaimanque.terremotos.controllers.EarthquakeCatalogController;
import cl.ucn.disc.dsm.fcaimanque.terremotos.model.Earthquake;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {
    MapView map;
    List<Earthquake> dataList;
    GeoPoint initial = new GeoPoint(-23.6812, -70.4102);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_main);
        map = findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        // Configurar mapa:
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.getController().setZoom(4);
        map.setVerticalMapRepetitionEnabled(false);
        map.setScrollableAreaLimitLatitude(TileSystem.MaxLatitude,-TileSystem.MaxLatitude, 0);
        map.getController().setCenter(initial);
        downloadData();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void createMarkers() {
        if (dataList != null && !dataList.isEmpty()) {
            for (Earthquake ed : dataList) {
                createEarthquakeMarker(ed);
            }
        }
    }

    private void createEarthquakeMarker(Earthquake data){
        Marker marker = new Marker(map);
        marker.setTitle(data.properties.title);
        marker.setSnippet(data.geometry.toString());

        GeoPoint point = new GeoPoint(data.geometry.getLatitude(), data.geometry.getLongitude());
        marker.setPosition(point);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        map.getOverlays().add(marker);
    }

    private void downloadData()
    {
        AsyncTask.execute(() -> {

            List<Earthquake> earthquakesData = null;
            try {
                earthquakesData = EarthquakeCatalogController.getEarthquakeCatalog();
            } catch (Exception e) {
            }
            if (earthquakesData != null) {
                dataList = earthquakesData;
                createMarkers();
            }
        });
    }

    public void onResume(){
        super.onResume();
        map.onResume();
    }

    public void onPause(){
        super.onPause();
        map.onPause();
    }
}