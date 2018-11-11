package com.zhaw.catiejo.whatsforlunch._campusinfo.sync;

/**
 * Describes the current state of the catering information.
 */
public enum CateringInformationState {
    /**
     * Data is being downloaded. Start state.
     */
    DOWNLOAD_IN_PROGRESS(State.NORMAL_STATE),

    /**
     * Data is available. Accept state.
     */
    AVAILABLE(State.ACCEPT_STATE),

    /**
     * Download of requested data failed. Error state.
     */
    DOWNLOAD_FAILED(State.ERROR_STATE);

    private final State state;

    CateringInformationState(State state) {
        this.state = state;
    }

    /**
     * @return true if this is an accept state, false otherwise.
     */
    public boolean isAcceptState() {
        return (state == State.ACCEPT_STATE);
    }

    /**
     * @return true if this is an error state, false otherwise.
     */
    public boolean isErrorState() {
        return (state == State.ERROR_STATE);
    }

    private enum State {
        NORMAL_STATE, ACCEPT_STATE, ERROR_STATE
    }
}
