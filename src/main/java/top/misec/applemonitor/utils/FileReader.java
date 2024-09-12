package top.misec.applemonitor.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author moshi
 */
@Slf4j
public class FileReader {
    /**
     * 读取指定路径的文件。
     *
     * @return fileContentStr
     */
    public static String readFile(String filePath) {
        String fileContentStr = null;
        try {
            InputStream inputStream = new FileInputStream(filePath);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            fileContentStr = new String(buffer, StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            log.debug("file not found", e);
        } catch (IOException e) {
            log.warn("file read exception", e);
        }
        return fileContentStr;
    }
}
