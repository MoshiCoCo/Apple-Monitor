package top.misec.applemonitor.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * FeiShuUtils
 *
 * @author Moshi
 * @since 2023/5/10
 */
public class FeiShuUtils {

    private static final String HMAC_SHA256 = "HmacSHA256";

    public static String genSign(String secret, long timestamp) {
        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;

        //使用HmacSHA256算法计算签名，签名内容为空
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256);
            mac.init(new SecretKeySpec(stringToSign.getBytes(StandardCharsets.UTF_8), HMAC_SHA256));
            return Base64.getEncoder().encodeToString(mac.doFinal());
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("飞书签名计算失败", e);
        }
    }
}
