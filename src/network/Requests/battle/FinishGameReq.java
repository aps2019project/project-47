package network.Requests.battle;

import network.ReqResType;
import network.Requests.Request;

public class FinishGameReq extends Request {

    public ReqResType getReqResType() {
        return ReqResType.battle;
    }
}
