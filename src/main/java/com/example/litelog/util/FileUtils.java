package com.example.litelog.util;

import java.util.Set;

/**
 * 文件处理工具类，统一文件扩展名提取与校验逻辑
 */
public final class FileUtils {

    private FileUtils() {
    }

    private static final Set<String> VALID_IMAGE_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif");

    /**
     * 从 MIME Content-Type 提取文件扩展名（不含点号）
     * 例如: "image/jpeg" -> "jpg", "image/png" -> "png"
     */
    public static String getExtensionFromContentType(String contentType) {
        if (contentType == null) {
            return "jpg";
        }
        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            default -> "jpg";
        };
    }

    /**
     * 从文件名提取扩展名（含点号）
     * 例如: "photo.jpg" -> ".jpg", "photo" -> ".jpg"（默认值）
     */
    public static String getExtensionFromFilename(String filename) {
        if (filename == null || !filename.contains(".")) {
            return ".jpg";
        }
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if (VALID_IMAGE_EXTENSIONS.contains(extension)) {
            return extension;
        }
        return ".jpg";
    }

    /**
     * 校验扩展名是否为有效的图片格式
     */
    public static boolean isValidImageExtension(String extension) {
        if (extension == null) {
            return false;
        }
        return VALID_IMAGE_EXTENSIONS.contains(extension.toLowerCase());
    }

    /**
     * 从文件扩展名获取 Content-Type
     * 例如: ".jpg" -> "image/jpeg", ".png" -> "image/png"
     */
    public static String getContentTypeFromExtension(String extension) {
        if (extension == null) {
            return "image/jpeg";
        }
        return switch (extension.toLowerCase()) {
            case ".jpg", ".jpeg" -> "image/jpeg";
            case ".png" -> "image/png";
            case ".gif" -> "image/gif";
            case ".webp" -> "image/webp";
            default -> "image/jpeg";
        };
    }
}
