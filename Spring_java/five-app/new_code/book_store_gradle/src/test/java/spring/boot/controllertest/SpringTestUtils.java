package spring.boot.controllertest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

@SpringBootTest(classes = TestWebController.class)
public class SpringTestUtils {

    private static final Logger log = LogManager.getLogger(SpringTestUtils.class);

    public static String readStringFromData(String dataPath) {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(MessageFormat.format("classpath:{0}",dataPath));
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException exception) {
            log.error("exception: {0}", exception);
            throw new RuntimeException(exception);
        }
    }
}
