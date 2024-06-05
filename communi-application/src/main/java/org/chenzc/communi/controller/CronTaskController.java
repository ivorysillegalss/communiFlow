package org.chenzc.communi.controller;

import jakarta.annotation.Resource;
import org.chenzc.communi.entity.BasicResult;
import org.chenzc.communi.service.CronTaskApiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cron/")
public class CronTaskController {

    @Resource
    private CronTaskApiService messageTemplateService;

    @PostMapping("start/{id}")
    public BasicResult startCron(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.startCronTask(id);
    }


    @PostMapping("stop/{id}")
    public BasicResult stopCron(@RequestBody @PathVariable("id") Long id) {
        return messageTemplateService.stopCronTask(id);
    }
}

