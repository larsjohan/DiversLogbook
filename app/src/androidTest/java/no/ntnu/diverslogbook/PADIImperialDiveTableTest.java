package no.ntnu.diverslogbook;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import no.ntnu.diverslogbook.exceptions.TableScopeException;
import no.ntnu.diverslogbook.utils.DiveTable;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PADIImperialDiveTableTest {

    private Context context;

    private DiveTable table;

    @Before
    public void setup(){
        this.context = InstrumentationRegistry.getTargetContext();
        this.table = new DiveTable(this.context, DiveTable.Table.PADI_IMPERIAL);
    }

    @Test
    public void preSISaturationTest() {
        assertEquals("B", this.table.getPreSISaturationGroup(140, 1));
        assertEquals("D", this.table.getPreSISaturationGroup(130, 7));
        assertEquals("B", this.table.getPreSISaturationGroup(100, 4));
        assertEquals("A", this.table.getPreSISaturationGroup(12, 3));
        assertEquals("Z", this.table.getPreSISaturationGroup(30, 200));
    }

    @Test(expected = TableScopeException.class)
    public void preSIOutOfTableScopeTest(){
        this.table.getPreSISaturationGroup(140, 15);
    }

    @Test
    public void siSaturationTest() {
        assertEquals("A", this.table.getSISaturation("G", 5, 0));
        assertEquals("A", this.table.getSISaturation("G", 2, 0));
        assertEquals("G", this.table.getSISaturation("G", 0, 0));
        assertEquals("F", this.table.getSISaturation("G", 0, 10));
    }

    @Test(expected = TableScopeException.class)
    public void siOutOfScopeTest(){
        this.table.getSISaturation("G", -1, 0);
    }

    @Test
    public void continuedDiveSaturationTest(){
        String firstDiveSaturation = this.table.getPreSISaturationGroup(70, 30);
        assertEquals("O", firstDiveSaturation);

        String afterSISaturation = this.table.getSISaturation(firstDiveSaturation, 0, 40);
        assertEquals("H", afterSISaturation);

        assertEquals("K", this.table.getPostSISaturationGroup(afterSISaturation, 10, 40));
    }

    @Test(expected = TableScopeException.class)
    public void postSIOutOfScopeTest(){

        String firstDiveSaturation = this.table.getPreSISaturationGroup(70, 30);
        assertEquals("O", firstDiveSaturation);

        String afterSISaturation = this.table.getSISaturation(firstDiveSaturation, 0, 40);
        assertEquals("H", afterSISaturation);

        assertEquals("K", this.table.getPostSISaturationGroup(afterSISaturation, 10, -40));
    }
}
