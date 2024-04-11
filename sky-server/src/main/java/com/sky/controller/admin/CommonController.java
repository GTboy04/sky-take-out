package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.CommonService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gt_boy
 * @description:
 * @date 2024/4/11 11:27
 */

@RestController
@RequestMapping("admin/common")
@Api(tags = "公共接口")
@Slf4j
public class CommonController {

    @Autowired
    private CommonService commonService;
    @PostMapping("upload")
    @ApiOperation(value = "上传图片")
    public Result<String> uploadImage(MultipartFile file){
        log.info("上传文件file{}",file);
        Result<String> result = commonService.uploadImage(file);
        return result;
    }

}
