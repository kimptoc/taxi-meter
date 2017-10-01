package uk.co.epsilontechnologies.taximeter;

import org.joda.time.DateTime;
import uk.co.epsilontechnologies.taximeter.calculator.FareCalculator;
import uk.co.epsilontechnologies.taximeter.model.Fare;
import uk.co.epsilontechnologies.taximeter.tariff.*;
import uk.co.epsilontechnologies.taximeter.utils.CalendarUtils;
import uk.co.epsilontechnologies.taximeter.utils.Log;

import java.math.BigDecimal;

import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.*;

/**
 * <p>API for a the meter of a TfL Taxi. This exposes the key features of a Taxi Journey's interaction with the Taxi Meter.
 *
 * @author Shane Gibson
 */
public class TflTaxiMeter implements Runnable, TaxiMeter {

    /**
     * The poller that will be used to update the fare consistently during the journey.
     */
    private final Poller poller;

    /**
     * The calculator that will be used to calculate the fare consistently during the journey.
     */
    private final FareCalculator fareCalculator;

    /**
     * The odometer that will be used to determine the distance travelled during the journey.
     */
    private final Odometer odometer;

    /**
     * The fare at any given point of the journey.
     */
    protected Fare fare;

    /**
     * The start time fo the journey.
     */
    protected DateTime startTime;

    /**
     * The end time fo the journey.
     */
    protected DateTime endTime;

    /**
     * Our timepiece
     */
    protected Clock clock = new Clock();

    /**
     * Constructs the Taxi Meter for the given Odometer, using the standard TariffLookup (with Tariff1, Tariff2 and
     * Tariff3) and standard Poller.
     *
     * @param odometer the odometer to use
     */
    public TflTaxiMeter(final Odometer odometer) {
        this(new Poller(), new FareCalculator(
                new TariffLookup(
                        new GenericTariff("2.40","254.6", "54.8", "17.20", "127.3", "27.4", "0.20", "89.2", "19.2", "0.20",
                                new TariffTimeFilter() {
                            @Override
                            public boolean applies(DateTime dateTime) {
                                return isWeekday(dateTime) && isBetweenHours(dateTime, 6, 20) && !CalendarUtils.isPublicHoliday(dateTime);
                            }
                        }),
                        new GenericTariff("2.40","206.8", "44.4", "20.80", "103.4", "22.2", "0.20", "89.2", "19.2", "0.20",
                                new TariffTimeFilter() {
                                    @Override
                                    public boolean applies(DateTime dateTime) {
                                        return ((isWeekday(dateTime) && isBetweenHours(dateTime, 20, 22)) || (isWeekend(dateTime) && isBetweenHours(dateTime, 6, 22)))
                                                && !CalendarUtils.isPublicHoliday(dateTime);
                                    }
                                }),
                        new GenericTariff("2.40","166.8", "35.8", "25.20", "83.4", "17.9", "0.20", "89.2", "19.2", "0.20",
                                new TariffTimeFilter() {
                                    @Override
                                    public boolean applies(DateTime dateTime) {
                                        return dateTime.getHourOfDay() < 6 || dateTime.getHourOfDay() >= 22 || CalendarUtils.isPublicHoliday(dateTime);
                                    }
                                })
                )),
                odometer);
    }

    /**
     * Constructs the Taxi Meter for the given Poller, Fare Calculator and Odometer.
     *
     * @param poller the poller to use
     * @param fareCalculator the fare calculator to use
     * @param odometer the odometer to use
     */
    protected TflTaxiMeter(final Poller poller, final FareCalculator fareCalculator, final Odometer odometer) {
        this.poller = poller;
        this.fareCalculator = fareCalculator;
        this.odometer = odometer;
    }

    /**
     * @see TaxiMeter#startJourney()
     */
    @Override
    public void startJourney() {
        if (startTime != null || endTime != null) {
            throw new IllegalStateException("Journey already in progress");
        }
        this.odometer.reset();
        this.startTime = clock.getNow();
        this.poller.start(this);
        this.fare = fareCalculator.getFlagFall(startTime);
    }

    /**
     * @see TaxiMeter#endJourney()
     */
    @Override
    public void endJourney() {
        if (startTime == null || endTime != null) {
            throw new IllegalStateException("Journey not in progress");
        }
        this.poller.stop();
    }

    /**
     * @see TaxiMeter#reset()
     */
    @Override
    public void reset() {
        if (startTime != null && endTime == null) {
            throw new IllegalStateException("Journey still in progress");
        }
        this.endTime = null;
        this.startTime = null;
        this.fare = null;
        this.odometer.reset();
    }

    /**
     * @see TaxiMeter#getFare()
     */
    @Override
    public BigDecimal getFare() {
        if (fare != null) {
            return fare.getAmount();
        }
        return null;
    }

    /**
     * Updates the fare according to the current time and / or distance.
     */
    @Override
    public void run() {
        try {
            final DateTime now = clock.getNow();
            BigDecimal duration = differenceInSeconds(now, startTime);
            BigDecimal distance = odometer.getDistance();
            this.fare = fareCalculator.calculateFare(fare, duration, distance, now);
//            Log.info(this.toString()+":Recalculating fare: "+fare.getAmount()+", elapsed seconds:"+duration+", distance:"+distance);
        } catch (Exception e) {
            Log.exception(e);
        }
    }

}