
package ChessCore.Pieces;
import ChessCore.ChessBoard.ChessBoard;
import ChessCore.Enum.CoordinateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Utils.Constants.*;

public class QueenPiece extends Piece {
    private final String pieceName = QUEEN_PIECE_NAME;
    private final String PC =  this.getPieceColor()+ pieceName;
    public QueenPiece(String name, CoordinateEnum coordinate) {
        super(name, coordinate);
    }

    public QueenPiece createInstance(String color, CoordinateEnum coor) {
        return new QueenPiece(color, coor);
    }

    @Override
    public String getPieceName() {
        return pieceName;
    }
    @Override
    public String getPC() {
        return PC;
    }

    @Override
    public List<CoordinateEnum> getPossibleMoves() {
        List<CoordinateEnum> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(getRightAndLeftPossibleMoves());
        possibleMoves.addAll(getUpAndDownPossibleMoves());
        possibleMoves.addAll(getUpAndDownRightDiagonal());
        possibleMoves.addAll(getUpAndDownLeftDiagonal());
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
            if (isThereObstacle(destinationCoordinate)) {
                return false;
            }
            if (piece != null) {
                if(isThereObstacle(destinationCoordinate)) {
                    return false;
                }

                if(Objects.equals(piece.getPieceColor(), this.getPieceColor()))
                    return false;
            }

            if(!isInCheck(this.getCurrentCoordinate(),destinationCoordinate))
                return true;
        }
        return false;
    }
    @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate, boolean isCheck) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate());
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();

        if (possibleMoves.contains(destinationCoordinate)) {
            Piece piece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
            if (isThereObstacle(destinationCoordinate)) {
                return false;
            }
            if (piece != null) {
                if(isThereObstacle(destinationCoordinate)) {
                    return false;
                }

            }

            if(!isInCheck(this.getCurrentCoordinate(),destinationCoordinate))
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
            Piece piece = chessBoardInstance.getChessBoardPiece(possibleMoves.get(i));
            if(!isValidMove(possibleMoves.get(i)))
                possibleMoves.set(i,null);
            if ( ( piece!= null && piece.getPieceColor().equals(this.getPieceColor()) )
                    || (possibleMoves.get(i)!=null)&&this.isThereObstacle(possibleMoves.get(i))) {
                possibleMoves.set(i, null);
            }
        }
        possibleMoves.removeIf(Objects::isNull);
        return possibleMoves;
    }
}
