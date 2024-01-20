package pt.ipleiria.estg.dei.ei.dae.smartpackagemanagement.utils;

import java.util.Date;

public class DateUtil {
    public static Date[] parseDates(Long startTime, Long endTime) {
        Date startDate = null;
        Date endDate = null;

        if (startTime != null) {
            startDate = new Date(startTime);
        }

        if (endTime != null) {
            endDate = new Date(endTime);
        }

        return new Date[]{startDate, endDate};
    }
}
