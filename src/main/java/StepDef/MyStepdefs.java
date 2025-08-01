package StepDef;

import io.cucumber.java.en.Given;
import org.example.APITest;

public class MyStepdefs {
    APITest test = new APITest();
    @Given("the user making the authoriztion call to server")
    public void theUserMakingTheAuthoriztionCallToServer() {
        test.authorization();
        test.addProduct();
        test.Placeorder();
        test.deleteProduct();
        test.getorderdetails();

    }
}
