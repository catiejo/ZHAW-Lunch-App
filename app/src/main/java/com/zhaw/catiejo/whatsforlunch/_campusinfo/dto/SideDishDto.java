package com.zhaw.catiejo.whatsforlunch._campusinfo.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Data Transfer Object to transmit a SideDish object over the wire.
 */
public class SideDishDto {

    private final long id;

    private final String name;

    private final String version;

    @JsonCreator
    public SideDishDto(@JsonProperty("id") long id,
                       @JsonProperty("name") String name,
                       @JsonProperty("version") String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    /**
     * Gets the ID of this SideDish (unique, assigned by the remote database).
     *
     * @return The ID of this SideDish.
     */
    public long getId() {
        return id;
    }

    /**
     * Gets the name of this SideDish (like "Kartoffelstock").
     *
     * @return The name of this SideDish.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the version of this SideDish.
     *
     * @return The Version of this SideDish.
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

        final SideDishDto other = (SideDishDto) obj;

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
