package company.com.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class PlayingGridView extends View implements View.OnTouchListener {
    private double x;
    private double y;

    double cellHeight;
    double cellWidth;

    private GameModel model = new GameModel();

    public PlayingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);

        x = -100;
        y = -100;
    }

    private void calcCellSize() {
        int size = Math.min(getHeight(), getWidth());
        cellHeight = (double)size / GameModel.GRID_HEIGHT;
        cellWidth = (double)size / GameModel.GRID_WIDTH;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        calcCellSize();

        drawGrid(canvas);
        drawCheckers(canvas);

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
        /*Log.d("----", "Heigth " + getHeight());
        Log.d("----", "Width " + getWidth());
        Log.d("----", "x " + x);
        Log.d("----", "y " + y);*/
        invalidate();
        return true;
    }

    private void drawGrid(Canvas canvas) {
        Paint paint = new Paint();

        for (int i = 0; i < GameModel.GRID_WIDTH; i++) {
            for (int j = 0; j < GameModel.GRID_HEIGHT; j++) {
                if( (i + j) % 2 == 0 )
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.LTGRAY);

                canvas.drawRect((float)cellWidth * i, (float)cellHeight * j,
                        (float)cellWidth * (i+1), (float)cellHeight * (j+1), paint);
            }
        }
    }

    private void drawCheckers(Canvas canvas) {
        int [][] grid = model.getGrid();

        Paint paint = new Paint();

        for (int i = 0; i < GameModel.GRID_WIDTH; i++) {
            for (int j = 0; j < GameModel.GRID_HEIGHT; j++) {
                switch (grid[i][j]) {
                    case GameModel.NOTHING :
                        paint.setColor(Color.TRANSPARENT);
                        break;
                    case GameModel.WHITE_CHECKER :
                        paint.setColor(Color.RED);
                        break;
                    case GameModel.BLACK_CHECKER :
                        paint.setColor(Color.BLACK);
                        break;
                    default :
                        paint.setColor(Color.TRANSPARENT);
                        break;
                }

                canvas.drawCircle((float)(cellHeight * j + cellHeight / 2.),
                        (float)(cellWidth * i + cellWidth / 2.),
                        (float)(cellHeight / 2.5), paint);
            }
        }
    }
}
