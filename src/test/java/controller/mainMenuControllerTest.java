package controller;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
class mainMenuControllerTest {

    @Test
    public void testSetUserId() throws NoSuchFieldException, IllegalAccessException {
        // Create an instance of mainMenuController
        mainMenuController controller = new mainMenuController();

        // Call the setUserId method
        int expectedUserId = 1;
        controller.setUserId(expectedUserId);

        // Use reflection to get the userId field in the mainMenuController class
        Field field = mainMenuController.class.getDeclaredField("userId");
        field.setAccessible(true);

        // Get the value of the userId field in the controller instance
        int actualUserId = (int) field.get(controller);

        // Check if the userId field was set to the expected value
        assertEquals(expectedUserId, actualUserId, "Fields didn't match");

        // If the test passes, print "successful"
        System.out.println("Successful");
    }
}