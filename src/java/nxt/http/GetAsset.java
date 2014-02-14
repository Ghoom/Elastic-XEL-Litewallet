package nxt.http;

import nxt.Asset;
import nxt.util.Convert;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static nxt.http.JSONResponses.INCORRECT_ASSET;
import static nxt.http.JSONResponses.MISSING_ASSET;
import static nxt.http.JSONResponses.UNKNOWN_ASSET;

public final class GetAsset extends HttpRequestDispatcher.HttpRequestHandler {

    static final GetAsset instance = new GetAsset();

    private GetAsset() {}

    @Override
    JSONStreamAware processRequest(HttpServletRequest req) {

        String asset = req.getParameter("asset");
        if (asset == null) {
            return MISSING_ASSET;
        }

        Asset assetData;
        try {
            assetData = Asset.getAsset(Convert.parseUnsignedLong(asset));
            if (assetData == null) {
                return UNKNOWN_ASSET;
            }
        } catch (RuntimeException e) {
            return INCORRECT_ASSET;
        }

        JSONObject response = new JSONObject();
        response.put("account", Convert.convert(assetData.getAccountId()));
        response.put("name", assetData.getName());
        if (assetData.getDescription().length() > 0) {
            response.put("description", assetData.getDescription());
        }
        response.put("quantity", assetData.getQuantity());

        return response;
    }

}