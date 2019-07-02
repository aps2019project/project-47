package network.Requests;

import network.ReqResType;

public class BuyRequest extends Request {

    @Override
    public ReqResType getReqResType() {
        return ReqResType.BUY;
    }
}
