package com.cht.network.monitoring.service.icmp;

public class IcmpPingResponse {

    private boolean successFlag;
    private boolean timeoutFlag;
    private String errorMessage;
    private Throwable throwable;
    private String host;
    private int size;
    private int rtt;
    private int ttl;
    private long duration;
    private String command;
    private String output;
    private Statistics statistics;
    private RoundTripTimes roundTripTimes;

    public static class Statistics {
        private int received;
        private int sent;
        private int lost;

        public int getReceived() {
            return received;
        }

        public void setReceived(int received) {
            this.received = received;
        }

        public int getSent() {
            return sent;
        }

        public void setSent(int sent) {
            this.sent = sent;
        }

        public int getLost() {
            return lost;
        }

        public void setLost(int lost) {
            this.lost = lost;
        }

        @Override
        public String toString() {
            return "Statistics{" +
                    "received=" + received +
                    ", sent=" + sent +
                    ", lost=" + lost +
                    '}';
        }
    }

    public static class RoundTripTimes {
        private int minium;
        private int maxium;
        private int average;

        public int getMinium() {
            return minium;
        }

        public void setMinium(int minium) {
            this.minium = minium;
        }

        public int getMaxium() {
            return maxium;
        }

        public void setMaxium(int maxium) {
            this.maxium = maxium;
        }

        public int getAverage() {
            return average;
        }

        public void setAverage(int average) {
            this.average = average;
        }

        @Override
        public String toString() {
            return "RoundTripTimes{" +
                    "minium=" + minium +
                    ", maxium=" + maxium +
                    ", average=" + average +
                    '}';
        }
    }
    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public boolean isTimeoutFlag() {
        return timeoutFlag;
    }

    public void setTimeoutFlag(boolean timeoutFlag) {
        this.timeoutFlag = timeoutFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getRtt() {
        return rtt;
    }

    public void setRtt(int rtt) {
        this.rtt = rtt;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public RoundTripTimes getRoundTripTimes() {
        return roundTripTimes;
    }

    public void setRoundTripTimes(RoundTripTimes roundTripTimes) {
        this.roundTripTimes = roundTripTimes;
    }

    @Override
    public String toString() {
        return "IcmpPingResponse{" +
                "successFlag=" + successFlag +
                ", timeoutFlag=" + timeoutFlag +
                ", errorMessage='" + errorMessage + '\'' +
                ", throwable=" + throwable +
                ", host='" + host + '\'' +
                ", size=" + size +
                ", rtt=" + rtt +
                ", ttl=" + ttl +
                ", duration=" + duration +
                ", command='" + command + '\'' +
                ", output='" + output + '\'' +
                ", statistics=" + statistics +
                ", roundTripTimes=" + roundTripTimes +
                '}';
    }
}
