
package ChessCore.Pieces;
import ChessCore.ChessBoard;
import ChessCore.Enum.CoordinateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Enum.CoordinateEnum.d6;
import static ChessCore.Enum.CoordinateEnum.f7;
import static ChessCore.Utils.Constants.BISHOP_PIECE_NAME;
import static ChessCore.Utils.Constants.BLACK;

import static ChessCore.Utils.Constants.ENPASSANT;
import static ChessCore.Utils.Constants.IN_VALID_MOVE;
import static ChessCore.Utils.Constants.PAWN_PIECE_NAME;
import static ChessCore.Utils.Constants.QUEEN_PIECE_NAME;
import static ChessCore.Utils.Constants.WHITE;

public class PawnPiece extends Piece {

    private final String pieceName = PAWN_PIECE_NAME;
    private final String PC =  this.getPieceColor()+ pieceName;
    private int initSquareMovement = 0;
    private boolean isEnPassantValid = true;
    private String promoteTo = "";

    public String getPromoteTo() {
        return promoteTo;
    }


    private void testIsEnPassantValid() {
//        System.out.println("Piece at " + this.getCurrentCoordinate());
//        if (this.getCurrentCoordinate() == f7) {
//            System.out.println("");
//        }
        ChessBoard chessBoard = ChessBoard.getInstance();
        int col = this.getPieceColor().equals(WHITE) ? 3 : 4;
        for (int row = 0 ; row < 8 ; row++) {
            Piece piece = chessBoard.getChessBoardPiece(row, col);
            if (piece instanceof PawnPiece) {
                PawnPiece p = (PawnPiece) piece;
                CoordinateEnum pawnPieceCoor = CoordinateEnum.getCoordinateEnum(row, col);
                if (p.getPieceColor().equals(this.getPieceColor())) {
//                System.out.print(pawnPiece.getCurrentCoordinate() + " *");
                    p.isEnPassantValid = false;
                    chessBoard.setPieceInCoordinate(pawnPieceCoor, piece);
                }
            }
        }
//        System.out.println();
        this.isEnPassantValid = true;
    }

    public boolean isEnPassantValid() {
        return isEnPassantValid;
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


    public int getInitSquareMovement() {
        return initSquareMovement;
    }

    public void setInitSquareMovement(int initSquareMovement) {
        this.initSquareMovement = initSquareMovement;
    }

    public PawnPiece createInstance(String color, CoordinateEnum coor) {
        return new PawnPiece(color, coor);
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
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate());
        Piece dstPiece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();
        if (this.getCurrentCoordinate().getYCoordinate() == 1 || this.getCurrentCoordinate().getYCoordinate() == 6) {
            if (destinationCoordinate.getYCoordinate() == 3 || destinationCoordinate.getYCoordinate() == 4) {
                this.initSquareMovement = 2;
            }
        }
        if (isEnPessant(destinationCoordinate)) {
            ChessBoard.addToOutputs(ENPASSANT);
//            System.out.println(ENPASSANT);
            testIsEnPassantValid();
            return true;
        }
        if (dstPiece != null) {
            if (Objects.equals(dstPiece.getPieceColor(), this.getPieceColor())) {
                return false;
            }
        }
        if (getValidMoves().contains(destinationCoordinate)) {
            if (dstPiece != null&&Objects.equals(dstPiece.getPieceColor(), this.getPieceColor()))
                    return false;

            if (dstPiece == null)
                return this.getCurrentCoordinate().getXCoordinate() == destinationCoordinate.getXCoordinate();

            return true;
        }
        if (!isPromoted()) {
            if (getValidMoves().contains(destinationCoordinate)) {
                Piece piece = chessBoardInstance.getChessBoardPiece(destinationCoordinate);
                if (piece != null) {
                    if (Objects.equals(piece.getPieceColor(), this.getPieceColor())) {
                        return false;
                    }
                }
                testIsEnPassantValid();
                return true;
            }
            return false;
        }
        else {
            this.setPiece(chessBoardInstance.getChessBoardPiece(this.getCurrentCoordinate()));
            testIsEnPassantValid();
            return true;
        }
    }

    private Boolean isEnPessant(CoordinateEnum destCoor) {
        if (destCoor == d6){
//            System.out.println("");
        }
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
            if ((yCoor == 3 || yCoor == 4) && (destCoorX == xCoor + sign && destCoorY == (yCoor + pieceColorSign))) {
                Piece piece = chessBoard.getChessBoardPiece(xCoor + sign, yCoor);
                if ((piece != null)
                        && (!piece.getPieceColor().equals(this.getPieceColor()))
                        && (CoordinateEnum.getCoordinateEnum(piece.getCurrentCoordinate()) != null)) {

                    chessBoard.setPieceInCoordinate(coordinateEnum, null);
                    return true;
                }
            }
        }
        return false;
    }

}
