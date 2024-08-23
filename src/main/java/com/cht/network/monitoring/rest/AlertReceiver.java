package com.cht.network.monitoring.rest;

import com.cht.network.monitoring.rest.vm.AlertVM;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.*;



@RestController
@RequestMapping("alerting")
public class AlertReceiver {

    private static final Logger log = LoggerFactory.getLogger(AlertReceiver.class);

    String cpu_alert = "0";

    @RequestMapping(value = "/receiver", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<Object> findAllRes(@RequestBody AlertVM vm, HttpServletRequest request) {
        log.info("==========================================");
        log.info("收到告警通知!!");
        log.info("這裡可以開始準備傳送相關訊息");
        log.info("擷取訊息--> {}", vm);
        if ("resolved".equals(vm.getStatus())) {
            log.warn("!!作業處理完成，取消告警!!");
            log.warn("其他資訊:" + vm.getAlerts()[0].getLabels() );
            log.warn("開始時間: {}, 結束時間: {}", vm.getAlerts()[0].getStartsAt(), vm.getAlerts()[0].getEndsAt());
            cpu_alert = "0";
        } else {
            log.error("狀態:" + vm.getStatus() );
            log.error("其他資訊:" + vm.getAlerts()[0].getLabels() );
            log.error("開始時間: {}", vm.getAlerts()[0].getStartsAt());
            cpu_alert = "1";
        }


        return ResponseEntity.ok().body(null);
    }

    @GetMapping(value = "/getalert")
    public String findAllRes2(HttpServletRequest request, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        //log.info("getalert {}", cpu_alert);

        return cpu_alert;
    }
    @CrossOrigin(origins = "*")
    @GetMapping(value = "/setting/cpu/{id}")
    public String settingCPU(@PathVariable String id, HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("E:\\Works\\教育部\\網路案\\Software\\prometheus-2.53.0.windows-amd64\\rules.yml"));
            String line = reader.readLine();
            StringBuilder config = new StringBuilder();
            //config.append(line + System.lineSeparator());
            while (line != null) {
                System.out.println(line);
                if (line.indexOf("windows_cpu_time_total") >= 0) {
                    line = line.substring(0, line.indexOf(">") + 1 ) + " " + id;
                    config.append(line + System.lineSeparator());
                } else {
                    config.append(line + System.lineSeparator());
                }
                // read next line
                line = reader.readLine();


            }
            reader.close();
            log.info("content: {}", config.toString());
            BufferedWriter writer = null;
            writer = new BufferedWriter( new FileWriter( "E:\\Works\\教育部\\網路案\\Software\\prometheus-2.53.0.windows-amd64\\rules.yml"));
            writer.write( config.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/setting/memory/{id}")
    public String settingMemory(@PathVariable String id, HttpServletResponse response) {
        //response.addHeader("Access-Control-Allow-Origin", "*");
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("E:\\Works\\教育部\\網路案\\Software\\prometheus-2.53.0.windows-amd64\\rules.yml"));
            String line = reader.readLine();
            StringBuilder config = new StringBuilder();
            //config.append(line + System.lineSeparator());
            while (line != null) {
                System.out.println(line);
                if (line.indexOf("windows_os_physical_memory_free_bytes") >= 0) {
                    line = line.substring(0, line.indexOf(">") + 1 ) + " " + id;
                    config.append(line + System.lineSeparator());
                } else {
                    config.append(line + System.lineSeparator());
                }
                // read next line
                line = reader.readLine();


            }
            reader.close();
            log.info("content: {}", config.toString());
            BufferedWriter writer = null;
            writer = new BufferedWriter( new FileWriter( "E:\\Works\\教育部\\網路案\\Software\\prometheus-2.53.0.windows-amd64\\rules.yml"));
            writer.write( config.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "success";
    }
}
