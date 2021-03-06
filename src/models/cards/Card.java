package models.cards;

public class Card implements Cloneable {
    private GraphicPack graphicPack;
    public int code; //number of card in the dock of phase1.
    public CardType cardType;
    private String name;
    private String cardId;
    private int mana;
    private String desc;
    private int price;
    private int plyNum; //one or two
    private boolean inserted;
    private boolean spawn;

    public Card(int code, String name, int mana, int price, CardType cardType) {
        this.code = code;
        this.name = name;
        this.mana = mana;
        this.cardType = cardType;
        this.price = price;
        this.graphicPack = new GraphicPack();
        inserted=false;
        spawn=false;
    }

    public GraphicPack getGraphicPack() {
        return graphicPack;
    }

    public String getName() {
        return name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCardId() {
        return cardId;
    }

    public int getMana() {
        return mana;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void show() {
    }

    public int determineAp() {
        return 0;
    }

    public int getPlyNum() {
        return plyNum;
    }

    public void setPlyNum(int plyNum) {
        this.plyNum = plyNum;
    }

    public int getCode() {
        return code;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public int getPrice() {
        return price;
    }

    public void showInfo(){
        System.out.println("card showInfo called!");
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Card newCard=new  Card(code,name,mana,price,cardType);
        newCard.setGraphicPack(this.graphicPack);
        return newCard;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isInserted() {
        return inserted;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public void setSpawn(boolean spawn) {
        this.spawn = spawn;
    }

    public void setGraphicPack(GraphicPack graphicPack) {
        this.graphicPack = graphicPack;
    }
}