package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Data Transfer Object to transmit a Location over the wire.
 */
public class LocationDto {

    private final long id;

    private final String name;

    private final String version;

    @JsonCreator
    public LocationDto(@JsonProperty("id") long id,
                       @JsonProperty("name") String name,
                       @JsonProperty("version") String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    /**
     * Gets the ID of this Location (unique, issued by remote database).
     *
     * @return The ID of this Location.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of the Location (like "Winterthur").
     *
     * @return The name of the Location.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the version of the Location.
     *
     * @return The version of this Location.
     */
    public String getVersion() {
        return version;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, version);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final LocationDto other = (LocationDto) obj;

        return Objects.equal(this.id, other.id) &&
                Objects.equal(this.name, other.name) &&
                Objects.equal(this.version, other.version);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("id", id).
                add("name", name).
                add("version", version).
                toString();
    }
}
