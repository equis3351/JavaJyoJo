package com.sparta.javajyojo.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Length(min = 4, max = 10)
    @Column(unique = true, nullable = false)
    private String username;

    @Length(min = 8, max = 15)
    @Column(nullable = false)
    private String password;

    @ElementCollection
    private List<String> pwUsdLst3Tms = new ArrayList<>();

    private String name;

    private String intro;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private String refreshToken;

    @Builder
    public User(String username, String password, String name, String intro, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.intro = intro;
        this.role = UserRoleEnum.valueOf(role);
        this.pwUsdLst3Tms = new ArrayList<>();
        this.pwUsdLst3Tms.add(password);
    }

    public void logOut() {
        refreshToken = null;
    }

    public void update(Optional<String> newPassword, Optional<String> name, Optional<String> intro) {
        if (newPassword.isPresent()) {
            this.password = newPassword.get();

            this.pwUsdLst3Tms.add(this.password);
            if (pwUsdLst3Tms.size() > 3) {
                pwUsdLst3Tms.remove(0);
            }
        }

        this.name = name.orElse(this.name);
        this.intro = intro.orElse(this.intro);
    }

}