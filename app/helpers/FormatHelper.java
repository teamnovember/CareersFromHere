package helpers;

/**
 * Created by el on 02/03/15.
 */
public abstract class FormatHelper {
    public static String SecToMMSS(int seconds) {
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}
