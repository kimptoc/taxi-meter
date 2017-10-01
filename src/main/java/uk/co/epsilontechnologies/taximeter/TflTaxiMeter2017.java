package uk.co.epsilontechnologies.taximeter;

import org.joda.time.DateTime;
import uk.co.epsilontechnologies.taximeter.calculator.FareCalculator;
import uk.co.epsilontechnologies.taximeter.tariff.GenericTariff;
import uk.co.epsilontechnologies.taximeter.tariff.TariffLookup;
import uk.co.epsilontechnologies.taximeter.tariff.TariffTimeFilter;
import uk.co.epsilontechnologies.taximeter.utils.CalendarUtils;

import java.math.BigDecimal;

import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.*;

/**
 * <p>API for a the meter of a TfL Taxi. This exposes the key features of a Taxi Journey's interaction with the Taxi Meter.
 *
 * @author Shane Gibson
 */
public class TflTaxiMeter2017 extends TflTaxiMeter {

    public TflTaxiMeter2017(Odometer odometer, Clock overrideClock) {
        this(odometer);
        this.clock = overrideClock;
    }

    /**
     * Constructs the Taxi Meter for the given Odometer, using the standard TariffLookup (with Tariff1, Tariff2 and
     * Tariff3) and standard Poller.
     *
     * @param odometer the odometer to use
     */
    public TflTaxiMeter2017(final Odometer odometer) {
        super(new Poller(), new FareCalculator(
                new TariffLookup(
                        new GenericTariff("2.60","234.8", "50.4", "17.20", "117.4", "25.2", "0.20", "86.9", "18.7", "0.20",
                                new TariffTimeFilter() {
                                    @Override
                                    public boolean applies(DateTime dateTime) {
                                        return isWeekday(dateTime) && isBetweenHours(dateTime, 5, 20) && !CalendarUtils.isPublicHoliday(dateTime);
                                    }
                                }),
                        new GenericTariff("2.60","191.0", "41.0", "20.80", "95.5", "20.5", "0.20", "86.9", "18.7", "0.20",
                                new TariffTimeFilter() {
                                    @Override
                                    public boolean applies(DateTime dateTime) {
                                        return ((isWeekday(dateTime) && isBetweenHours(dateTime, 20, 22)) || (isWeekend(dateTime) && isBetweenHours(dateTime, 5, 22)))
                                                && !CalendarUtils.isPublicHoliday(dateTime);
                                    }
                                }),
                        new GenericTariff("2.60","162.4", "35.0", "25.20", "81.2", "17.5", "0.20", "86.9", "18.7", "0.20",
                                new TariffTimeFilter() {
                                    @Override
                                    public boolean applies(DateTime dateTime) {
                                        return dateTime.getHourOfDay() < 5 || dateTime.getHourOfDay() >= 22 || CalendarUtils.isPublicHoliday(dateTime);
                                    }
                                })
                )),
                odometer);
    }


}