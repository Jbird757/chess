package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

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
        this.board.resetBoard();
        this.teamTurn = TeamColor.WHITE;
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
        ChessPiece pieceToMove = this.board.getPiece(startPosition);

        //If there is no piece, there are no valid moves
        if (pieceToMove == null) {
            return null;
        }

        //Get normal moves from ChessPiece and make a copy
        Collection<ChessMove> moves = pieceToMove.pieceMoves(this.board, startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>(moves);

        //Iterate through moves and remove invalid moves
        for (ChessMove move : moves) {
            if (!testCheck(pieceToMove.getTeamColor(), pieceToMove, move)) {
                validMoves.remove(move);
            }
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece pieceToMove = this.board.getPiece(move.getStartPosition());

        if (pieceToMove == null) {
            throw new InvalidMoveException("No piece on that square");
        } else if (pieceToMove.getTeamColor() != this.teamTurn) {
            throw new InvalidMoveException("It's the other team's turn");
        } else if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid Move");
        }

        if (move.getPromotionPiece() != null) {
            this.board.updateBoard(move.getEndPosition(), new ChessPiece(pieceToMove.getTeamColor(), move.getPromotionPiece()));
        } else {
            this.board.updateBoard(move.getEndPosition(), pieceToMove);
        }
        this.board.updateBoard(move.getStartPosition(), null);

        if (pieceToMove.getTeamColor() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        } else {
            setTeamTurn(TeamColor.WHITE);
            }
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
                break;
            }
        }

        //Iterate through other team's pieces, if a move ends on the position of the king, isInCheck is true
        for (Map.Entry<ChessPosition, ChessPiece> entry : otherTeam.entrySet()) {
            ChessPiece piece = entry.getValue();
            Collection<ChessMove> possibleMoves = piece.pieceMoves(this.board, entry.getKey());
            for (ChessMove move : possibleMoves) {
                if (move.getEndPosition().equals(kingPosition)) {
                    isInCheck = true;
                    break;
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
        Map<ChessPosition, ChessPiece> checkTeam = this.board.getTeamPieceLists(teamColor).getFirst();
        boolean escape = false;

        //Run isInCheck, if false, return false
        if (!isInCheck(teamColor)) {
            return false;
        }

        //Pretend to make a move, and then run isInCheck, if at any time isInCheck is false, return false
        for (Map.Entry<ChessPosition, ChessPiece> entry: checkTeam.entrySet()) {
            for (ChessMove move : entry.getValue().pieceMoves(this.board, entry.getKey())) {
                escape = testCheck(teamColor, entry.getValue(), move);
                if (escape) {
                    break;
                }
            }
        }
        return !escape;
    }

    //Makes a move and returns true if move does not end in check, and unmakes move
    boolean testCheck(TeamColor teamColor, ChessPiece piece, ChessMove move) {
        ChessPiece capturedPiece = null;
        boolean captured = false;
        boolean newMoveEndsNoCheck = false;

        //If piece captured by move, get it to add back later
        if (this.board.getPiece(move.getEndPosition()) != null) {
            capturedPiece = this.board.getPiece(move.getEndPosition());
            captured = true;
        }

        //Make move and check if in check
        this.board.updateBoard(move.getEndPosition(), piece);
        this.board.updateBoard(move.getStartPosition(), null);
        if (!isInCheck(teamColor)) {
            newMoveEndsNoCheck = true;
        }

        //Unmake move
        if (captured) {
            this.board.updateBoard(move.getEndPosition(), capturedPiece);
        } else {
            this.board.updateBoard(move.getEndPosition(), null);
        }
        this.board.updateBoard(move.getStartPosition(), piece);

        return newMoveEndsNoCheck;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Map<ChessPosition, ChessPiece> checkTeam = this.board.getTeamPieceLists(teamColor).getFirst();

        //run isInCheck, if true, return false
        if (isInCheck(teamColor)) {
            return false;
        }

        //Get Moves of all pieces on team, if piece can move, check for king in check, if no check, return false
        for (Map.Entry<ChessPosition, ChessPiece> entry: checkTeam.entrySet()) {
            for (ChessMove move : entry.getValue().pieceMoves(this.board, entry.getKey())) {
                if (testCheck(teamColor, entry.getValue(), move)) {
                    return false;
                }
            }
        }

        //If not, return true
        return true;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
