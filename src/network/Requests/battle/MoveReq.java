package network.Requests.battle;

import network.ReqResType;
import network.Requests.Request;

public class MoveReq extends Request {
    @Override
    public ReqResType getReqResType() {
        return ReqResType.battle;
    }
}