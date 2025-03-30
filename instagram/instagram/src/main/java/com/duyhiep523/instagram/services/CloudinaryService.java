package com.duyhiep523.instagram.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.duyhiep523.instagram.exeptions.CloudinaryUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Uploads an image to Cloudinary.
     *
     * @param file The image file to upload.
     * @return The URL of the uploaded image.
     * @throws CloudinaryUploadException if there is an error during the upload process.
     */
    public String uploadImage(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new CloudinaryUploadException("File không được để trống");
            }
            long maxSize = 5 * 1024 * 1024; // 5MB
            if (file.getSize() > maxSize) {
                throw new CloudinaryUploadException("Dung lượng file vượt quá 5MB");
            }
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String encodedFileName = generateUniqueFileName(fileExtension);
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("public_id", encodedFileName, "resource_type", "auto"));

            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new CloudinaryUploadException("Lỗi khi đọc file", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CloudinaryUploadException("Lỗi khi tạo mã hóa tên file", e);
        } catch (Exception e) {
            throw new CloudinaryUploadException("Lỗi không xác định khi upload ảnh "+ e.getMessage(), e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') == -1) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }

    private String generateUniqueFileName(String fileExtension) throws NoSuchAlgorithmException {
        // Sử dụng UUID và mã hóa thành MD5 để tạo tên file duy nhất
        String uniqueName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] encodedHash = digest.digest(uniqueName.getBytes());

        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedHash) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString() + "." + fileExtension;
    }
}
