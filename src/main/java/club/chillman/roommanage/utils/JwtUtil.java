package club.chillman.roommanage.utils;

import com.auth0.jwt.JWT;

/**
 * @author NIU
 * @createTime 2021/4/22 0:41
 */
public class JwtUtil {
    public static String getUserIdByToken(String token) {
        return JWT.decode(token).getClaim("userId").asString();
    }
}
