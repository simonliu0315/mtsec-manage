package com.cht.network.monitoring.rest.vm;

import java.util.Arrays;
import java.util.Date;

public class AlertVM {

    private String version;

    private String groupKey;

    private int truncatedAlerts;

    private String status;

    private String receiver;

    private Object groupLabels;

    private Object commonLabels;

    private Object commonAnnotations;

    private String externalURL;
    private alerts[] alerts;

    public static class alerts {
        private String status;

        private Object labels;

        private Object annotations;

        private Date startsAt;

        private Date endsAt;

        private String generatorURL;

        private String fingerprint;
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Object getLabels() {
            return labels;
        }

        public void setLabels(Object labels) {
            this.labels = labels;
        }

        public Object getAnnotations() {
            return annotations;
        }

        public void setAnnotations(Object annotations) {
            this.annotations = annotations;
        }

        public Date getStartsAt() {
            return startsAt;
        }

        public void setStartsAt(Date startsAt) {
            this.startsAt = startsAt;
        }

        public Date getEndsAt() {
            return endsAt;
        }

        public void setEndsAt(Date endsAt) {
            this.endsAt = endsAt;
        }

        public String getGeneratorURL() {
            return generatorURL;
        }

        public void setGeneratorURL(String generatorURL) {
            this.generatorURL = generatorURL;
        }

        public String getFingerprint() {
            return fingerprint;
        }

        public void setFingerprint(String fingerprint) {
            this.fingerprint = fingerprint;
        }

        @Override
        public String toString() {
            return "alerts{" +
                    "status='" + status + '\'' +
                    ", labels=" + labels +
                    ", annotations=" + annotations +
                    ", startsAt=" + startsAt +
                    ", endsAt=" + endsAt +
                    ", generatorURL='" + generatorURL + '\'' +
                    ", fingerprint='" + fingerprint + '\'' +
                    '}';
        }
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(String groupKey) {
        this.groupKey = groupKey;
    }

    public int getTruncatedAlerts() {
        return truncatedAlerts;
    }

    public void setTruncatedAlerts(int truncatedAlerts) {
        this.truncatedAlerts = truncatedAlerts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AlertVM.alerts[] getAlerts() {
        return alerts;
    }

    public void setAlerts(AlertVM.alerts[] alerts) {
        this.alerts = alerts;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public Object getGroupLabels() {
        return groupLabels;
    }

    public void setGroupLabels(Object groupLabels) {
        this.groupLabels = groupLabels;
    }

    public Object getCommonLabels() {
        return commonLabels;
    }

    public void setCommonLabels(Object commonLabels) {
        this.commonLabels = commonLabels;
    }

    public Object getCommonAnnotations() {
        return commonAnnotations;
    }

    public void setCommonAnnotations(Object commonAnnotations) {
        this.commonAnnotations = commonAnnotations;
    }

    public String getExternalURL() {
        return externalURL;
    }

    public void setExternalURL(String externalURL) {
        this.externalURL = externalURL;
    }

    @Override
    public String toString() {
        return "AlertVM{" +
                "version='" + version + '\'' +
                ", groupKey='" + groupKey + '\'' +
                ", truncatedAlerts=" + truncatedAlerts +
                ", status='" + status + '\'' +
                ", receiver='" + receiver + '\'' +
                ", groupLabels=" + groupLabels +
                ", commonLabels=" + commonLabels +
                ", commonAnnotations=" + commonAnnotations +
                ", externalURL='" + externalURL + '\'' +
                ", alerts=" + Arrays.toString(alerts) +
                '}';
    }
}
