package demo.sneufeind.patchmergeexample.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import java.io.IOException;

public class JsonMergePatchUtils {

    public static <T> T mergePatch(final T orig, final String patchJson, final Class<T> clazz) throws IOException, JsonPatchException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode origNode = mapper.convertValue(orig, JsonNode.class);
        final JsonNode patchNode = mapper.readTree(patchJson);
        final JsonMergePatch mergePatch = JsonMergePatch.fromJson(patchNode);
        final JsonNode mergePatchedNode = mergePatch.apply(origNode);
        return mapper.treeToValue(mergePatchedNode, clazz);
    }
}
