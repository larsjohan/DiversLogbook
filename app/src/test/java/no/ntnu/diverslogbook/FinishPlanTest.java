package no.ntnu.diverslogbook;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;


/**
 * Tests functions in the FinishPLan activity.
 */
public class FinishPlanTest extends FinishPlan {


    /**
     * Tests the function that validates input data.
     */
    @Test
    public void allInputDataIsValid() {
        String depth = "20";
        String tankPressure = "200";

        assertTrue(allInputDataIsValid(depth, tankPressure));
    }
}
