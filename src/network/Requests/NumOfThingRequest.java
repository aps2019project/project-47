package network.Requests;

import network.ReqResType;

public class NumOfThingRequest extends Request {

    private String cardOrItemName;
    private Boolean cardOrItem;

    @Override
    public ReqResType getReqResType() {
        return ReqResType.shop;
    }

    public NumOfThingRequest(String token, String cardOrItemName, Boolean cardOrItem) {
        this.authToken = token;
        this.cardOrItemName = cardOrItemName;
        this.cardOrItem = cardOrItem;
    }

    public String getCardOrItemName() {
        return cardOrItemName;
    }

    public Boolean getCardOrItem() {
        return cardOrItem;
    }
}

