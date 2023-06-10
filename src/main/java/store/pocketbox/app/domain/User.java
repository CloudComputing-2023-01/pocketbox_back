package store.pocketbox.app.domain;

import lombok.*;
import store.pocketbox.app.domain.base.BaseEntity;
import store.pocketbox.app.domain.enums.UserStatus;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "String default ''")
    private String avatar_url;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(columnDefinition = "Boolean default false")
    private Boolean social_only;

    @Column(columnDefinition = "Boolean default false")
    private Boolean acceptance_status;

}
