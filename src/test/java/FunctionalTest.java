import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.skyscreamer.jsonassert.*;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.*;

public class FunctionalTest extends TestBase{

    private static Logger logger = LoggerFactory.getLogger(FunctionalTest.class);

    /*
    comments
     */
    @Test
    public void testGetAllGames() {
        Response response  = given().spec(videoDBRequestSpec).get(Constants.ALL_VIDEO_GAMES);
        assertEquals(response.getStatusCode(), SC_OK);
        assertNotNull(response.jsonPath().getString("id[0]"));
    }

    @DataProvider(name="createGameProvider")
    public Object[][] gameProvider(){
        return new Object[][] {
                {"smoke/videogame-request.json", randomID()}
        };
    }


    @Test(dataProvider = "createGameProvider")
    public void createGame(String inputFile, String id) throws IOException {
        String inputPayload = readPayloadFromFile(inputFile, id);
        logger.info(inputPayload);
        Response response  = given().spec(videoDBRequestSpec).body(inputPayload).post(Constants.ALL_VIDEO_GAMES);
        assertEquals(response.getStatusCode(), SC_OK);
    }


    @Test
    public void getAllTeamData_DoCheckFirst() throws JSONException, IOException {
        Response response =
                given().spec(footballRequestSpec).get("teams/57");
        String jsonResponseAsString = response.asString();
        logger.info(jsonResponseAsString);
        String expectedPayload = readPayloadFromFile("smoke/football-team-response.json");
        JSONCompareResult result = JSONCompare.compareJSON(expectedPayload, jsonResponseAsString, JSONCompareMode.LENIENT);
        logger.info(result.toString());

        JSONAssert.assertEquals("message: " , expectedPayload, jsonResponseAsString,
                new CustomComparator(JSONCompareMode.LENIENT,
                        new Customization("activeCompetitions[code=PL].lastUpdated", (o1, o2) -> true)
                )
        );
    }

}


//activeCompetitions[code=PL].lastUpdated