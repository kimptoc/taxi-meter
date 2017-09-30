package uk.co.epsilontechnologies.taximeter.tariff;

import org.joda.time.DateTime;
import uk.co.epsilontechnologies.taximeter.utils.CalendarUtils;

import java.math.BigDecimal;

import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.isBetweenHours;
import static uk.co.epsilontechnologies.taximeter.utils.CalendarUtils.isWeekday;

/**
 * @see Tariff
 *
 * <p>For any hiring during Monday to Friday other than on a public holiday between 06:00 and 20:00:
 *
 * <ul>
 *  <li>For the first 254.6 metres or 54.8 seconds (whichever is reached first) there is a minimum charge of £2.40
 *  <li>For each additional 127.3 metres or 27.4 seconds (whichever is reached first), or part thereof, if the fare is less than £17.20 then there is a charge of 20p
 *  <li>Once the fare is £17.20 or greater then there is a charge of 20p for each additional 89.2 metres or 19.2 seconds (whichever is reached first), or part thereof
 * </ul>
 *
 * @author Shane Gibson
 */
public class Tariff1 extends GenericTariff {

    public Tariff1() {
        super("2.40","254.6", "54.8", "17.20", "127.3", "27.4", "0.20", "89.2", "19.2", "0.20",
                new TariffTimeFilter() {
                    @Override
                    public boolean applies(DateTime dateTime) {
                        return isWeekday(dateTime) && isBetweenHours(dateTime, 6, 20) && !CalendarUtils.isPublicHoliday(dateTime);
                    }
                });
    }
}