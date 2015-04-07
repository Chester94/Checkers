package company.com.checkers.test;

import android.app.Application;
import android.test.ApplicationTestCase;

import company.com.checkers.GameModel;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void testActiveFalse_White() {
        GameModel game = new GameModel();
        game.startStep( 0, 0 );
        assertEquals( false, game.isActive() );
    }

    public void testActiveTrue_White() {
        GameModel game = new GameModel();
        game.startStep( 5, 0 );
        assertEquals( true, game.isActive() );
    }

    public void testActiveFalse_Black() {
        GameModel game = new GameModel();
        game.setTurn( GameModel.BLACK );
        game.startStep( 0, 0 );
        assertEquals( false, game.isActive() );
    }

    public void testActiveTrue_Black() {
        GameModel game = new GameModel();
        game.setTurn( GameModel.BLACK );
        game.startStep( 2, 1 );
        assertEquals( true, game.isActive() );
    }

    public void testAttack() {
        GameModel game = new GameModel();

        game.startStep( 5, 2 );
        game.stopStep( 4, 3 );

        game.startStep( 2, 1 );
        game.stopStep( 3, 2 );

        game.startStep( 4, 3 );
        game.stopStep( 2, 1 );

        assertEquals( GameModel.NONE, game.getGrid()[3][2] );
    }

    public void testError() {
        GameModel game = new GameModel();

        game.startStep( -5, -1 );
        game.stopStep( -5, -6 );

        game.stopStep( 0, 0 );
    }
}