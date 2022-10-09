package modificators;

@SuppressWarnings("unused")
public enum Effect {
    BOLD            ("\u001B[1m"),
    FAINT           ("\u001B[2m"),
    ITALIC          ("\u001B[3m"),
    UNDERLINE       ("\u001B[4m"),
    REVERSE         ("\u001B[7m"),
    STRIKEOUT       ("\u001B[9m"),
    NOT_BOLD_ITALIC ("\u001B[23m"),
    NOT_UNDERLINE   ("\u001B[24m"),
    NOT_REVERSE     ("\u001B[27m"),
    NOT_STRIKEOUT   ("\u001B[29m");

    private final String EFFECT;

    Effect(String effect) {
        this.EFFECT = effect;
    }

    @Override
    public String toString() {
        return EFFECT;
    }
}