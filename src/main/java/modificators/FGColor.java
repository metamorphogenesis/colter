package modificators;

@SuppressWarnings("unused")
public enum FGColor {
    // 8 colors
    FG_BLACK    ("\u001B[30m"),
    FG_RED      ("\u001B[31m"),
    FG_GREEN    ("\u001B[32m"),
    FG_YELLOW   ("\u001B[33m"),
    FG_BLUE     ("\u001B[34m"),
    FG_MAGENTA  ("\u001B[35m"),
    FG_CYAN     ("\u001B[36m"),
    FG_WHITE    ("\u001B[37m"),
    FG_DEFAULT  ("\u001B[39m"),

    // 16 colors
    FG_BRIGHT_BLACK     ("\u001B[30;1m"),
    FG_BRIGHT_RED       ("\u001B[31;1m"),
    FG_BRIGHT_GREEN     ("\u001B[32;1m"),
    FG_BRIGHT_YELLOW    ("\u001B[33;1m"),
    FG_BRIGHT_BLUE      ("\u001B[34;1m"),
    FG_BRIGHT_MAGENTA   ("\u001B[35;1m"),
    FG_BRIGHT_CYAN      ("\u001B[36;1m"),
    FG_BRIGHT_WHITE     ("\u001B[37;1m");

    private final String COLOR;

    FGColor(String color) {
        this.COLOR = color;
    }

    @Override
    public String toString() {
        return COLOR;
    }
}
