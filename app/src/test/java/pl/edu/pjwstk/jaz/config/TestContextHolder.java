package pl.edu.pjwstk.jaz.config;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
public class TestContextHolder implements TestExecutionListener {
    private static final ThreadLocal<ApplicationContext> applicationContext = new ThreadLocal<>();
    @Override
    public void beforeTestClass(TestContext testContext) {
        applicationContext.set(testContext.getApplicationContext());
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.get().getBean(requiredType);
    }
}
