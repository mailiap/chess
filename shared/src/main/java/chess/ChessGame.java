package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (null == o || this.getClass() != o.getClass()) return false;
        final ChessGame chessGame=(ChessGame) o;
        return Objects.equals(this.board, chessGame.board) && this.team == chessGame.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.board, this.team);
    }

    @Override
    public String toString() {
        return  "board=" + board +
                ", team=" + team +
                '}';
    }

    private ChessBoard board;
    private TeamColor team;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (getBoard() == null) {
            return null;
        }

        ChessPiece piece = getBoard().getPiece(startPosition);

        if (piece == null || !piece.getTeamColor().equals(getTeamTurn())) {
            return null;
        }

        return piece.pieceMoves(getBoard(), startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (getBoard() == null) {
            throw new InvalidMoveException("No chessboard initialized.");
        }

        ChessPiece piece = getBoard().getPiece(move.getStartPosition());

        if (piece == null || piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid move: No piece at the starting position or not the current team's turn.");
        }

        Collection<ChessMove> validMoves = piece.pieceMoves(getBoard(), move.getStartPosition());

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException("Invalid move: The specified move is not a valid move for the piece.");
        }

        ChessBoard newBoard = getBoard(); // Implement a method to clone the board
        movePiece(move);

        if (isInCheck(piece.getTeamColor())) {
            throw new InvalidMoveException("Invalid move: Move puts the team's king in check.");
        }

        // If the move is valid, update the game state
        setBoard(newBoard);
        switchTeamTurn();
    }
    public void movePiece(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        // Check if the move is valid (e.g., within the board boundaries)
        if (!ChessPiece.isValidPosition(start.getRow(), start.getColumn()) || !ChessPiece.isValidPosition(end.getRow(), end.getColumn())) {
            throw new InvalidMoveException("Invalid move: Positions are not within the board boundaries.");
        }

        // Retrieve the piece at the starting position
        ChessPiece piece = getBoard().getPiece(start);

        // Check if there is a piece at the starting position
        if (piece == null) {
            throw new InvalidMoveException("Invalid move: No piece at the starting position.");
        }

        // Check if the move is valid for the specific piece
        if (!piece.pieceMoves(getBoard(), start).contains(move)) {
            throw new InvalidMoveException("Invalid move: The specified move is not a valid move for the piece.");
        }

        // Check if the target position is occupied by a piece of the same team
        ChessPiece targetPiece = getBoard().getPiece(end);
        if (targetPiece != null && targetPiece.getPieceType().equals(piece.getTeamColor())) {
            throw new InvalidMoveException("Invalid move: Target position is occupied by a piece of the same team.");
        }

        // Perform the move by updating the board state
        getBoard().addPiece(end, piece);
        getBoard().addPiece(start, null);
    }

    private void switchTeamTurn() {
        setTeamTurn((getTeamTurn() == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        if (getBoard() == null) {
            return false;
        }

        // Find the position of the king for the specified team
        ChessPosition kingPosition = getBoard().findKingPosition(teamColor);

        if (kingPosition == null) {
            // If the king is not found, the team cannot be in check
            return false;
        }

        // Get the opponent's team color
        TeamColor opponentTeam = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        // Iterate over the opponent's pieces and check if any can capture the king
        for (ChessPiece opponentPiece : getColorPieces(opponentTeam)) {
            Collection<ChessMove> opponentMoves = opponentPiece.pieceMoves(getBoard(), opponentPiece.getPosition());
            for (ChessMove opponentendMove : opponentMoves) {
                if (opponentendMove.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }

        // If no opponent's move can capture the king, the team is not in check
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor) || !hasValidMoves(teamColor)) {
            return false;
        }

        // Check if there is any move that can get the king out of check
        for (ChessPiece piece : getColorPieces(teamColor)) {
            for (ChessMove move : piece.pieceMoves(getBoard(), piece.getPosition())) {
                ChessBoard newBoard = getBoard(); // Implement a method to clone the board
                newBoard.addPiece(move.getEndPosition(), piece);
                if (!isInCheck(teamColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasValidMoves(TeamColor teamColor) {
        for (ChessPiece piece : getColorPieces(teamColor)) {
            if (!piece.pieceMoves(getBoard(), piece.getPosition()).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor) || hasValidMoves(teamColor)) {
            return false;
        }

        // No valid moves and not in check, it's a stalemate
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board=board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
    public Collection<ChessPiece> getColorPieces(ChessGame.TeamColor teamColor) {
        Collection<ChessPiece> teamPieces = new ArrayList<>();

        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece piece = getBoard().getPiece(newPosition);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    teamPieces.add(piece);
                    piece.setPosition(row, col);
                }
            }
        }
        return teamPieces;
    }
}
