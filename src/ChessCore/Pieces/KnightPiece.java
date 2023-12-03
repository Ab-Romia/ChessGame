package ChessCore.Pieces;

import ChessCore.ChessBoard;
import ChessCore.Enum.CoordinateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Utils.Constants.KNIGHT_PIECE_NAME;

public class KnightPiece extends Piece {


    private final String pieceName = KNIGHT_PIECE_NAME;
    private final String PC =  this.getPieceColor()+ pieceName;

    public KnightPiece createInstance(String color, CoordinateEnum coor) {
        return new KnightPiece(color, coor);
    }
    @Override
    public String getPieceName() {
        return pieceName;
    }
    @Override
    public String getPC() {
        return PC;
    }
    public KnightPiece(String name, CoordinateEnum coordinate) {
        super(name, coordinate);
    }
    @Override
    public List<CoordinateEnum> getPossibleMoves() {
        int xCorr = this.getCurrentCoordinate().getXCoordinate();
        int yCorr = this.getCurrentCoordinate().getYCoordinate();
        List<CoordinateEnum> possibleMoves = new ArrayList<>();
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr + 1, yCorr + 2));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr + 1, yCorr - 2));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr + 2, yCorr + 1));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr + 2, yCorr - 1));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr - 1, yCorr + 2));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr - 1, yCorr - 2));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr - 2, yCorr + 1));
                possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCorr - 2, yCorr - 1));
        return possibleMoves;
    }
    @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate){
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate());
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        if (possibleMoves.contains(destinationCoordinate)) {
            Piece piece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
            if (piece != null) {
                if (Objects.equals(piece.getPieceColor(), this.getPieceColor())) {
                    return false;
                }
            }
            if (isInCheck(this.getCurrentCoordinate(), destinationCoordinate)) {
                return false;
            }
            return true;
        }
        return false;
    }
    @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate, boolean isCheck){
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate());
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        if (possibleMoves.contains(destinationCoordinate)) {

            if (isInCheck(this.getCurrentCoordinate(), destinationCoordinate)) {
                return false;
            }
            return true;
        }
        return false;
    }
}
