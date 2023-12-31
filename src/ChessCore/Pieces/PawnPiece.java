
package ChessCore.Pieces;
import ChessCore.ChessBoard.ChessBoard;
import ChessCore.Enum.CoordinateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Utils.Constants.BLACK;

import static ChessCore.Utils.Constants.PAWN_PIECE_NAME;
import static ChessCore.Utils.Constants.WHITE;
import static java.lang.Math.abs;

public class PawnPiece extends Piece {

    private final String pieceName = PAWN_PIECE_NAME;
    private String PC =  this.getPieceColor()+ pieceName;
    private int initSquareMovement = 0;
    private boolean passant = true;
    private boolean isEnPassantValid = true;
    private String promoteTo = "";



    public String getPromoteTo() {
        return promoteTo;
    }
    public PawnPiece copy() {
    PawnPiece newPawn = new PawnPiece(this.getPieceColor(),this.getCurrentCoordinate());
    newPawn.PC = this.PC;
    newPawn.isEnPassantValid = this.isEnPassantValid;
    newPawn.initSquareMovement = this.initSquareMovement;
    newPawn.passant = this.passant;
    newPawn.promoteTo = this.promoteTo;
    return newPawn;
}

    public void testIsEnPassantValid() {

        ChessBoard chessBoard = ChessBoard.getInstance();
        int col = this.getPieceColor().equals(WHITE) ? 3 : 4;
        for (int row = 0 ; row < 8 ; row++) {
            Piece piece = chessBoard.getChessBoardPiece(row, col);
            if (piece instanceof PawnPiece) {
                PawnPiece p = (PawnPiece) piece;
                if (p.getPieceColor().equals(this.getPieceColor())||p.initSquareMovement!=2) {
                    p.isEnPassantValid = false;
                }
            }
        }
        if(!passant)
            this.isEnPassantValid = false;
        else {
            this.isEnPassantValid = true;
            passant = false;
        }
    }

    public Boolean isPromoted() {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        int yCorr = this.getCurrentCoordinate().getYCoordinate();
        if (this.getPromoteTo().isEmpty()) {
            return false;
        }
        if ((this.getPieceColor().equals(WHITE) && yCorr == 7)
                || this.getPieceColor().equals(BLACK) && yCorr == 0) {

            switch (this.getPromoteTo()) {
                case "K" : {
                    chessBoardInstance.setPieceInCoordinate(this.getCurrentCoordinate(), new KnightPiece(this.getPieceColor(), this.getCurrentCoordinate()));
                    this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));
                    return true;
                }
                case "R" : {
                    chessBoardInstance.setPieceInCoordinate(this.getCurrentCoordinate(), new RookPiece(this.getPieceColor(), this.getCurrentCoordinate()));
                    this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));
                    return true;
                }
                case "B" : {
                    chessBoardInstance.setPieceInCoordinate(this.getCurrentCoordinate(), new BishopPiece(this.getPieceColor(), this.getCurrentCoordinate()));
                    this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));
                    return true;
                }
                default : {
                    chessBoardInstance.setPieceInCoordinate(this.getCurrentCoordinate(), new QueenPiece(this.getPieceColor(), this.getCurrentCoordinate()));
                    this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public void setPromoteTo(String promoteTo) {
        this.promoteTo = promoteTo;
    }
    public void setInitSquareMovement(int initSquareMovement) {
        this.initSquareMovement = initSquareMovement;
    }
    @Override
    public String getPieceName() {
        return pieceName;
    }
    @Override
    public String getPC() {
        return PC;
    }

    public PawnPiece(String name, CoordinateEnum coordinate) {
        super(name, coordinate);
    }

    @Override
    public List<CoordinateEnum> getPossibleMoves() {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> piecePossibleMoves = new ArrayList<>();
        int xCorr = this.getCurrentCoordinate().getXCoordinate();
        int yCorr = this.getCurrentCoordinate().getYCoordinate();

        int frontOrRightSign = this.getFrontOrRightSign();
        int downOrLeftSign = this.getDownOrLeftSign();
        int newX, newY;
        newX = xCorr;


        newY = yCorr + frontOrRightSign;
        if (CoordinateEnum.getCoordinateEnum(newX, newY) != null  // check if outside board
                && chessBoardInstance.getChessBoardPiece(newX, newY) == null) {
            piecePossibleMoves.add(CoordinateEnum.getCoordinateEnum(newX, newY));
        }

        if (piecePossibleMoves.size() == 1) {
            newY = yCorr + (2 * frontOrRightSign);
            if (yCorr == 1 || yCorr == 6 // check if first time
                    && CoordinateEnum.getCoordinateEnum(newX, newY) != null // check if outside board
                    && chessBoardInstance.getChessBoardPiece(newX, newY) == null) { //check if a piece exist in the position
                piecePossibleMoves.add(CoordinateEnum.getCoordinateEnum(newX, newY));
            }
        }

        newY = yCorr + frontOrRightSign;
        newX = xCorr + frontOrRightSign;
        if (CoordinateEnum.getCoordinateEnum(newX, newY) != null) {
            piecePossibleMoves.add(CoordinateEnum.getCoordinateEnum(newX, newY));
        }
        newX = xCorr + downOrLeftSign;
        if (CoordinateEnum.getCoordinateEnum(newX, newY) != null) {
            piecePossibleMoves.add(CoordinateEnum.getCoordinateEnum(newX, newY));
        }
        return piecePossibleMoves;

    }

    @Override
    public List<CoordinateEnum> getValidMoves() {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = getPossibleMoves();
        for (int i = 0; i < possibleMoves.size(); i++) {
            if(possibleMoves.get(i) == null)
                continue;
            Piece possibleMove = chessBoardInstance.getChessBoardPiece(possibleMoves.get(i));
            if (possibleMove != null && (possibleMoves.get(i).getXCoordinate() == this.getCurrentCoordinate().getXCoordinate())
                    || (possibleMove != null && possibleMove.getPieceColor().equals(this.getPieceColor()))
            || (possibleMove == null && possibleMoves.get(i).getXCoordinate() != this.getCurrentCoordinate().getXCoordinate())) {
                possibleMoves.set(i, null);
            }
        }
        possibleMoves.removeIf(Objects::isNull);
        return possibleMoves;
    }

    @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        if (this.getCurrentCoordinate().getYCoordinate() == 1 || this.getCurrentCoordinate().getYCoordinate() == 6) {
            if (destinationCoordinate.getYCoordinate() == 3 || destinationCoordinate.getYCoordinate() == 4) {
                this.initSquareMovement = 2;
            }
        }
        if (isEnPassant(destinationCoordinate)) {
            return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
        }
        if (dstPiece != null) {
            if (Objects.equals(dstPiece.getPieceColor(), this.getPieceColor())) {
                return false;
            }
        }
        if (getValidMoves().contains(destinationCoordinate)) {
            if (dstPiece != null&&Objects.equals(dstPiece.getPieceColor(), this.getPieceColor()))
                    return false;

            if (dstPiece == null) {
                if(isInCheck(this.getCurrentCoordinate(),destinationCoordinate))
                    return false;
                return this.getCurrentCoordinate().getXCoordinate() == destinationCoordinate.getXCoordinate();
            }
            return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
        }
        if (!isPromoted()) {
            if (getValidMoves().contains(destinationCoordinate)) {
                Piece piece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
                if (piece != null) {
                    if (Objects.equals(piece.getPieceColor(), this.getPieceColor())) {
                        return false;
                    }
                }
                return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
            }
            return false;
        }
        else {
            this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));
            return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
        }
    }
 @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate, boolean isCheck) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        if (this.getCurrentCoordinate().getYCoordinate() == 1 || this.getCurrentCoordinate().getYCoordinate() == 6) {
            if (destinationCoordinate.getYCoordinate() == 3 || destinationCoordinate.getYCoordinate() == 4) {
                this.initSquareMovement = 2;
            }
        }
        if (isEnPassant(destinationCoordinate)) {
            return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
        }

        if (getValidMoves().contains(destinationCoordinate)) {


            if (dstPiece == null) {
                if(isInCheck(this.getCurrentCoordinate(),destinationCoordinate))
                    return false;
                return this.getCurrentCoordinate().getXCoordinate() == destinationCoordinate.getXCoordinate();
            }
            return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
        }
        if (!isPromoted()) {
            if (getValidMoves().contains(destinationCoordinate)) {


                return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
            }
            return false;
        }
        else {
            this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));

            return !isInCheck(this.getCurrentCoordinate(), destinationCoordinate);
        }
    }

    public void doEnpassant(CoordinateEnum destCor)
    {
        ChessBoard chessBoard = ChessBoard.getInstance();
        int xCoor = this.getCurrentCoordinate().getXCoordinate();
        int yCoor = this.getCurrentCoordinate().getYCoordinate();
        int destCoorX = destCor.getXCoordinate();
        int sign = destCoorX - xCoor; // left or right
        CoordinateEnum coordinateEnum = CoordinateEnum.getCoordinateEnum(xCoor + sign, yCoor);
        if(isEnPassant(destCor)) {
            chessBoard.setPieceInCoordinate(coordinateEnum, null);

        }

    }
    public Boolean isEnPassant(CoordinateEnum destCoor) {

        ChessBoard chessBoard = ChessBoard.getInstance();
        int xCoor = this.getCurrentCoordinate().getXCoordinate();
        int yCoor = this.getCurrentCoordinate().getYCoordinate();
        int destCoorX = destCoor.getXCoordinate();
        int destCoorY = destCoor.getYCoordinate();
        int pieceColorSign = this.getPieceColor().equals(WHITE) ? 1 : -1;
        int sign = destCoorX - xCoor; // left or right
        CoordinateEnum coordinateEnum = CoordinateEnum.getCoordinateEnum(xCoor + sign, yCoor);
        Piece p = chessBoard.getChessBoardPiece(coordinateEnum);

        if (p instanceof PawnPiece) {

            PawnPiece pawnPiece = (PawnPiece) p;
            if (!pawnPiece.isEnPassantValid) {
                return false;
            }
            if ((destCoorX != xCoor + sign && destCoorY != (yCoor + pieceColorSign))) {
                if (!pawnPiece.getPieceColor().equals(this.getPieceColor())) {
                    pawnPiece.setInitSquareMovement(0);
                }
            }
            if (pawnPiece.initSquareMovement != 2) {
                return false;
            }
            if ((yCoor == 4 && pawnPiece.getPieceColor().equals(BLACK) || yCoor == 3 &&pawnPiece.getPieceColor().equals(WHITE)) && (abs(destCoorX-xCoor)==1 && destCoorY == (yCoor + pieceColorSign))) {
                Piece piece = chessBoard.getChessBoardPiece(xCoor + sign, yCoor);
                return (piece != null)
                        && (!piece.getPieceColor().equals(this.getPieceColor()))
                        && (CoordinateEnum.getCoordinateEnum(piece.getCurrentCoordinate()) != null);
            }
        }
        return false;
    }

}
