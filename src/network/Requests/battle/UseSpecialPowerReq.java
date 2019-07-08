package network.Requests.battle;

import models.battle.board.Location;
import network.Requests.Request;

public class UseSpecialPowerReq extends Request {
    String heroId;
    Location target;

}
