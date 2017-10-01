package uk.co.epsilontechnologies.taximeter;

import org.joda.time.DateTime;

/**
 * Something to tell the time from. Can be extended to allow us to change time!
 */
public class Clock {

    public DateTime getNow() {
        return new DateTime();
    }

}
