package uk.co.epsilontechnologies.taximeter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.TimeZone;

import static junit.framework.Assert.assertEquals;

/**
 * Test Class for {@link TflTaxiMeter}.
 *
 * @author Shane Gibson
 */
public class TaxiMeter2017Test {

    private final int meterTickTime = 130;
    private TflTaxiMeter2017 underTest;

    private BigDecimal distanceTravelled = BigDecimal.ZERO;
    private DateTime currentTime = null;

    @Before
    public void setUp() {
        distanceTravelled = BigDecimal.ZERO;
        this.underTest = new TflTaxiMeter2017(new Odometer() {
            @Override
            public BigDecimal getDistance() {
                return distanceTravelled;
            }

            @Override
            public void reset() {
            }
        }, new ManualClock());
    }

    @Test
    public void shouldCalculateFareTariff1() throws InterruptedException {

        currentTime = new DateTime(2017,9, 29, 9,0,0, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/London")));
        underTest.startJourney();

        // wait for taxi meter to tick over
        Thread.sleep(meterTickTime);

        // arrange
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        distanceTravelled = new BigDecimal("234.0");
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        distanceTravelled = new BigDecimal("235.1");
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.80"), underTest.getFare() );


    }

    @Test
    public void shouldCalculateFareTariff1TimeBased() throws InterruptedException {

        currentTime = new DateTime(2017,9, 29, 9,0,0, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/London")));
        underTest.startJourney();

        // wait for taxi meter to tick over
        Thread.sleep(meterTickTime);

        // arrange
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        // 30 seconds later, no fare change
        currentTime = new DateTime(2017,9, 29, 9,0,30, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/London")));
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        // another 30 seconds, fare should go up
        currentTime = new DateTime(2017,9, 29, 9,1,0, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/London")));
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.80"), underTest.getFare() );


    }

    @Test
    public void shouldCalculateFareTariff2() throws InterruptedException {

        currentTime = new DateTime(2017,9, 29, 20,1,0, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/London")));
        underTest.startJourney();

        // wait for taxi meter to tick over
        Thread.sleep(meterTickTime);

        // arrange
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        distanceTravelled = new BigDecimal("190.5");
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        distanceTravelled = new BigDecimal("191.1");
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.80"), underTest.getFare() );


    }

    @Test
    public void shouldCalculateFareTariff3() throws InterruptedException {

        currentTime = new DateTime(2017,9, 29, 22,1,0, DateTimeZone.forTimeZone(TimeZone.getTimeZone("Europe/London")));
        underTest.startJourney();

        // wait for taxi meter to tick over
        Thread.sleep(meterTickTime);

        // arrange
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        distanceTravelled = new BigDecimal("162.3");
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.60"), underTest.getFare() );

        distanceTravelled = new BigDecimal("162.5");
        Thread.sleep(meterTickTime);
        assertEquals(new BigDecimal("2.80"), underTest.getFare() );


    }

    class ManualClock extends Clock {

        @Override
        public DateTime getNow() {
            return currentTime;
        }
    }
}

