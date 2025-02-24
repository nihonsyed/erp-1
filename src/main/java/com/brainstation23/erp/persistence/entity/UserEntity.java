package com.brainstation23.erp.persistence.entity;

import com.brainstation23.erp.constant.EntityConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


@Entity(name = "_user")
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class UserEntity {

    @Id
    @Type(type = "uuid-char")
    private UUID id;
    private String firstName;
    private String lastName;

    private String email;

    private  String password;

    private String role;




}
