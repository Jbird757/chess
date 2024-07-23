package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

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
                moveList = bishopMoves(board, myPosition, row, col);
                moveList.addAll(rookMoves(board, myPosition, row, col));
                break;
            case BISHOP:
                moveList = bishopMoves(board, myPosition, row, col);
                break;
            case KNIGHT:
                moveList = knightMoves(board, myPosition, row, col);
                break;
            case ROOK:
                moveList = rookMoves(board, myPosition, row, col);
                break;
            case PAWN:
                moveList = pawnMoves(board, myPosition, row, col);
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

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> rookMoveList = new ArrayList<>();
        boolean isRightBlocked = false;
        boolean isLeftBlocked = false;
        boolean isUpBlocked = false;
        boolean isDownBlocked = false;

        //Vertical and Horizontal
        for (int i = 1; i <= 8; i++) {
            //Right
            if (!isRightBlocked) {
                ChessPosition rightGuess = new ChessPosition(row, col+i);

                // First check for range errors
                if (col+i > 8) {
                    isRightBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isRightBlocked && board.getPiece(rightGuess) != null) {
                    if (board.getPiece(rightGuess).getTeamColor() != this.getTeamColor()) {
                        rookMoveList.add(new ChessMove(myPosition, rightGuess, null));
                    }
                    isRightBlocked = true;
                }

                // If none of the above, is valid move
                if (!isRightBlocked) {
                    rookMoveList.add(new ChessMove(myPosition, rightGuess, null));
                }
            }

            //Left
            if (!isLeftBlocked) {
                ChessPosition leftGuess = new ChessPosition(row, col-i);

                // First check for range errors
                if (col-i < 1) {
                    isLeftBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isLeftBlocked && board.getPiece(leftGuess) != null) {
                    if (board.getPiece(leftGuess).getTeamColor() != this.getTeamColor()) {
                        rookMoveList.add(new ChessMove(myPosition, leftGuess, null));
                    }
                    isLeftBlocked = true;
                }

                // If none of the above, is valid move
                if (!isLeftBlocked) {
                    rookMoveList.add(new ChessMove(myPosition, leftGuess, null));
                }
            }

            //Up
            if (!isUpBlocked) {
                ChessPosition upGuess = new ChessPosition(row+i, col);

                // First check for range errors
                if (row+i > 8) {
                    isUpBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isUpBlocked && board.getPiece(upGuess) != null) {
                    if (board.getPiece(upGuess).getTeamColor() != this.getTeamColor()) {
                        rookMoveList.add(new ChessMove(myPosition, upGuess, null));
                    }
                    isUpBlocked = true;
                }

                // If none of the above, is valid move
                if (!isUpBlocked) {
                    rookMoveList.add(new ChessMove(myPosition, upGuess, null));
                }
            }

            //Down
            if (!isDownBlocked) {
                ChessPosition downGuess = new ChessPosition(row-i, col);

                // First check for range errors
                if (row-i < 1) {
                    isDownBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isDownBlocked && board.getPiece(downGuess) != null) {
                    if (board.getPiece(downGuess).getTeamColor() != this.getTeamColor()) {
                        rookMoveList.add(new ChessMove(myPosition, downGuess, null));
                    }
                    isDownBlocked = true;
                }

                // If none of the above, is valid move
                if (!isDownBlocked) {
                    rookMoveList.add(new ChessMove(myPosition, downGuess, null));
                }
            }
        }
        return rookMoveList;
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> bishopMoveList = new ArrayList<>();
        boolean isRightUpBlocked = false;
        boolean isLeftUpBlocked = false;
        boolean isRightDownBlocked = false;
        boolean isLeftDownBlocked = false;

        //Diagonal
        for (int i = 1; i <= 8; i++) {
            //Right Up
            if (!isRightUpBlocked) {
                ChessPosition rightUpGuess = new ChessPosition(row+i, col+i);

                // First check for range errors
                if (col+i > 8 || row+i > 8) {
                    isRightUpBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isRightUpBlocked && board.getPiece(rightUpGuess) != null) {
                    if (board.getPiece(rightUpGuess).getTeamColor() != this.getTeamColor()) {
                        bishopMoveList.add(new ChessMove(myPosition, rightUpGuess, null));
                    }
                    isRightUpBlocked = true;
                }

                // If none of the above, is valid move
                if (!isRightUpBlocked) {
                    bishopMoveList.add(new ChessMove(myPosition, rightUpGuess, null));
                }
            }

            //Left Up
            if (!isLeftUpBlocked) {
                ChessPosition leftUpGuess = new ChessPosition(row-i, col+i);

                // First check for range errors
                if ((col+i) > 8 || (row-i) < 1) {
                    isLeftUpBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isLeftUpBlocked && board.getPiece(leftUpGuess) != null) {
                    if (board.getPiece(leftUpGuess).getTeamColor() != this.getTeamColor()) {
                        bishopMoveList.add(new ChessMove(myPosition, leftUpGuess, null));
                    }
                    isLeftUpBlocked = true;
                }

                // If none of the above, is valid move
                if (!isLeftUpBlocked) {
                    bishopMoveList.add(new ChessMove(myPosition, leftUpGuess, null));
                }
            }

            //Right Down
            if (!isRightDownBlocked) {
                ChessPosition rightDownGuess = new ChessPosition(row+i, col-i);

                // First check for range errors
                if (row+i > 8 || col-i < 1) {
                    isRightDownBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isRightDownBlocked && board.getPiece(rightDownGuess) != null) {
                    if (board.getPiece(rightDownGuess).getTeamColor() != this.getTeamColor()) {
                        bishopMoveList.add(new ChessMove(myPosition, rightDownGuess, null));
                    }
                    isRightDownBlocked = true;
                }

                // If none of the above, is valid move
                if (!isRightDownBlocked) {
                    bishopMoveList.add(new ChessMove(myPosition, rightDownGuess, null));
                }
            }

            //Left Down
            if (!isLeftDownBlocked) {
                ChessPosition downGuess = new ChessPosition(row-i, col-i);

                // First check for range errors
                if (row-i < 1 || col-i < 1) {
                    isLeftDownBlocked = true;
                }

                // Then check to see if there is a piece already on the square
                if (!isLeftDownBlocked && board.getPiece(downGuess) != null) {
                    if (board.getPiece(downGuess).getTeamColor() != this.getTeamColor()) {
                        bishopMoveList.add(new ChessMove(myPosition, downGuess, null));
                    }
                    isLeftDownBlocked = true;
                }

                // If none of the above, is valid move
                if (!isLeftDownBlocked) {
                    bishopMoveList.add(new ChessMove(myPosition, downGuess, null));
                }
            }
        }
        return bishopMoveList;
    }

    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> knightMoveList = new ArrayList<>();
        int[][] moves = {
                {2, 1},
                {1, 2},
                {-1, 2},
                {-2, 1},
                {-2, -1},
                {-1, -2},
                {1, -2},
                {2, -1}
        };

        for (int[] move : moves) {
            int trialRow = row+move[0];
            int trialCol = col+move[1];
            ChessPosition trialPosition = new ChessPosition(trialRow, trialCol);

            // First check for range errors
            if ((trialRow) < 1 || (trialRow) > 8 || (trialCol) < 1 || (trialCol) > 8) {
                continue;
            }

            // Then check to see if there is a piece already on the square
            if (board.getPiece(trialPosition) != null) {
                if (board.getPiece(trialPosition).getTeamColor() != this.getTeamColor()) {
                    knightMoveList.add(new ChessMove(myPosition, trialPosition, null));
                    continue;
                }
                continue;
            }

            // If none of the above, is valid move
            knightMoveList.add(new ChessMove(myPosition, trialPosition, null));
        }
        return knightMoveList;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, int row, int col) {
        Collection<ChessMove> pawnMoveList = new ArrayList<>();
        boolean move2 = false;
        boolean promote = false;

        //White
        if (this.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //List all possible moves, including captures
            Collection<ChessPosition> possibleMoves = new ArrayList<>(Arrays.asList(
                new ChessPosition(row+1, col),
                new ChessPosition(row+2, col),
                new ChessPosition(row+1, col+1),
                new ChessPosition(row+1, col-1)
            ));

            if (row == 2) {
                move2 = true;
            }
            if (row+1 == 8) {
                promote = true;
            }

            for (ChessPosition move: possibleMoves) {
                // First check for range errors
                if (move.getColumn() < 1 || move.getColumn() > 8) {
                    continue;
                }

                //Skip double move if not enabled
                if (move.getRow() == row+2 && !move2) {
                    continue;
                }

                // Then check to see if there is a piece already on the square
                if (board.getPiece(move) != null) {
                    if (board.getPiece(move).getTeamColor() != this.getTeamColor() && move.getColumn() != col) {
                        //If promoting
                        promote(myPosition, pawnMoveList, promote, move);
                        continue;
                    }
                    continue;
                } else { //If space is empty
                    //Don't allow capture moves
                    if (move.getColumn() != col) {
                        continue;
                    }
                    //If it's the double move, check to make sure the first space isn't blocked
                    if (move.getRow() == row+2 && board.getPiece(new ChessPosition(row+1, col)) != null) {
                        continue;
                    }
                }
                // If none of the above, is valid move. Promotions first
                promote(myPosition, pawnMoveList, promote, move);
            }
        }
        else { //Black
            //List of possible moves
            Collection<ChessPosition> possibleMoves = new ArrayList<>(Arrays.asList(
                    new ChessPosition(row-1, col),
                    new ChessPosition(row-2, col),
                    new ChessPosition(row-1, col-1),
                    new ChessPosition(row-1, col+1)
            ));

            if (row == 7) {
                move2 = true;
            }
            if (row-1 == 1) {
                promote = true;
            }

            for (ChessPosition move: possibleMoves) {
                // First check for range errors
                if (move.getColumn() < 1 || move.getColumn() > 8) {
                    continue;
                }

                //Skip double move if not enabled
                if (move.getRow() == row-2 && !move2) {
                    continue;
                }

                // Then check to see if there is a piece already on the square
                if (board.getPiece(move) != null) {
                    if (board.getPiece(move).getTeamColor() != this.getTeamColor() && move.getColumn() != col) {
                        //If Promoting
                        promote(myPosition, pawnMoveList, promote, move);
                        continue;
                    }
                    continue;
                } else { //If end space is free
                    //Don't allow capture moves
                    if (move.getColumn() != col) {
                        continue;
                    }
                    //If moving 2, make sure first space is free too
                    if (move.getRow() == row-2 && board.getPiece(new ChessPosition(row-1, col)) != null) {
                        continue;
                    }
                }
                // If none of the above, is valid move. Promotions first
                promote(myPosition, pawnMoveList, promote, move);
            }
        }
        return pawnMoveList;
    }

    private void promote(ChessPosition myPosition, Collection<ChessMove> pawnMoveList, boolean promote, ChessPosition move) {
        if (promote) {
            pawnMoveList.add(new ChessMove(myPosition, move, PieceType.KNIGHT));
            pawnMoveList.add(new ChessMove(myPosition, move, PieceType.QUEEN));
            pawnMoveList.add(new ChessMove(myPosition, move, PieceType.BISHOP));
            pawnMoveList.add(new ChessMove(myPosition, move, PieceType.ROOK));
            return;
        }
        pawnMoveList.add(new ChessMove(myPosition, move, null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
