package wonmocyberschool.wcsblogapi.util;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import wonmocyberschool.wcsblogapi.config.JwtProperties;
import wonmocyberschool.wcsblogapi.entity.Admin;
import wonmocyberschool.wcsblogapi.entity.BlogUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Component
public class JwtUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private JwtProperties jwtProperties;
    public JwtUtil(JwtProperties jwtProperties){
        this.jwtProperties = jwtProperties;
    }

    public String createAdminToken(Admin admin) {

        Date expDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expDate);
        int exp = parseInt(jwtProperties.getExpiration());
        calendar.add(Calendar.SECOND, exp);
        expDate = calendar.getTime();
        logger.info(expDate.toString());

        Claims claim = Jwts.claims();
        claim.put("email", admin.getAdminEmail());

        return Jwts.builder()
                .setClaims(claim)
                .setSubject(Long.toString(admin.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .compact();
    }

    public String createBlogUserToken(BlogUser blogUser) {
        Date expDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(expDate);
        int exp = parseInt(jwtProperties.getExpiration());
        calendar.add(Calendar.SECOND, exp);
        expDate = calendar.getTime();
        logger.info(expDate.toString());

        Claims claim = Jwts.claims();
        claim.put("email", blogUser.getEmail());
        claim.put("username", blogUser.getUsername());
        return Jwts.builder()
                .setClaims(claim)
                .setSubject(Long.toString(blogUser.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expDate)
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey())
                .compact();
    }


    public Map<String, Object> getUserIdAndEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody();
        Map<String, Object> tokenResult = new HashMap<>();
        tokenResult.put("email", claims.get("email"));
        tokenResult.put("id", Long.parseLong(claims.getSubject()));
        return tokenResult;
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
}
