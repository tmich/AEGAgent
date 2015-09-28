package it.aeg2000srl.aegagent;

import android.content.Context;

import it.aeg2000srl.aegagent.infrastructure.DbHelper;

/**
 * Created by tiziano.michelessi on 28/09/2015.
 */
public class TestDbHelper extends DbHelper {
    public static String DATABASE_NAME = "agent_test.db";

    public TestDbHelper(Context context) {
        super(context, DATABASE_NAME);
    }
}
