package uk.co.epsilontechnologies.taximeter.tariff;

import org.joda.time.DateTime;

import java.math.BigDecimal;

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
public class GenericTariff extends AbstractTariff {

    private final String minimumFare;
    private final String initialDistance;
    private final String initialTime;
    private final String lowFareDistance;
    private final String highLowFareBoundary;
    private final String lowFareTimeLimit;
    private final String lowFareIncrement;
    private final String highFareIncrement;
    private final String highFareDistance;
    private final String highFareTimeLimit;
    private TariffTimeFilter timeFilter;

    public GenericTariff(String minimumFare, String initialDistance, String initialTime, String highLowFareBoundary, String lowFareDistance, String lowFareTimeLimit, String lowFareIncrement, String highFareDistance, String highFareTimeLimit, String highFareIncrement, TariffTimeFilter timeFilter) {
        this.minimumFare = minimumFare;
        this.initialDistance = initialDistance;
        this.initialTime = initialTime;
        this.highLowFareBoundary = highLowFareBoundary;
        
        this.lowFareDistance = lowFareDistance;
        this.lowFareTimeLimit = lowFareTimeLimit;
        this.lowFareIncrement = lowFareIncrement;

        this.highFareDistance = highFareDistance;
        this.highFareTimeLimit = highFareTimeLimit;
        this.highFareIncrement = highFareIncrement;

        this.timeFilter = timeFilter;
    }

    /**
     * @see Tariff#applies(DateTime)
     */
    @Override
    public boolean applies(final DateTime dateTime) {
        return timeFilter.applies(dateTime);
    }

    /**
     * @see Tariff#getFlagFallAmount()
     */
    @Override
    public BigDecimal getFlagFallAmount() {
        return new BigDecimal(minimumFare);
    }

    /**
     * @see Tariff#getFlagFallDistanceLimit()
     */
    @Override
    public BigDecimal getFlagFallDistanceLimit() {
        return new BigDecimal(initialDistance);
    }

    /**
     * @see Tariff#getFlagFallTimeLimit()
     */
    @Override
    public BigDecimal getFlagFallTimeLimit() {
        return new BigDecimal(initialTime);
    }

    /**
     * @see Tariff#getHighLowFareBoundary()
     */
    @Override
    public BigDecimal getHighLowFareBoundary() {
        return new BigDecimal(highLowFareBoundary);
    }

    /**
     * @see Tariff#getLowFareSubTariff()
     */
    @Override
    public SubTariff getLowFareSubTariff() {
        return new SubTariff() {

            /**
             * @see SubTariff#getDistanceLimit()
             */
            @Override
            public BigDecimal getDistanceLimit() {
                return new BigDecimal(lowFareDistance);
            }

            /**
             * @see SubTariff#getTimeLimit()
             */
            @Override
            public BigDecimal getTimeLimit() {
                return new BigDecimal(lowFareTimeLimit);
            }

            /**
             * @see SubTariff#getIncrementAmount()
             */
            @Override
            public BigDecimal getIncrementAmount() {
                return new BigDecimal(lowFareIncrement);
            }

        };
    }

    /**
     * @see Tariff#getHighFareSubTariff()
     */
    @Override
    public SubTariff getHighFareSubTariff() {
        return new SubTariff() {

            /**
             * @see SubTariff#getIncrementAmount()
             */
            @Override
            public BigDecimal getIncrementAmount() {
                return new BigDecimal(highFareIncrement);
            }

            /**
             * @see SubTariff#getDistanceLimit()
             */
            @Override
            public BigDecimal getDistanceLimit() {
                return new BigDecimal(highFareDistance);
            }

            /**
             * @see SubTariff#getTimeLimit()
             */
            @Override
            public BigDecimal getTimeLimit() {
                return new BigDecimal(highFareTimeLimit);
            }

        };
    }

}