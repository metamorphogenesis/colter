package modificators;

@SuppressWarnings("unused")
public enum BGColor {
    // 8 colors
    BG_BLACK    ("\u001B[40m"),
    BG_RED      ("\u001B[41m"),
    BG_GREEN    ("\u001B[42m"),
    BG_YELLOW   ("\u001B[43m"),
    BG_BLUE     ("\u001B[44m"),
    BG_MAGENTA  ("\u001B[45m"),
    BG_CYAN     ("\u001B[46"),
    BG_WHITE    ("\u001B[47m"),
    BG_DEFAULT  ("\u001B[49m"),

    // 16 colors
    BG_BRIGHT_BLACK     ("\u001B[40;1m"),
    BG_BRIGHT_RED       ("\u001B[41;1m"),
    BG_BRIGHT_GREEN     ("\u001B[42;1m"),
    BG_BRIGHT_YELLOW    ("\u001B[43;1m"),
    BG_BRIGHT_BLUE      ("\u001B[44;1m"),
    BG_BRIGHT_MAGENTA   ("\u001B[45;1m"),
    BG_BRIGHT_CYAN      ("\u001B[46;1m"),
    BG_BRIGHT_WHITE     ("\u001B[47;1m");

    private final String COLOR;

    BGColor(String color) {
        this.COLOR = color;
    }

    @Override
    public String toString() {
        return COLOR;
    }
}
