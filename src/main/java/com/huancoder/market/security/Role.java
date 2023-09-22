package com.huancoder.market.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Sets.newHashSet(
            Permission.admin_change,
            Permission.admin_read,
            Permission.admin_write,
            Permission.user_change,
            Permission.user_read,
            Permission.user_write
            )),
    USER(Sets.newHashSet(
            Permission.user_change,
            Permission.user_read,
            Permission.user_write
    ));
    private Set<Permission> permissions;
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }
    public Set<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities
                =permissions.stream()
                .map(auth-> new SimpleGrantedAuthority(auth.name()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}
