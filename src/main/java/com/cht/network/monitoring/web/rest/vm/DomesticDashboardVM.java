package com.cht.network.monitoring.web.rest.vm;

import com.cht.network.monitoring.domain.EventLog;
import com.cht.network.monitoring.dto.DomesticCircuitDto;
import com.cht.network.monitoring.dto.EventLogDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

import java.util.List;

public class DomesticDashboardVM {


    public static class FindEventNotClosedReq {

        @Schema(description = "response參數1: 查詢結果清單 包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)")
        private String filter;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }
    }

    public static class FindEventNotClosedRes {

        @Schema(description = "response參數1: 查詢結果清單 包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)")
        private Page<EventLog> eventLog;

            private int criticalCnt;

            private int minorCnt;

            private int normalCnt;

        public Page<EventLog> getEventLog() {
            return eventLog;
        }

        public void setEventLog(Page<EventLog> eventLog) {
            this.eventLog = eventLog;
        }

        public int getCriticalCnt() {
            return criticalCnt;
        }

        public void setCriticalCnt(int criticalCnt) {
            this.criticalCnt = criticalCnt;
        }

        public int getMinorCnt() {
            return minorCnt;
        }

        public void setMinorCnt(int minorCnt) {
            this.minorCnt = minorCnt;
        }

        public int getNormalCnt() {
            return normalCnt;
        }

        public void setNormalCnt(int normalCnt) {
            this.normalCnt = normalCnt;
        }
    }
}
