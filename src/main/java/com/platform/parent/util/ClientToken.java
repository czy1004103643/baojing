package com.platform.parent.util;

public class ClientToken {
    //{"access_token":"5_wbbh323QMnPij66jS6siChfL1nXp3Ik8P2y2L5vd_n59P8G30s571_VdNCQ9rRE-r-fYO9JCCdGfhl5tHO8Ur-z_pI1v68yzQT8ieeuQWGtBcdwHAoLYy6Jm2EyI6lvEJ_75mKZ7vztoslPJRJMeAGAKCF","expires_in":7200}.
    private String access_token;

    private Long expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }

    public String toString()
    {
        return "access_token: " + access_token + ", expires_in: " + expires_in;
    }
}
