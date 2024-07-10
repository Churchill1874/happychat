package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.exception.DataException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "工具")
@RequestMapping("/manage/tools")
public class ToolsController {

    @ApiOperation("上传图片")

    @PostMapping("/upload")
    public R<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()){
                throw new DataException("上传图片是空");
            }

            // 获取Linux服务器上保存文件的目录
            String uploadDir = "/bignews/image/";

            // 获取文件后缀
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)){
                throw new DataException("上传图片名称不能为空");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            String imageName = UUID.randomUUID().toString() + fileExtension;

            // 构建文件的保存路径
            Path filePath = Paths.get(uploadDir, imageName);

            // 创建目录（如果目录不存在）
            Files.createDirectories(filePath.getParent());

            // 将文件保存到指定目录
            Files.write(filePath, file.getBytes());

            return R.ok(uploadDir + imageName);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("上传失败");
        }
    }
}

