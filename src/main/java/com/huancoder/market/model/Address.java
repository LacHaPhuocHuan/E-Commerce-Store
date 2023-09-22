package com.huancoder.market.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbAddress")
@NamedQuery(name = "Address.findByUserId", query = "select a from Address a join a.user u where u.id=: userId")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isMain;
    private String name;
    private String content;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
