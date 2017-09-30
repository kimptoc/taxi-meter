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
 * <p>For any hiring either during Monday to Friday between 20:00 and 22:00 or during Saturday or Sunday between 06:00 and 22:00, other than on a public holiday:
 *
 * <ul>
 *  <li>For the first 206.8 metres or 44.4 seconds (whichever is reached first) there is a minimum charge of £2.40
 *  <li>For each additional 103.4 metres or 22.2 seconds (whichever is reached first), or part thereof, if the fare is less than £20.80 there is a charge of 20p
 *  <li>Once the fare is £20.80 or greater then there is a charge of 20p for each additional 89.2 metres or 19.2 seconds (whichever is reached first), or part thereof
 * </ul>
 *
 * @author Shane Gibson
 */
public class Tariff2 extends GenericTariff {

    public Tariff2() {
        super("2.40","206.8", "44.4", "20.80", "103.4", "22.2", "0.20", "89.2", "19.2", "0.20",
                new TariffTimeFilter() {
                    @Override
                    public boolean applies(DateTime dateTime) {
                        return ((isWeekday(dateTime) && isBetweenHours(dateTime, 20, 22)) || (isWeekend(dateTime) && isBetweenHours(dateTime, 6, 22)))
                                && !CalendarUtils.isPublicHoliday(dateTime);
                    }
                });
    }
}