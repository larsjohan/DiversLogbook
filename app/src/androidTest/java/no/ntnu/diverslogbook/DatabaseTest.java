package no.ntnu.diverslogbook;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.util.Database;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private Context context;

    private Diver diver;


    public DatabaseTest() {
        this.context = InstrumentationRegistry.getContext();

        this.diver = new Diver(
                "testID",
                "name",
                "email",
                "123455678",
                "/photo"
        );



        Database.init();
    }

    @Before
    public void setup(){
        FirebaseDatabase.getInstance().goOffline();
    }

    @Test
    public void setLoggedInDiverTest(){
        assertNull(Database.getLoggedInDiver());
        Database.setLoggedInDiver(this.diver);
        assertEquals(this.diver, Database.getLoggedInDiver());
    }


    @After
    public void teardown(){
        FirebaseDatabase.getInstance().goOnline();
    }

}
