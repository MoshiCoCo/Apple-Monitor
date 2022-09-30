package top.misec.applemonitor.push;

import org.junit.jupiter.api.Test;

import top.misec.applemonitor.push.impl.BarkPush;

class BarkPushTest {

    @Test
    void push() {
        BarkPush.push("", "", "");
    }
}