import modificators.BGColor;
import modificators.Effect;
import modificators.FGColor;

public class Main {
    public static void main(String[] args) {
        Colter col = new Colter();
        col.println("hello world", FGColor.FG_RED, BGColor.BG_BLACK, Effect.ITALIC);
        System.out.println("hello world");
    }
}