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

    double cellSize;

    private GameModel model;

    public void setModel(GameModel model) {
        this.model = model;
    }

    public PlayingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);

        x = -100;
        y = -100;
    }

    private void calcCellSize() {
        int size = Math.min(getHeight(), getWidth());
        cellSize = (double)size / GameModel.GRID_DIMENSION;
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

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                if( (i + j) % 2 == 0 )
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.DKGRAY);

                canvas.drawRect((float)cellSize * i, (float)cellSize * j,
                        (float)cellSize * (i+1), (float)cellSize * (j+1), paint);
            }
        }
    }

    private void drawCheckers(Canvas canvas) {
        int [][] grid = model.getGrid();

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                float x = (float)(cellSize * j + cellSize / 2.);
                float y = (float)(cellSize * i + cellSize / 2.);
                drawChecker(canvas, x, y, grid[i][j]);
            }
        }
    }

    private void drawChecker(Canvas canvas, float x, float y, int type) {
        Paint mainPaint = new Paint();
        Paint strokePaint = new Paint();
        Paint queenPaint = new Paint();

        switch (type) {
            case GameModel.NOTHING : default :
                return;

            case GameModel.WHITE_CHECKER :
                mainPaint.setColor(Color.WHITE);
                strokePaint.setColor(Color.BLACK);
                queenPaint.setColor(Color.TRANSPARENT);
                break;

            case GameModel.BLACK_CHECKER :
                mainPaint.setColor(Color.BLACK);
                strokePaint.setColor(Color.WHITE);
                queenPaint.setColor(Color.TRANSPARENT);
                break;

            case GameModel.WHITE_CHECKER_QUEEN :
                mainPaint.setColor(Color.WHITE);
                strokePaint.setColor(Color.BLACK);
                queenPaint.setColor(Color.RED);
                break;

            case GameModel.BLACK_CHECKER_QUEEN :
                mainPaint.setColor(Color.BLACK);
                strokePaint.setColor(Color.WHITE);
                queenPaint.setColor(Color.RED);
                break;
        }

        canvas.drawCircle(x, y, (float)(cellSize / 2.5 + 2), strokePaint);
        canvas.drawCircle(x, y, (float)(cellSize / 2.5), mainPaint);
        canvas.drawCircle(x, y, (float)(cellSize / 4.5), queenPaint);
    }
}
