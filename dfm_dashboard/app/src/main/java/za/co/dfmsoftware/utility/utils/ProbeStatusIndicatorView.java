package za.co.dfmsoftware.utility.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProbeStatusIndicatorView extends View {

    private Paint paint;
    private Paint paintStroke;
    private int dotRadius;
    private int x, y; //used for calculating centre of ProbeStatusIndicatorView

    public ProbeStatusIndicatorView(Context context) {
        super(context);
        this.configureView();
    }

    public ProbeStatusIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.configureView();
    }

    public ProbeStatusIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.configureView();
    }

    //todo @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProbeStatusIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.configureView();
    }

    private void configureView() {
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paint.setStyle(Paint.Style.FILL);

        this.paintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.paintStroke.setStrokeWidth(4);
        this.paintStroke.setAntiAlias(true);
        this.paintStroke.setStyle(Paint.Style.STROKE);
        this.paintStroke.setColor(Color.LTGRAY);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if(this.dotRadius == 0) {
            this.dotRadius = this.getWidth() / 2;
            this.x = this.getWidth() / 2;
            this.y = this.getWidth() / 2;
        }

        canvas.drawCircle(x, y, this.dotRadius, paint);
        canvas.drawCircle(x, y, this.dotRadius - 2, this.paintStroke);
    }

    public void setColor(int color) { this.paint.setColor(color); }
}
