package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class DrawGameBoard {

    private static final int BOARD_SIZE_IN_SQUARES=8;
    private static final int SQUARE_SIZE_IN_CHARS=3;
    private static final int LINE_WIDTH_IN_CHARS=1;
    private static int column=1;
    private static int reverseColumn=8;
    private static String[] headers={" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};

    public static void main(String[] args) {
        // Example usage:
        ChessBoard observerBoard=new ChessBoard();
        observerBoard.resetBoard();
        ChessGame observerChessGame=new ChessGame();
        observerChessGame.setBoard(observerBoard);
        observerChessGame.setTeamTurn(null);
        drawChessboard(observerChessGame);

        ChessBoard blackBoard=new ChessBoard();
        blackBoard.resetBoard();
        ChessGame blackChessGame=new ChessGame();
        blackChessGame.setBoard(blackBoard);
        blackChessGame.setTeamTurn(ChessGame.TeamColor.BLACK);
        drawChessboard(blackChessGame);

        ChessBoard whiteBoard=new ChessBoard();
        whiteBoard.resetBoard();
        ChessGame whiteChessGame=new ChessGame();
        whiteChessGame.setBoard(whiteBoard);
        whiteChessGame.setTeamTurn(ChessGame.TeamColor.WHITE);
        drawChessboard(whiteChessGame);
    }

    static void drawChessboard(ChessGame game) {
        PrintStream out=new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        // pass through ChessGame game
        // iterate through each piece on board and print it

        if (game.getTeamTurn() == null) {
            drawWhiteHeader(out);
            drawWhiteAtBottom(out, game);
            drawWhiteHeader(out);
        } else if (game.getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
            drawWhiteHeader(out);
            drawWhiteAtBottom(out, game);
            drawWhiteHeader(out);
        } else {
            drawBlackHeader(out);
            drawBlackAtBottom(out, game);
            drawBlackHeader(out);
        }

        if (game.getTeamTurn() == null) {
            System.out.println(SET_TEXT_COLOR_YELLOW);
            System.out.println(String.format("You are an observer."));
        } else if (game.getTeamTurn().equals(ChessGame.TeamColor.WHITE)) {
            System.out.println(SET_TEXT_COLOR_BLUE);
            System.out.println(String.format("You are on the WHITE team"));

        } else {
            System.out.println(SET_TEXT_COLOR_RED);
            System.out.println(String.format("You are on the BLACK team"));
        }
    }

    private static void drawBorder(PrintStream out) {
        for (int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {

            if (boardCol == BOARD_SIZE_IN_SQUARES - 1) {
                setMagenta(out);
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }
    }

    static void drawBlackHeader(PrintStream out) {
        column=1;
        reverseColumn=8;
        setMagenta(out);
        drawBorder(out);

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

    static void drawWhiteHeader(PrintStream out) {
        column=1;
        reverseColumn=8;
        setMagenta(out);
        drawBorder(out);

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
    }

    private static void drawColumn(PrintStream out, int column) {
        setMagenta(out);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(" " + column + " ");
    }

    private static void drawWhiteAtBottom(PrintStream out, ChessGame game) {
        for (int boardRow=BOARD_SIZE_IN_SQUARES; boardRow > 0; boardRow--) {
            for (int squareRow=SQUARE_SIZE_IN_CHARS; squareRow > 0; squareRow--) {
                for (int boardCol=BOARD_SIZE_IN_SQUARES; boardCol > 0; boardCol--) {
                    boolean isEvenRow=boardRow % 2 == 0;
                    boolean isEvenCol=boardCol % 2 == 0;
                    boolean isLightSquare=(isEvenRow && isEvenCol) || (!isEvenRow && !isEvenCol);
                    String color=null;

                    if (squareRow != 2 && boardCol == 8) {
                        drawBorder(out);
                    }

                    if (isLightSquare) {
                        setWhite(out);
                        color="WHITE";
                    } else {
                        setBlack(out);
                        color="BLACK";
                    }

                    if (squareRow == 2) {
                        setMagenta(out);
                        out.print(SET_TEXT_COLOR_BLACK);

                        if (boardCol == 8) {
                            drawColumn(out, reverseColumn);
                            reverseColumn--;
                        }

                        int prefixLength=SQUARE_SIZE_IN_CHARS / 2;
                        int suffixLength=SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                        if (color == "WHITE") {
                            setWhite(out);
                        } else {
                            setBlack(out);
                        }
                        out.print(EMPTY.repeat(prefixLength));
                        ChessPiece piece=game.getBoard().getPiece(new ChessPosition(boardRow, boardCol));
                        var squareContent=(piece != null) ? piece.getPieceType() : " ";
                        if (piece != null) {
                            printPlayer(out, squareContent.toString(), piece.getTeamColor());
                        } else {
                            printSpace(out, squareContent.toString());
                        }

                        out.print(EMPTY.repeat(suffixLength));
                        continue;
                    } else {
                        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                    }
                }

                if (squareRow == 2) {
                    drawColumn(out, reverseColumn + 1);
                } else {
                    drawBorder(out);
                }
                resetAll(out);
                out.println();
            }
        }
    }

    private static void drawBlackAtBottom(PrintStream out, ChessGame game) {
        for (int boardRow=0; boardRow < BOARD_SIZE_IN_SQUARES; boardRow++) {
            for (int squareRow=0; squareRow < SQUARE_SIZE_IN_CHARS; ++squareRow) {
                for (int boardCol=0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
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
                            drawColumn(out, column);
                            column++;
                        }

                        int prefixLength=SQUARE_SIZE_IN_CHARS / 2;
                        int suffixLength=SQUARE_SIZE_IN_CHARS - prefixLength - 1;

                        if (color == "WHITE") {
                            setWhite(out);
                        } else {
                            setBlack(out);
                        }
                        out.print(EMPTY.repeat(prefixLength));
                        ChessPiece piece=game.getBoard().getPiece(new ChessPosition(boardRow + 1, boardCol + 1));
                        var squareContent=(piece != null) ? piece.getPieceType() : " ";
                        if (piece != null) {
                            printPlayer(out, squareContent.toString(), piece.getTeamColor());
                        } else {
                            printSpace(out, squareContent.toString());
                        }

                        out.print(EMPTY.repeat(suffixLength));
                        continue;
                    } else {
                        out.print(EMPTY.repeat(SQUARE_SIZE_IN_CHARS));
                    }
                }

                if (squareRow == 1) {
                    drawColumn(out, column - 1);
                } else {
                    drawBorder(out);
                }
                resetAll(out);
                out.println();
            }
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

    private static void printPlayer(PrintStream out, String playerPiece, ChessGame.TeamColor pieceColor) {
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (playerPiece.equals("KING")) {
                playerPiece=WHITE_KING;
            } else if (playerPiece.equals("QUEEN")) {
                playerPiece=WHITE_QUEEN;
            } else if (playerPiece.equals("BISHOP")) {
                playerPiece=WHITE_BISHOP;
            } else if (playerPiece.equals("KNIGHT")) {
                playerPiece=WHITE_KNIGHT;
            } else if (playerPiece.equals("ROOK")) {
                playerPiece=WHITE_ROOK;
            } else if (playerPiece.equals("PAWN")) {
                playerPiece=WHITE_PAWN;
            }
            out.print(SET_TEXT_BOLD);
            out.print(SET_TEXT_COLOR_BLUE);
        } else {
            if (playerPiece.equals("KING")) {
                playerPiece=BLACK_KING;
            } else if (playerPiece.equals("QUEEN")) {
                playerPiece=BLACK_QUEEN;
            } else if (playerPiece.equals("BISHOP")) {
                playerPiece=BLACK_BISHOP;
            } else if (playerPiece.equals("KNIGHT")) {
                playerPiece=BLACK_KNIGHT;
            } else if (playerPiece.equals("ROOK")) {
                playerPiece=BLACK_ROOK;
            } else if (playerPiece.equals("PAWN")) {
                playerPiece=BLACK_PAWN;
            }
            out.print(SET_TEXT_BOLD);
            out.print(SET_TEXT_COLOR_RED);
        }
        out.print(playerPiece);
    }

    private static void printSpace(PrintStream out, String space) {
        space = EMPTY;
        out.print(space);
    }
}
