package company.com.checkers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class CheckersActivity extends Activity {
    private final int NOT_CREATE = 0;
    private final int CREATE = 1;

    GameModel model = new GameModel();
    PlayingGridView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_checkers);

        view = (PlayingGridView) findViewById(R.id.view_playingField);
        view.setModel(model);

        startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pref = getSharedPreferences(getLocalClassName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("CREATE", CREATE);

        editor.putInt("TURN", model.getTurn());

        editor.putBoolean("ACTIVE", model.isActive());
        editor.putInt("ACTIVE_ROW", model.getActiveCheckerRow());
        editor.putInt("ACTIVE_COLUMN", model.getActiveCheckerColumn());

        editor.putBoolean("COMBO", model.isAttackStreak());
        editor.putInt("COMBO_ROW", model.getComboAttackerRow());
        editor.putInt("COMBO_COLUMN", model.getComboAttackerColumn());

        int[][] grid = model.getGrid();

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                editor.putInt("GRID_" + i + "_" + j, grid[i][j]);
            }
        }

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences pref = getSharedPreferences(getLocalClassName(), Context.MODE_PRIVATE);
        int create = pref.getInt("CREATE", NOT_CREATE);
        if (create == NOT_CREATE) {
            startGame();
            return;
        }

        model.setTurn(pref.getInt("TURN", GameModel.WHITE));

        model.setActive(pref.getBoolean("ACTIVE", false));
        model.setActiveCheckerRow(pref.getInt("ACTIVE_ROW", -1));
        model.setActiveCheckerColumn(pref.getInt("ACTIVE_COLUMN", -1));

        model.setAttackStreak(pref.getBoolean("COMBO", false));
        model.setComboAttackerRow(pref.getInt("COMBO_ROW", -1));
        model.setComboAttackerColumn(pref.getInt("COMBO_COLUMN", -1));

        int[][] grid = new int[GameModel.GRID_DIMENSION][GameModel.GRID_DIMENSION];

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                grid[i][j] = pref.getInt("GRID_" + i + "_" + j, GameModel.NONE);
            }
        }

        model.setGrid(grid);
    }

    public void startGame() {
        model.initGame();
        view.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_new_game) {
            startGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
