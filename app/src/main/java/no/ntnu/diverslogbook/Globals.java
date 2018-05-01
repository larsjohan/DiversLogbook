package no.ntnu.diverslogbook;


/**
 * Contains a list of global variables.
 */
public class Globals {

    /**
     * Used for sending security stops with intent.
     */
    public static final String SECURITYSTOPS = "security_stops";

    /**
     * Used for sending a newly created plan with intent.
     */
    public static final String FINISH_PLAN_LOG = "finish_plan_diver";

    /**
     * Used for sending the logged in user with intent.
     */
    public static final String FINISH_PLAN_DIVER = "finish_plan_diver";

    /**
     * Used when checking the value of time since last dive and alcohol intake.
     */
    public static final String MORETHAN24H = "more_than_24_hours";

    /**
     * Used to verify if the diver is allowed to dive or not.
     */
    public static final int MAX_HOURS_SINCE_ALCOHOL = 12;
}
