package network.Requests.battle;

import network.ReqResType;
import network.Requests.Request;

public class AttackReq extends Request {
    @Override
    public ReqResType getReqResType() {
        return ReqResType.battle;
    }
}
