package com.cht.network.monitoring.web.rest.vm;

import com.cht.network.monitoring.dto.DomesticCircuitDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public class DeviceConfigurationVM {

    public static class FindDiffRes {

        @Schema(description = "response參數1: 查詢結果清單 包含項次(rowCount)、素材名稱(name)、素材描述(description)、最新版次(maxResVerCreated)、最新版本日期(maxResVer)")
        private String diffString;

        public String getDiffString() {
            return diffString;
        }

        public void setDiffString(String diffString) {
            this.diffString = diffString;
        }
    }
}
