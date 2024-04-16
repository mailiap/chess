package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece; // Assuming you have a ChessPiece class
import chess.ChessPosition;

import java.io.PrintStream;

public class DrawGameBoard1 {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static String[] headers = { " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h " };

    public static void drawBoard(PrintStream out, ChessGame game) {
        drawBorder(out);

        for (int row = 1; row < BOARD_SIZE_IN_SQUARES; row++) {
            drawRow(out, row, game);
        }
        drawBorder(out);

    }

    private static void drawRow(PrintStream out, int row, ChessGame game) {
        for (int col = 1; col < BOARD_SIZE_IN_SQUARES; col++) {
            ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row, col));
            var squareContent = (piece != null) ? piece.getPieceType() : " ";

            out.print("| " + squareContent + " ");
        }
        out.println("|");
    }

    private static void drawBorder(PrintStream out) {
        for (int i = 0; i < BOARD_SIZE_IN_SQUARES; i++) {
            out.print(headers[i]);
        }
        out.println();
    }

    public static void main(String[] args) {
        // Example usage:
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame chessGame = new ChessGame();
        chessGame.setBoard(board);
        chessGame.setTeamTurn(ChessGame.TeamColor.WHITE);
        drawBoard(System.out, chessGame);
    }
}
