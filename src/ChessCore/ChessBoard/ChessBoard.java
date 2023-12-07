
package ChessCore.ChessBoard;
import ChessCore.Enum.CoordinateEnum;
import ChessCore.Pieces.*;


import static ChessCore.Utils.Constants.*;

public class ChessBoard {
    private boolean gameEnded = false;
    private String currentTurnColor = WHITE;
    private static ChessBoard chessBoardInstance;
    private ChessBoard() {
        Initializer initializer = new Initializer(this);
        initializer.initializeGame();
    }
    public String getCurrentTurnColor() {
        return currentTurnColor;
    }
    public static ChessBoard getInstance() {
        if (chessBoardInstance == null) {
            chessBoardInstance = new ChessBoard();
        }
        return chessBoardInstance;
    }
    private final Piece[][] chessBoard = new Piece[8][8];
    public Piece getChessBoardPiece(CoordinateEnum coordinateEnum) {
        return chessBoard[coordinateEnum.getXCoordinate()][coordinateEnum.getYCoordinate()];
    }
    public Piece getChessBoardPiece(int xCor, int yCor) {
        return chessBoard[xCor][yCor];
    }
    public void resetBoard() {
        Initializer initializer = new Initializer(this);
        initializer.initializeGame();
        currentTurnColor = WHITE;
        gameEnded = false;

    }
    public boolean isGameEnded() {
        return gameEnded;
    }
    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }
    public void setCurrentTurnColor(String currentTurnColor) {
        this.currentTurnColor = currentTurnColor;
    }
    public void setChessBoardPiece(CoordinateEnum srcCor, CoordinateEnum destCor) {

        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCor);
        srcPiece.setCurrentCoordinate(destCor);
        chessBoardInstance.setPieceInCoordinate(destCor, srcPiece);
        chessBoardInstance.setPieceInCoordinate(srcCor, null);

    }
    void addPieceInCoordinate(CoordinateEnum coordinate, Piece piece) {
        this.chessBoard[coordinate.getXCoordinate()][coordinate.getYCoordinate()] = piece;
    }
    public void setPieceInCoordinate(CoordinateEnum coordinate, Piece piece) {
        this.chessBoard[coordinate.getXCoordinate()][coordinate.getYCoordinate()] = piece;
    }
    public void play(CoordinateEnum srcCor, CoordinateEnum destCor, String name, boolean mode) throws Exception {
        Player player = new Player(this);
        player.play(srcCor, destCor, name);
    }
    public boolean isInsufficient() {
        GameStateChecker checker = new InsufficientChecker();
        return checker.check(this);
    }
    public boolean isCapture(CoordinateEnum srcCor, CoordinateEnum destCor) {
        GameStateChecker checker = new CaptureChecker(srcCor, destCor);
        return checker.check(this);
    }
    public void printChessBoard() {
        for (int col = 7; col >= 0; col--) {
            for (int row = 0; row < 8; row++) {
                Piece piece = getChessBoardPiece(row, col);
                if (piece != null)
                    System.out.print(piece.getPieceName().substring(0, 2) + "-" + piece.getPieceColor().charAt(0) + "  ");
                else
                    System.out.print("X     ");
            }
            System.out.println();
        }
    }

}
