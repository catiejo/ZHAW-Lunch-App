package com.zhaw.catiejo.whatsforlunch;
import org.joda.time.LocalDate;
import java.io.Serializable;
import static com.google.common.base.Preconditions.checkNotNull;

public class MensaContainer implements Serializable {
    private final LocalDate day;
    private final long facilityId;
    private final String name;

    public MensaContainer(final LocalDate day, final long facilityId, final String name) {
        this.day = checkNotNull(day);
        this.facilityId = checkNotNull(facilityId);
        this.name = checkNotNull(name);
    }

    public String getName() {
        return name;
    }
    public long getFacilityId() { return facilityId; }
    public LocalDate getDay() { return day; }
}
