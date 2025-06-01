package com.matchday.matchdayserver.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    @Id
    private Long userId;

    @Column(nullable = false, length = 1000)
    private String token;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void update(String newToken) {
        this.token = newToken;
        this.createdAt = LocalDateTime.now();
    }
}
