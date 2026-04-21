package com.ent.happychat.controller.manage;

import com.baomidou.mybatisplus.extension.api.R;
import com.ent.happychat.common.annotation.AdminLoginCheck;
import com.ent.happychat.common.constant.enums.FileTypeEnum;
import com.ent.happychat.common.exception.DataException;
import com.ent.happychat.common.tools.TokenTools;
import com.ent.happychat.common.tools.api.FileTools;
import com.ent.happychat.service.UploadRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.*;
import java.util.UUID;

@Slf4j
@RestController
@Api(tags = "工具")
@RequestMapping("/manage/tools")
public class ToolsController {

    @Autowired
    private UploadRecordService uploadRecordService;

    @ApiOperation("上传图片")
    @PostMapping("/upload")
    @AdminLoginCheck
    public R<String> handleFileUpload(@RequestPart("file") MultipartFile file) {
        try {
            // 1️⃣ 校验
            if (file.isEmpty()) {
                throw new DataException("上传的是空");
            }

            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isBlank(originalFilename)) {
                throw new DataException("上传文件名称不能为空");
            }

            int index = originalFilename.lastIndexOf(".");
            if (index == -1) {
                throw new DataException("文件格式错误");
            }

            // 2️⃣ 后缀 & 类型
            String fileExtension = originalFilename.substring(index).toLowerCase();
            FileTypeEnum fileTypeEnum = FileTools.getFileType(fileExtension);

            // 3️⃣ 上传目录（你现在用 root 目录）
            //String uploadDir = "D:" + "\\bignews\\" + fileTypeEnum.getName() + "\\";
            String uploadDir =  "/bignews/" + fileTypeEnum.getName() + "/";

            // 4️⃣ 文件名
            String fileName;
            if (fileTypeEnum == FileTypeEnum.IMAGE) {
                fileName = UUID.randomUUID() + ".jpg";
            } else {
                fileName = UUID.randomUUID() + fileExtension;
            }

            // 5️⃣ 路径
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());

            // 6️⃣ 临时文件
            Path tempPath = Paths.get(uploadDir, UUID.randomUUID() + "_temp" + fileExtension);
            // ✅ 先创建目录（关键）
            Files.createDirectories(tempPath.getParent());
            file.transferTo(tempPath.toFile());

            File inputFile = tempPath.toFile();
            File outputFile = filePath.toFile();

            // 7️⃣ 压缩逻辑（核心）
            if (fileTypeEnum == FileTypeEnum.IMAGE) {

                if (fileExtension.equalsIgnoreCase(".webp")) {
                    // 👉 直接保存（不压缩）
                    Files.copy(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING);

                } else {
                    // 👉 正常压缩
                    Thumbnails.of(inputFile)
                            .size(800, 800)
                            .outputQuality(0.7f)
                            .outputFormat("jpg")
                            .toFile(outputFile);
                }

            } else {
                Files.copy(tempPath, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            // 8️⃣ 删除临时文件
            Files.deleteIfExists(tempPath);

            // 9️⃣ 返回路径（用于前端访问）
            String path = "/bignews/" + fileTypeEnum.getName() + "/" + fileName;

            uploadRecordService.insertRecord(path, fileTypeEnum, TokenTools.getAdminToken(true).getName());

            return R.ok(path);

        } catch (Exception e) {
            log.error("上传失败", e);
            return R.failed("上传失败");
        }
    }
}