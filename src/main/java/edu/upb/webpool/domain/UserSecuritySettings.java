package edu.upb.webpool.domain;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("users")
public class UserSecuritySettings {

    @PrimaryKey
    private String id;

    @Column("email")
    private String email;

    @Column("login")
    private String login;

    @Column("two_factor_secret")
    private String twoFactorSecret;

    @Column("two_factor_enabled")
    private boolean twoFactorEnabled;

    @Column("vote_confirmation_method")
    private String voteConfirmationMethod;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }
    public String getTwoFactorSecret() { return twoFactorSecret; }
    public void setTwoFactorSecret(String twoFactorSecret) { this.twoFactorSecret = twoFactorSecret; }
    public boolean isTwoFactorEnabled() { return twoFactorEnabled; }
    public void setTwoFactorEnabled(boolean twoFactorEnabled) { this.twoFactorEnabled = twoFactorEnabled; }
    public String getVoteConfirmationMethod() { return voteConfirmationMethod; }
    public void setVoteConfirmationMethod(String voteConfirmationMethod) { this.voteConfirmationMethod = voteConfirmationMethod; }
}
