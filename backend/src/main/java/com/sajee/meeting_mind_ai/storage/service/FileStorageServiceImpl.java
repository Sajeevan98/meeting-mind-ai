package com.sajee.meeting_mind_ai.storage.service;

import com.sajee.meeting_mind_ai.common.exception.business.FileStorageException;
import com.sajee.meeting_mind_ai.storage.config.StorageProperties;
import com.sajee.meeting_mind_ai.storage.dto.StoredFile;
import com.sajee.meeting_mind_ai.storage.util.FileUtils;
import com.sajee.meeting_mind_ai.storage.util.StorageMessages;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final StorageProperties storageProperties;

    @Override
    public StoredFile store(MultipartFile file) {

        validate(file);

        try {
            Path uploadDirectory = createUploadDirectory();

            String extension = FileUtils.getExtension(file.getOriginalFilename());

            String storedFileName = generateStoredFileName(extension);

            Path targetPath = uploadDirectory.resolve(storedFileName);

            log.info("Uploading file: {}", file.getOriginalFilename());

            Files.copy(
                    file.getInputStream(),
                    targetPath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String checksum = calculateChecksum(targetPath);

            log.info("File stored successfully: {}", storedFileName);
            log.info("File Path: {}", targetPath.toString());

            return new StoredFile(
                    file.getOriginalFilename(),
                    storedFileName,
                    targetPath.toString(),
                    file.getContentType(),
                    extension,
                    file.getSize(),
                    checksum
            );

        } catch (IOException ex) {

            log.error("Failed to store file: {}", file.getOriginalFilename(), ex);

            throw new FileStorageException(StorageMessages.STORE_FAILED, ex);
        }
    }

    @Override
    public void delete(String filePath) {

        try {
            Path path = Path.of(filePath);

            Files.deleteIfExists(path);

            log.info("Deleted file: {}", filePath);

        } catch (IOException ex) {

            log.error("Failed to delete file: {}", filePath, ex);

            throw new FileStorageException(StorageMessages.DELETE_FAILED, ex);
        }
    }

    // -----------------------------------------------------------
    // Validation
    // -----------------------------------------------------------
    private void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            log.warn("Empty file upload attempted.");
            throw new FileStorageException(StorageMessages.EMPTY_FILE);
        }

        if (file.getSize() > storageProperties.getMaxSize()) {

            log.warn("File size exceeds maximum allowed limit. Maximum size: {}, Uploaded file size: {}",
                    storageProperties.getMaxSize(), file.getSize());

            throw new FileStorageException(StorageMessages.FILE_TOO_LARGE);
        }

        if (!storageProperties.getAllowedTypes().contains(file.getContentType())) {

            log.warn("Unsupported file type: {}", file.getContentType());
            throw new FileStorageException(StorageMessages.INVALID_FILE_TYPE);
        }
    }

    // -----------------------------------------------------------
    // Directory
    // -----------------------------------------------------------
    private Path createUploadDirectory() throws IOException {

        Path uploadDirectory = Path.of(storageProperties.getUploadDir());

        Files.createDirectories(uploadDirectory);

        return uploadDirectory;
    }

    // -----------------------------------------------------------
    // Filename
    // -----------------------------------------------------------
    private String generateStoredFileName(String extension) {

        String uuid = UUID.randomUUID().toString();

        if (extension == null || extension.isBlank())
            return uuid;

        return uuid + "." + extension;
    }

    // -----------------------------------------------------------
    // SHA-256
    // -----------------------------------------------------------
    private String calculateChecksum(Path file) throws IOException {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            try (InputStream inputStream = Files.newInputStream(file)) {

                byte[] buffer = new byte[8192];

                int bytesRead;

                while ((bytesRead = inputStream.read(buffer)) != -1) {

                    digest.update(buffer, 0, bytesRead);
                }
            }
            return HexFormat.of()
                    .formatHex(digest.digest());

        } catch (NoSuchAlgorithmException ex) {

            throw new IllegalStateException("SHA-256 algorithm not available.", ex);
        }
    }
}
