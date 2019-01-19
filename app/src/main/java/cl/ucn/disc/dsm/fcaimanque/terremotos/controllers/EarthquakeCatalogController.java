package cl.ucn.disc.dsm.fcaimanque.terremotos.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import cl.ucn.disc.dsm.fcaimanque.terremotos.adapters.Deserializer;
import cl.ucn.disc.dsm.fcaimanque.terremotos.model.Earthquake;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class EarthquakeCatalogController {
    /**
     * Representa el servicio que consume la API.
     */
    public interface EarthquakeCatalogService {

        @GET("query?format=geojson&starttime=2018-12-1&limit=150")
        Call<List<Earthquake>> getCatalog();
    }

    /**
     * ...
     */
    private static final HttpLoggingInterceptor interceptor  = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS);

    /**
     * ...
     */
    private static final OkHttpClient cliente = new OkHttpClient.Builder()
            .addInterceptor(interceptor).build();

    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(List.class, new Deserializer<List<Earthquake>>())
            .create();

    /**
     * Instancia de retrofit.
     */
    private static final Retrofit retro = new Retrofit.Builder()
            .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(cliente)
            .build();


    private static final EarthquakeCatalogService servicio = retro.create(EarthquakeCatalogService.class);

    public static List<Earthquake> getEarthquakeCatalog() throws IOException {
        Call<List<Earthquake>> earthquakeCatalogCall = servicio.getCatalog();

        return earthquakeCatalogCall.execute().body();
    }

}
