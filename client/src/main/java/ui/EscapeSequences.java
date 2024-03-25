package ui;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    private static final String UNICODE_ESCAPE="\u001b";
    private static final String ANSI_ESCAPE="\033";

    public static final String ERASE_SCREEN=ANSI_ESCAPE + "[H" + ANSI_ESCAPE + "[2J";
    public static final String ERASE_LINE=ANSI_ESCAPE + "[2K";

    public static final String SET_TEXT_BOLD=ANSI_ESCAPE + "[1m";
    public static final String SET_TEXT_FAINT=ANSI_ESCAPE + "[2m";
    public static final String RESET_TEXT_BOLD_FAINT=ANSI_ESCAPE + "[22m";
    public static final String SET_TEXT_ITALIC=ANSI_ESCAPE + "[3m";
    public static final String RESET_TEXT_ITALIC=ANSI_ESCAPE + "[23m";
    public static final String SET_TEXT_UNDERLINE=ANSI_ESCAPE + "[4m";
    public static final String RESET_TEXT_UNDERLINE=ANSI_ESCAPE + "[24m";
    public static final String SET_TEXT_BLINKING=ANSI_ESCAPE + "[5m";
    public static final String RESET_TEXT_BLINKING=ANSI_ESCAPE + "[25m";

    private static final String SET_TEXT_COLOR=ANSI_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR=ANSI_ESCAPE + "[48;5;";

    public static final String SET_TEXT_COLOR_BLACK=SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_LIGHT_GREY=SET_TEXT_COLOR + "242m";
    public static final String SET_TEXT_COLOR_DARK_GREY=SET_TEXT_COLOR + "235m";
    public static final String SET_TEXT_COLOR_RED=SET_TEXT_COLOR + "160m";
    public static final String SET_TEXT_COLOR_GREEN=SET_TEXT_COLOR + "46m";
    public static final String SET_TEXT_COLOR_YELLOW=SET_TEXT_COLOR + "226m";
    public static final String SET_TEXT_COLOR_BLUE=SET_TEXT_COLOR + "12m";
    public static final String SET_TEXT_COLOR_MAGENTA=SET_TEXT_COLOR + "5m";
    public static final String SET_TEXT_COLOR_WHITE=SET_TEXT_COLOR + "15m";
    public static final String RESET_TEXT_COLOR=ANSI_ESCAPE + "[0m";

    public static final String SET_BG_COLOR_BLACK=SET_BG_COLOR + "0m";
    public static final String SET_BG_COLOR_LIGHT_GREY=SET_BG_COLOR + "242m";
    public static final String SET_BG_COLOR_DARK_GREY=SET_BG_COLOR + "235m";
    public static final String SET_BG_COLOR_RED=SET_BG_COLOR + "160m";
    public static final String SET_BG_COLOR_GREEN=SET_BG_COLOR + "46m";
    public static final String SET_BG_COLOR_DARK_GREEN=SET_BG_COLOR + "22m";
    public static final String SET_BG_COLOR_YELLOW=SET_BG_COLOR + "226m";
    public static final String SET_BG_COLOR_BLUE=SET_BG_COLOR + "12m";
    public static final String SET_BG_COLOR_MAGENTA=SET_BG_COLOR + "5m";
    public static final String SET_BG_COLOR_WHITE=SET_BG_COLOR + "15m";
    public static final String RESET_BG_COLOR=SET_BG_COLOR + "0m";

//    public static final String WHITE_KING = " ♔ ";
//    public static final String WHITE_QUEEN = " ♕ ";
//    public static final String WHITE_BISHOP = " ♗ ";
//    public static final String WHITE_KNIGHT = " ♘ ";
//    public static final String WHITE_ROOK = " ♖ ";
//    public static final String WHITE_PAWN = " ♙ ";
//    public static final String BLACK_KING = " ♚ ";
//    public static final String BLACK_QUEEN = " ♛ ";
//    public static final String BLACK_BISHOP = " ♝ ";
//    public static final String BLACK_KNIGHT = " ♞ ";
//    public static final String BLACK_ROOK = " ♜ ";
//    public static final String BLACK_PAWN = " ♟ ";
//    public static final String EMPTY = " \u2003 ";

    public static String moveCursorToLocation(int x, int y) {
        return ANSI_ESCAPE + "[" + y + ";" + x + "H";
    }

    public static final String WHITE_KING=" K ";
    public static final String WHITE_QUEEN=" Q ";
    public static final String WHITE_BISHOP=" B ";
    public static final String WHITE_KNIGHT=" N ";
    public static final String WHITE_ROOK=" R ";
    public static final String WHITE_PAWN=" P ";
    public static final String BLACK_KING=" K ";
    public static final String BLACK_QUEEN=" Q ";
    public static final String BLACK_BISHOP=" B ";
    public static final String BLACK_KNIGHT=" N ";
    public static final String BLACK_ROOK=" R ";
    public static final String BLACK_PAWN=" P ";
    public static final String EMPTY="   ";

//    public static final String WHITE_KING = " ♔ "; //" \u2654 ";
//    public static final String WHITE_QUEEN = " ♕ "; //" \u2655 ";
//    public static final String WHITE_BISHOP = " ♗ "; //" \u2656 ";
//    public static final String WHITE_KNIGHT = " ♘ "; //" \u2657 ";
//    public static final String WHITE_ROOK = " ♖ "; //" \u2658 ";
//    public static final String WHITE_PAWN = " ♙ "; //" \u2659 ";
//    public static final String BLACK_KING = " ♚ "; //" \u265A ";
//    public static final String BLACK_QUEEN = " ♛ "; //" \u265B ";
//    public static final String BLACK_BISHOP = " ♝ "; //" \u265C ";
//    public static final String BLACK_KNIGHT = " ♞ "; //" \u265D ";
//    public static final String BLACK_ROOK = " ♜ "; //" \u265E ";
//    public static final String BLACK_PAWN = " ♟ "; //" \u265F ";
//    public static final String EMPTY = " \u2003 ";

}
