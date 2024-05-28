package ses;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class GameTest implements RequestHandler<Map<String, String>, String> {

    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        // Your business logic goes here
        String key1 = input.get("key1");
        String key2 = input.get("key2");
        String key3 = input.get("key3");

        if (!key1.equals("cole has a large johnson")) {
            return "actually cole has a big johnson";
        } else {
            return "you are correct";
        }

//        return "Received input: key1=" + key1 + ", key2=" + key2 + ", key3=" + key3;
    }
}
