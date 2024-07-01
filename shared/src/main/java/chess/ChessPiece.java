package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moveList = new ArrayList<>();

        switch (this.type) {
            case KING:
                moveList = kingMoves(board, myPosition);
            case QUEEN:
                return null;
            case BISHOP:
                return null;
            case KNIGHT:
                return null;
            case ROOK:
                return null;
            case PAWN:
                return null;
        }

        return moveList;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> kingMoveList = new ArrayList<>();
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[] spaces = {-1, 0, 1};




        for (int r: spaces) {
            for (int c: spaces) {
                // First check for range errors
                if (row + r < 0 || row + r > 8 || col + c < 0 || col + c > 8) {
                    continue;
                }
                // Then check to see if there is a piece already on the square
                if (board.getPiece(myPosition) != null) {
                    continue;
                }
                // If none of the above, is valid move
                kingMoveList.add(new ChessMove(myPosition, new ChessPosition(row+r, col+c), null));
            }
        }


        return kingMoveList;
    }
}
