package za.co.dfmsoftware.utility.utils;

import com.google.gson.annotations.SerializedName;

public class GraphFilterSelection {

    @SerializedName("filterType")
    private final int filterType;

    @SerializedName("days")
    private final int days;

    public GraphFilterSelection(int filterType, int days) {
        this.filterType = filterType;
        this.days = days;
    }

    public int getFilterType() {
        return filterType;
    }

    public int getDays() {
        return days;
    }
}
