package com.example.controller;

import com.example.mapper.UserMapper;
import com.example.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.UUID;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class UploadController {
    @Autowired
    private UserMapper userMapper;

    @Value("${file.upload-dir}") // 注入配置文件中的路径
    private String uploadDir;

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) {

        Result result = new Result();
        try {
            // 1. 校验文件是否为空
            if (file.isEmpty()) {
                result.setCode(400);
                result.setMsg("文件不能为空");
                return result;
            }

            // 2. 生成唯一文件名（保留原始扩展名）
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + fileExtension;

            // 3. 保存文件到配置的目录
            Path targetPath = Paths.get(uploadDir, filename);
            file.transferTo(targetPath);

            // 4. 返回 HTTP 可访问的 URL（而非本地路径）
            String fileUrl = "http://localhost:8080/images/" + filename;
            result.setCode(200);
            result.setMsg("上传成功");
            result.setData(fileUrl); // 返回 URL 而非本地路径
            return result;
        } catch (Exception e) {
            System.out.println(e);
            result.setCode(500);
            result.setMsg("上传失败: " + e.getMessage());
            return result;
        }
    }

}


