package company.com.checkers;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class PlayingGridView extends View implements View.OnTouchListener {
    private float x;
    private float y;

    double cellSize;

    private GameModel model;

    public void setModel(GameModel model) {
        this.model = model;
    }

    public PlayingGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    public void calcCellSize() {
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
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int row = (int)Math.round(event.getY() / cellSize);
        int column = (int)Math.round(event.getX() / cellSize);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (model.isOutOfField(row, column))
                    return true;

                if (model.getTurn() == model.getPlayerColor() &&
                        model.isCheckerBelongsToPlayer(row, column)) {
                    if (model.areAttackAvailableForPlayer()) {
                        if (model.areAttackAvailable(row, column))
                            model.activateDragging(row, column);

                        return true;
                    }

                    if (model.areMovesAvailableForPlayer()) {
                        if (model.areMovesAvailable(row, column))
                            model.activateDragging(row, column);

                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if (!model.isActiveDragging())
                    return true;

                if (model.areAttackAvailableForPlayer()) {
                    Log.d("1111", "yes");
                    if (model.isAttackValid(model.getActiveCheckerRow(), model.getActiveCheckerColumn(), row, column))
                        model.attack(row, column);
                }
                else {
                    if (model.isMoveValid(row, column)) {
                        Log.d("1111", "yes1");
                        model.move(row, column);
                    }
                }

                model.deactivateDragging();
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

    private void drawCheckers(Canvas canvas) {
        int [][] grid = model.getGrid();

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                if (model.isActiveDragging(i, j))
                    continue;

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
