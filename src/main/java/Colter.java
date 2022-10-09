import modificators.BGColor;
import modificators.Effect;
import modificators.FGColor;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;
import java.awt.Color;

import static modificators.BGColor.BG_DEFAULT;
import static modificators.FGColor.FG_DEFAULT;

/**
 * The {@code Colter} class provides objects which are able to console foreground
 * and background text color and effects control. Colter object can change text
 * attributes for a console without printing in it and then these changes will
 * be applied to any console text, if not overridden. Colter object can also print
 * any Object using its specified method {@code toString()}.
 * <p>
 * Colors can be specified in three different ways:
 * <ul>
 *      <li> by {@code Enum} of 16 basic colors (there are colors names specified for each color);
 *      <li> by {@code Color} object;
 *      <li> by {@code int} color number of 256 colors;
 *      <li> by {@code (R, G, B)} codes (0..255 each component) of the 24-bit true color.
 * </ul>
 * Colors can be set separately for text foreground, and background in different ways.
 * <p>
 * Also, text style can be specified by style name from {@code Enum} (for example {@code Bold, Italic} etc.)
 * <p>
 * {@code Colter} object will store its parameters which have been set by {@code set()} methods or
 * passed to the constructor.<br>
 * Each object can be created with almost any set of parameters:
 * <ul>
 *      <li> with or without Object to print;
 *      <li> with or without text foreground color;
 *      <li> with or without text background color;
 *      <li> with or without text style.
 * </ul>
 * Any of these parameters can be overwritten by {@code set()} methods at any time.
 * Colter objects allow method chaining to set parameters.
 * <p>
 * Using {@code print()} methods with attributes passed into them makes able to
 * temporarily override parameters for the current time without overwriting them in the
 * {@code Colter} object.
 * <p>
 *     Examples of using
 * <blockquote><pre>
 *     String s = "abc";
 *     Colter col = new Colter(s);
 *     col.setFG(FG_RED);
 *     col.setBG(BG_BLACK);
 *     col.println();
 *
 *     Colter col = new Colter("line");
 *     col
 *          .setBG(BG_RED)
 *          .setFG(120, 255, 255)
 *          .printlnAndReset();
 *     col.println("temp string");
 *
 *     System.out.println(col);
 * </pre></blockquote>
 *
 * @author  Viacheslav Shuminskyi
 *
 * @see     Color
 * @see     FGColor
 * @see     BGColor
 * @see     Effect
 *
 * @version 1.0
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "SpellCheckingInspection"})
public class Colter {
    private static final String RESET = "\u001B[0m";
    private String text = "";
    private String foregroundColor = "";
    private String backgroundColor = "";
    private String effect = "";
    private Effect[] effects = new Effect[0];

    /**
     * Default constructor without any parameters. Creates an object which does not store parameters.
     * Parameters can be set later using {@code set()} methods
     */
    public Colter() {
    }

    /**
     * Constructor with Object as a parameter. If Object is an instance of FGColor, BGColor or
     * Effect Enum the corresponding parameter will be set. Otherwise, {@code Objet.toString()}
     * will be written to the {@code text} parameter.
     * @param   o   Object that can be as text modificator as text value.
     */
    public Colter(@NotNull Object o) {
               if (o instanceof FGColor) {
            foregroundColor = o.toString();
        } else if (o instanceof BGColor) {
            backgroundColor = o.toString();
        } else if (o instanceof Effect) {
            effect = o.toString();
            effects = new Effect[]{(Effect) o};
        } else if (o instanceof Effect[]) {
            effect = effect((Effect[]) o);
            effects = (Effect[]) o;
        } else {
            text = o.toString();
        }
    }

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        foregroundColor = fgColor.toString();
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull Color fgColor, @NotNull BGColor bgColor) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(int fgColor, @NotNull BGColor bgColor) {
        foregroundColor = fg(fgColor);
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with foreground and background colors as parameters. The foreground color is represented by
     * the 24-bit true-color sRGB model, where each of the components <i>(Red, Green, Blue)</i>
     * can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(@NotNull FGColor fgColor, @NotNull Color bgColor) {
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with object and foreground and background colors as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(@NotNull Color fgColor, @NotNull Color bgColor) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(int fgColor, @NotNull Color bgColor) {
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with foreground and background colors as parameters. The foreground color is represented by
     * the 24-bit true-color sRGB model, where each of the components <i>(Red, Green, Blue)</i>
     * can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public Colter(@NotNull FGColor fgColor, int bgColor) {
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor);
    }//

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public Colter(@NotNull Color fgColor, int bgColor) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor);
    }//

    /**
     * Constructor with foreground and background colors as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public Colter(int fgColor, int bgColor) {
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor);
    }//

    /**
     * Constructor with foreground and background colors as parameters. The background color is represented by
     * the 24-bit true-color sRGB model, where each of the components <i>(Red, Green, Blue)</i>
     * can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public Colter(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
    }//

    /**
     * Constructor with foreground and background colors as parameters. The background color is represented by
     * the 24-bit true-color sRGB model, where each of the components <i>(Red, Green, Blue)</i>
     * can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public Colter(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
    }//

    /**
     * Constructor with foreground and background colors as parameters.
     * Foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public Colter(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
    }//

    /**
     * Constructor with foreground color and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull FGColor fgColor, @NotNull Effect ... effects) {
        foregroundColor = fgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }//

    /**
     * Constructor with background color and effects as parameters.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull BGColor bgColor, @NotNull Effect ... effects) {
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }//

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        foregroundColor = fgColor.toString();
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }//

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor);
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground and background colors, and effects as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground and background colors, and effects as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground and background colors, and effects as parameters.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(int fgColor, int bgColor, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with foreground and background colors, and effects as parameters.
     * Foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground color as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor) {
        text = o.toString();
        foregroundColor = fgColor.toString();
    }

    /**
     * Constructor with object and background color as parameters.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull Object o, @NotNull BGColor bgColor) {
        text = o.toString();
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with object and effects as parameters.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull Effect ... effects) {
        text = o.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull Object o, int fgColor, @NotNull BGColor bgColor) {
        text = o.toString();
        foregroundColor = fg(fgColor);
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public Colter(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        text = o.toString();
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bgColor.toString();
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with object and foreground and background colors as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(@NotNull Object o, int fgColor, @NotNull Color bgColor) {
        text = o.toString();
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public Colter(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        text = o.toString();
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, int bgColor) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor);
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, int bgColor) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor);
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public Colter(@NotNull Object o, int fgColor, int bgColor) {
        text = o.toString();
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor);
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
    }

    /**
     * Constructor with object, and foreground and background colors as parameters.
     * Foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public Colter(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        text = o.toString();
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
    }

    /**
     * Constructor with object, foreground color and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, background color and effects as parameters.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor);
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bgColor.toString();
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground and background colors, and effects as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground and background colors, and effects as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object and foreground and background colors, and effects as parameters.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgColor);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgColor);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, int fgColor, int bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor);
        backgroundColor = bg(bgColor);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fgColor.toString();
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Constructor with object, foreground and background colors, and effects as parameters.
     * Foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       {@code o.toString()} will be written to the {@code text} value.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     */
    public Colter(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        text = o.toString();
        foregroundColor = fg(fgRed, fgGreen, fgBlue);
        backgroundColor = bg(bgRed, bgGreen, bgBlue);
        this.effects = effects;
        this.effect = effect(effects);
    }

    /**
     * Returns the current <b>text</b> value which will be printed by default.
     * @return  the current {@code text} value.
     */
    public String getText() {
        return text;
    }

    /**
     * Returns the current <b>foreground</b> color value. Printing this value applies the color
     * of the text which will be printed all next times until a new foreground color is printed.
     * @return  the current {@code foreground} color value.
     */
    public String getFG() {
        return foregroundColor;
    }

    /**
     * Returns the current text <b>background</b> color value. Printing this value applies the background color
     * of the text which will be printed all next times until a new background color is printed.
     * @return  the current text {@code background} color value.
     */
    public String getBG() {
        return backgroundColor;
    }

    /**
     * Returns the current text <b>effects</b> value. Printing this value adds corresponding effects to ones
     * that have already been set.<br>
     * @return  the current set of {@code effect} value.
     */
    public String getEffect() {
        return effect;
    }

    /**
     * Returns the current text <b>effects</b> array. This array can be passed into a constructor or
     * printing method.
     * @return  array of the current {@code effect} values.
     */
    public Effect[] getEffects() {
        return effects;
    }

    /**
     * Sets the Object's {@code toString()} as the text value which will be prited by default.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   o   the result of the {@code o.toString()} will be written to the text value.
     * @return  {@code this}
     */
    public Colter setText(@NotNull Object o) {
        this.text = o.toString();
        return this;
    }

    /**
     * Sets the text <b>effects</b> value.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter setEffect(@NotNull Effect ... effects) {
        this.effects = effects;
        this.effect = effect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   color   {@code FGColor} value which will be set as the foreground color.
     * @return  {@code this}
     */
    public Colter setFG(@NotNull FGColor color) {
        foregroundColor = color.toString();
        return this;
    }

    /**
     * Sets the text <b>foreground</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   color   {@code int} foreground color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter setFG(int color) {
        checkColor(color);
        foregroundColor = fg(color);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   color {@link Color} object, which represents the foreground color.
     * @return  {@code this}
     */
    public Colter setFG(@NotNull Color color) {
        foregroundColor = fg(color.getRed(), color.getGreen(), color.getBlue());
        return this;
    }

    /**
     * Sets the text <b>foreground</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   red   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   green {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   blue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @return  {@code this}
     */
    public Colter setFG(int red, int green, int blue) {
        foregroundColor = fg(red, green, blue);
        return this;
    }

    /**
     * Sets the text <b>default foreground</b> color value.
     * Returns this {@link Colter} object to allow method chaining.
     * @return  {@code this}
     */
    public Colter setDefaultFG() {
        foregroundColor = FG_DEFAULT.toString();
        return this;
    }

    /**
     * Sets the text <b>background</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   color   {@link BGColor} value which will be set as the background color.
     * @return  {@code this}
     */
    public Colter setBG(@NotNull BGColor color) {
        backgroundColor = color.toString();
        return this;
    }

    /**
     * Sets the text <b>background</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   color   {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter setBG(int color) {
        backgroundColor = bg(color);
        return this;
    }

    /**
     * Sets the text <b>background</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * @param   color {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter setBG(@NotNull Color color) {
        backgroundColor = bg(color.getRed(), color.getGreen(), color.getBlue());
        return this;
    }

    /**
     * Sets the text <b>background</b> color value.
     * Returns this {@code Colter} object to allow method chaining.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   red   {@code int} value of the <b>Red</b> component of the background color.
     * @param   green {@code int} value of the <b>Green</b> component of the background color.
     * @param   blue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter setBG(int red, int green, int blue) {
        backgroundColor = bg(red, green, blue);
        return this;
    }

    /**
     * Sets the text <b>default background</b> color value.
     * Returns this {@link Colter} object to allow method chaining.
     * @return  {@code this}
     */
    public Colter setDefaultBG() {
        backgroundColor = BG_DEFAULT.toString();
        return this;
    }

    /**
     * Sets the text <b>default effect</b> value.
     * Returns this {@link Colter} object to allow method chaining.
     * @return  {@code this}
     */
    public Colter setDefaultEffect() {
        String fore = getFG();
        String back = getBG();
        reset();
        foregroundColor = fore;
        backgroundColor = back;
        return this;
    }

    /**
     * Sets the text <b>foreground</b> color and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>background</b> color and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(int fgColor, int bgColor, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }
    
    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> color value.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor) {
        setFG(fgColor);
        return this;
    }

    /**
     * Sets the text <b>background</b> color value.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull BGColor bgColor) {
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, @NotNull BGColor bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, @NotNull Color bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, @NotNull Color bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(int fgColor, @NotNull BGColor bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(int fgColor, @NotNull Color bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, int bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, int bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter set(int fgColor, int bgColor) {
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter set(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()} and text <b>effects</b> value.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Effect ... effects) {
        setText(o);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b> color
     * and <b>effects</b> values. Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>background</b> color
     * and <b>effects</b> values. Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setText(o);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgColor, int bgColor, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setText(o);
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b> color value.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor) {
        setText(o);
        setFG(fgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>background</b> color value.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull BGColor bgColor) {
        setText(o);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgColor, @NotNull BGColor bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgColor, @NotNull Color bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        setText(o);
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        setText(o);
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, int bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, int bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgColor, int bgColor) {
        setText(o);
        setFG(fgColor);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        setText(o);
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        setText(o);
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter set(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        setText(o);
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter setFG8_BG24(int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter setFG8_BG24(int fgColor, int bgRed, int bgGreen, int bgBlue) {
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter setFG8_BG24(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        text = o.toString();
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The background color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @return  {@code this}
     */
    public Colter setFG8_BG24(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue) {
        text = o.toString();
        setFG(fgColor);
        setBG(bgRed, bgGreen, bgBlue);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter setFG24_BG8(int fgRed, int fgGreen, int fgBlue, int bgColor, @NotNull Effect ... effects) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the text <b>foreground</b> and <b>background</b> colors values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter setFG24_BG8(int fgRed, int fgGreen, int fgBlue, int bgColor) {
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors, and <b>effects</b> values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style. 
     *                  Can be passed in separated by comma.
     * @return  {@code this}
     */
    public Colter setFG24_BG8(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgColor, @NotNull Effect ... effects) {
        text = o.toString();
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        setEffect(effects);
        return this;
    }

    /**
     * Sets the <b>text</b> value from the given {@link Object}{@code .toString()}, text <b>foreground</b>
     * and <b>background</b> colors values.
     * The foreground color is represented by the 24-bit true-color sRGB model,
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * Returns this {@link Colter} object to allow method chaining.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @return  {@code this}
     */
    public Colter setFG24_BG8(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgColor) {
        text = o.toString();
        setFG(fgRed, fgGreen, fgBlue);
        setBG(bgColor);
        return this;
    }

    /**
     * Resets the text <b>foreground</b> and <b>background</b> colors, and <b>effects</b> values for
     * {@code this} and discards the console changes.
     * Returns this {@link Colter} object to allow method chaining.
     * @return  {@code this}
     */
    public Colter reset() {
        foregroundColor = "";
        backgroundColor = "";
        effect = "";
        effects = new Effect[0];
        System.out.print(RESET);
        return this;
    }

    /**
     * Prints the current text with colors and effects, which were applied to {@code this},
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     */
    public void print() {
        print(foregroundColor + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with colors and effects, which were applied to {@code this},
     * to the console without line break, and then sets the console to default.
     */
    public void printAndReset() {
        print(foregroundColor + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the current text with colors and effects, which were applied to {@code this},
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     */
    public void println() {
        println(foregroundColor + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with colors and effects, which were applied to {@code this},
     * to the console with line break, and then sets the console to default.
     */
    public void printlnAndReset() {
        println(foregroundColor + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with colors and effects, which were applied to {@code this}, 
     * to the console without line break. These text settings will be applied to the console
     * and text which will be printed next will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     */
    public void print(@NotNull Object o) {
        print(foregroundColor + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with colors and effects, which were applied to {@code this},
     * to the console without line break, and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     */
    public void printAndReset(@NotNull Object o) {
        print(foregroundColor + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with colors and effects, which were applied to {@code this},
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     */
    public void println(@NotNull Object o) {
        println(foregroundColor + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with colors and effects, which were applied to {@code this},
     * to the console with line break, and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     */
    public void printlnAndReset(@NotNull Object o) {
        println(foregroundColor + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void print(@NotNull FGColor fgColor) {
        print(fgColor + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console without line break, and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void printAndReset(@NotNull FGColor fgColor) {
        print(fgColor + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void println(@NotNull FGColor fgColor) {
        println(fgColor + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console with line break, and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void printlnAndReset(@NotNull FGColor fgColor) {
        println(fgColor + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor) {
        print(fgColor + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console without line break, and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor) {
        print(fgColor + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor) {
        println(fgColor + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the background color and effects, which were applied to {@code this},
     * and with the custom foreground color, to the console with line break, and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor) {
        println(fgColor + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(@NotNull BGColor bgColor) {
        print(foregroundColor + bgColor + effect + text);
    }

    /**
     * Prints the current text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console without line break, and then sets the console to default.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(@NotNull BGColor bgColor) {
        print(foregroundColor + bgColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(@NotNull BGColor bgColor) {
        println(foregroundColor + bgColor + effect + text);
    }

    /**
     * Prints the current text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console with line break, and then sets the console to default.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(@NotNull BGColor bgColor) {
        println(foregroundColor + bgColor + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(@NotNull Object o, @NotNull BGColor bgColor) {
        print(foregroundColor + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console without line break, and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(@NotNull Object o, @NotNull BGColor bgColor) {
        print(foregroundColor + bgColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(@NotNull Object o, @NotNull BGColor bgColor) {
        println(foregroundColor + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color and effects, which were applied to {@code this},
     * and with the custom background color, to the console with line break, and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull BGColor bgColor) {
        println(foregroundColor + bgColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(@NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        print("" + fgColor + bgColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console without line break, and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(@NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        print("" + fgColor + bgColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(@NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        println("" + fgColor + bgColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console with line break, and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        println("" + fgColor + bgColor + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        print("" + fgColor + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        print("" + fgColor + bgColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        println("" + fgColor + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom background and foreground, to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor) {
        println("" + fgColor + bgColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Effect ... effects) {
        print(foregroundColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console without line break,
     * and then sets the console to default.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Effect ... effects) {
        print(foregroundColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Effect ... effects) {
        println(foregroundColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console with line break,
     * and then sets the console to default.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Effect ... effects) {
        println(foregroundColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull Effect ... effects) {
        print(foregroundColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull Effect ... effects) {
        print(foregroundColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull Effect ... effects) {
        println(foregroundColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the colors, which were applied to {@code this},
     * and with the custom effects, to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Effect ... effects) {
        println(foregroundColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull FGColor fgColor, @NotNull Effect ... effects) {
        print(fgColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console without line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull FGColor fgColor, @NotNull Effect ... effects) {
        print(fgColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull FGColor fgColor, @NotNull Effect ... effects) {
        println(fgColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console with line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, @NotNull Effect ... effects) {
        println(fgColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Effect ... effects) {
        print(fgColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Effect ... effects) {
        print(fgColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Effect ... effects) {
        println(fgColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the background color, which was applied to {@code this},
     * and with the custom foreground color and effects, to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Effect ... effects) {
        println(fgColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(foregroundColor + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console without line break,
     * and then sets the console to default.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(foregroundColor + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(foregroundColor + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console with line break,
     * and then sets the console to default.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(foregroundColor + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(foregroundColor + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(foregroundColor + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(foregroundColor + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the foreground color, which was applied to {@code this},
     * and with the custom background color and effects, to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(foregroundColor + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print("" + fgColor +  bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print("" + fgColor +  bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println("" + fgColor +  bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println("" + fgColor +  bgColor + effect(effects) + text + RESET);
    }
    
    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print("" + fgColor +  bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print("" + fgColor +  bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println("" + fgColor +  bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println("" + fgColor +  bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Color fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fgColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Color fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fgColor + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(int fgColor, @NotNull BGColor bgColor) {
        print(fg(fgColor) + bgColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(int fgColor, @NotNull BGColor bgColor) {
        print(fg(fgColor) + bgColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(int fgColor, @NotNull BGColor bgColor) {
        println(fg(fgColor) + bgColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(int fgColor, @NotNull BGColor bgColor) {
        println(fg(fgColor) + bgColor + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(@NotNull Object o, int fgColor, @NotNull BGColor bgColor) {
        print(fg(fgColor) + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(@NotNull Object o, int fgColor, @NotNull BGColor bgColor) {
        print(fg(fgColor) + bgColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(@NotNull Object o, int fgColor, @NotNull BGColor bgColor) {
        println(fg(fgColor) + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, @NotNull BGColor bgColor) {
        println(fg(fgColor) + bgColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void print(int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printAndReset(int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void println(int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printlnAndReset(int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void print(@NotNull Object o, int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printAndReset(@NotNull Object o, int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void println(@NotNull Object o, int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgColor) + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgColor) + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + text + RESET);
    }
    
    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void print(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void println(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     */
    public void printlnAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console without line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console with line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor Enum of type {@link BGColor}, which represents one of 16 background colors.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull BGColor bgColor, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bgColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void print(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + text + RESET);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void println(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + text);
    }

    /**
     * Prints the current text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printlnAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + text + RESET);
    }
    
    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void print(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console without line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void println(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * and with the custom foreground and background colors, to the console with line break,
     * and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     */
    public void printlnAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console without line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        print(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the effects, which were applied to {@code this},
     * to the console with line break, and then sets the console to default.
     * The foreground color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgColor {@link Color} object, which represents the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, @NotNull Color bgColor, @NotNull Effect ... effects) {
        String backgroundColor = bg(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue());
        println(fg(fgRed, fgGreen, fgBlue) + backgroundColor + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors 
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void print(@NotNull FGColor fgColor, int bgColor) {
        print(fgColor + bg(bgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printAndReset(@NotNull FGColor fgColor, int bgColor) {
        print(fgColor + bg(bgColor) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void println(@NotNull FGColor fgColor, int bgColor) {
        println(fgColor + bg(bgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, int bgColor) {
        println(fgColor + bg(bgColor) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, int bgColor) {
        print(fgColor + bg(bgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgColor) {
        print(fgColor + bg(bgColor) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, int bgColor) {
        println(fgColor + bg(bgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgColor) {
        println(fgColor + bg(bgColor) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fgColor + bg(bgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fgColor + bg(bgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fgColor + bg(bgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fgColor + bg(bgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fgColor + bg(bgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fgColor + bg(bgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fgColor + bg(bgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fgColor + bg(bgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors 
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void print(@NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printAndReset(@NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void println(@NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printlnAndReset(@NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void print(@NotNull Object o, @NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printAndReset(@NotNull Object o, @NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void println(@NotNull Object o, @NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Color fgColor, int bgColor) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        print(foregroundColor + bg(bgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Color fgColor, int bgColor, @NotNull Effect ... effects) {
        String foregroundColor = fg(fgColor.getRed(), fgColor.getGreen(), fgColor.getBlue());
        println(foregroundColor + bg(bgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors 
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void print(int fgColor, int bgColor) {
        print(fg(fgColor) + bg(bgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printAndReset(int fgColor, int bgColor) {
        print(fg(fgColor) + bg(bgColor) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void println(int fgColor, int bgColor) {
        println(fg(fgColor) + bg(bgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printlnAndReset(int fgColor, int bgColor) {
        println(fg(fgColor) + bg(bgColor) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void print(@NotNull Object o, int fgColor, int bgColor) {
        print(fg(fgColor) + bg(bgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printAndReset(@NotNull Object o, int fgColor, int bgColor) {
        print(fg(fgColor) + bg(bgColor) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void println(@NotNull Object o, int fgColor, int bgColor) {
        println(fg(fgColor) + bg(bgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, int bgColor) {
        println(fg(fgColor) + bg(bgColor) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bg(bgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bg(bgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bg(bgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bg(bgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bg(bgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgColor, int bgColor, @NotNull Effect ... effects) {
        print(fg(fgColor) + bg(bgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bg(bgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgColor {@code int} background color number of {@code 0..255}.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, int bgColor, @NotNull Effect ... effects) {
        println(fg(fgColor) + bg(bgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors 
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components 
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor Enum of type {@link FGColor}, which represents one of 16 foreground colors.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull FGColor fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@link Color} object, which represents the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, @NotNull Color fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fgColor + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(int fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(int fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(int fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(int fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The background color is represented by the 24-bit true-color sRGB model, where each of the components
     * <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgColor {@code int} foreground color number of {@code 0..255}.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgColor, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(bg(bgRed, bgGreen, bgBlue) + fg(fgColor) + effect(effects) + o + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + text + RESET);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + text);
    }

    /**
     * Prints the current text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void print(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console without line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void println(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + o);
    }

    /**
     * Prints the {@code o.toString()} text with effects and the custom foreground and background colors
     * to the console with line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     */
    public void printlnAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect + o + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text + RESET);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text);
    }

    /**
     * Prints the current text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + text + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break.
     * These text settings will be applied to the console and text which will be printed next
     * will have the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void print(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console without line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        print(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o + RESET);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break.
     * These text settings will be applied to the console and text which will be printed next will have
     * the same colors and effects.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void println(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o);
    }

    /**
     * Prints the {@code o.toString()} text with the custom foreground and background colors, and effects,
     * to the console with line break,
     * and then sets the console to default.
     * The foreground and background colors are represented by the 24-bit true-color sRGB model, 
     * where each of the components <i>(Red, Green, Blue)</i> can be in the range of {@code 0..255}.
     * @param   o       the {@link Object} whose {@code .toString()} value is to be printed.
     * @param   fgRed   {@code int} value of the <b>Red</b> component of the foreground color.
     * @param   fgGreen {@code int} value of the <b>Green</b> component of the foreground color.
     * @param   fgBlue  {@code int} value of the <b>Blue</b> component of the foreground color.
     * @param   bgRed   {@code int} value of the <b>Red</b> component of the background color.
     * @param   bgGreen {@code int} value of the <b>Green</b> component of the background color.
     * @param   bgBlue  {@code int} value of the <b>Blue</b> component of the background color.
     * @param   effects Enums of type {@link Effect}, which represent defined text style.
     */
    public void printlnAndReset(@NotNull Object o, int fgRed, int fgGreen, int fgBlue, int bgRed, int bgGreen, int bgBlue, @NotNull Effect ... effects) {
        println(fg(fgRed, fgGreen, fgBlue) + bg(bgRed, bgGreen, bgBlue) + effect(effects) + o + RESET);
    }

    private void print(String text) {
        System.out.print(text);
    }

    private void println(String text) {
        System.out.println(text);
    }

    private static String fg(int color) {
        checkColor(color);
        return ("\u001b[38;5;" + color + "m");
    }

    private static String fg(int red, int green, int blue) {
        checkColor(red, green, blue);
        return ("\u001b[38;2;" + red + ";" + green + ";" + blue + "m");
    }

    private static String bg(int color) {
        checkColor(color);
        return ("\u001b[48;5;" + color + "m");
    }

    private static String bg(int red, int green, int blue) {
        checkColor(red, green, blue);
        return ("\u001b[48;2;" + red + ";" + green + ";" + blue + "m");
    }

    private static void checkColor(int color) {
        String message = "";

        if (color < 0) {
            message = "Color number out of range. Expected: color >= 0, actual: color = " + color;
        }

        if (color > 255) {
            message = "Color number out of range. Expected: color <= 255, actual: color = " + color;
        }

        if (!message.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkColor(int r, int g, int b) {
        String message = "";

        if (r < 0) {
            message += "\nRed component out of range. Expected: r >= 0, actual: r = " + r;
        }

        if (r > 255) {
            message += "\nRed component out of range. Expected: r <= 255, actual: r = " + r;
        }

        if (g < 0) {
            message += "\nGreen component out of range. Expected: g >= 0, actual: g = " + g;
        }

        if (g > 255) {
            message += "\nGreen component out of range. Expected: g <= 255, actual: g = " + g;
        }

        if (b < 0) {
            message += "\nBlue component out of range. Expected: b >= 0, actual: b = " + b;
        }

        if (b > 255) {
            message += "\nBlue component out of range. Expected: b <= 255, actual: b = " + b;
        }

        if (!message.isEmpty()) {
            throw new IllegalArgumentException("At least one argument is invalid:" + message);
        }
    }

    private String effect(Effect ... effects) {
        String effect = "";

        for (Effect e : effects) {
            effect += e;
        }

        return effect;
    }

    @Override
    public String toString() {
        return (foregroundColor + backgroundColor + effect + text);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass() || o.hashCode() != this.hashCode()) {
            return false;
        }

        Colter colter = (Colter) o;
        return (Objects.equals(text, colter.text)
             && Objects.equals(foregroundColor, colter.foregroundColor)
             && Objects.equals(backgroundColor, colter.backgroundColor)
             && Objects.equals(effect, colter.effect));
    }

    @Override
    public int hashCode() {
        return Objects.hash(text, foregroundColor, backgroundColor, effect, Arrays.hashCode(effects)
        );
    }
}