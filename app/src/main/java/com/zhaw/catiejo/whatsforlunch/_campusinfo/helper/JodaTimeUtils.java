package com.zhaw.catiejo.whatsforlunch._campusinfo.helper;

import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import org.joda.time.DateMidnight;
import org.joda.time.DateTimeZone;
import org.joda.time.base.BaseDateTime;

/**
 * Various utilities for comparing and manipulating dates and times.
 */
public final class JodaTimeUtils {

    private JodaTimeUtils() {
    }

    /**
     * Tests whether both dates are on the same day.
     *
     * @param lhs      First date to compare.
     * @param rhs      Second date to compare.
     * @return True if both are on the same day, false otherwise.
     */
    public static boolean onSameDay(BaseDateTime lhs, BaseDateTime rhs) {
        if (lhs == null || rhs == null) {
            return false;
        }
        rhs = rhs.toDateTime(lhs.getZone());

        int result = ComparisonChain.start()
                .compare(lhs.getYear(), rhs.getYear())
                .compare(lhs.getMonthOfYear(), rhs.getMonthOfYear())
                .compare(lhs.getDayOfMonth(), rhs.getDayOfMonth())
                .result();

        return (result == 0);
    }

    /**
     * {@link Predicate} that tests whether the passed {@link DateMidnight} is today in comparison to {@code refDate}
     * (in the {@link DateTimeZone} of {@code refDate}).
     *
     * @param refDate Reference date used for the comparison.
     * @return Predicate
     */
    public static Predicate<BaseDateTime> onSameDay(final DateMidnight refDate) {
        return new com.google.common.base.Predicate<BaseDateTime>() {
            @Override
            public boolean apply(BaseDateTime dateTime) {
                return (dateTime.compareTo(refDate) > -1 &&
                        dateTime.compareTo(refDate.plusDays(1)) < 0);
            }
        };
    }

    /**
     * {@link Predicate} that tests whether the passed {@link DateMidnight} is on the next day in comparison to
     * {@code refDate} (in the {@link DateTimeZone} of {@code refDate}).
     *
     * @param refDate Reference date used for the comparison.
     * @return Predicate
     */
    public static Predicate<BaseDateTime> onNextDay(final DateMidnight refDate) {
        return new com.google.common.base.Predicate<BaseDateTime>() {
            @Override
            public boolean apply(BaseDateTime dateTime) {
                return (dateTime.compareTo(refDate.plusDays(1)) > -1 &&
                        dateTime.compareTo(refDate.plusDays(2)) < 0);
            }
        };
    }

    /**
     * {@link Predicate} that tests whether the passed {@link DateMidnight} was on the previous day in comparison to
     * {@code refDate} (in the {@link DateTimeZone} of {@code refDate}).
     *
     * @param refDate Reference date used for the comparison.
     * @return Predicate
     */
    public static Predicate<BaseDateTime> onPreviousDay(final DateMidnight refDate) {
        return new Predicate<BaseDateTime>() {
            @Override
            public boolean apply(BaseDateTime dateTime) {
                return (dateTime.compareTo(refDate) < 0 &&
                        dateTime.compareTo(refDate.minusDays(1)) > -1);
            }
        };
    }
}