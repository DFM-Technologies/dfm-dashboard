package za.co.dfmsoftware.utility.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import io.reactivex.subjects.PublishSubject;
import za.co.dfmsoftware.utility.R;

public class GraphTypeSelectorView extends LinearLayout {
    private RadioGroup radioGroupTypeSelection;
    private PublishSubject<Integer> onFilterSelectionUpdate = PublishSubject.create();

    public GraphTypeSelectorView(Context context) {
        super(context);
        this.inflateView(context);
    }

    public GraphTypeSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.inflateView(context);
    }

    public GraphTypeSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.inflateView(context);
    }

    public GraphTypeSelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.inflateView(context);
    }

    private void inflateView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_graph_type_selector, this);
        this.radioGroupTypeSelection = findViewById(R.id.radio_group_selector);

        this.radioGroupTypeSelection.setOnCheckedChangeListener((radioGroup, i) -> this.onFilterSelectionUpdate.onNext(i+1));
    }

    public void restoreFilterSelection(int graphType) {
        ((RadioButton) this.radioGroupTypeSelection.getChildAt(graphType - 1)).setChecked(true);
    }

    public int getFilterSelection() {
        int radioButtonId = this.radioGroupTypeSelection.getCheckedRadioButtonId();
        RadioButton radioButton = this.radioGroupTypeSelection.findViewById(radioButtonId);
        int selectedIndex = this.radioGroupTypeSelection.indexOfChild(radioButton);

        return selectedIndex + 1;
    }

    public PublishSubject<Integer> getOnFilterSelectionUpdate() {
        return onFilterSelectionUpdate;
    }
}
