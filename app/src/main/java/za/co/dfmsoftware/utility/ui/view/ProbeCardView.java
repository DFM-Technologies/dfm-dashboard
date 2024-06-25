package za.co.dfmsoftware.utility.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.model.enums.ProbeStatus;

public class ProbeCardView extends FrameLayout {

    private static final int TOTAL_CARDS_PER_ROW = 2;

    private View viewColorOverlay;
    private TextView textTotal;
    private TextView textTitle;

    public ProbeCardView(@NonNull Context context){
        super(context);
        this.inflateView(context);
    }

    public ProbeCardView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.inflateView(context);
    }

    public ProbeCardView(@NonNull Context context, @Nullable AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.inflateView(context);
    }

    public void inflateView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_probe_card, this);
        viewColorOverlay = findViewById(R.id.colour_overlay_view);
        textTotal = findViewById(R.id.total_probes_textview);
        textTitle = findViewById(R.id.card_title_textview);

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        int cardSize = ((int) dpWidth / TOTAL_CARDS_PER_ROW);

        LayoutParams layoutParams = new LayoutParams(cardSize, cardSize);
        this.setLayoutParams(layoutParams);
    }

    //NEW CTRL-V
    public void configureView(@NonNull ProbeStatus probeStatus, int totalProbes) {
        this.viewColorOverlay.setBackgroundColor(this.getProbeStatusColor(probeStatus));
        this.textTitle.setText(this.getProbeStatusTitle(probeStatus));
        this.textTotal.setText(String.valueOf(totalProbes));
    }

    private int getProbeStatusColor(@NonNull ProbeStatus probeStatus) {
        Resources resources = this.getContext().getResources();
        TypedArray colors = resources.obtainTypedArray(R.array.probeStatusColor);

        int colorTransparent = resources.getColor(R.color.transparent);

        if (probeStatus == ProbeStatus.ALL_PROBES ||
                probeStatus.getStatus() >= colors.length()) {
            return colorTransparent;
        }

        int statusColor = colors.getColor(probeStatus.getStatus(), colorTransparent);

        colors.recycle();

        return statusColor;
    }

    private String getProbeStatusTitle(@NonNull ProbeStatus probeStatus) {

        Resources resources = this.getContext().getResources();

        if (probeStatus == ProbeStatus.ALL_PROBES) {
            return resources.getString(R.string.all_label);
        }

        String[] titles = resources.getStringArray(R.array.probeStatusTitle);

        if (probeStatus.getStatus() >= titles.length) {
            return resources.getString(R.string.none_label);
        }

        return titles[probeStatus.getStatus()];
    }

//OLD BELOW

//    private View viewColourOverlay;
//    private TextView textTotal;
//    private TextView textTitle;
//
//    public ProbeCardView(@NonNull Context context) {
//        super(context);
//        init(context);
//    }
//
//    public ProbeCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public ProbeCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        init(context);
//    }
//
//    public ProbeCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context);
//    }
//
//    public void configureView(@NonNull ProbeStatus probeStatus, int totalProbes) {
////        this.viewColourOverlay.setBackgroundColor(this.getProbeStatusColour(probeStatus));
//        this.textTitle.setText(this.getProbeStatusTitle(probeStatus));
//        this.textTotal.setText(String.valueOf(totalProbes));
//    }
//
//    //===
////    private int getProbeStatusColour(@NonNull ProbeStatus probeStatus){}
//
//    private String getProbeStatusTitle(@NonNull ProbeStatus probeStatus) {
//        Resources resources = this.getContext().getResources();
//
//        if(probeStatus == ProbeStatus.ALL_PROBES) {
//            return resources.getString(R.string.all_label);
//        }
//
//        String[] titles = resources.getStringArray(R.array.probeStatusTitle);
//
//        if(probeStatus.getStatus() >= titles.length) {
//            return resources.getString(R.string.none_label);
//        }
//
//        return titles[probeStatus.getStatus()];
//    }
//
//    private void init(final Context context){
//        LayoutInflater.from(context).inflate(R.layout.view_probe_card, this, true);
//
//        viewColourOverlay = findViewById(R.id.colour_overlay_view);
//        textTotal = findViewById(R.id.total_probes_textview);
//        textTitle = findViewById(R.id.card_title_textview);
//
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        float dpWidth = displayMetrics.widthPixels;
//        int cardSize = ((int) dpWidth / TOTAL_CARDS_PER_ROW);
//
//        LayoutParams layoutParams = new LayoutParams(cardSize, cardSize);
//        this.setLayoutParams(layoutParams);
//    }
}
