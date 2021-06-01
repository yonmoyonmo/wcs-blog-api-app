package wonmocyberschool.wcsblogapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    private String secretKey;
    private String expiration;

    public String getSecretKey() {
        return secretKey;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
}
