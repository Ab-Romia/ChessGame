package ChessCore.ChessBoard;
import ChessCore.Enum.CoordinateEnum;
import ChessCore.Pieces.*;
import static ChessCore.Enum.CoordinateEnum.*;
import java.util.List;

import static ChessCore.Enum.CoordinateEnum.getCoordinateEnum;
import static ChessCore.Utils.Constants.*;
public class Initializer {
    private final ChessBoard chessBoardInstance;
    public Initializer(ChessBoard chessBoardInstance) {
        this.chessBoardInstance = chessBoardInstance;
    }
    public void initializeGame() {
        createPlayerBoard(List.of(a8, b8, c8, d8, e8, f8, g8, h8), BLACK);
        createPlayerBoard(BLACK, 6);
        createPlayerBoard(List.of(a1, b1, c1, d1, e1, f1, g1, h1), WHITE);
        createPlayerBoard(WHITE, 1);
    }
    private void createPlayerBoard(List<CoordinateEnum> piecePos, String player) {

        chessBoardInstance.addPieceInCoordinate(piecePos.get(0), new RookPiece(player, piecePos.get(0)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(1), new KnightPiece(player, piecePos.get(1)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(2), new BishopPiece(player, piecePos.get(2)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(3), new QueenPiece(player, piecePos.get(3)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(4), new KingPiece(player, piecePos.get(4)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(5), new BishopPiece(player, piecePos.get(5)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(6), new KnightPiece(player, piecePos.get(6)));
        chessBoardInstance.addPieceInCoordinate(piecePos.get(7), new RookPiece(player, piecePos.get(7)));
        for(int i =  2; i<6; i++){
            for(int j = 0; j<8; j++){
                chessBoardInstance.addPieceInCoordinate(getCoordinateEnum(j,i), null);
            }
        }
    }

    private void createPlayerBoard(String player, Integer col) {
        for (int row = 0; row < 8; row++) {
            chessBoardInstance.addPieceInCoordinate(getCoordinateEnum(row, col), new PawnPiece(player, getCoordinateEnum(row, col)));
        }
    }
}
