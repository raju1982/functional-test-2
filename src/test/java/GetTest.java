import io.restassured.response.Response;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetTest extends TestBase{

    private static Logger logger = LoggerFactory.getLogger(GetTest.class);

    /*
    comments
     */
    @Test
    public void testListGames() {
        Response response  = given().spec(videoDBRequestSpec).get(Constants.ALL_VIDEO_GAMES);
        assertEquals(response.getStatusCode(), SC_OK);
        assertNotNull(response.jsonPath().getString("id[0]"));
    }

}
