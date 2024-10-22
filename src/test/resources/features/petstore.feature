Feature: Pet Store API Tests

  Scenario: Get available pets
    Given the pet store has available pets
    When I fetch the available pets
    Then I should receive a list of available pets

  Scenario: Post a new pet
    Given I have a new pet to add to the store
    When I add the pet to the store
    Then the new pet should be added successfully

  Scenario: Update the pet status to sold
    Given the pet exists in the store
    When I update the pet status to "sold"
    Then the pet status should be updated successfully

  Scenario: Delete the pet
    Given the pet exists in the store
    When I delete the pet
    Then the pet should be deleted successfully
