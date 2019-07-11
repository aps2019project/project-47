package network.Responses.battle;

import com.gilecode.yagson.YaGson;
import controllers.Constants;
import controllers.graphical.BattleController;
import models.Account;
import network.Client;
import network.Requests.battle.BattleActionRequest;
import network.Requests.battle.UpdateScoreBoardRequest;
import network.Responses.Response;

public class BattleActionResponse extends Response {
    public BattleActionResponse(BattleActionRequest request) {
        this.request = request;
    }

    @Override
    public void handleRequest() {

    }

    @Override
    public void handleResponse() {
        BattleController.instance.doOneAction(((BattleActionRequest) request).getBattleAction());
        UpdateScoreBoardRequest updateScoreBoardRequest = new UpdateScoreBoardRequest(null);
        YaGson yaGson = new YaGson();
        Client.getWriter().println(yaGson.toJson(updateScoreBoardRequest));
        Client.getWriter().flush();
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
