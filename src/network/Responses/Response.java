package network.Responses;

import controllers.Constants;
import network.ReqResType;
import network.Requests.Request;

public abstract class Response{
    protected ReqResType type;
    protected Request request;
    protected Constants requestResult;
    public abstract void handleRequest();
    public abstract Constants getRequestResult();

    public ReqResType getType() {
        return type;
    }

    public void handle(){
        
    }
//namoosan vase requestResult setter tarif nakonin injoori harki eshghesh
    // bekeshe mitoone taghiresh bede vali bedone setter faghat to package mishe
    // taghiresh dad kholase chon jozi az classe farzande tojihi nadare setter
    // dashte bashe
}
