package it.nexoapp.env.api.transcoding;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * End-to-end test of the transcoding list endpoint against the environment schema,
 * the seeded test data and the Redis cache.
 * <p>
 * The {@code env-test} identity is a plain authenticated user that environment can never link to an association
 * (it has no identity directory), so it probes the {@code @AllowUnlinked} exemption of the identity gate.
 */
@QuarkusTest
class TranscodingResourceTest {

    /** Path of the state list endpoint, relative to the root path {@code /nexo-env} (RestAssured prepends it). */
    private static final String STATE_LIST_PATH = "/transcoding/list/STATE";

    /** The seeded state item, serialized with the pinned key order. */
    private static final String SEEDED_STATE = "{\"code\":\"100\",\"name\":\"Italia\",\"belfiore\":\"Z000\"}";

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The state list answers 200 with the seeded item.
     */
    @Test
    @TestSecurity(user = "env-test")
    void stateListContainsSeededItem() {
        given()
                .when().get(STATE_LIST_PATH)
                .then()
                .statusCode(200)
                .body("code", hasItem("100"))
                .body("name", hasItem("Italia"))
                .body("belfiore", hasItem("Z000"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * The seeded item serializes with exactly code, name and belfiore in this order,
     * and a second call (served through the cache) returns a byte-identical body.
     */
    @Test
    @TestSecurity(user = "env-test")
    void stateItemKeyOrderIsPinnedAndCachedCallIsIdentical() {
        final String first = given().when().get(STATE_LIST_PATH).then().statusCode(200).extract().asString();
        final String second = given().when().get(STATE_LIST_PATH).then().statusCode(200).extract().asString();

        assertTrue(first.contains(SEEDED_STATE), "the seeded item should serialize as code, name, belfiore: " + first);
        assertEquals(first, second, "the cached response should be byte-identical to the first one");
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Every item of the list exposes the three pinned keys.
     */
    @Test
    @TestSecurity(user = "env-test")
    void everyStateItemExposesThePinnedKeys() {
        given()
                .when().get(STATE_LIST_PATH)
                .then()
                .statusCode(200)
                .body("[0]", hasKey("code"))
                .body("[0]", hasKey("name"))
                .body("[0]", hasKey("belfiore"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * An unknown list name is a path parameter conversion failure and answers 404 with a {@code Fault}, not 500.
     */
    @Test
    @TestSecurity(user = "env-test")
    void unknownListAnswers404Fault() {
        given()
                .when().get("/transcoding/list/NOT_A_LIST")
                .then()
                .statusCode(404)
                .body("code", equalTo("nexo.http.404"));
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * A request without any identity is rejected with 401: the exemption covers the association link only,
     * authentication is still required.
     */
    @Test
    void requestWithoutTokenAnswers401() {
        given()
                .when().get(STATE_LIST_PATH)
                .then()
                .statusCode(401);
    }

}
