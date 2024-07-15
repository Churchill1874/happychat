package com.ent.happychat.controller.player;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.common.tools.api.FileTools;
import com.ent.happychat.service.UploadRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "工具")
@RequestMapping("/player/tools")
public class ToolsApi {

    @Autowired
    private UploadRecordService uploadRecordService;

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public R<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            if (file.isEmpty()){
                throw new DataException("上传图片是空");
            }

            // 获取文件后缀
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)){
                throw new DataException("上传图片名称不能为空");
            }

            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 获取Linux服务器上保存文件的目录
            FileTypeEnum fileTypeEnum = FileTools.getFileType(fileExtension);

            String uploadDir = "/bignews/" + fileTypeEnum.getName();

            String imageName = UUID.randomUUID().toString() + fileExtension;

            // 构建文件的保存路径
            Path filePath = Paths.get(uploadDir, imageName);

            // 创建目录（如果目录不存在）
            Files.createDirectories(filePath.getParent());

            // 将文件保存到指定目录
            Files.write(filePath, file.getBytes());

            String path = uploadDir + imageName;
            uploadRecordService.insertRecord(path, fileTypeEnum, TokenTools.getPlayerToken(true).getName());
            return R.ok(path);
        } catch (Exception e) {
            e.printStackTrace();
            return R.failed("上传失败");
        }
    }


}

