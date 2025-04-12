package org.bestservers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.time.Instant;

@RestController
public class URLRedirectController {

    private final URLStorageService urlStorageService = new URLStorageService();

    @GetMapping("/{shortUrl}")
    public Object redirectUrl(@PathVariable String shortUrl) {
        String originalUrl = urlStorageService.getOriginalUrl(shortUrl);
        if (originalUrl == null || originalUrl.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("URL not found or expired");
        }
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header("Location", originalUrl)
                .build();

    }

    private static class URLStorageService {
        private final String url = "jdbc:postgresql://postgres:5432/url_db";
        private final String user = "user";
        private final String password = "password";

        public String getOriginalUrl(String shortUrl) {
            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String query = "SELECT original_url, expiry_timestamp FROM urls WHERE short_url = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, shortUrl);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        Timestamp expiry = rs.getTimestamp("expiry_timestamp");
                        if (expiry.toInstant().isBefore(Instant.now())) {
                            return null; // expired
                        }
                        return rs.getString("original_url");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
