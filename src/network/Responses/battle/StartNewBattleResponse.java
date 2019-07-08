package network.Responses.battle;

import controllers.Constants;
import models.Account;
import models.battle.Battle;
import models.battle.MatchType;
import models.battle.Player;
import network.Requests.battle.StartNewBattleRequest;
import network.Responses.Response;
import network.Server;

public class StartNewBattleResponse extends Response {
    public StartNewBattleResponse(StartNewBattleRequest request){
        this.request = request;
    }
    Player player1;
    Player player2;
    MatchType matchType;
    int numberOfFlags;
    @Override
    public void handleRequest() {
        Account account1 = null;
        for (String authToken : Account.getAccountsMapper().keySet()){
            if (((StartNewBattleRequest) request).getUserNameOfOpponent().equals(Account.getAccountsMapper().get(authToken).getUserName())){
                account1 = Account.getAccountsMapper().get(authToken);
                break;
            }
        }
        Account account2 = Account.getAccountsMapper().get(request.getAuthToken());
        //todo playerNum chiye daghighan
        player1 = account1.makePlayer(1);
        player2 = account2.makePlayer(2);
        matchType = ((StartNewBattleRequest) request).getMatchType();
        numberOfFlags = ((StartNewBattleRequest) request).getNumberOfFlags();
    }

    @Override
    public void handleResponse() {
        Battle battle = new Battle(player1, player2, matchType, numberOfFlags);
    }

    @Override
    public Constants getRequestResult() {
        return null;
    }
}
