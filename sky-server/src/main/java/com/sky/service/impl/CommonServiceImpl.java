package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.service.CommonService;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author Gt_boy
 * @description:
 * @date 2024/4/11 11:28
 */

@Service
@Slf4j
public class CommonServiceImpl implements CommonService {
    @Autowired
    private AliOssUtil aliOssUtil;
    @Override
    public Result<String> uploadImage(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = UUID.randomUUID().toString() + suffix;
            String uploadPath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(uploadPath);
        } catch (IOException e) {
            return Result.error(MessageConstant.UPLOAD_FAILED);
        }
    }

//    @Autowired
//    private

}
