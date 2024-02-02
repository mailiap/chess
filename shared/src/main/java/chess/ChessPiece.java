package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final ChessPiece that=(ChessPiece) o;
        return this.pieceColor == that.pieceColor && this.type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pieceColor, this.type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;
    private ChessPosition currentPosition;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor=pieceColor;
        this.type=type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public void setPosition(int row, int col) {
        this.currentPosition = new ChessPosition(row, col);
    }

    /**
     * @return The current position of the chess piece on the chessboard
     */
    public ChessPosition getPosition() {
        return currentPosition;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> movesList = new ArrayList<>();

        // BISHOP moves
        if (type == PieceType.BISHOP) {
            // Iterate over direction: diagonally
            int[][] bishopMoves = {
                    {1, -1}, {1, 1},
                    {-1, 1}, {-1, -1}
            };
            for (int[] move : bishopMoves) {
                int currRow = myPosition.getRow();
                int currCol = myPosition.getColumn();
                int rowDirection = move[0];
                int colDirection = move[1];

                multiMovesToAdd(board, myPosition, movesList, rowDirection, colDirection, currRow, currCol);
            }
        }

        // KING moves
        if (type == PieceType.KING) {
            // Iterate over directions: horizontally, vertically, and diagonally
            int[][] kingMoves={
                    {1, -1}, {1, 0}, {1, 1},
                    {0, -1}, /*current position*/ {0, 1},
                    {-1, -1}, {-1, 0}, {-1, 1}
            };
            for (int[] move : kingMoves) {
                int currRow = myPosition.getRow() + move[0];
                int currCol = myPosition.getColumn() + move[1];

                oneMoveToAdd(board, myPosition, movesList, currRow, currCol);
            }
        }

        // KNIGHT moves
        if (type == PieceType.KNIGHT) {
            // Iterate over direction: L
            int[][] knightMoves = {
                    {2, -1}, {2, 1},
                    {1, -2}, {1, 2},
                    {-1, 2}, {-1, -2},
                    {-2, 1}, {-2, -1}
            };
            for (int[] move : knightMoves) {
                int currRow = myPosition.getRow() + move[0];
                int currCol = myPosition.getColumn() + move[1];

                oneMoveToAdd(board, myPosition, movesList, currRow, currCol);
            }
        }

        // PAWN moves
        if (type == PieceType.PAWN) {
            int rowDirection = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
            int currRow = myPosition.getRow();
            int currCol = myPosition.getColumn();
            int moveDirection = currRow + rowDirection;

            if (isValidPosition(moveDirection, currCol)) {
                ChessPosition newPosition = new ChessPosition(moveDirection, currCol);

                // Pawn moves forward one square
                if (board.getPiece(newPosition) == null) {
                    addPawnMove(board, myPosition, newPosition, movesList, moveDirection, currCol, moveDirection);
                }

                // Pawn's special two-square move on its first move
                if ((getTeamColor() == ChessGame.TeamColor.WHITE && currRow == 2) || (getTeamColor() == ChessGame.TeamColor.BLACK && currRow == 7) && (board.getPiece(newPosition) == null)) {
                    int twoSquaresForward = currRow + 2 * rowDirection;
                    addPawnMove(board, myPosition, newPosition, movesList, twoSquaresForward, currCol, moveDirection);
                }

                // Pawn captures diagonally
                int[][] pawnMoves={
                        {1, -1}, {1, 1},
                        {-1, 1}, {-1, -1}
                };
                for (int[] move : pawnMoves) {
                    int rowCapture = currRow + move[0];
                    int colCapture = currCol + move[1];

                    ChessPosition capturePosition = new ChessPosition(rowCapture, colCapture);

                    if (board.getPiece(capturePosition) != null && (!board.getPiece(capturePosition).getTeamColor().equals(board.getPiece(myPosition).getTeamColor()))) {
                        if (moveDirection == 8 || moveDirection == 1) {
                            addPawnMove(board, myPosition, newPosition, movesList, rowCapture, colCapture, moveDirection);
                        }
                        else {
                            oneMoveToAdd(board, myPosition, movesList, rowCapture, colCapture);
                        }
                    }
                }
            }
        }

        // QUEEN moves
                if (type == PieceType.QUEEN) {
            // Iterate over directions: horizontally, vertically, and diagonally
            int[][] queenMoves = {
                    {1, -1}, {1, 0}, {1, 1},
                    {0, -1}, /*current position*/ {0, 1},
                    {-1, -1}, {-1, 0}, {-1, 1}
            };
            for (int[] move : queenMoves) {
                int currRow = myPosition.getRow();
                int currCol = myPosition.getColumn();
                int rowDirection = move[0];
                int colDirection = move[1];

                multiMovesToAdd(board, myPosition, movesList, rowDirection, colDirection, currRow, currCol);
            }
        }

        // ROOK moves
        if (type == PieceType.ROOK) {
            // Iterate over directions: horizontally and vertically
            int[][] rookMoves={
                    {0, 1}, {0, -1},
                    {1, 0}, {-1, 0}
            };
            for (int[] move : rookMoves) {
                int currRow = myPosition.getRow();
                int currCol = myPosition.getColumn();
                int rowDirection = move[0];
                int colDirection = move[1];

                multiMovesToAdd(board, myPosition, movesList, rowDirection, colDirection, currRow, currCol);
            }
        }

        return movesList;
    }

    private void oneMoveToAdd(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> movesList, int currRow, int currCol) {
        // Check if the new position is within the board boundaries
        if (isValidPosition(currRow, currCol)) {
            ChessPosition newPosition = new ChessPosition(currRow, currCol);
            // Check if the new position is empty or has an opponent's piece
            if (board.getPiece(newPosition) == null || (!board.getPiece(newPosition).getTeamColor().equals(board.getPiece(myPosition).getTeamColor()))) {
                movesList.add(new ChessMove(myPosition, newPosition));
            }
        }
    }

    private void multiMovesToAdd(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> movesList, int rowDirection, int colDirection, int currRow, int currCol) {
        // Loop to check all diagonal positions
         while (isValidPosition(currRow + rowDirection, currCol + colDirection)) {
             currRow += rowDirection;
             currCol += colDirection;

             ChessPosition newPosition = new ChessPosition(currRow, currCol);

             // Check if position is empty
              if (board.getPiece(newPosition) == null) {
                  movesList.add(new ChessMove(myPosition, newPosition));
              } else {
                  if (!board.getPiece(newPosition).getTeamColor().equals(board.getPiece(myPosition).getTeamColor())) {
                      movesList.add(new ChessMove(myPosition, newPosition));
                      break;
                  } else {
                      // If the position is not empty can't move further
                       break;
                  }
              }
         }
    }

    private void addPawnMove(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition, Collection<ChessMove> movesList, int currRow, int currCol, int moveDirection) {
        if (isValidPosition(currRow, currCol) && board.getPiece(new ChessPosition(currRow, currCol)) == null && moveDirection != 1 && moveDirection != 8) {
            movesList.add(new ChessMove(myPosition, new ChessPosition(currRow, currCol)));
        }
        // Pawn promotion piece
        if ((getTeamColor() == ChessGame.TeamColor.WHITE && moveDirection == 8) || (getTeamColor() == ChessGame.TeamColor.BLACK && moveDirection == 1) && (board.getPiece(newPosition) == null)) {

            // Promote to Queen
            movesList.add(new ChessMove(myPosition, new ChessPosition(moveDirection, currCol), PieceType.QUEEN));

            // Promote to Rook
            movesList.add(new ChessMove(myPosition, new ChessPosition(moveDirection, currCol), PieceType.ROOK));

            // Promote to Bishop
            movesList.add(new ChessMove(myPosition, new ChessPosition(moveDirection, currCol), PieceType.BISHOP));

            // Promote to Knight
            movesList.add(new ChessMove(myPosition, new ChessPosition(moveDirection, currCol), PieceType.KNIGHT));
        }
    }
    
    public static boolean isValidPosition(int row, int col) {
        // 8x8 board
        return (row >= 1 && row <= 8 && col >= 1 && col <= 8);
    }
}
