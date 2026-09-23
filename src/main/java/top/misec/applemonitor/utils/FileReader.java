package top.misec.applemonitor.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

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
        try {
            return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e) {
            log.debug("file not found: {}", filePath);
        } catch (IOException e) {
            log.warn("file read exception", e);
        }
        return null;
    }
}
