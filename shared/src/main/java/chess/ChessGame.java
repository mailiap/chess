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
        ChessPiece piece = getBoard().getPiece(startPosition);

        if (piece == null) {
            return null;
        }

        setTeamTurn(piece.getTeamColor());
        Collection<ChessMove> legalMoves = piece.pieceMoves(getBoard(), startPosition);
        Collection<ChessMove> validList = new ArrayList<>();

        // input the color of the piece that moves
        // clone = orginal .clone for everytim is works

        for (ChessMove move : legalMoves) {
            ChessBoard newBoard = new ChessBoard(getBoard()); // clone the board
            ChessGame newGame = new ChessGame();
            newGame.setBoard(newBoard);
            ChessPosition start = move.getStartPosition();
            ChessPosition end = move.getEndPosition();

            try {
                movePiece(move, newBoard);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }

            if (!newGame.isInCheck(piece.getTeamColor())) {
                validList.add(move);
            }
            // setting back to orginal position
            newBoard.addPiece(start, piece);
            newBoard.addPiece(end, null);
        }
        return validList;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (getBoard() == null) {
            throw new InvalidMoveException();
        }

//        ChessBoard newBoard = new ChessBoard(getBoard());
        ChessPiece piece = getBoard().getPiece(move.getStartPosition());

        if (piece == null || piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException();
        }

        ChessPosition start = move.getStartPosition();
        Collection<ChessMove> validMoves = validMoves(start);

        if (!validMoves.contains(move)) {
            throw new InvalidMoveException();
        }

        movePiece(move, getBoard());

//        if (isInCheck(piece.getTeamColor())) {
//            throw new InvalidMoveException();
//        }

//        setBoard(newBoard);
        switchTeamTurn();
    }
    public void movePiece(ChessMove move, ChessBoard newBoard) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        // Check if the move is valid (e.g., within the board boundaries)
        if (!ChessPiece.isValidPosition(start.getRow(), start.getColumn()) || !ChessPiece.isValidPosition(end.getRow(), end.getColumn())) {
            throw new InvalidMoveException();
        }

        // Retrieve the piece at the starting position
        ChessPiece piece = newBoard.getPiece(start);

        // Check if there is a piece at the starting position
        if (piece == null) {
            throw new InvalidMoveException();
        }

        // Check if the move is valid for the specific piece
        if (!piece.pieceMoves(newBoard, start).contains(move)) {
            throw new InvalidMoveException();
        }

        // Check if the target position is occupied by a piece of the same team
        ChessPiece targetPiece = newBoard.getPiece(end);
        if (targetPiece != null && targetPiece.getPieceType().equals(piece.getTeamColor())) {
            throw new InvalidMoveException();
        }

        // Perform the move by updating the board state
        if (piece.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
            if (getTeamTurn() == ChessGame.TeamColor.WHITE && start.getRow() == 7 && end.getRow() == 8 || getTeamTurn() == TeamColor.BLACK && start.getRow() == 2 && end.getRow() == 1) {
                // new piece using move for piece type and piece color
                ChessPiece newPiece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
                newBoard.addPiece(end, newPiece);
                newBoard.addPiece(start, null);
            }
            else {
                newBoard.addPiece(end, piece);
                newBoard.addPiece(start, null);
            }
        }
        else {
            newBoard.addPiece(end, piece);
            newBoard.addPiece(start, null);
        }
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
        ChessPosition kingPosition = getBoard().findKingPosition(teamColor);

        if (kingPosition == null) {
            return false;
        }

        TeamColor opponentTeam = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        for (ChessPiece opponentPiece : getColorPieces(opponentTeam)) {
                Collection<ChessMove> opponentMoves = opponentPiece.pieceMoves(getBoard(), opponentPiece.getPosition());
            for (ChessMove opponentendMove : opponentMoves) {
                if (opponentendMove.getEndPosition().equals(kingPosition)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        for (ChessPiece piece : getColorPieces(teamColor)) {
            if (piece.getPieceType().equals(ChessPiece.PieceType.KING)) {
                for (ChessMove move : piece.pieceMoves(getBoard(), piece.getPosition())) {
                    ChessBoard newBoard = new ChessBoard(getBoard());
                    newBoard.addPiece(move.getEndPosition(), piece);
                    if (isInCheck(teamColor)) {
                        return true;
                    }
                }
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
        if (teamColor.equals(getTeamTurn())) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPosition currentPosition = new ChessPosition(row, col);
                    ChessPiece currentPiece = getBoard().getPiece(currentPosition);

                    if (currentPiece != null && currentPiece.getTeamColor().equals(teamColor)) {
                        if (hasValidMoves(teamColor)) {
                            return true;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    public boolean hasValidMoves(TeamColor teamColor) {
        for (ChessPiece piece : getColorPieces(teamColor)) {
            if (!piece.pieceMoves(getBoard(), piece.getPosition()).isEmpty()) {
                return true;
            }
        }
        return false;
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
