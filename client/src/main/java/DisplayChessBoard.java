import chess.ChessBoard;
import ui.EscapeSequences;

public class DisplayChessBoard {
    ChessBoard board;
    Boolean playerIsWhite = false;
    String[][] boardLayout = new String[10][10];

    final String whiteSquare = EscapeSequences.SET_BG_COLOR_YELLOW+"   ";
    final String blackSquare = EscapeSequences.SET_BG_COLOR_DARK_GREY+"   ";
    final String rim = EscapeSequences.SET_BG_COLOR_DARK_GREEN+"   ";
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
        boardLayout[0][0] = rim;
        boardLayout[0][9] = rim;
        boardLayout[9][0] = rim;
        boardLayout[9][9] = rim;

        if (playerIsWhite) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    if (i == 0 && j != 0 && j != 9) {
                        boardLayout[i][j] = rim + " "+j+" ";
                    }
                    
                }
            }
        }

    }

    public void printBoard() {

    }
}
