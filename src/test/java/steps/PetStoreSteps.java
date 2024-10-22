package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PetStoreSteps {

    private Response response;
    private Map<String, Object> newPet;
    private final String BASE_URL = "https://petstore.swagger.io/v2/pet";
    private int petId;
//1
    @Given("the pet store has available pets")
    public void thePetStoreHasAvailablePets() {
        RestAssured.baseURI = BASE_URL;
    }

    @When("I fetch the available pets")
    public void iFetchTheAvailablePets() {
        response = given()
                .queryParam("status", "available")
                .get("/findByStatus");
    }

    @Then("I should receive a list of available pets")
    public void iShouldReceiveAListOfAvailablePets() {
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.jsonPath().getList("$").size() > 0, "No available pets found.");
    }

    //2
    @Given("I have a new pet to add to the store")
    public void iHaveANewPetToAddToTheStore() {
        newPet = new HashMap<>();
        newPet.put("id", 123456);
        newPet.put("name", "Fluffy");
        newPet.put("status", "available");
    }

    @When("I add the pet to the store")
    public void iAddThePetToTheStore() {
        response = given()
                .header("Content-Type", "application/json")
                .body(newPet)
                .post();
    }

    @Then("the new pet should be added successfully")
    public void theNewPetShouldBeAddedSuccessfully() {
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals(newPet.get("id"), response.jsonPath().getInt("id"));
        Assertions.assertEquals(newPet.get("name"), response.jsonPath().getString("name"));
    }

    //3
    @Given("the pet exists in the store")
    public void thePetExistsInTheStore() {
        petId = 123456;  // Assuming this is the ID of the pet added in previous steps
    }

    @When("I update the pet status to {string}")
    public void iUpdateThePetStatusToSold(String status) {
        newPet.put("status", status);
        response = given()
                .header("Content-Type", "application/json")
                .body(newPet)
                .put();
    }

    @Then("the pet status should be updated successfully")
    public void thePetStatusShouldBeUpdatedSuccessfully() {
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertEquals("sold", response.jsonPath().getString("status"));
    }

    @When("I delete the pet")
    public void iDeleteThePet() {
        response = given().delete("/" + petId);
    }

    @Then("the pet should be deleted successfully")
    public void thePetShouldBeDeletedSuccessfully() {
        Assertions.assertEquals(200, response.getStatusCode());
        // Verify pet deletion
        Response checkResponse = given().get("/" + petId);
        Assertions.assertEquals(404, checkResponse.getStatusCode());
    }
}
