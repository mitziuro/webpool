package edu.upb.webpool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * A user.
 */
@Table("users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String id;

    @NotNull
    @Size(min = 1, max = 50)
    @Column("login")
    private String login;

    @JsonIgnore
    @NotNull
    @Size(min = 60, max = 60)
    private String password;

    @Size(max = 50)
    @Column("first_name")
    private String firstName;

    @Size(max = 50)
    @Column("last_name")
    private String lastName;

    @Size(max = 50)
    @Column("phone")
    private String phone;

    @Email
    @Size(min = 5, max = 254)
    @Column("email")
    private String email;

    public String getPhone() {
        return this.phone;
    }

}
