package com.cht.network.monitoring.web.rest.vm;

import com.cht.network.monitoring.domain.Inventory;
import com.cht.network.monitoring.dto.InventoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public class InventoryVM extends InventoryDto {

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
        private Page<InventoryDto> inventoryDto;

        public Page<InventoryDto> getInventoryDto() {
            return inventoryDto;
        }

        public void setInventoryDto(Page<InventoryDto> inventoryDto) {
            this.inventoryDto = inventoryDto;
        }
    }

    public static class FindOneReq {

        @Schema(description = "")
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
    public static class FindOneRes {

        @Schema(description = "")
        private InventoryDto inventoryDto;

        public InventoryDto getInventoryDto() {
            return inventoryDto;
        }

        public void setInventoryDto(InventoryDto inventoryDto) {
            this.inventoryDto = inventoryDto;
        }
    }

    public static class UpdateOneReq extends Inventory {

    }

    public static class DeleteOneReq extends Inventory {

    }
}
