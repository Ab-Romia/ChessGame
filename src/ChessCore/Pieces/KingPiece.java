
package ChessCore.Pieces;
import ChessCore.ChessBoard.ChessBoard;
import ChessCore.Enum.CoordinateEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Enum.CoordinateEnum.*;
import static ChessCore.Utils.Constants.*;

public class KingPiece extends Piece {

    private final String pieceName = KING_PIECE_NAME;
    private boolean rCastle = true;
    private boolean lCastle = false;
    private String PC =  this.getPieceColor()+ pieceName;
    public KingPiece(String name, CoordinateEnum coordinate) {
        super(name, coordinate);
    }
    @Override
    public String getPieceName() {
        return KING_PIECE_NAME;
    }
    @Override
    public String getPC() {
        return PC;
    }
    public KingPiece copy()
    {
        KingPiece kingPiece = new KingPiece(this.getPieceColor(), this.getCurrentCoordinate());
        kingPiece.PC = this.PC;
        kingPiece.rCastle = this.rCastle;
        kingPiece.lCastle = this.lCastle;
        return kingPiece;
    }
    @Override
    public List<CoordinateEnum> getPossibleMoves() {
        int xCoor = this.getCurrentCoordinate().getXCoordinate();
        int yCoor = this.getCurrentCoordinate().getYCoordinate();
        List<CoordinateEnum> possibleMoves = new ArrayList<>();
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor + 1, yCoor));
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor + 1, yCoor + 1));
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor + 1, yCoor - 1));

        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor - 1, yCoor));
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor - 1, yCoor + 1));
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor - 1, yCoor - 1));

        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor, yCoor + 1));
        possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor, yCoor - 1));
        if(isLeftCastling(CoordinateEnum.getCoordinateEnum(xCoor-2, yCoor)))
            possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor-2, yCoor));
        if(isRightCastling(CoordinateEnum.getCoordinateEnum(xCoor+2, yCoor)))
            possibleMoves.add(CoordinateEnum.getCoordinateEnum(xCoor+2, yCoor));

        return possibleMoves;

    }
    private boolean isKingSafe(int xCor, int yCor) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0 ;j < 8 ; j++) {
                Piece piece = chessBoard.getChessBoardPiece(i, j);
                KingPiece kp = getKingPieceByColor(getPieceColor());
                assert kp != null;
                chessBoard.setPieceInCoordinate(kp.getCurrentCoordinate(), null);
                if (piece instanceof PawnPiece &&!Objects.equals(piece.getPieceColor(), this.getPieceColor())&&piece.getCurrentCoordinate().getXCoordinate()!=xCor&&piece.getPossibleMoves().contains(getCoordinateEnum(xCor, yCor)))
                {
                    chessBoard.setPieceInCoordinate(kp.getCurrentCoordinate(), kp);
                    return false;
                }
                else
                 if (piece != null
                        && !Objects.equals(piece.getPieceColor(), this.getPieceColor())
                        && (piece.isValidMove(CoordinateEnum.getCoordinateEnum(xCor, yCor),true))) {
//                    System.out.println("King is not safe");
                    if(Objects.equals(piece.getPieceName(), PAWN_PIECE_NAME)&&i==xCor) {
                        chessBoard.setPieceInCoordinate(kp.getCurrentCoordinate(), kp);
                        return true;
                    }
                    chessBoard.setPieceInCoordinate(kp.getCurrentCoordinate(), kp);
                    return false;
                }
                else
                    chessBoard.setPieceInCoordinate(kp.getCurrentCoordinate(), kp);
            }
        }
        return true;
    }
    public boolean isRightCastling(CoordinateEnum destCor) {
        if(!rCastle)
            return false;
        ChessBoard chessBoard = ChessBoard.getInstance();
        int kingXCoor = this.getCurrentCoordinate().getXCoordinate();
        int kingYCoor = this.getCurrentCoordinate().getYCoordinate();
        CoordinateEnum kingCoor, rookCoor;
        if (this.getPieceColor().equals(WHITE)) {
            kingCoor = e1;
            rookCoor = h1;
            if (destCor != g1)
                return false;
        } else {
            kingCoor = e8;
            rookCoor = h8;
            if (destCor != g8)
                return false;
        }


        if(chessBoard.getChessBoardPiece(kingCoor)!=null&&chessBoard.getChessBoardPiece(rookCoor)!=null)
            if(!chessBoard.getChessBoardPiece(kingCoor).getPieceName().equals(KING_PIECE_NAME)||!chessBoard.getChessBoardPiece(rookCoor).getPieceName().equals(ROOK_PIECE_NAME))
                 return false;

        CoordinateEnum bishopCor = CoordinateEnum.getCoordinateEnum(kingXCoor + 1, kingYCoor);
        CoordinateEnum horseCor = CoordinateEnum.getCoordinateEnum(kingXCoor + 2, kingYCoor);
        if(Objects.requireNonNull(getKingPieceByColor(getPieceColor())).getCurrentCoordinate()!=kingCoor)
            return false;
        Piece bishoopPiece = chessBoard.getChessBoardPiece(bishopCor);
        Piece horsePiece = chessBoard.getChessBoardPiece(horseCor);

        return bishoopPiece == null && horsePiece == null;
    }
    public boolean isLeftCastling(CoordinateEnum destCor) {
        if(!lCastle)
            return false;
        ChessBoard chessBoard = ChessBoard.getInstance();
        int kingXCor = this.getCurrentCoordinate().getXCoordinate();
        int kingYCor = this.getCurrentCoordinate().getYCoordinate();
        CoordinateEnum kingCor, rookCor;
        if (this.getPieceColor().equals(WHITE)) {
            kingCor = e1;
            rookCor = a1;
            if (destCor != c1)
                return false;
        } else {
            kingCor = e8;
            rookCor = a8;
            if (destCor != c8)
                return false;
        }
        if(Objects.requireNonNull(getKingPieceByColor(getPieceColor())).getCurrentCoordinate()!=kingCor)
            return false;
        if(chessBoard.getChessBoardPiece(kingCor)==null||chessBoard.getChessBoardPiece(rookCor)==null)
            return false;


        CoordinateEnum bishopCor = CoordinateEnum.getCoordinateEnum(kingXCor - 1, kingYCor);
        CoordinateEnum horseCor = CoordinateEnum.getCoordinateEnum(kingXCor - 2, kingYCor);
        Piece bishoopPiece = chessBoard.getChessBoardPiece(bishopCor);
        Piece horsePiece = chessBoard.getChessBoardPiece(horseCor);

        return bishoopPiece == null && horsePiece == null;
    }
    public Boolean isValidMove(CoordinateEnum destCoor) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();

        if (Objects.equals(getPieceColor(), WHITE))
        {
            FalseCastle(chessBoardInstance, e1, h1, a1);

        }
        else {
            FalseCastle(chessBoardInstance, e8, h8, a8);
        }
        if (isRightCastling(destCoor)) {
//            System.out.println(CASTLE);
            return isRightCastling(destCoor)&&isKingSafe(destCoor.getXCoordinate(), destCoor.getYCoordinate());
        } else if (isLeftCastling(destCoor)) {
//            System.out.println(CASTLE);
            return isLeftCastling(destCoor)&&isKingSafe(destCoor.getXCoordinate(), destCoor.getYCoordinate());
        } else {
            if (possibleMoves.contains(destCoor)) {
                Piece piece = chessBoardInstance.getChessBoardPiece(destCoor);
                if (piece != null) {
                    return !Objects.equals(piece.getPieceColor(), this.getPieceColor())&&isKingSafe(destCoor.getXCoordinate(), destCoor.getYCoordinate());
                }
                return isKingSafe(destCoor.getXCoordinate(), destCoor.getYCoordinate());
            }
            return false;
        }
    }
    private void FalseCastle(ChessBoard chessBoardInstance, CoordinateEnum coordinateEnum, CoordinateEnum coordinateEnum2, CoordinateEnum coordinateEnum3) {
        if(Objects.requireNonNull(getKingPieceByColor(getPieceColor())).getCurrentCoordinate() != coordinateEnum)
        {
            rCastle =false;
            lCastle = false;
        }
        if(chessBoardInstance.getChessBoardPiece(coordinateEnum2) instanceof RookPiece) {
            RookPiece rookPiece = (RookPiece) chessBoardInstance.getChessBoardPiece(coordinateEnum2);
            if(rookPiece.getDidRookMove())
                rCastle = false;
        }
        else rCastle =false;
        if(chessBoardInstance.getChessBoardPiece(coordinateEnum3) instanceof RookPiece) {
            RookPiece rookPiece = (RookPiece) chessBoardInstance.getChessBoardPiece(coordinateEnum3);
            if(rookPiece.getDidRookMove())
                lCastle = false;
        }
        else lCastle =false;
    }
    @Override
    public Boolean isValidMove(CoordinateEnum destinationCoordinate, boolean king) {
        return false;
    }
    public static KingPiece getKingPieceByColor(String color) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = chessBoardInstance.getChessBoardPiece(i, j);
                if (piece != null && piece.getPieceName().equals(KING_PIECE_NAME) && piece.getPieceColor().equals(color)) {
                    return (KingPiece) piece;
                }
            }
        }
        return null;
    }
    public List<CoordinateEnum> getValidMoves() {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = getPossibleMoves();
        if (KingPiece.isKingAtRisk(this.getPieceColor())) {
            //for all possibleMoves of kingPiece is king safe//

            for (int i = 0 ; i < possibleMoves.size() ; i++) {
                if(possibleMoves.get(i) == null)
                    continue;
                //check if the possible move contains a piece of the same color
                Piece piece = chessBoardInstance.getChessBoardPiece(possibleMoves.get(i));
                if (piece != null && piece.getPieceColor().equals(this.getPieceColor())) {
                    possibleMoves.set(i, null);

                }
               else if (!isKingSafe(possibleMoves.get(i).getXCoordinate(), possibleMoves.get(i).getYCoordinate())) {
                    possibleMoves.set(i, null);
                }
               else if(piece != null && !piece.getPieceColor().equals(this.getPieceColor())) {
                   //move the piece to the possible move
                     CoordinateEnum srcCoor = this.getCurrentCoordinate();
                     CoordinateEnum dstCoor = possibleMoves.get(i);
                     Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCoor);
                        Piece dstPiece = chessBoardInstance.getChessBoardPiece(dstCoor);
                        chessBoardInstance.setPieceInCoordinate(srcCoor, null);
                        chessBoardInstance.setPieceInCoordinate(dstCoor, srcPiece);
                        if (!isKingSafe(dstCoor.getXCoordinate(), dstCoor.getYCoordinate())) {
                            possibleMoves.set(i, null);
                        }
                        chessBoardInstance.setPieceInCoordinate(srcCoor, srcPiece);
                        chessBoardInstance.setPieceInCoordinate(dstCoor, dstPiece);
               }

            }
            //If castling is a possible move check if it is valid


                possibleMoves.removeIf(Objects::isNull);
            return possibleMoves;
        }
        possibleMoves.removeIf(Objects::isNull);
        for (int i = 0 ; i < possibleMoves.size() ; i++) {
            if (chessBoardInstance.getChessBoardPiece(possibleMoves.get(i)) != null) {
                possibleMoves.set(i, null);
            }
        }
        //check if castling is available if so add to valid moves

        possibleMoves.removeIf(Objects::isNull);
        return possibleMoves;
    }
    public static boolean isKingAtRisk(String color) {

        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        KingPiece kingPiece = KingPiece.getKingPieceByColor(color);
        boolean flag = false;
        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = chessBoardInstance.getChessBoardPiece(i, j);
                if(piece!=null&& Objects.equals(piece.getPieceName(), KING_PIECE_NAME))
                    continue;
                if (piece != null && !piece.getPieceColor().equals(color)) {
                    assert kingPiece != null;
                    try {
                        if (piece.isValidMove(kingPiece.getCurrentCoordinate())) {

                            flag = true;
                        }
                    }catch(NullPointerException e)
                    {
                        System.out.println(e.getMessage());
                        flag = false;

                    }
                }
            }
        }
        return flag;
    }
    public static boolean didWin(String color) {
        KingPiece kingPiece = KingPiece.getKingPieceByColor(color);
        if (!KingPiece.isKingAtRisk(color) || !Objects.requireNonNull(kingPiece).getValidMoves().isEmpty())
            return false;

//        return kingPiece.getValidMoves().isEmpty();
        //return if there exist a valid move on the board
        return !kingPiece.isThereValidMoves(color);

    }
    
}
