package com.huancoder.market.model;

import com.huancoder.market.security.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import static com.huancoder.market.security.TokenType.BEARER;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbJwtToken")
@NamedQuery(name = "JwtToken.findAllByUserId", query = " select t from JwtToken t join t.user u where u.id=:user_id and" +
        " ( t.isNonExpired=true or  t.isEnabled = true) ")
public class JwtToken {
    @Id
    @GeneratedValue()
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(length = 10000)
    private String token;
    private boolean isNonExpired;
    private boolean isEnabled;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType=BEARER;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
