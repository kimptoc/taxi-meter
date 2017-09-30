package uk.co.epsilontechnologies.taximeter.tariff;

import org.joda.time.DateTime;
import uk.co.epsilontechnologies.taximeter.utils.CalendarUtils;

import java.math.BigDecimal;

import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.isBetweenHours;
import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.isWeekday;
import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.isWeekend;

/**
 * @see Tariff
 *
 * <p>For any hiring between 22:00 on any day and 06:00 the following day or at any time on a public holiday:
 *
 * <ul>
 *  <li>For the first 166.8 metres or 35.8 seconds (whichever is reached first) there is a minimum charge of £2.40
 *  <li>For each additional 83.4 metres or 17.9 seconds (whichever is reached first), or part thereof, if the fare is less than £25.20 there is a charge of 20p
 *  <li>Once the fare is £25.20 or greater then there is a charge of 20p for each additional 89.2 metres or 19.2 seconds (whichever is reached first)
 * </ul>
 *
 * @author Shane Gibson
 */
public class Tariff3 extends GenericTariff {

    public Tariff3() {
        super("2.40","166.8", "35.8", "25.20", "83.4", "17.9", "0.20", "89.2", "19.2", "0.20",
                new TariffTimeFilter() {
                    @Override
                    public boolean applies(DateTime dateTime) {
                        return dateTime.getHourOfDay() < 6 || dateTime.getHourOfDay() >= 22 || CalendarUtils.isPublicHoliday(dateTime);
                    }
                });
    }
}