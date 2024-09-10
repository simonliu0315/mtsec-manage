package com.cht.network.monitoring.web.rest;

import com.cht.network.monitoring.web.rest.vm.DomesticCircuitVM;
import com.cht.network.monitoring.web.rest.vm.DeviceConfigurationVM;
import com.github.difflib.DiffUtils;
import com.github.difflib.UnifiedDiffUtils;
import com.github.difflib.patch.Patch;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("deviceConfiguration")
public class DeviceConfigurationResource {

    private static final Logger log = LoggerFactory.getLogger(DeviceConfigurationResource.class);

    @Operation(summary = "取得素材清單")
    @PostMapping(value = "/find/diff", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ResponseEntity<DeviceConfigurationVM.FindDiffRes> findDiff(@Valid @RequestBody DomesticCircuitVM.FindAllReq findAllReq,
                                                                                 @ParameterObject Pageable page, HttpServletResponse response) {

        log.info("findAllRes {}  {}", findAllReq.getFilter(),page);

        DeviceConfigurationVM.FindDiffRes res = new DeviceConfigurationVM.FindDiffRes();
        List<String> text1= Arrays.asList("this is a test","a test");
        List<String> text2= Arrays.asList("this is a testfile","a test");

        text1= Arrays.asList("a test","a testA", "a test","a test", "a test","a test");
        text2= Arrays.asList("a test","a test", "a test","a test", "a testB","a test");
        //text1= Arrays.asList("this is a test","a test", "this is a test","a test", "this is a test","a test", "this is a test","a test");
        //text2= Arrays.asList("this is a testfile","a test", "this is a testfile","a test", "this is a testfile","a test","this is a testfile","a test", "this is a testfile","a test");


        //generating diff information.
        Patch<String> diff = DiffUtils.diff(text1, text2);

        //generating unified diff format
        List<String> unifiedDiff = UnifiedDiffUtils.generateUnifiedDiff("original-file.txt", "new-file.txt", text1, diff, 3);

        unifiedDiff.forEach(System.out::println);
        StringBuilder diffText = new StringBuilder();
        unifiedDiff.forEach(diffStr -> {
            log.info("{}", diffStr);
            diffText.append(diffStr + "\n");
        });

        //importing unified diff format from file or here from memory to a Patch
        Patch<String> importedPatch = UnifiedDiffUtils.parseUnifiedDiff(unifiedDiff);


        try{
            //apply patch to original list
            List<String> patchedText = DiffUtils.patch(text1, importedPatch);
            log.info("patchedText {}", patchedText);

        } catch(Exception e) {
            log.error("{}", e);
        }


        res.setDiffString(diffText.toString());
        return ResponseEntity.ok().body(res);
    }
}
