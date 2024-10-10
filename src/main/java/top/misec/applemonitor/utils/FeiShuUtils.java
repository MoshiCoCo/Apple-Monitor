package top.misec.applemonitor.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.HmacUtils;

import java.nio.charset.StandardCharsets;

/**
 * FeiShuUtils
 *
 * @author Moshi
 * @since 2023/5/10
 */
@Slf4j
public class FeiShuUtils {
    public static String genSign(String secret, long timestamp) {
        //把timestamp+"\n"+密钥当做签名字符串
        String stringToSign = timestamp + "\n" + secret;

        //使用HmacSHA256算法计算签名
        byte[] signData = HmacUtils.getInitializedMac("HmacSHA256", stringToSign.getBytes(StandardCharsets.UTF_8))
                .doFinal();
        return new String(Base64.encodeBase64(signData));
    }
}
