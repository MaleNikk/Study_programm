package com.fluxing.testing;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationTest {

    private final Logger log = LoggerFactory.getLogger(ApplicationTest.class);

    private final String info = "Init start test!";

    @Test
    public void testStartProgram(){
        log.info(info);
        log.error(info);
        log.debug(info);
    }

    @Test
    public void checkUserPresent(){
        log.warn(info);
        log.trace("Do something good and never do bad!");
    }
}
