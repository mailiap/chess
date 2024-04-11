package ui;

import java.io.PrintStream;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class DrawGameBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static int column = 1;
    private static int reverseColumn = 8;
    private static String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };
    private static boolean atBottom = true;

    private static void drawBorder(PrintStream out) {
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

            if (boardCol == BOARD_SIZE_IN_SQUARES - 1) {
                setMagenta(out);
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }
    }

    static void drawHeader(PrintStream out) {
        column=1;
        reverseColumn=8;
        setMagenta(out);
        drawBorder(out);

        if (!atBottom) {
            for (int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                int prefixLength=SQUARE_SIZE_IN_CHARS / 2;
                int suffixLength=SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                setMagenta(out);
                out.print(EMPTY.repeat(prefixLength));
                out.print(SET_TEXT_COLOR_BLACK);

                out.print(headers[boardCol]);

                setMagenta(out);
                out.print(EMPTY.repeat(suffixLength));

                if (boardCol == BOARD_SIZE_IN_SQUARES - 1) {
                    setMagenta(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }
            }
            resetAll(out);
            out.println();
        } else {
            for (int boardCol=BOARD_SIZE_IN_SQUARES - 1; boardCol >= 0; --boardCol) {
                int prefixLength=SQUARE_SIZE_IN_CHARS / 2;
                int suffixLength=SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                setMagenta(out);
                out.print(EMPTY.repeat(prefixLength));
                out.print(SET_TEXT_COLOR_BLACK);

                out.print(headers[boardCol]);

                setMagenta(out);
                out.print(EMPTY.repeat(suffixLength));

                if (boardCol == 0) {
                    setMagenta(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
                }
            }
            resetAll(out);
            out.println();
        }
    }

    private static void drawColumn(PrintStream out, int column) {
        setMagenta(out);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + column + " ");
    }

    static void drawChessboard(PrintStream out, boolean whiteAtBottom) {
        atBottom=whiteAtBottom;

        drawHeader(out);

        String[] chessPiecesWhite={WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
        String[] pawnPiecesWhite={WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN, WHITE_PAWN};
        String[] chessPiecesBlack={BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
        String[] pawnPiecesBlack={BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN, BLACK_PAWN};

        if (whiteAtBottom) {
            drawRowOfSquares(out, chessPiecesBlack, 0,"BLACK");
            drawRowOfSquares(out, pawnPiecesBlack, 1, "BLACK");
            for (int i=0; i < 4; i++) {
                drawEmptyRow(out, i);
            }
            drawRowOfSquares(out, pawnPiecesWhite, 6, "WHITE");
            drawRowOfSquares(out, chessPiecesWhite, 7, "WHITE");
        } else {
            drawRowOfSquares(out, chessPiecesWhite, 0, "WHITE");
            drawRowOfSquares(out, pawnPiecesWhite, 1, "WHITE");
            for (int i=0; i < 4; i++) {
                drawEmptyRow(out, i);
            }
            drawRowOfSquares(out, pawnPiecesBlack, 6, "BLACK");
            drawRowOfSquares(out, chessPiecesBlack, 7, "BLACK");
        }
    }

    private static void drawRowOfSquares(PrintStream out, String[] playerPieces, int boardRow, String pieceColor) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                boolean isEvenRow=boardRow % 2 == 0;
                boolean isEvenCol=boardCol % 2 == 0;
                boolean isLightSquare=(isEvenRow && isEvenCol) || (!isEvenRow && !isEvenCol);
                String color=null;

                if (squareRow != 1 && boardCol == 0) {
                    drawBorder(out);
                }

                if (isLightSquare) {
                    setWhite(out);
                    color="WHITE";
                } else {
                    setBlack(out);
                    color="BLACK";
                }

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    setMagenta(out);
                    out.print(SET_TEXT_COLOR_BLACK);

                    if (boardCol == 0) {
                        if (atBottom) {
                            drawColumn(out, column);
                            column++;
                        } else {
                            drawColumn(out, reverseColumn);
                            reverseColumn--;
                        }
                    }

                    int prefixLength=SQUARE_SIZE_IN_CHARS / 2;
                    int suffixLength=SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                    if (color == "WHITE") {
                        setWhite(out);
                    } else {
                        setBlack(out);
                    }
                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, playerPieces[boardCol], boardRow, pieceColor);
                    out.print(EMPTY.repeat(suffixLength));
                    continue;
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }
            }

            if (squareRow == 1) {
                if (atBottom) {
                    drawColumn(out, column-1);
                } else {
                    drawColumn(out, reverseColumn+1);
                }
            } else {
                drawBorder(out);
            }
            resetAll(out);
            out.println();
        }
    }

    private static void drawEmptyRow(PrintStream out, int boardRow) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                boolean isEvenRow=boardRow % 2 == 0;
                boolean isEvenCol=boardCol % 2 == 0;
                boolean isLightSquare=(isEvenRow && isEvenCol) || (!isEvenRow && !isEvenCol);
                String color=null;

                if (squareRow != 1 && boardCol == 0) {
                    drawBorder(out);
                }

                if (isLightSquare) {
                    setWhite(out);
                    color="WHITE";
                } else {
                    setBlack(out);
                    color="BLACK";
                }

                if (squareRow == SQUARE_SIZE_IN_CHARS / 2) {
                    setMagenta(out);
                    out.print(SET_TEXT_COLOR_BLACK);

                    if (boardCol == 0) {
                        if (atBottom) {
                            drawColumn(out, column);
                            column++;
                        } else {
                            drawColumn(out, reverseColumn);
                            reverseColumn--;
                        }
                    }

                    if (color == "WHITE") {
                        setWhite(out);
                    } else {
                        setBlack(out);
                    }
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));

                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                }
            }

            if (squareRow == 1) {
                if (atBottom) {
                    drawColumn(out, column-1);
                } else {
                    drawColumn(out, reverseColumn+1);
                }
            } else {
                drawBorder(out);
            }
            resetAll(out);
            out.println();
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setMagenta(PrintStream out) {
        out.print(SET_BG_COLOR_MAGENTA);
        out.print(SET_TEXT_COLOR_MAGENTA);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void resetAll(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    private static void printPlayer(PrintStream out, String player, int boardRow, String pieceColor) {
        if (pieceColor == "WHITE") {
            out.print(SET_TEXT_BOLD);
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            out.print(SET_TEXT_BOLD);
            out.print(SET_TEXT_COLOR_RED);
        }
        out.print(player);
    }
}
