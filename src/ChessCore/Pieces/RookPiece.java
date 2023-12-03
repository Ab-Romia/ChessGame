
package ChessCore.Pieces;
import ChessCore.ChessBoard;
import ChessCore.Enum.CoordinateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Utils.Constants.IN_CHECK;
import static ChessCore.Utils.Constants.ROOK_PIECE_NAME;

public class RookPiece extends Piece {
    private final String pieceName = ROOK_PIECE_NAME;
    private final String PC =  this.getPieceColor()+ pieceName;
    private boolean didRookMove = false;

    public boolean getDidRookMove() {
        return didRookMove;
    }

    @Override
    public String getPieceName() {
        return pieceName;
    }
    @Override
    public String getPC() {
        return PC;
    }

    public RookPiece createInstance(String color, CoordinateEnum coor) {
        return new RookPiece(color, coor);
    }

    public RookPiece(String name, CoordinateEnum coordinate) {
        super(name, coordinate);
    }

    @Override
    public List<CoordinateEnum> getPossibleMoves() {
        List<CoordinateEnum> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(this.getRightAndLeftPossibleMoves());
        possibleMoves.addAll(this.getUpAndDownPossibleMoves());
        return possibleMoves;

    }

    @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate());
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();

        if (possibleMoves.contains(destinationCoordinate)) {
            Piece piece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
            if (piece != null) {
                if(isThereObstacle(destinationCoordinate)) {
                    return false;
                }
                return !Objects.equals(piece.getPieceColor(), this.getPieceColor());
            }
            if (isThereObstacle(destinationCoordinate)) {
                return false;
            }
            if (isInCheck(this.getCurrentCoordinate(), destinationCoordinate)) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public List<CoordinateEnum> getValidMoves() {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = getPossibleMoves();
        possibleMoves.removeIf(Objects::isNull);
        for (int i = 0 ; i < possibleMoves.size() ; i++) {
            if (chessBoardInstance.getChessBoardPiece(possibleMoves.get(i)) != null
                    || (possibleMoves.get(i)!=null)&&this.isThereObstacle(possibleMoves.get(i))) {
                possibleMoves.set(i, null);
            }
        }
        possibleMoves.removeIf(Objects::isNull);
        return possibleMoves;
    }


}
