package za.co.dfmsoftware.utility.ui.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import io.reactivex.subjects.PublishSubject;
import za.co.dfmsoftware.utility.R;

public class GraphDaySelectorView extends RelativeLayout {

    private ImageButton backButton, forwardButton;
    private TextView textViewTitle;

    private PublishSubject<Integer> onFilterSelectionUpdatedSubject = PublishSubject.create();
    private int selectedIndex;

    public GraphDaySelectorView(Context context) {
        super(context);
        this.inflateView(context);
    }

    public GraphDaySelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.inflateView(context);
    }

    public GraphDaySelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.inflateView(context);
    }

    public GraphDaySelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.inflateView(context);
    }

    private void inflateView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_graph_day_selector, this);

        backButton = findViewById(R.id.navigate_back_button);
        forwardButton = findViewById(R.id.navigate_forward_button);
        textViewTitle = findViewById(R.id.title_textview);

        backButton.setOnClickListener(view -> {
            selectedIndex--;
            navigateToIndex(selectedIndex);
        });

        forwardButton.setOnClickListener(view -> {
            selectedIndex++;
            navigateToIndex(selectedIndex);
        });

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            backButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.graph_filter_days_navigate_button_tint)));
            forwardButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.graph_filter_days_navigate_button_tint)));
        }
    }

    public void restoreFilterSelection(int days) {
        selectedIndex = getGraphFilterSelectionDays(days);
        navigateToIndex(selectedIndex);
    }

    public int getFilterSelection() {
        return this.getGraphFilterSelectionDays(this.selectedIndex);
    }

    private void navigateToIndex(int index) {
        String title = getGraphFilterSelectionDaysTitles(index);
        textViewTitle.setText(title);
        onFilterSelectionUpdatedSubject.onNext(getGraphFilterSelectionDays(index));

        backButton.setEnabled(selectedIndex > 0);
        forwardButton.setEnabled(selectedIndex < (getGraphFilterSelectionCount() - 1));
    }

    private String getGraphFilterSelectionDaysTitles(int index) {
        Resources resources = getContext().getResources();
        String[] titles = resources.getStringArray(R.array.graphFilterSelectionDaysTitles);
        return titles[index];
    }

    private int getGraphFilterSelectionDays(int index) {
        Resources resources = getContext().getResources();
        TypedArray days = resources.obtainTypedArray(R.array.graphFilterSelectionDays);

        int dayIndex = days.getInt(index, 0);
        days.recycle();

        return dayIndex;
    }

    private int getGraphFilterSelectionCount() {
        Resources resources = getContext().getResources();
        TypedArray days = resources.obtainTypedArray(R.array.graphFilterSelectionDays);
        int length = days.length();
        days.recycle();
        return  length;
    }

    public PublishSubject<Integer> getOnFilterSelectionUpdatedSubject() {
        return onFilterSelectionUpdatedSubject;
    }
}
