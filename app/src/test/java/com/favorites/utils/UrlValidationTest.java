package com.favorites.utils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * UrlValidation 工具类测试（JUnit 4 版本）
 */
public class UrlValidationTest {
    
    // ==================== 格式验证测试 ====================
    
    @Test
    public void testValidUrlFormats() {
        assertTrue(UrlValidation.isValidFormat("https://www.google.com"));
        assertTrue(UrlValidation.isValidFormat("http://example.com"));
        assertTrue(UrlValidation.isValidFormat("https://www.baidu.com/path/to/page"));
        assertTrue(UrlValidation.isValidFormat("https://github.com/user/repo?tab=readme"));
        assertTrue(UrlValidation.isValidFormat("http://localhost:8080/api/test"));
        assertTrue(UrlValidation.isValidFormat("https://sub.domain.example.co.uk"));
        assertTrue(UrlValidation.isValidFormat("ftp://ftp.example.com/file.zip"));
    }
    
    @Test
    public void testInvalidUrlFormats() {
        assertFalse(UrlValidation.isValidFormat(null));
        assertFalse(UrlValidation.isValidFormat(""));
        assertFalse(UrlValidation.isValidFormat("   "));
        assertFalse(UrlValidation.isValidFormat("not-a-url"));
        assertFalse(UrlValidation.isValidFormat("www.example.com"));  // 缺少协议
        assertFalse(UrlValidation.isValidFormat("http:/example.com")); // 协议格式错误
        assertFalse(UrlValidation.isValidFormat("https://"));         // 缺少域名
        assertFalse(UrlValidation.isValidFormat("http://.com"));      // 无效域名
    }
    
    // ==================== URL 规范化测试 ====================
    
    @Test
    public void testNormalizeUrl() {
        assertEquals("http://example.com", UrlValidation.normalizeUrl("example.com"));
        assertEquals("http://www.google.com", UrlValidation.normalizeUrl("www.google.com"));
        assertEquals("https://secure.site.com", UrlValidation.normalizeUrl("https://secure.site.com"));
        assertEquals("http://localhost:8080", UrlValidation.normalizeUrl("localhost:8080"));
        assertNull(UrlValidation.normalizeUrl(null));
        assertNull(UrlValidation.normalizeUrl(""));
        assertNull(UrlValidation.normalizeUrl("   "));
    }
    
    // ==================== 域名提取测试 ====================
    
    @Test
    public void testGetDomain() {
        assertEquals("www.google.com", UrlValidation.getDomain("https://www.google.com/search"));
        assertEquals("example.com", UrlValidation.getDomain("http://example.com"));
        assertEquals("github.com", UrlValidation.getDomain("https://github.com/user/repo"));
        assertNull(UrlValidation.getDomain("invalid-url"));
        assertNull(UrlValidation.getDomain(null));
    }
    
    // ==================== 可访问性测试（需要网络） ====================
    
    @Test
    public void testReachableUrls() {
        System.out.println("注意：此测试需要网络连接，且访问外部网站");
        
        // 测试已知可访问的网站
        boolean result = UrlValidation.isReachable("https://www.baidu.com", 10000);
        System.out.println("百度可访问性测试结果: " + result);
        
        result = UrlValidation.isReachable("https://www.google.com", 10000);
        System.out.println("Google 可访问性测试结果: " + result);
    }
    
    @Test
    public void testUnreachableUrls() {
        // 测试不存在的 URL
        boolean result = UrlValidation.isReachable("https://this-domain-does-not-exist-12345.com", 3000);
        assertFalse(result);
        
        // 测试无效格式的 URL
        result = UrlValidation.isReachable("not-a-url");
        assertFalse(result);
    }
    
    @Test
    public void testTimeoutSetting() {
        long startTime = System.currentTimeMillis();
        boolean result = UrlValidation.isReachable("https://192.168.2.999:9999", 1000);
        long endTime = System.currentTimeMillis();
        
        assertFalse(result);
        // 验证大约在超时时间内返回
        assertTrue((endTime - startTime) < 2000);
    }
    
    // ==================== 完整验证测试 ====================
    
    @Test
    public void testFullValidation() {
        // 测试有效的 URL
        UrlValidation.ValidationResult result1 = UrlValidation.validateFull("https://www.baidu.com", 5000);
        System.out.println("URL 验证结果1: " + result1);
        assertTrue(result1.isFormatValid());
        
        // 测试格式无效的 URL
        UrlValidation.ValidationResult result2 = UrlValidation.validateFull("invalid", 5000);
        System.out.println("URL 验证结果2: " + result2);
        assertFalse(result2.isFormatValid());
        assertFalse(result2.isValid());
        
        // 测试格式有效但不可访问的 URL
        UrlValidation.ValidationResult result3 = UrlValidation.validateFull("https://this-domain-does-not-exist-12345.com", 3000);
        System.out.println("URL 验证结果3: " + result3);
        assertTrue(result3.isFormatValid());
        assertFalse(result3.isReachable());
    }
    
    // ==================== 性能测试 ====================
    
    @Test
    public void testPerformance() {
        String[] testUrls = {
            "https://www.google.com",
            "http://example.com",
            "https://github.com",
            "https://stackoverflow.com",
            "http://localhost:8080",
            "https://sub.domain.com/path/to/page",
            "invalid-url-1",
            "invalid-url-2",
            "https://very-long-url-with-many-characters.com/path?query=value&another=value",
            "ftp://ftp.example.com/file.txt"
        };
        
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10000; i++) {
            for (String url : testUrls) {
                UrlValidation.isValidFormat(url);
            }
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        System.out.println("验证 10,000 次 URL 耗时: " + duration + " ms");
        assertTrue("性能应该足够好，耗时小于1秒", duration < 1000);
    }
}