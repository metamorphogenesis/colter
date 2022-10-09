import modificators.BGColor;
import modificators.Effect;
import modificators.FGColor;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.*;
import static org.junit.jupiter.api.Assertions.*;

class ColterTest {
//    private static final PrintStream OUT_BACKUP = System.out;
//    private static final List<FGColor> ALL_FG_COLORS = Collections.unmodifiableList(Arrays.asList(FGColor.values()));
//    private static final List<BGColor> ALL_BG_COLORS = Collections.unmodifiableList(Arrays.asList(BGColor.values()));
//    private static final List<Effect> allEffects = Arrays.asList(Effect.values());
//    private static ByteArrayOutputStream mockOut;
//    private FGColor fg_fgColor;
//    private Color fg_color;
//    private int fg_256;
//    private int fg_red;
//    private int fg_green;
//    private int fg_blue;
//    private String fg_fgColor_string;
//    private String fg_256_string;
//    private String fg_rgb_string;
//
//    private BGColor bg_bgColor;
//    private Color bg_color;
//    private int bg_256;
//    private int bg_red;
//    private int bg_green;
//    private int bg_blue;
//    private String bg_bgColor_string;
//    private String bg_256_string;
//    private String bg_rgb_string;
//
//    private Effect effect;
//    private Effect[] effects;
//    private String effect_string;
//    private String effects_string = "";
//
//    private String text;
//
//    private Colter colter;
//
//    @BeforeEach
//    void init() {
//        fg_fgColor = ALL_FG_COLORS.get((int) (random() * ALL_FG_COLORS.size()));
//        fg_256 = (int) (random() * 256);
//        fg_red = (int) (random() * 256);
//        fg_green = (int) (random() * 256);
//        fg_blue = (int) (random() * 256);
//        fg_color = new Color(fg_red, fg_green, fg_blue);
//
//        fg_fgColor_string = fg_fgColor.toString();
//        fg_256_string = "\u001b[38;5;" + fg_256 + "m";
//        fg_rgb_string = "\u001b[38;2;" + fg_green + ";" + fg_green + ";" + fg_blue + "m";
//
//        bg_bgColor = ALL_BG_COLORS.get((int) (random() * ALL_BG_COLORS.size()));
//        bg_256 = (int) (random() * 256);
//        bg_red = (int) (random() * 256);
//        bg_green = (int) (random() * 256);
//        bg_blue = (int) (random() * 256);
//        bg_color = new Color(bg_red, bg_green, bg_blue);
//
//        bg_bgColor_string = bg_bgColor.toString();
//        bg_256_string = "\u001b[48;5;" + bg_256 + "m";
//        bg_rgb_string = "\u001b[48;2;" + bg_red + ";" + bg_green + ";" + bg_blue + "m";
//
//        Collections.shuffle(allEffects);
//        int effectsCount = (int) (random() * allEffects.size() + 1);
//        effects = new Effect[effectsCount];
//
//        for (int i = 0; i < effectsCount; i++) {
//            effects[i] = allEffects.get(i);
//        }
//
//        effect = effects[0];
//
//        effect_string = effect.toString();
//
//        for (Effect effect : effects) {
//            effects_string += effect;
//        }
//
//        text = RandomStringUtils.randomAlphanumeric(20);

//        mockOut = new ByteArrayOutputStream();
//        PrintStream stream = new PrintStream(mockOut);
//        System.setOut(stream);
//    }

//    @AfterEach
//    public void restore() {
//        System.setOut(OUT_BACKUP);
//    }

//    @Test
//    void colter() {
//        colter = new Colter();
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter1() {
//        colter = new Colter(fg_fgColor);
//
//        testAllFields("", fg_fgColor_string, "", "");
//    }
//
//    @Test
//    void colter2() {
//        colter = new Colter(bg_bgColor);
//
//        testAllFields("", "", bg_bgColor_string, "");
//    }
//
//    @Test
//    void colter3() {
//        colter = new Colter(effect);
//
//        testAllFields("", "", "", effect_string);
//    }
//
//    @Test
//    void colter4() {
//        colter = new Colter(effects);
//
//        testAllFields("", "", "", effects_string);
//    }
//
//    @Test
//    void colter5() {
//        colter = new Colter(text);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter6() {
//        colter = new Colter(fg_fgColor, bg_bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter7() {
//        colter = new Colter(fg_color, bg_bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter8() {
//        colter = new Colter(fg_256, bg_bgColor);
//
//        testAllFields("", fg_256_string, bg_bgColor_string, "");
//    }
//
//    @Test
//    void colter9() {
//        colter = new Colter(fg_red, fg_green, fg_blue, bg_bgColor);
//
//        testAllFields("", fg_rgb_string, bg_bgColor_string, "");
//    }
//
//    @Test
//    void colter10() {
//        Color bgColor = new Color(75, 11, 198);
//        colter = new Colter(FGColor.FG_RED, bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter11() {
//        Color fgColor = new Color(161, 164, 67);
//        Color bgColor = new Color(121, 10, 75);
//        colter = new Colter(fgColor, bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter12() {
//        Color bgColor = new Color(11, 13, 23);
//        colter = new Colter(237, bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter13() {
//        Color bgColor = new Color(74, 227, 150);
//        colter = new Colter(47, 128, 241, bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter14() {
//        Color bgColor = new Color(199, 103, 35);
//        colter = new Colter(FGColor.FG_GREEN, bgColor);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter15() {
//        colter = new Colter(FGColor.FG_GREEN, 150);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter16() {
//        Color fgColor = new Color(fg_red, fg_green, fg_blue);
//        colter = new Colter(fgColor, 78);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter17() {
//        colter = new Colter(96, 1);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter18() {
//        colter = new Colter(FGColor.FG_YELLOW, 75, 7, 11);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter19() {
//        Color fgColor = new Color(199, 11, 11);
//        colter = new Colter(fgColor, 7, 102, 19);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter20() {
//        colter = new Colter(13, 98, 45, 89, 1, 236);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter21() {
//        colter = new Colter(FGColor.FG_BLUE, Effect.FAINT);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter22() {
//        colter = new Colter(BGColor.BG_BLACK, Effect.REVERSE, Effect.ITALIC);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void colter23() {
//        colter = new Colter(FGColor.FG_MAGENTA, BGColor.BG_RED, Effect.BOLD, Effect.NOT_UNDERLINE, Effect.FAINT);
//
//        testAllFields("", "", "", "");
//    }
//
//    @Test
//    void getText() {
//    }
//
//    @Test
//    void getFG() {
//    }
//
//    @Test
//    void getBG() {
//    }
//
//    @Test
//    void getEffect() {
//    }
//
//    @Test
//    void getEffects() {
//    }
//
//    @Test
//    void setText() {
//    }
//
//    @Test
//    void setEffect() {
//    }
//
//    @Test
//    void setFG() {
//    }
//
//    @Test
//    void testSetFG() {
//    }
//
//    @Test
//    void testSetFG1() {
//    }
//
//    @Test
//    void testSetFG2() {
//    }
//
//    @Test
//    void setDefaultFG() {
//    }
//
//    @Test
//    void setBG() {
//    }
//
//    @Test
//    void testSetBG() {
//    }
//
//    @Test
//    void testSetBG1() {
//    }
//
//    @Test
//    void testSetBG2() {
//    }
//
//    @Test
//    void setDefaultBG() {
//    }
//
//    @Test
//    void setDefaultEffect() {
//    }
//
//    @Test
//    void set() {
//    }
//
//    @Test
//    void testSet() {
//    }
//
//    @Test
//    void testSet1() {
//    }
//
//    @Test
//    void testSet2() {
//    }
//
//    @Test
//    void testSet3() {
//    }
//
//    @Test
//    void testSet4() {
//    }
//
//    @Test
//    void testSet5() {
//    }
//
//    @Test
//    void testSet6() {
//    }
//
//    @Test
//    void testSet7() {
//    }
//
//    @Test
//    void testSet8() {
//    }
//
//    @Test
//    void testSet9() {
//    }
//
//    @Test
//    void testSet10() {
//    }
//
//    @Test
//    void testSet11() {
//    }
//
//    @Test
//    void testSet12() {
//    }
//
//    @Test
//    void testSet13() {
//    }
//
//    @Test
//    void testSet14() {
//    }
//
//    @Test
//    void testSet15() {
//    }
//
//    @Test
//    void testSet16() {
//    }
//
//    @Test
//    void testSet17() {
//    }
//
//    @Test
//    void testSet18() {
//    }
//
//    @Test
//    void testSet19() {
//    }
//
//    @Test
//    void testSet20() {
//    }
//
//    @Test
//    void testSet21() {
//    }
//
//    @Test
//    void testSet22() {
//    }
//
//    @Test
//    void testSet23() {
//    }
//
//    @Test
//    void testSet24() {
//    }
//
//    @Test
//    void testSet25() {
//    }
//
//    @Test
//    void testSet26() {
//    }
//
//    @Test
//    void testSet27() {
//    }
//
//    @Test
//    void testSet28() {
//    }
//
//    @Test
//    void testSet29() {
//    }
//
//    @Test
//    void testSet30() {
//    }
//
//    @Test
//    void testSet31() {
//    }
//
//    @Test
//    void testSet32() {
//    }
//
//    @Test
//    void testSet33() {
//    }
//
//    @Test
//    void testSet34() {
//    }
//
//    @Test
//    void testSet35() {
//    }
//
//    @Test
//    void testSet36() {
//    }
//
//    @Test
//    void testSet37() {
//    }
//
//    @Test
//    void testSet38() {
//    }
//
//    @Test
//    void testSet39() {
//    }
//
//    @Test
//    void testSet40() {
//    }
//
//    @Test
//    void testSet41() {
//    }
//
//    @Test
//    void testSet42() {
//    }
//
//    @Test
//    void testSet43() {
//    }
//
//    @Test
//    void testSet44() {
//    }
//
//    @Test
//    void testSet45() {
//    }
//
//    @Test
//    void testSet46() {
//    }
//
//    @Test
//    void testSet47() {
//    }
//
//    @Test
//    void testSet48() {
//    }
//
//    @Test
//    void testSet49() {
//    }
//
//    @Test
//    void testSet50() {
//    }
//
//    @Test
//    void testSet51() {
//    }
//
//    @Test
//    void testSet52() {
//    }
//
//    @Test
//    void testSet53() {
//    }
//
//    @Test
//    void testSet54() {
//    }
//
//    @Test
//    void testSet55() {
//    }
//
//    @Test
//    void testSet56() {
//    }
//
//    @Test
//    void testSet57() {
//    }
//
//    @Test
//    void testSet58() {
//    }
//
//    @Test
//    void testSet59() {
//    }
//
//    @Test
//    void testSet60() {
//    }
//
//    @Test
//    void testSet61() {
//    }
//
//    @Test
//    void testSet62() {
//    }
//
//    @Test
//    void testSet63() {
//    }
//
//    @Test
//    void setFG8_BG24() {
//    }
//
//    @Test
//    void testSetFG8_BG24() {
//    }
//
//    @Test
//    void testSetFG8_BG241() {
//    }
//
//    @Test
//    void testSetFG8_BG242() {
//    }
//
//    @Test
//    void setFG24_BG8() {
//    }
//
//    @Test
//    void testSetFG24_BG8() {
//    }
//
//    @Test
//    void testSetFG24_BG81() {
//    }
//
//    @Test
//    void testSetFG24_BG82() {
//    }
//
//    @Test
//    void reset() {
//    }
//
//    @Test
//    void print() {
//    }
//
//    @Test
//    void printAndReset() {
//    }
//
//    @Test
//    void println() {
//    }
//
//    @Test
//    void printlnAndReset() {
//    }
//
//    @Test
//    void testPrint() {
//    }
//
//    @Test
//    void testPrintAndReset() {
//    }
//
//    @Test
//    void testPrintln() {
//    }
//
//    @Test
//    void testPrintlnAndReset() {
//    }
//
//    @Test
//    void testPrint1() {
//    }
//
//    @Test
//    void testPrintAndReset1() {
//    }
//
//    @Test
//    void testPrintln1() {
//    }
//
//    @Test
//    void testPrintlnAndReset1() {
//    }
//
//    @Test
//    void testPrint2() {
//    }
//
//    @Test
//    void testPrintAndReset2() {
//    }
//
//    @Test
//    void testPrintln2() {
//    }
//
//    @Test
//    void testPrintlnAndReset2() {
//    }
//
//    @Test
//    void testPrint3() {
//    }
//
//    @Test
//    void testPrintAndReset3() {
//    }
//
//    @Test
//    void testPrintln3() {
//    }
//
//    @Test
//    void testPrintlnAndReset3() {
//    }
//
//    @Test
//    void testPrint4() {
//    }
//
//    @Test
//    void testPrintAndReset4() {
//    }
//
//    @Test
//    void testPrintln4() {
//    }
//
//    @Test
//    void testPrintlnAndReset4() {
//    }
//
//    @Test
//    void testPrint5() {
//    }
//
//    @Test
//    void testPrintAndReset5() {
//    }
//
//    @Test
//    void testPrintln5() {
//    }
//
//    @Test
//    void testPrintlnAndReset5() {
//    }
//
//    @Test
//    void testPrint6() {
//    }
//
//    @Test
//    void testPrintAndReset6() {
//    }
//
//    @Test
//    void testPrintln6() {
//    }
//
//    @Test
//    void testPrintlnAndReset6() {
//    }
//
//    @Test
//    void testPrint7() {
//    }
//
//    @Test
//    void testPrintAndReset7() {
//    }
//
//    @Test
//    void testPrintln7() {
//    }
//
//    @Test
//    void testPrintlnAndReset7() {
//    }
//
//    @Test
//    void testPrint8() {
//    }
//
//    @Test
//    void testPrintAndReset8() {
//    }
//
//    @Test
//    void testPrintln8() {
//    }
//
//    @Test
//    void testPrintlnAndReset8() {
//    }
//
//    @Test
//    void testPrint9() {
//    }
//
//    @Test
//    void testPrintAndReset9() {
//    }
//
//    @Test
//    void testPrintln9() {
//    }
//
//    @Test
//    void testPrintlnAndReset9() {
//    }
//
//    @Test
//    void testPrint10() {
//    }
//
//    @Test
//    void testPrintAndReset10() {
//    }
//
//    @Test
//    void testPrintln10() {
//    }
//
//    @Test
//    void testPrintlnAndReset10() {
//    }
//
//    @Test
//    void testPrint11() {
//    }
//
//    @Test
//    void testPrintAndReset11() {
//    }
//
//    @Test
//    void testPrintln11() {
//    }
//
//    @Test
//    void testPrintlnAndReset11() {
//    }
//
//    @Test
//    void testPrint12() {
//    }
//
//    @Test
//    void testPrintAndReset12() {
//    }
//
//    @Test
//    void testPrintln12() {
//    }
//
//    @Test
//    void testPrintlnAndReset12() {
//    }
//
//    @Test
//    void testPrint13() {
//    }
//
//    @Test
//    void testPrintAndReset13() {
//    }
//
//    @Test
//    void testPrintln13() {
//    }
//
//    @Test
//    void testPrintlnAndReset13() {
//    }
//
//    @Test
//    void testPrint14() {
//    }
//
//    @Test
//    void testPrintAndReset14() {
//    }
//
//    @Test
//    void testPrintln14() {
//    }
//
//    @Test
//    void testPrintlnAndReset14() {
//    }
//
//    @Test
//    void testPrint15() {
//    }
//
//    @Test
//    void testPrintAndReset15() {
//    }
//
//    @Test
//    void testPrintln15() {
//    }
//
//    @Test
//    void testPrintlnAndReset15() {
//    }
//
//    @Test
//    void testPrint16() {
//    }
//
//    @Test
//    void testPrintAndReset16() {
//    }
//
//    @Test
//    void testPrintln16() {
//    }
//
//    @Test
//    void testPrintlnAndReset16() {
//    }
//
//    @Test
//    void testPrint17() {
//    }
//
//    @Test
//    void testPrintAndReset17() {
//    }
//
//    @Test
//    void testPrintln17() {
//    }
//
//    @Test
//    void testPrintlnAndReset17() {
//    }
//
//    @Test
//    void testPrint18() {
//    }
//
//    @Test
//    void testPrintAndReset18() {
//    }
//
//    @Test
//    void testPrintln18() {
//    }
//
//    @Test
//    void testPrintlnAndReset18() {
//    }
//
//    @Test
//    void testPrint19() {
//    }
//
//    @Test
//    void testPrintAndReset19() {
//    }
//
//    @Test
//    void testPrintln19() {
//    }
//
//    @Test
//    void testPrintlnAndReset19() {
//    }
//
//    @Test
//    void testPrint20() {
//    }
//
//    @Test
//    void testPrintAndReset20() {
//    }
//
//    @Test
//    void testPrintln20() {
//    }
//
//    @Test
//    void testPrintlnAndReset20() {
//    }
//
//    @Test
//    void testPrint21() {
//    }
//
//    @Test
//    void testPrintAndReset21() {
//    }
//
//    @Test
//    void testPrintln21() {
//    }
//
//    @Test
//    void testPrintlnAndReset21() {
//    }
//
//    @Test
//    void testPrint22() {
//    }
//
//    @Test
//    void testPrintAndReset22() {
//    }
//
//    @Test
//    void testPrintln22() {
//    }
//
//    @Test
//    void testPrintlnAndReset22() {
//    }
//
//    @Test
//    void testPrint23() {
//    }
//
//    @Test
//    void testPrintAndReset23() {
//    }
//
//    @Test
//    void testPrintln23() {
//    }
//
//    @Test
//    void testPrintlnAndReset23() {
//    }
//
//    @Test
//    void testPrint24() {
//    }
//
//    @Test
//    void testPrintAndReset24() {
//    }
//
//    @Test
//    void testPrintln24() {
//    }
//
//    @Test
//    void testPrintlnAndReset24() {
//    }
//
//    @Test
//    void testPrint25() {
//    }
//
//    @Test
//    void testPrintAndReset25() {
//    }
//
//    @Test
//    void testPrintln25() {
//    }
//
//    @Test
//    void testPrintlnAndReset25() {
//    }
//
//    @Test
//    void testPrint26() {
//    }
//
//    @Test
//    void testPrintAndReset26() {
//    }
//
//    @Test
//    void testPrintln26() {
//    }
//
//    @Test
//    void testPrintlnAndReset26() {
//    }
//
//    @Test
//    void testPrint27() {
//    }
//
//    @Test
//    void testPrintAndReset27() {
//    }
//
//    @Test
//    void testPrintln27() {
//    }
//
//    @Test
//    void testPrintlnAndReset27() {
//    }
//
//    @Test
//    void testPrint28() {
//    }
//
//    @Test
//    void testPrintAndReset28() {
//    }
//
//    @Test
//    void testPrintln28() {
//    }
//
//    @Test
//    void testPrintlnAndReset28() {
//    }
//
//    @Test
//    void testPrint29() {
//    }
//
//    @Test
//    void testPrintAndReset29() {
//    }
//
//    @Test
//    void testPrintln29() {
//    }
//
//    @Test
//    void testPrintlnAndReset29() {
//    }
//
//    @Test
//    void testPrint30() {
//    }
//
//    @Test
//    void testPrintAndReset30() {
//    }
//
//    @Test
//    void testPrintln30() {
//    }
//
//    @Test
//    void testPrintlnAndReset30() {
//    }
//
//    @Test
//    void testPrint31() {
//    }
//
//    @Test
//    void testPrintAndReset31() {
//    }
//
//    @Test
//    void testPrintln31() {
//    }
//
//    @Test
//    void testPrintlnAndReset31() {
//    }
//
//    @Test
//    void testPrint32() {
//    }
//
//    @Test
//    void testPrintAndReset32() {
//    }
//
//    @Test
//    void testPrintln32() {
//    }
//
//    @Test
//    void testPrintlnAndReset32() {
//    }
//
//    @Test
//    void testPrint33() {
//    }
//
//    @Test
//    void testPrintAndReset33() {
//    }
//
//    @Test
//    void testPrintln33() {
//    }
//
//    @Test
//    void testPrintlnAndReset33() {
//    }
//
//    @Test
//    void testPrint34() {
//    }
//
//    @Test
//    void testPrintAndReset34() {
//    }
//
//    @Test
//    void testPrintln34() {
//    }
//
//    @Test
//    void testPrintlnAndReset34() {
//    }
//
//    @Test
//    void testPrint35() {
//    }
//
//    @Test
//    void testPrintAndReset35() {
//    }
//
//    @Test
//    void testPrintln35() {
//    }
//
//    @Test
//    void testPrintlnAndReset35() {
//    }
//
//    @Test
//    void testPrint36() {
//    }
//
//    @Test
//    void testPrintAndReset36() {
//    }
//
//    @Test
//    void testPrintln36() {
//    }
//
//    @Test
//    void testPrintlnAndReset36() {
//    }
//
//    @Test
//    void testPrint37() {
//    }
//
//    @Test
//    void testPrintAndReset37() {
//    }
//
//    @Test
//    void testPrintln37() {
//    }
//
//    @Test
//    void testPrintlnAndReset37() {
//    }
//
//    @Test
//    void testPrint38() {
//    }
//
//    @Test
//    void testPrintAndReset38() {
//    }
//
//    @Test
//    void testPrintln38() {
//    }
//
//    @Test
//    void testPrintlnAndReset38() {
//    }
//
//    @Test
//    void testPrint39() {
//    }
//
//    @Test
//    void testPrintAndReset39() {
//    }
//
//    @Test
//    void testPrintln39() {
//    }
//
//    @Test
//    void testPrintlnAndReset39() {
//    }
//
//    @Test
//    void testPrint40() {
//    }
//
//    @Test
//    void testPrintAndReset40() {
//    }
//
//    @Test
//    void testPrintln40() {
//    }
//
//    @Test
//    void testPrintlnAndReset40() {
//    }
//
//    @Test
//    void testPrint41() {
//    }
//
//    @Test
//    void testPrintAndReset41() {
//    }
//
//    @Test
//    void testPrintln41() {
//    }
//
//    @Test
//    void testPrintlnAndReset41() {
//    }
//
//    @Test
//    void testPrint42() {
//    }
//
//    @Test
//    void testPrintAndReset42() {
//    }
//
//    @Test
//    void testPrintln42() {
//    }
//
//    @Test
//    void testPrintlnAndReset42() {
//    }
//
//    @Test
//    void testPrint43() {
//    }
//
//    @Test
//    void testPrintAndReset43() {
//    }
//
//    @Test
//    void testPrintln43() {
//    }
//
//    @Test
//    void testPrintlnAndReset43() {
//    }
//
//    @Test
//    void testPrint44() {
//    }
//
//    @Test
//    void testPrintAndReset44() {
//    }
//
//    @Test
//    void testPrintln44() {
//    }
//
//    @Test
//    void testPrintlnAndReset44() {
//    }
//
//    @Test
//    void testPrint45() {
//    }
//
//    @Test
//    void testPrintAndReset45() {
//    }
//
//    @Test
//    void testPrintln45() {
//    }
//
//    @Test
//    void testPrintlnAndReset45() {
//    }
//
//    @Test
//    void testPrint46() {
//    }
//
//    @Test
//    void testPrintAndReset46() {
//    }
//
//    @Test
//    void testPrintln46() {
//    }
//
//    @Test
//    void testPrintlnAndReset46() {
//    }
//
//    @Test
//    void testPrint47() {
//    }
//
//    @Test
//    void testPrintAndReset47() {
//    }
//
//    @Test
//    void testPrintln47() {
//    }
//
//    @Test
//    void testPrintlnAndReset47() {
//    }
//
//    @Test
//    void testPrint48() {
//    }
//
//    @Test
//    void testPrintAndReset48() {
//    }
//
//    @Test
//    void testPrintln48() {
//    }
//
//    @Test
//    void testPrintlnAndReset48() {
//    }
//
//    @Test
//    void testPrint49() {
//    }
//
//    @Test
//    void testPrintAndReset49() {
//    }
//
//    @Test
//    void testPrintln49() {
//    }
//
//    @Test
//    void testPrintlnAndReset49() {
//    }
//
//    @Test
//    void testPrint50() {
//    }
//
//    @Test
//    void testPrintAndReset50() {
//    }
//
//    @Test
//    void testPrintln50() {
//    }
//
//    @Test
//    void testPrintlnAndReset50() {
//    }
//
//    @Test
//    void testPrint51() {
//    }
//
//    @Test
//    void testPrintAndReset51() {
//    }
//
//    @Test
//    void testPrintln51() {
//    }
//
//    @Test
//    void testPrintlnAndReset51() {
//    }
//
//    @Test
//    void testPrint52() {
//    }
//
//    @Test
//    void testPrintAndReset52() {
//    }
//
//    @Test
//    void testPrintln52() {
//    }
//
//    @Test
//    void testPrintlnAndReset52() {
//    }
//
//    @Test
//    void testPrint53() {
//    }
//
//    @Test
//    void testPrintAndReset53() {
//    }
//
//    @Test
//    void testPrintln53() {
//    }
//
//    @Test
//    void testPrintlnAndReset53() {
//    }
//
//    @Test
//    void testPrint54() {
//    }
//
//    @Test
//    void testPrintAndReset54() {
//    }
//
//    @Test
//    void testPrintln54() {
//    }
//
//    @Test
//    void testPrintlnAndReset54() {
//    }
//
//    @Test
//    void testPrint55() {
//    }
//
//    @Test
//    void testPrintAndReset55() {
//    }
//
//    @Test
//    void testPrintln55() {
//    }
//
//    @Test
//    void testPrintlnAndReset55() {
//    }
//
//    @Test
//    void testPrint56() {
//    }
//
//    @Test
//    void testPrintAndReset56() {
//    }
//
//    @Test
//    void testPrintln56() {
//    }
//
//    @Test
//    void testPrintlnAndReset56() {
//    }
//
//    @Test
//    void testPrint57() {
//    }
//
//    @Test
//    void testPrintAndReset57() {
//    }
//
//    @Test
//    void testPrintln57() {
//    }
//
//    @Test
//    void testPrintlnAndReset57() {
//    }
//
//    @Test
//    void testPrint58() {
//    }
//
//    @Test
//    void testPrintAndReset58() {
//    }
//
//    @Test
//    void testPrintln58() {
//    }
//
//    @Test
//    void testPrintlnAndReset58() {
//    }
//
//    @Test
//    void testPrint59() {
//    }
//
//    @Test
//    void testPrintAndReset59() {
//    }
//
//    @Test
//    void testPrintln59() {
//    }
//
//    @Test
//    void testPrintlnAndReset59() {
//    }
//
//    @Test
//    void testPrint60() {
//    }
//
//    @Test
//    void testPrintAndReset60() {
//    }
//
//    @Test
//    void testPrintln60() {
//    }
//
//    @Test
//    void testPrintlnAndReset60() {
//    }
//
//    @Test
//    void testPrint61() {
//    }
//
//    @Test
//    void testPrintAndReset61() {
//    }
//
//    @Test
//    void testPrintln61() {
//    }
//
//    @Test
//    void testPrintlnAndReset61() {
//    }
//
//    @Test
//    void testPrint62() {
//    }
//
//    @Test
//    void testPrintAndReset62() {
//    }
//
//    @Test
//    void testPrintln62() {
//    }
//
//    @Test
//    void testPrintlnAndReset62() {
//    }
//
//    @Test
//    void testPrint63() {
//    }
//
//    @Test
//    void testPrintAndReset63() {
//    }
//
//    @Test
//    void testPrintln63() {
//    }
//
//    @Test
//    void testPrintlnAndReset63() {
//    }
//
//    @Test
//    void testPrint64() {
//    }
//
//    @Test
//    void testPrintAndReset64() {
//    }
//
//    @Test
//    void testPrintln64() {
//    }
//
//    @Test
//    void testPrintlnAndReset64() {
//    }
//
//    @Test
//    void testToString() {
//    }
//
//    @Test
//    void testEquals() {
//    }
//
//    @Test
//    void testHashCode() {
//    }
//
//    private void testAllFields(String text, String foregroundColor, String backgroundColor, String effect) {
//        assertEquals(text, colter.getText(), "Actual text doesn't match expected");
//        assertEquals(foregroundColor, colter.getFG(), "Actual foreground color doesn't match expected");
//        assertEquals(backgroundColor, colter.getBG(), "Actual background color doesn't match expected");
//        assertEquals(effect, colter.getEffect(), "Actual effect doesn't match expected");
//        colter.printlnAndReset("    TEST PASSED    ");
//    }
}