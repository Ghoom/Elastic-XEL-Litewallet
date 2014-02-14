package nxt.http;

import nxt.Order;
import nxt.util.Convert;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

import static nxt.http.JSONResponses.INCORRECT_ORDER;
import static nxt.http.JSONResponses.MISSING_ORDER;
import static nxt.http.JSONResponses.UNKNOWN_ORDER;

public final class GetBidOrder extends HttpRequestDispatcher.HttpRequestHandler {

    static final GetBidOrder instance = new GetBidOrder();

    private GetBidOrder() {}

    @Override
    JSONStreamAware processRequest(HttpServletRequest req) {

        String order = req.getParameter("order");
        if (order == null) {
            return MISSING_ORDER;
        }

        Order.Bid orderData;
        try {
            orderData = Order.Bid.getBidOrder(Convert.parseUnsignedLong(order));
            if (orderData == null) {
                return UNKNOWN_ORDER;
            }
        } catch (RuntimeException e) {
            return INCORRECT_ORDER;
        }

        JSONObject response = new JSONObject();
        response.put("account", Convert.convert(orderData.getAccount().getId()));
        response.put("asset", Convert.convert(orderData.getAssetId()));
        response.put("quantity", orderData.getQuantity());
        response.put("price", orderData.getPrice());

        return response;

    }

}