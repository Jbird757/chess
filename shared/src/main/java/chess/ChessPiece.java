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
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        switch (this.type) {
            case KING:
                moveList = kingMoves(board, myPosition, row, col);
                break;
            case QUEEN:
                break;
            case BISHOP:
                break;
            case KNIGHT:
                break;
            case ROOK:
                break;
            case PAWN:
                break;
        }

        return moveList;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> kingMoveList = new ArrayList<>();
        int[] spaces = {-1, 0, 1};

        for (int r: spaces) {
            for (int c: spaces) {
                //Initialize new space
                int trialRow = row+r;
                int trialCol = col+c;
                ChessPosition trialPosition = new ChessPosition(trialRow, trialCol);

                // First check for range errors
                if ((trialRow) < 1 || (trialRow) > 8 || (trialCol) < 1 || (trialCol) > 8) {
                    continue;
                }

                // Then check to see if there is a piece already on the square
                if (board.getPiece(trialPosition) != null) {
                    if (board.getPiece(trialPosition).getTeamColor() != this.getTeamColor()) {
                        kingMoveList.add(new ChessMove(myPosition, trialPosition, null));
                        continue;
                    }
                    continue;
                }

                // If none of the above, is valid move
                kingMoveList.add(new ChessMove(myPosition, trialPosition, null));
            }
        }
        return kingMoveList;
    }
}
