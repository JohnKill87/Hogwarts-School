package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {

    Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final StudentService studentService;
    private final AvatarRepository avatarRepository;
    private final String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService, @Value("${path.to.avatars.folder}") String avatarsDir) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
        this.avatarsDir = avatarsDir;
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.debug("Upload avatar");
        Student student = studentService.findStudent(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = new Avatar();
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
        logger.debug("Avatar uploaded");
    }
    private String getExtensions(String fileName) {
        logger.debug("Getting extensions");
        logger.debug("Extensions received");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public ResponseEntity<byte[]> downloadAvatarByStudentFromDb(Long studentId) {

        logger.debug("Downloading avatar by student from db");

        Optional<Avatar> avatarOpt = avatarRepository.findByStudentId(studentId);

        if (avatarOpt.isEmpty()) {
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }

        Avatar avatar = avatarOpt.get();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        logger.debug("Avatar by student from db loaded");
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    public void downloadAvatarFromFileSystem(Long studentId, HttpServletResponse response) throws IOException {

        logger.debug("Downloading avatar from file system");

        Optional<Avatar> avatarOpt = avatarRepository.findByStudentId(studentId);

        if (avatarOpt.isEmpty()) {
            return;
        }

        Avatar avatar = avatarOpt.get();

        Path path = Path.of(avatar.getFilePath());
        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream();) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
        logger.debug("Avatar from file system loaded");
    }

    public List<Avatar> getAllAvatars(Integer pageNumber, Integer pageSize) {
        logger.debug("Getting all avatars");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        logger.debug("All avatars received");
        return avatarRepository.findAll(pageRequest).getContent();
    }
}
