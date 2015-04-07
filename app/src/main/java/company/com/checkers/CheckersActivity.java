package company.com.checkers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class CheckersActivity extends Activity implements Lose {
    public final static String UUID = "e91521df-92b9-47bf-96d5-c52ee838f6f6";

    private final int NOT_CREATE = 0;
    private final int CREATE = 1;

    private final String CREATE_FLAG = "CREATE_FLAG";
    private final String TURN = "TURN";
    private final String ACTIVE_FLAG = "ACTIVE_FLAG";
    private final String ACTIVE_ROW = "ACTIVE_ROW";
    private final String ACTIVE_COLUMN = "ACTIVE_COLUMN";
    private final String COMBO_FLAG = "COMBO_FLAG";
    private final String COMBO_ROW = "COMBO_ROW";
    private final String COMBO_COLUMN = "COMBO_COLUMN";
    private final String GRID = "GRID";

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

        model.setLoserListener(this);
        startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences pref = getSharedPreferences(getLocalClassName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(CREATE_FLAG, CREATE);

        editor.putInt(TURN, model.getTurn());

        editor.putBoolean(ACTIVE_FLAG, model.isActive());
        editor.putInt(ACTIVE_ROW, model.getActiveCheckerRow());
        editor.putInt(ACTIVE_COLUMN, model.getActiveCheckerColumn());

        editor.putBoolean(COMBO_FLAG, model.isAttackStreak());
        editor.putInt(COMBO_ROW, model.getComboAttackerRow());
        editor.putInt(COMBO_COLUMN, model.getComboAttackerColumn());

        int[][] grid = model.getGrid();

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                editor.putInt(GRID + "_" + i + "_" + j, grid[i][j]);
            }
        }

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        model.setLoserListener(this);

        SharedPreferences pref = getSharedPreferences(getLocalClassName(), Context.MODE_PRIVATE);
        int create = pref.getInt(CREATE_FLAG, NOT_CREATE);
        if (create == NOT_CREATE) {
            startGame();
            return;
        }

        model.setTurn(pref.getInt(TURN, GameModel.WHITE));

        model.setActive(pref.getBoolean(ACTIVE_FLAG, false));
        model.setActiveCheckerRow(pref.getInt(ACTIVE_ROW, -1));
        model.setActiveCheckerColumn(pref.getInt(ACTIVE_COLUMN, -1));

        model.setAttackStreak(pref.getBoolean(COMBO_FLAG, false));
        model.setComboAttackerRow(pref.getInt(COMBO_ROW, -1));
        model.setComboAttackerColumn(pref.getInt(COMBO_COLUMN, -1));

        int[][] grid = new int[GameModel.GRID_DIMENSION][GameModel.GRID_DIMENSION];

        for (int i = 0; i < GameModel.GRID_DIMENSION; i++) {
            for (int j = 0; j < GameModel.GRID_DIMENSION; j++) {
                grid[i][j] = pref.getInt(GRID + "_" + i + "_" + j, GameModel.NONE);
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

        if (id == R.id.action_whose_turn) {
            whoseTurn();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void losing() {
        int loser = model.getTurn();
        String l = getString(R.string.losing);

        if (loser == GameModel.WHITE)
            l = getString(R.string.white) + l;
        else
            l = getString(R.string.black) + l;

        Toast toast = Toast.makeText(getApplicationContext(), l, Toast.LENGTH_SHORT);
        toast.show();

        startGame();
    }

    private void whoseTurn() {
        int loser = model.getTurn();
        String l;

        if (loser == GameModel.WHITE)
            l = getString(R.string.white);
        else
            l = getString(R.string.black);

        Toast toast = Toast.makeText(getApplicationContext(), l, Toast.LENGTH_SHORT);
        toast.show();
    }
}
