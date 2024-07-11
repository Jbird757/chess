package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        this.board = new ChessBoard();

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
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
        ChessPiece pieceToMove = board.getPiece(startPosition);

        if (pieceToMove == null) {
            return null;
        }

        Collection<ChessMove> moves = pieceToMove.pieceMoves(board, startPosition);
        for (ChessMove move : moves) {
        }

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = board.getPiece(move.getStartPosition());

        if (pieceToMove == null || pieceToMove.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Invalid move 1");
        } else if (!pieceToMove.pieceMoves(board, move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move 2");
        }

        ChessPosition newPosition = move.getEndPosition();
        //Update board lists, run isInCheck, if not, move is valid, if so, revert move and throw exception
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Map<ChessPosition, ChessPiece> checkTeam = this.board.getTeamPieceLists(teamColor).get(0);
        Map<ChessPosition, ChessPiece> otherTeam = this.board.getTeamPieceLists(teamColor).get(1);
        ChessPosition kingPosition = null;
        boolean isInCheck = false;

        //Find King, get position
        for (Map.Entry<ChessPosition, ChessPiece> entry : checkTeam.entrySet()) {
            ChessPiece piece = entry.getValue();
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                kingPosition = entry.getKey();
            }
        }

        //Iterate through other team's pieces, if a move ends on the position of the king, isInCheck is true
        for (Map.Entry<ChessPosition, ChessPiece> entry : otherTeam.entrySet()) {
            ChessPiece piece = entry.getValue();
            Collection<ChessMove> possibleMoves = piece.pieceMoves(board, entry.getKey());
            for (ChessMove move : possibleMoves) {
                if (move.getEndPosition().equals(kingPosition)) {
                    isInCheck = true;
                }
            }
        }
        return isInCheck;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        Map<ChessPosition, ChessPiece> checkTeam = this.board.getTeamPieceLists(teamColor).get(0);
        Map<ChessPosition, ChessPiece> otherTeam = this.board.getTeamPieceLists(teamColor).get(1);
        ChessPosition kingPosition = null;
        Collection<ChessMove> possibleEnemyMoves = new ArrayList<>();
        Collection<ChessMove> kingMoves = new ArrayList<>();

        //Run isInCheck, if false, return false
        if (!isInCheck(teamColor)) {
            return false;
        }

        //Find King, get position
        for (Map.Entry<ChessPosition, ChessPiece> entry : checkTeam.entrySet()) {
            ChessPiece piece = entry.getValue();
            if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                kingPosition = entry.getKey();
                kingMoves = piece.pieceMoves(board, kingPosition);
            }
        }

        //Get set of enemy moves
        for (Map.Entry<ChessPosition, ChessPiece> entry : otherTeam.entrySet()) {
            Collection<ChessMove> moves = entry.getValue().pieceMoves(board, entry.getKey());
            if (moves != null) {
                possibleEnemyMoves.addAll(moves);
            }
        }

        //For all kingMoves, see if new tiles are in check
        Collection<ChessMove> freeKingMoves = kingMoves;
        for (ChessMove move : kingMoves) {
            for (ChessMove enemyMove: possibleEnemyMoves) {
                if (enemyMove.getEndPosition() == move.getEndPosition()) {
                    freeKingMoves.remove(move);
                }
            }
        }

        if (!freeKingMoves.isEmpty()) {
            return false;
        }

        //Pretend to make a move, and then run isInCheck, if at any time isInCheck is false, return false
        return !canEscapeCheck(teamColor);
    }

    boolean canEscapeCheck(TeamColor teamColor) {
        Map<ChessPosition, ChessPiece> checkTeam = this.board.getTeamPieceLists(teamColor).get(0);
        Map<ChessPosition, ChessPiece> checkTeamCopy = checkTeam;
        boolean escape = false;

        for (Map.Entry<ChessPosition, ChessPiece> entry: checkTeam.entrySet()) {
            for (ChessMove move : entry.getValue().pieceMoves(board, entry.getKey())) {
                checkTeamCopy.remove(entry.getKey());
                checkTeamCopy.put(move.getEndPosition(), entry.getValue());
                if (!isInCheck(teamColor)) {
                    escape = true;
                }
                checkTeam.remove(move.getEndPosition());
                checkTeam.put(entry.getKey(), entry.getValue());
            }
        }

        return escape;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
        //run isInCheck, if true, return false
        //Get Moves of all pieces on team, if piece can move, check for king in check, if no check, return false
        //If not, return true
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
