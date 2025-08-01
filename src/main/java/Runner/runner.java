package Runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@CucumberOptions(
        features = "src/main/resources/FeatureFiles",
        glue = "StepDef",
        tags = "@ecommerce")
@Test
public class runner extends AbstractTestNGCucumberTests {
    public runner(){

    }
}
