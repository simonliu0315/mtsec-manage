package com.cht.network.monitoring.web.rest.vm;

import com.cht.network.monitoring.dto.DomesticCircuitDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public class DomesticCircuitVM {

    @Schema(description = "素材管理查詢任務Request" +
            "目的: 依據素材名稱、素材描述，進行素材查詢，顯示於素材清單列表 (應含未刪除註記、最新版次、最新版本日期)" +
            "列表包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)"
    )
    public static class FindAllReq {
        @Schema(description = "request參數1: 輸入之關鍵字")
        private String filter;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }
    }

    @Schema(description = "素材管理查詢任務Request" +
            "目的: 依據素材名稱、素材描述，進行素材查詢，顯示於素材清單列表 (應含未刪除註記、最新版次、最新版本日期)" +
            "列表包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)"
    )
    public static class FindAllRes {

        @Schema(description = "response參數1: 查詢結果清單 包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)")
        private Page<DomesticCircuitDto> domesticCircuitDto;

        public Page<DomesticCircuitDto> getDomesticCircuitDto() {
            return domesticCircuitDto;
        }

        public void setDomesticCircuitDto(Page<DomesticCircuitDto> domesticCircuitDto) {
            this.domesticCircuitDto = domesticCircuitDto;
        }
    }

    public static class FindEventReq {
        @Schema(description = "request參數1: 輸入之關鍵字")
        private String filter;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

    }
    public static class FindEventRes {

        private int normalCnt;

        private int minorCnt;

        private int criticalCnt;

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

        public int getCriticalCnt() {
            return criticalCnt;
        }

        public void setCriticalCnt(int criticalCnt) {
            this.criticalCnt = criticalCnt;
        }
    }

    public static class FindEventHistoryReq {
        @Schema(description = "request參數1: 輸入之關鍵字")
        private String filter;

        private int timeInterval;

        public String getFilter() {
            return filter;
        }

        public void setFilter(String filter) {
            this.filter = filter;
        }

        public int getTimeInterval() {
            return timeInterval;
        }

        public void setTimeInterval(int timeInterval) {
            this.timeInterval = timeInterval;
        }
    }
    public static class FindEventHistoryRes {

        private String[] checkTime;

        private int[] normalCnt;

        private int[] minorCnt;

        private int[] criticalCnt;

        public String[] getCheckTime() {
            return checkTime;
        }

        public void setCheckTime(String[] checkTime) {
            this.checkTime = checkTime;
        }

        public int[] getNormalCnt() {
            return normalCnt;
        }

        public void setNormalCnt(int[] normalCnt) {
            this.normalCnt = normalCnt;
        }

        public int[] getMinorCnt() {
            return minorCnt;
        }

        public void setMinorCnt(int[] minorCnt) {
            this.minorCnt = minorCnt;
        }

        public int[] getCriticalCnt() {
            return criticalCnt;
        }

        public void setCriticalCnt(int[] criticalCnt) {
            this.criticalCnt = criticalCnt;
        }
    }
}
