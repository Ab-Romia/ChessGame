
package ChessCore.Pieces;
import ChessCore.ChessBoard;
import ChessCore.Enum.CoordinateEnum;
import Exceptions.InvalidMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Enum.CoordinateEnum.*;
import static ChessCore.Utils.Constants.*;

public class KingPiece extends Piece {

    private final String pieceName = KING_PIECE_NAME;
    private final String PC =  this.getPieceColor()+ pieceName;
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

    private boolean isKingSafe(int xCoor, int yCoor) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0 ;j < 8 ; j++) {
                Piece piece = chessBoard.getChessBoardPiece(i, j);

                if (piece != null
                        && !Objects.equals(piece.getPieceColor(), this.getPieceColor())
                        && (piece.isValidMove(CoordinateEnum.getCoordinateEnum(xCoor, yCoor)))) {
//                    System.out.println("King is not safe");
                    if(Objects.equals(piece.getPieceName(), PAWN_PIECE_NAME)&&i==xCoor)
                        return true;
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isRightCastling(CoordinateEnum destCoor) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        int kingXCoor = this.getCurrentCoordinate().getXCoordinate();
        int kingYCoor = this.getCurrentCoordinate().getYCoordinate();
        CoordinateEnum kingCoor, rookCoor;
        if (this.getPieceColor().equals(WHITE)) {
            kingCoor = e1;
            rookCoor = h1;
            if (destCoor != g1)
                return false;
        } else {
            kingCoor = e8;
            rookCoor = h8;
            if (destCoor != g8)
                return false;
        }
        boolean isKingPiece = chessBoard.getChessBoardPiece(kingCoor).getPieceName().equals(KING_PIECE_NAME);
        boolean isRookPiece = chessBoard.getChessBoardPiece(rookCoor).getPieceName().equals(ROOK_PIECE_NAME);

        if (!isKingPiece || !isRookPiece) {
            return false;
        }

        CoordinateEnum bishopCoor = CoordinateEnum.getCoordinateEnum(kingXCoor + 1, kingYCoor);
        CoordinateEnum horseCoor = CoordinateEnum.getCoordinateEnum(kingXCoor + 2, kingYCoor);
        Piece bishoopPiece = chessBoard.getChessBoardPiece(bishopCoor);
        Piece horsePiece = chessBoard.getChessBoardPiece(horseCoor);

        if (bishoopPiece != null || horsePiece != null) {
            return false;
        }

        return true;
    }

    public boolean isLeftCastling(CoordinateEnum destCoor) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        int kingXCoor = this.getCurrentCoordinate().getXCoordinate();
        int kingYCoor = this.getCurrentCoordinate().getYCoordinate();
        CoordinateEnum kingCoor, rookCoor;
        if (this.getPieceColor().equals(WHITE)) {
            kingCoor = e1;
            rookCoor = a1;
            if (destCoor != c1)
                return false;
        } else {
            kingCoor = e8;
            rookCoor = a8;
            if (destCoor != c8)
                return false;
        }
        if(chessBoard.getChessBoardPiece(kingCoor)==null||chessBoard.getChessBoardPiece(rookCoor)==null)
            return false;


        CoordinateEnum bishopCoor = CoordinateEnum.getCoordinateEnum(kingXCoor - 1, kingYCoor);
        CoordinateEnum horseCoor = CoordinateEnum.getCoordinateEnum(kingXCoor - 2, kingYCoor);
        Piece bishoopPiece = chessBoard.getChessBoardPiece(bishopCoor);
        Piece horsePiece = chessBoard.getChessBoardPiece(horseCoor);

        if (bishoopPiece != null || horsePiece != null) {
            return false;
        }

        return true;
    }

    public void doLeftCastle(CoordinateEnum destCoor) {
        // mesh mthdeden
        // mesh mt7rkeen
        ChessBoard chessBoard = ChessBoard.getInstance();
        int kingXCoor = this.getCurrentCoordinate().getXCoordinate();
        int kingYCoor = this.getCurrentCoordinate().getYCoordinate();
        CoordinateEnum kingCoor, rookCoor;
        if (this.getPieceColor().equals(WHITE)) {
            kingCoor = e1;
            rookCoor = a1;

        } else {
            kingCoor = e8;
            rookCoor = a8;

        }

        CoordinateEnum bishopCoor = CoordinateEnum.getCoordinateEnum(kingXCoor - 1, kingYCoor);
        CoordinateEnum horseCoor = CoordinateEnum.getCoordinateEnum(kingXCoor - 2, kingYCoor);
        chessBoard.setChessBoardPiece(rookCoor, bishopCoor);
        ChessBoard.addToOutputs(CASTLE);

    }

    public Boolean isValidMove(CoordinateEnum destCoor) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = this.getPossibleMoves();
        if (isRightCastling(destCoor)) {
//            System.out.println(CASTLE);
            return isRightCastling(destCoor);
        } else if (isLeftCastling(destCoor)) {
//            System.out.println(CASTLE);
            return isLeftCastling(destCoor);
        } else {
            if (possibleMoves.contains(destCoor)) {
                Piece piece = chessBoardInstance.getChessBoardPiece(destCoor);
                if (piece != null) {
                    return !Objects.equals(piece.getPieceColor(), this.getPieceColor());
                }
                return isKingSafe(destCoor.getXCoordinate(), destCoor.getYCoordinate());
            }
            return false;
        }
    }

    public void doRightCastle(CoordinateEnum destCoor) {
        // mesh mthdeden
        // mesh mt7rkeen
        ChessBoard chessBoard = ChessBoard.getInstance();
        int kingXCoor = this.getCurrentCoordinate().getXCoordinate();
        int kingYCoor = this.getCurrentCoordinate().getYCoordinate();
        CoordinateEnum kingCoor, rookCoor;
        if (this.getPieceColor().equals(WHITE)) {
            kingCoor = e1;
            rookCoor = h1;

        } else {
            kingCoor = e8;
            rookCoor = h8;

        }
        CoordinateEnum bishopCoor = CoordinateEnum.getCoordinateEnum(kingXCoor + 1, kingYCoor);
        CoordinateEnum horseCoor = CoordinateEnum.getCoordinateEnum(kingXCoor + 2, kingYCoor);
        chessBoard.setChessBoardPiece(rookCoor, bishopCoor);
        ChessBoard.addToOutputs(CASTLE);

    }

    private static KingPiece getKingPieceByColor(String color) {
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
                    continue;
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
        if (!KingPiece.isKingAtRisk(color) || !kingPiece.getValidMoves().isEmpty())
            return false;

//        return kingPiece.getValidMoves().isEmpty();
        return !kingPiece.isThereValidMoves(color);
    }
    
}
