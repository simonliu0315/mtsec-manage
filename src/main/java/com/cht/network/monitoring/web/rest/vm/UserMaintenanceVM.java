package com.cht.network.monitoring.web.rest.vm;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.dto.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.domain.Page;

public class UserMaintenanceVM extends UserDto {

    @Schema(description = "使用者查詢Request 目的: 依據關鍵字搜尋，顯示於列表清單"
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

    @Schema(description = "使用者查詢Response" + "目的: 依據關鍵字搜尋，顯示於列表清單。" +
            "列表包含姓名(name)、公司名稱(company)、職位(jobTitle)、手機號碼(mobile)、電話號碼(telephone)、" +
            "傳真(fax)、電子郵件(email)、備註(remark)"
    )
    public static class FindAllResp {

        @Schema(description = "response參數1: 查詢結果清單 包含")
        private Page<UserDto> userDto;

        public Page<UserDto> getUserDto() {
            return userDto;
        }

        public void setUserDto(Page<UserDto> userDto) {
            this.userDto = userDto;
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

    public static class FindOneResp {

        @Schema(description = "")
        private UserDto userDto;

        public UserDto getUserDto() {
            return userDto;
        }

        public void setUserDto(UserDto userDto) {
            this.userDto = userDto;
        }
    }

    public static class UpdateOneReq extends UserDto {

    }

    public static class DeleteOneReq extends UserDto {

    }
}
