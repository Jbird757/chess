import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

import java.util.HashMap;
import java.util.Map;

public class DisplayChessBoard {
    ChessBoard board;
    Boolean playerIsWhite = false;
    String[][] boardLayout = new String[10][10];

    final String whiteSquare = EscapeSequences.SET_BG_COLOR_DARK_GREY;
    final String blackSquare = EscapeSequences.SET_BG_COLOR_BLACK;
    final String emptySquare = "   ";
    final String rim = EscapeSequences.SET_BG_COLOR_LIGHT_GREY + EscapeSequences.RESET_TEXT_COLOR;
    final String blackKing = EscapeSequences.SET_TEXT_COLOR_BLUE+" K ";
    final String blackQueen = EscapeSequences.SET_TEXT_COLOR_BLUE+" Q ";
    final String blackKnight = EscapeSequences.SET_TEXT_COLOR_BLUE+" N ";
    final String blackRook = EscapeSequences.SET_TEXT_COLOR_BLUE+" R ";
    final String blackBishop = EscapeSequences.SET_TEXT_COLOR_BLUE+" B ";
    final String blackPawn = EscapeSequences.SET_TEXT_COLOR_BLUE+" P ";
    final String whiteKing = EscapeSequences.SET_TEXT_COLOR_RED+" K ";
    final String whiteQueen = EscapeSequences.SET_TEXT_COLOR_RED+" Q ";
    final String whiteKnight = EscapeSequences.SET_TEXT_COLOR_RED+" N ";
    final String whiteRook = EscapeSequences.SET_TEXT_COLOR_RED+" R ";
    final String whiteBishop = EscapeSequences.SET_TEXT_COLOR_RED+" B ";
    final String whitePawn = EscapeSequences.SET_TEXT_COLOR_RED+" P ";

    public DisplayChessBoard(String playerColor) {
        this.board = new ChessBoard();
        this.board.resetBoard();
        if (playerColor.equals("WHITE")) {
            this.playerIsWhite = true;
        }
    }

    public DisplayChessBoard(String playerColor, ChessBoard board) {
        this.board = board;
        if (playerColor.equals("WHITE")) {
            this.playerIsWhite = true;
        }
    }

    public void makeBoard() {
        boardLayout[0][0] = rim + "   ";
        boardLayout[0][9] = rim + "   ";
        boardLayout[9][0] = rim + "   ";
        boardLayout[9][9] = rim + "   ";
        Map<Integer, Character> intToChar = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            intToChar.put(i, (char) ('a' + i - 1));
        }


        if (playerIsWhite) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if ((i == 0 || i ==9) && j != 0 && j !=9) {
                        boardLayout[i][j] = rim + " " + intToChar.get(j) + " ";
                    } else if ((j == 9 || j == 0) && (i != 0 && i != 9)) {
                        boardLayout[i][j] = rim + " " + (9 - i) + " ";
                    } else if (j != 0 && j != 9) {
                        boardLayout[i][j] = getChessPiece(i, j);
                    }
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if ((i == 0 || i ==9) && j != 0 && j !=9) {
                        boardLayout[i][j] = rim + " " + intToChar.get(9 -j) + " ";
                    } else if ((j == 9 || j == 0) && (i != 0 && i != 9)) {
                        boardLayout[i][j] = rim + " " + i + " ";
                    }  else if (j != 0 && j != 9) {
                        boardLayout[9-i][9-j] = getChessPiece(i, j);
                    }
                }
            }
        }
    }

    String getChessPiece(int i, int j) {
        String pieceToPut = emptySquare;
        ChessPiece piece = board.getPiece(new ChessPosition(i, j));
        if (piece != null) {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                switch (piece.getPieceType()) {
                    case KING -> pieceToPut = blackKing;
                    case QUEEN -> pieceToPut = blackQueen;
                    case BISHOP -> pieceToPut = blackBishop;
                    case KNIGHT -> pieceToPut = blackKnight;
                    case ROOK -> pieceToPut = blackRook;
                    case PAWN -> pieceToPut = blackPawn;
                }
            } else {
                switch (piece.getPieceType()) {
                    case KING -> pieceToPut = whiteKing;
                    case QUEEN -> pieceToPut = whiteQueen;
                    case BISHOP -> pieceToPut = whiteBishop;
                    case KNIGHT -> pieceToPut = whiteKnight;
                    case ROOK -> pieceToPut = whiteRook;
                    case PAWN -> pieceToPut = whitePawn;
                }
            }
        }

        if ((i+j) % 2 == 0) {
            return whiteSquare + pieceToPut;
        } else {
            return blackSquare + pieceToPut;
        }
    }

    public void printBoard() {
        makeBoard();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print(boardLayout[i][j]);
            }
            System.out.print(EscapeSequences.RESET_BG_COLOR + "\n");
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
    }
}
