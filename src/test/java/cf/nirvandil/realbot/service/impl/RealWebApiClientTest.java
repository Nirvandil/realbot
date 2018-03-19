package cf.nirvandil.realbot.service.impl;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.ApiContextInitializer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RealWebApiClientTest {
    @Autowired
    private RealWebApiClient realApiClient;

    @BeforeClass
    public static void initApp() {
        ApiContextInitializer.init();
    }

    @Test
    public void splicer() {
        realApiClient.splicer()
                .apply("79787650792");
    }
}