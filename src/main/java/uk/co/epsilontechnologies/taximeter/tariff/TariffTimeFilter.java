package uk.co.epsilontechnologies.taximeter.tariff;

import org.joda.time.DateTime;

public interface TariffTimeFilter {

    boolean applies(final DateTime dateTime);

}
