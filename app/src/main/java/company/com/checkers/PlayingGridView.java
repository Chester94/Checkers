package company.com.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * View class. Displays to the user model. Initial processing of events.
 */
public class PlayingGridView extends View implements View.OnTouchListener {
    /**
     * X-coord active checker
     */
    private float xActive;

    /**
     * Y-coord active checker
     */
    private float yActive;

    /**
     * The pixel size of the grid cells
     */
    private double cellSize;

    /**
     * Model, play field
     */
    private GameModel model;

    /**
     * Setter.
     * @param model installed model
     */
    public void setModel(GameModel model) {
        this.model = model;
    }

    /**
     * Constructor.
     * @param context system param
     * @param attrs system param
     */
    public PlayingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    /**
     * Calculating the size of the cell.
     */
    public void calcCellSize() {
        int size = Math.min(getHeight(), getWidth());
        cellSize = (double)size / GameModel.GRID_DIMENSION;
    }

    /**
     * Draw view.
     * @param canvas system param (draw there)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);

        calcCellSize();

        drawGrid(canvas);
        drawCheckers(canvas);
    }

    /**
     * Event handling Touch Screen
     * @param v system param
     * @param event system param
     * @return true - processed, false - non-processed
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int row = (int)Math.floor(event.getY() / cellSize);
        int column = (int)Math.floor(event.getX() / cellSize);

        xActive = event.getX();
        yActive = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                model.startStep(row, column);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                model.stopStep(row, column);
                break;
        }
        invalidate();
        return true;
    }

    /**
     * Drawing chessboard. Dimensions of the model.
     * @param canvas system param (draw there)
     */
    private void drawGrid(Canvas canvas) {
        Paint paint = new Paint();
        float x1, x2, y1, y2;

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                if( (i + j) % 2 == 0 )
                    paint.setColor(Color.WHITE);
                else
                    paint.setColor(Color.DKGRAY);

                x1 = (float)cellSize * i;
                y1 = (float)cellSize * j;
                x2 = (float)cellSize * (i+1);
                y2 = (float)cellSize * (j+1);

                canvas.drawRect(x1, y1, x2, y2, paint);
            }
        }
    }

    /**
     * Drawing of the chips, which currently exist
     * @param canvas system param (draw there)
     */
    private void drawCheckers(Canvas canvas) {
        int [][] grid = model.getGrid();
        int typeActiveChecker = GameModel.NONE;

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                if (model.isActiveDragging(i, j)) {
                    typeActiveChecker = grid[i][j];
                    continue;
                }

                float x = (float)(cellSize * j + cellSize / 2.);
                float y = (float)(cellSize * i + cellSize / 2.);
                drawChecker(canvas, x, y, grid[i][j]);
            }
        }

        drawChecker(canvas, xActive, yActive, typeActiveChecker);
    }

    /**
     * Drawing particular Checkers.
     * @param canvas system param (draw there)
     * @param x X-coord
     * @param y Y-coord
     * @param type type being drawn Checkers
     */
    private void drawChecker(Canvas canvas, float x, float y, int type) {
        Paint mainPaint = new Paint();
        Paint strokePaint = new Paint();
        Paint queenPaint = new Paint();

        switch (type) {
            case GameModel.NONE : default :
                return;

            case GameModel.WHITE :
                mainPaint.setColor(Color.WHITE);
                strokePaint.setColor(Color.BLACK);
                queenPaint.setColor(Color.TRANSPARENT);
                break;

            case GameModel.BLACK :
                mainPaint.setColor(Color.BLACK);
                strokePaint.setColor(Color.WHITE);
                queenPaint.setColor(Color.TRANSPARENT);
                break;

            case GameModel.WHITE_QUEEN :
                mainPaint.setColor(Color.WHITE);
                strokePaint.setColor(Color.BLACK);
                queenPaint.setColor(Color.RED);
                break;

            case GameModel.BLACK_QUEEN :
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
