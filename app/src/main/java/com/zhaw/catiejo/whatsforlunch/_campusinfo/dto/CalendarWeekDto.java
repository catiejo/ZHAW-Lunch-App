package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

/**
 * Data Transfer Object to transmit a CalendarWeek over the wire.
 */
public final class CalendarWeekDto implements Comparable<CalendarWeekDto> {

    private final int year;

    private final int week;

    @JsonCreator
    public CalendarWeekDto(@JsonProperty("year") int year, @JsonProperty("week") int week) {
        this.year = year;
        this.week = week;
    }

    /**
     * Gets the year of the calendar week.
     *
     * @return The year of the calendar week.
     */
    public int getYear() {
        return year;
    }

    /**
     * Gets the week of the calendar week.
     *
     * @return The week of the calendar week.
     */
    public int getWeek() {
        return week;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("year", year).
                add("week", week).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(year, week);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final CalendarWeekDto other = (CalendarWeekDto) obj;

        return Objects.equal(this.year, other.year) &&
                Objects.equal(this.week, other.week);
    }

    @Override
    public int compareTo(CalendarWeekDto that) {
        return ComparisonChain.start()
                .compare(this.year, that.year)
                .compare(this.week, that.week).result();
    }
}
