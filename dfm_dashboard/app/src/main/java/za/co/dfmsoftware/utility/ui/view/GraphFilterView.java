package za.co.dfmsoftware.utility.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.os.Handler;

import javax.annotation.Nullable;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.utils.GraphFilterSelection;

public class GraphFilterView extends LinearLayout {
    private static final int DEFAULT_GRAPH_TYPE = 2;
    private static final int DEFAULT_DAYS = 7;

    private GraphTypeSelectorView graphTypeSelector;
    private GraphDaySelectorView graphDaySelector;

    public PublishSubject<GraphFilterSelection> onFilterSelectionUpdateSubject = PublishSubject.create();
    private Handler delayHandler;

    public GraphFilterView(Context context) {
        super(context);
        this.inflateView(context);
    }

    public GraphFilterView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.inflateView(context);
    }

    public GraphFilterView(Context context, @Nullable AttributeSet attributeSet, int defStyleRes) {
        super(context, attributeSet, defStyleRes);
        this.inflateView(context);
    }

    public GraphFilterView(Context context, AttributeSet attributeSet, int defStyleAttributeSet, int defStyleRes) {
        super(context, attributeSet, defStyleAttributeSet, defStyleRes);
        this.inflateView(context);
    }

    @SuppressLint("CheckResult")
    private void inflateView(final Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_graph_filter, this);
        graphTypeSelector = findViewById(R.id.graph_type_selector_view);
        graphDaySelector = findViewById(R.id.graph_days_selector_view);

        this.delayHandler = new Handler();

        this.graphTypeSelector.getOnFilterSelectionUpdate()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(graphType -> this.updateGraphFilterSelection());

        this.graphDaySelector.getOnFilterSelectionUpdatedSubject()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(days -> this.updateGraphFilterSelection());
    }

    public void restoreFilterSelection(@Nullable GraphFilterSelection graphFilterSelection) {
        int graphType = DEFAULT_GRAPH_TYPE;
        int days = DEFAULT_DAYS;

        if(graphFilterSelection != null) {
            graphType = graphFilterSelection.getFilterType();
            days = graphFilterSelection.getDays();
        }

        this.graphTypeSelector.restoreFilterSelection(graphType);
        this.graphDaySelector.restoreFilterSelection(days);
    }

    public GraphFilterSelection getCurrentFilterSelection() {
        return new GraphFilterSelection(this.graphTypeSelector.getFilterSelection(), this.graphDaySelector.getFilterSelection());
    }

    public PublishSubject<GraphFilterSelection> getOnFilterSelectionUpdateSubject() {
        return onFilterSelectionUpdateSubject;
    }

    private void updateGraphFilterSelection() {
        this.delayHandler.postDelayed(() ->
                this.onFilterSelectionUpdateSubject.onNext(new GraphFilterSelection(
                        this.graphTypeSelector.getFilterSelection(),
                        this.graphDaySelector.getFilterSelection()
                )
            ),
        800);
    }
}
