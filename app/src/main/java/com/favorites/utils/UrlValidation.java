package com.favorites.utils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * URL 验证工具类
 * 用于验证 URL 格式是否正确以及是否可访问
 * 
 * @author Your Name
 * @date 2026-04-23
 */
public class UrlValidation {
    
    // URL 正则表达式（支持 http, https, ftp）
    private static final String URL_REGEX = "^(https?|ftp)://" +
                                        "(" +
                                        "localhost" +  // 支持 localhost
                                        "|" +
                                        "([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,}" +  // 域名
                                        "|" +
                                        "([0-9]{1,3}\\.){3}[0-9]{1,3}" +  // IP 地址
                                        ")" +
                                        "(:[0-9]{1,5})?" +  // 端口（可选）
                                        "(/[^\\s]*)?$";     // 路径（可选）
    
    private static final Pattern URL_PATTERN = Pattern.compile(URL_REGEX, Pattern.CASE_INSENSITIVE);
    
    /**
     * 验证 URL 格式是否合法
     * 
     * @param url 待验证的 URL 字符串
     * @return true-合法，false-不合法
     */
    public static boolean isValidFormat(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }
        return URL_PATTERN.matcher(url.trim()).matches();
    }
    
    /**
     * 验证 URL 是否可访问（发送 HEAD 请求）
     * 
     * @param url 待验证的 URL 字符串
     * @param timeoutMs 超时时间（毫秒）
     * @return true-可访问，false-不可访问
     */
    public static boolean isReachable(String url, int timeoutMs) {
        if (!isValidFormat(url)) {
            return false;
        }
        
        HttpURLConnection connection = null;
        try {
            URL targetUrl = new URL(url);
            connection = (HttpURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(timeoutMs);
            connection.setReadTimeout(timeoutMs);
            
            int responseCode = connection.getResponseCode();
            // 200-299 表示成功
            return responseCode >= 200 && responseCode < 300;
            
        } catch (Exception e) {
            return false;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
    
    /**
     * 验证 URL 是否可访问（使用默认超时时间 5000ms）
     * 
     * @param url 待验证的 URL 字符串
     * @return true-可访问，false-不可访问
     */
    public static boolean isReachable(String url) {
        return isReachable(url, 5000);
    }
    
    /**
     * 获取 URL 的域名部分
     * 
     * @param url 完整的 URL
     * @return 域名，如果格式不合法返回 null
     */
    public static String getDomain(String url) {
        if (!isValidFormat(url)) {
            return null;
        }
        
        try {
            URL targetUrl = new URL(url);
            return targetUrl.getHost();
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 规范化 URL（自动添加 http:// 前缀）
     * 
     * @param url 原始 URL 字符串
     * @return 规范化后的 URL
     */
    public static String normalizeUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            return null;
        }
        
        String trimmedUrl = url.trim();
        
        // 如果没有协议前缀，添加 http://
        if (!trimmedUrl.startsWith("http://") && !trimmedUrl.startsWith("https://")) {
            trimmedUrl = "http://" + trimmedUrl;
        }
        
        return trimmedUrl;
    }
    
    /**
     * 完整验证：格式 + 可访问性
     * 
     * @param url 待验证的 URL
     * @param timeoutMs 超时时间
     * @return 验证结果对象
     */
    public static ValidationResult validateFull(String url, int timeoutMs) {
        ValidationResult result = new ValidationResult();
        
        // 检查格式
        if (!isValidFormat(url)) {
            result.setValid(false);
            result.setMessage("URL 格式不正确");
            return result;
        }
        
        result.setFormatValid(true);
        
        // 检查可访问性
        boolean reachable = isReachable(url, timeoutMs);
        result.setReachable(reachable);
        
        if (reachable) {
            result.setValid(true);
            result.setMessage("URL 有效且可访问");
        } else {
            result.setValid(false);
            result.setMessage("URL 格式正确但无法访问");
        }
        
        return result;
    }
    
    /**
     * 验证结果内部类
     */
    public static class ValidationResult {
        private boolean valid;
        private boolean formatValid;
        private boolean reachable;
        private String message;
        
        public ValidationResult() {
            this.valid = false;
            this.formatValid = false;
            this.reachable = false;
            this.message = "";
        }
        
        // Getters and Setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        
        public boolean isFormatValid() { return formatValid; }
        public void setFormatValid(boolean formatValid) { this.formatValid = formatValid; }
        
        public boolean isReachable() { return reachable; }
        public void setReachable(boolean reachable) { this.reachable = reachable; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        @Override
        public String toString() {
            return String.format("ValidationResult{valid=%s, formatValid=%s, reachable=%s, message='%s'}",
                    valid, formatValid, reachable, message);
        }
    }
}