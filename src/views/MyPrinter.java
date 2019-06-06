package views;

public enum MyPrinter {
    red,black,green,yellow,blue,purple,cyan,white;

    public static void red(String string){
        System.out.println(ANSI_RED+string+ANSI_RED);
    }
    public static void green(String string){
        System.out.println(ANSI_GREEN+string+ANSI_GREEN);
    }
    public static void purple(String string){
        System.out.println(ANSI_PURPLE+string+ANSI_PURPLE);
    }
    public static void yellow(String string){
        System.out.println(ANSI_YELLOW+string+ANSI_YELLOW);
    }
    public static void blue(String string){
        System.out.println(ANSI_BLUE+string+ANSI_BLUE);
    }
    public static void cyan(String string){
        System.out.println(ANSI_CYAN+string+ANSI_CYAN);
    }
    public static void setBackgroundWhite(){
        System.out.print("\u001B[7m");
    }
    public static void clearBackground(){
        System.out.print("\u001B[0m");
    }
    public static void colorBlue(){
        System.out.print(ANSI_BLUE);
    }
    public static void colorPurple(){
        System.out.print("\u001B[35m");
    }
    public static void colorGreen(){
        System.out.print("\u001B[32m");
    }
    public static void colorRed(){
        System.out.print("\u001B[31m");
    }
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

}
