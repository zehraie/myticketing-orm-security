package com.cydeo.entity;

import com.cydeo.enums.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity
@Table(name="users")
@Where(clause="is_Deleted=false")
public class User extends BaseEntity {

    private String firstName;
    private String lastName;
    @Column(unique = true,nullable = false)
    private String userName;
    private String passWord;
    private boolean enabled;
    private String phone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="role_id")
    private Role role;
    //order number olmamasi icin @koymaliyiz
    @Enumerated(EnumType.STRING)
    private Gender gender;
//Many users/one role
    //user table has role_id

    }


