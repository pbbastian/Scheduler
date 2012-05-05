package softwarehuset.scheduler.domain;

public enum Status {
    ONGOING,
    PAUSED,
    COMPLETED,
    CANCELED;

    public String toString() {
        if (this.equals(ONGOING))
            return "Ongoing";
        else if (this.equals(PAUSED))
            return "Paused";
        else if (this.equals(COMPLETED))
            return "Completed";
        else if (this.equals(CANCELED))
            return "Canceled";
        return super.toString();
    }
}
