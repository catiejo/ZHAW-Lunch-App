package com.zhaw.catiejo.whatsforlunch.mensadata;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

/**
 * Result of a operation performed by the {@link ICateringRemoteFacade}.
 *
 * @param <T> Type of the resulting payload that is returned by the operation.
 */
public class CateringRemoteFacadeResult<T> {

    private final Outcome outcome;
    private final Optional<T> result;

    public CateringRemoteFacadeResult(Outcome outcome, Optional<T> result) {
        this.outcome = outcome;
        this.result = result;
    }

    /**
     * @return The outcome of the operation.
     */
    public Outcome getOutcome() {
        return outcome;
    }

    /**
     * @return The resulting payload that is returned by the operation (if any).
     */
    public Optional<T> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("outcome", outcome).
                add("result", result).
                toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(outcome, result);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final CateringRemoteFacadeResult other = (CateringRemoteFacadeResult) obj;

        return Objects.equal(this.outcome, other.outcome) &&
                Objects.equal(this.result, other.result);
    }

    /**
     * Defines the possible outcomes of a operation performed by the {@link ICateringRemoteFacade}.
     */
    public enum Outcome {
        /**
         * The operation was successful, the resulting payload is present (if any).
         */
        SUCCESS,

        /**
         * The operation was successful, but the requested information could not be found. Therefore, the resulting
         * payload is not present.
         */
        NOT_FOUND,

        /**
         * The operation was not successful, the resulting payload is not present.
         */
        ERROR
    }
}
