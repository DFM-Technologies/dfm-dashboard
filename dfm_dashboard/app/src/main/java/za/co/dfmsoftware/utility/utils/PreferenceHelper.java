package za.co.dfmsoftware.utility.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import za.co.dfmsoftware.utility.BuildConfig;

public class PreferenceHelper {

    private static final String KEY_GRAPH_SELECTION_FILTER = "graph.selection.filter";
    private static PreferenceHelper INSTANCE;
    private final SharedPreferences preferences;
    private final Gson gson;

    public static PreferenceHelper getInstance(@NonNull Context context) {
        if(INSTANCE == null) {
            INSTANCE = new PreferenceHelper(context);
        }

        return INSTANCE;
    }

    public PreferenceHelper(@NonNull Context context) {
        this.preferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        this.gson = new Gson();
    }

    public void setGraphFilterSelection(@NonNull GraphFilterSelection graphFilterSelection) {
        String json = this.gson.toJson(graphFilterSelection);
        this.saveStringConfig(KEY_GRAPH_SELECTION_FILTER, json);
    }

    @Nullable
    public GraphFilterSelection getGraphFilterSelection() {
        String json = this.getStringConfig(KEY_GRAPH_SELECTION_FILTER);
        if(TextUtils.isEmpty(json)){
            return null;
        }

        return this.gson.fromJson(json, GraphFilterSelection.class);
    }

    private void saveStringConfig(@NonNull String key, @Nullable String value) {
        this.preferences.edit().putString(key, value);
    }

    @Nullable
    private String getStringConfig(@NonNull String key) {
        return this.preferences.getString(key, null);
    }
}
