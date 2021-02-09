package pl.edu.pjwstk.jaz.config;

import io.restassured.RestAssured;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import java.util.Objects;

public class RestAssurePortListener implements TestExecutionListener {

    @Override
    public void beforeTestClass(TestContext testContext) {
        var environment = testContext.getApplicationContext().getBean(Environment.class);
        var portAsString = Objects.requireNonNull(environment.getProperty("local.server.port"));
        RestAssured.port = Integer.parseInt(portAsString);
    }
}
