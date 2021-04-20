package com.zyx.controller;


//import com.joincheer.schoolday.configure.SchoolDayProperties;
import com.zyx.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/file/{type}")
public class FileController {

  @PostMapping(value = "/upload")
  public Result upload(@PathVariable("type") String type, @RequestParam("file") MultipartFile file,HttpSession session) throws IOException {
    if (file.isEmpty()) {
      return Result.errorOf("请选择一个上传文件");
    }
    String fileName = file.getOriginalFilename();
    String fileSuffix = fileName.substring(fileName.lastIndexOf(".") + 1);
    fileName = UUID.randomUUID().toString() + "." + fileSuffix;
//    Path filePath = Paths.get(schoolDayProperties.getFileLocation(), type, fileName);
    String realPath = session.getServletContext().getRealPath("/img");
    Path filePath = Paths.get(realPath, type, fileName);
    if (!Files.exists(filePath.getParent())) {
      Files.createDirectory(filePath.getParent());
    }

    Files.copy(file.getInputStream(), filePath);
    //输出原图
    write(file.getInputStream(), 2400, filePath, fileSuffix);
    //输出缩略图
    write(file.getInputStream(), 320, Paths.get(realPath, type, "mini-" + fileName), fileSuffix);
    
    return Result.dataOf(fileName);
  }

  private void write(InputStream inputStream, int max, Path path, String fileSuffix) {
    BufferedImage bufferedImage;
    try {
      BufferedImage scaleBufferedImage;
      bufferedImage = ImageIO.read(inputStream);
      if (bufferedImage.getWidth() > max) {
        int width = max;
        int height = bufferedImage.getHeight() * max / bufferedImage.getWidth();
        scaleBufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //将原始位图缩小后绘制到bufferedImage对象中
        scaleBufferedImage.getGraphics().drawImage(bufferedImage, 0, 0, width, height, null);
      } else {
        scaleBufferedImage = bufferedImage;
      }
      ImageIO.write(scaleBufferedImage, fileSuffix, path.toFile());
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  @GetMapping(value = "/view/{fileName}")
  public Result view(@PathVariable("type") String type, @PathVariable(value = "fileName") String fileName, HttpServletResponse response,HttpSession session) throws IOException {
//    Path filePath = Paths.get(schoolDayProperties.getFileLocation(), type, fileName);
      String realPath = session.getServletContext().getRealPath("/img");
      Path filePath = Paths.get(realPath, type, fileName);
    File file = new File(filePath.toUri());
    if (file.exists()) {
      response.setContentType(new MimetypesFileTypeMap().getContentType(file));
      Files.copy(filePath, response.getOutputStream());
      return null;
    }
    return Result.errorOf("文件不存在");
  }


}
