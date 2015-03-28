package company.com.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PlayingGridView extends View implements View.OnTouchListener {
    private double x;
    private double y;

    public PlayingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);

        x = -100;
        y = -100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        drawGrid(canvas);

        canvas.drawCircle((float)x, (float)y, 40, paint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x = event.getX();
                y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x = -100;
                y = -100;
                break;
        }
        invalidate();
        return true;
    }

    private void drawGrid(Canvas canvas) {
        int size = Math.min(getHeight(), getWidth());
        double cellHeight = (double)size / GameModel.GRID_HEIGHT;
        double cellWidth = (double)size / GameModel.GRID_WIDTH;

        Paint paint = new Paint();

        for (int i = 0; i < GameModel.GRID_WIDTH; i++) {
            for (int j = 0; j < GameModel.GRID_HEIGHT; j++) {
                if( (i + j) % 2 == 0 )
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.DKGRAY);

                canvas.drawRect((float)cellWidth * i, (float)cellHeight * j,
                        (float)cellWidth * (i+1), (float)cellHeight * (j+1), paint);
            }
        }
    }
}
