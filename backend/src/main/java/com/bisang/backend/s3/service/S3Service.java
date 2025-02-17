package com.bisang.backend.s3.service;

import static com.bisang.backend.common.exception.ExceptionCode.INVALID_REQUEST;
import static com.bisang.backend.common.exception.ExceptionCode.UNSUPPORTED_EXTENSIONS;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bisang.backend.common.exception.S3Exception;
import com.bisang.backend.s3.annotation.S3Limiter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {
    public static final String CAT_IMAGE_URI = "https://bisang-mimo-bucket.s3.ap-northeast-2.amazonaws.com/"
        + "a09365ab-3291-49a9-ad93-0aa41301166a.jpg";

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;

    // 파일 삭제 S3 서버 내에서
    public void deleteFile(String fileUrl) {
        String[] urlParts = fileUrl.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucket)) {
            throw new S3Exception(INVALID_REQUEST);
        }

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        if (!amazonS3.doesObjectExist(bucket, objectKey)) {
            throw new S3Exception(INVALID_REQUEST);
        }

        try {
            amazonS3.deleteObject(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            log.warn("File delete fail : " + e.getMessage());
            throw new S3Exception(INVALID_REQUEST);
        } catch (SdkClientException e) {
            log.warn("AWS SDK client error : " + e.getMessage());
            throw new S3Exception(INVALID_REQUEST);
        }
    }

    // 단일 파일 저장 S3 서버에
    public String saveFile(Long userId, MultipartFile file) {
        String randomFilename = generateRandomFilename(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            throw new S3Exception(INVALID_REQUEST);
        } catch (SdkClientException e) {
            log.warn("AWS SDK client error while uploading file: " + e.getMessage());
            throw new S3Exception(INVALID_REQUEST);
        } catch (IOException e) {
            log.warn("IO error while uploading file: " + e.getMessage());
            throw new S3Exception(INVALID_REQUEST);
        }

        return amazonS3.getUrl(bucket, randomFilename).toString();
    }

    // 랜덤파일명 생성 (파일명 중복 방지)
    private String generateRandomFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        String randomFilename = UUID.randomUUID() + "." + fileExtension;
        return randomFilename;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg", "webp");

        if (!allowedExtensions.contains(fileExtension)) {
            throw new S3Exception(UNSUPPORTED_EXTENSIONS);
        }
        return fileExtension;
    }
}
