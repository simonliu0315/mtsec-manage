package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.domain.OperationTeam;
import com.cht.network.monitoring.domain.User;
import com.cht.network.monitoring.dto.OperationTeamDto;
import com.cht.network.monitoring.dto.UserDto;
import com.cht.network.monitoring.security.SecurityUtils;
import com.cht.network.monitoring.service.UserService;
import com.cht.network.monitoring.web.rest.vm.OperationTeamVM;
import com.cht.network.monitoring.web.rest.vm.UserMaintenanceVM;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserMaintenance", description = "使用者資料維護")
@RestController
@PreAuthorize("hasAnyAuthority('AUTH_UserMaintenance')")
@RequestMapping("api/userMaintenance")
public class UserMaintanceResource {

    private static final Logger log = LoggerFactory.getLogger(UserMaintanceResource.class);

    private final UserService userService;

    public UserMaintanceResource(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "修改使用者")
    @PostMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<UserMaintenanceVM.FindOneResp> updateUserMaintenanceUser(@Valid @RequestBody UserMaintenanceVM.UpdateOneReq updateOneReq,
                                                                                                                HttpServletResponse response) {

        log.info("updateUserMaintenanceUser {}", updateOneReq);
        User user = userService.save(updateOneReq.getId(),
                updateOneReq.getUserId(),
                updateOneReq.getUsername(),
                updateOneReq.getEmail(),
                updateOneReq.getRole().name());
        UserMaintenanceVM.FindOneResp resp = new UserMaintenanceVM.FindOneResp();
        resp.setUserDto(null);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "刪除資產")
    @PostMapping(value = "/delete", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<UserMaintenanceVM.FindOneResp> deleteUserMaintenanceOneUser(@Valid @RequestBody UserMaintenanceVM.DeleteOneReq deleteOneReq,
                                                                                                                   HttpServletResponse response) {

        log.info("deleteUserMaintenanceOneUser {}", deleteOneReq);
        userService.delete(deleteOneReq.getId());
        UserMaintenanceVM.FindOneResp resp = new UserMaintenanceVM.FindOneResp();
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "取得所有的使用者")
    @PostMapping(value = "/find/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<UserMaintenanceVM.FindAllResp> findUserMaintenanceAllUser(@Valid @RequestBody UserMaintenanceVM.FindAllReq findAllReq,
                                                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {
        log.info("findUserMaintenanceAllUser: {}, page: {}", findAllReq.getFilter(), page);
        log.info("---> {}", SecurityUtils.getCurrentUser());
        log.info("---> {}", SecurityUtils.getAuthority());
        Page<UserDto> findAllPage = userService.findAll(findAllReq.getFilter(), page);
        UserMaintenanceVM.FindAllResp resp = new UserMaintenanceVM.FindAllResp();
        resp.setUserDto(findAllPage);
        return ResponseEntity.ok().body(resp);
    }

    @Operation(summary = "取得單一資產")
    @PostMapping(value = "/find/one", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<UserMaintenanceVM.FindOneResp> findUserMaintenanceOneUser(@Valid @RequestBody UserMaintenanceVM.FindOneReq findOneReq,
                                                                                                                 HttpServletResponse response) {

        log.info("findOneRes {}", findOneReq.getId());
        UserDto dto = userService.findOne(Long.parseLong(findOneReq.getId()), "");
        UserMaintenanceVM.FindOneResp resp = new UserMaintenanceVM.FindOneResp();
        resp.setUserDto(dto);
        return ResponseEntity.ok().body(resp);
    }
}
