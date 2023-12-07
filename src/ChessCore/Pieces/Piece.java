


package ChessCore.Pieces;
import ChessCore.Enum.CoordinateEnum;
import ChessCore.ChessBoard.ChessBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ChessCore.Utils.Constants.*;



/**
 *
 * @author romia
 */
public abstract class Piece {
    private final String pieceColor;
    private CoordinateEnum currentCoordinate;
    private Piece piece;

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Piece(String pieceColor, CoordinateEnum currentCoordinate) {
        this.pieceColor = pieceColor;
        this.currentCoordinate = currentCoordinate;
    }

    public String getPieceColor() {
        return pieceColor;
    }

    public abstract String getPieceName();
    public abstract String getPC();

    public CoordinateEnum getCurrentCoordinate() {
        return currentCoordinate;
    }
    public void setCurrentCoordinate(CoordinateEnum newCoordinate) {
        this.currentCoordinate = newCoordinate;
    }

    public abstract List<CoordinateEnum>  getPossibleMoves();


    public Boolean isThereObstacle(CoordinateEnum destCoor) {
        int destXCoor = destCoor.getXCoordinate() ;
        int destYCoor = destCoor.getYCoordinate();
        int srcXCoor = this.getCurrentCoordinate().getXCoordinate();
        int srcYCoor = this.getCurrentCoordinate().getYCoordinate();

        if (destXCoor == srcXCoor) {
            if (destYCoor  > srcYCoor ) { // upper
                List<Piece> coordinateEnums = getUpperPossibleMoves(destCoor);
                coordinateEnums.removeIf(Objects::isNull);
                return !coordinateEnums.isEmpty();
            } else { // lower
                List<Piece> coordinateEnums = getLowerPossibleMoves(destCoor);
                coordinateEnums.removeIf(Objects::isNull);
                return !coordinateEnums.isEmpty();
            }
        }

        if (destYCoor == srcYCoor) {
            if (destXCoor > srcXCoor ) { // right
                List<Piece> coordinateEnums = getRightPossibleMoves(destCoor);
                coordinateEnums.removeIf(Objects::isNull);
                return !coordinateEnums.isEmpty();
            } else { // left
                List<Piece> coordinateEnums = getLeftPossibleMoves(destCoor);
                coordinateEnums.removeIf(Objects::isNull);
                return !coordinateEnums.isEmpty();
            }
        }
        if (destXCoor > srcXCoor ) {
            if (destYCoor > srcYCoor) {
                List<Piece> coordinateEnums = getUpperRightDiagonalPossibleMoves(destCoor);
                coordinateEnums.removeIf(Objects::isNull);
                return !coordinateEnums.isEmpty();
            } else {
                // lower right
                List<Piece> coordinateEnums = getLowerRightDiagonalPossibleMoves(destCoor);
                coordinateEnums.removeIf(Objects::isNull);
                return !coordinateEnums.isEmpty();
            }

        }
        if (destYCoor > srcYCoor) {
            // upper left
            List<Piece> coordinateEnums = getUpperLeftDiagonalPossibleMoves(destCoor);
            coordinateEnums.removeIf(Objects::isNull);
            return !coordinateEnums.isEmpty();
        } else {
            // lower left
            List<Piece> coordinateEnums = getLowerLeftDiagonalPossibleMoves(destCoor);
            coordinateEnums.removeIf(Objects::isNull);
            return !coordinateEnums.isEmpty();
        }

    }


    public Boolean isInCheck(CoordinateEnum srcCoor, CoordinateEnum destCoor) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCoor);
        String validPieceColor = null, validPieceName = null;
        if (destPiece != null){
            validPieceColor = destPiece.getPieceColor();
            validPieceName = destPiece.getPieceName();
        }
        KingPiece kingPiece =  KingPiece.getKingPieceByColor(this.getPieceColor());
        chessBoardInstance.setChessBoardPiece(srcCoor, destCoor);
        if (destPiece!=null&&destPiece.getPieceName().equals(KING_PIECE_NAME)&&KingPiece.isKingAtRisk(this.getPieceColor())) {
            undoMove(validPieceColor, validPieceName, srcCoor, destCoor);
            return false;
        }
        if ((kingPiece!=null)&&KingPiece.isKingAtRisk(this.getPieceColor())) {
            undoMove(validPieceColor, validPieceName, srcCoor, destCoor);
            return true;
        }
        undoMove(validPieceColor, validPieceName, srcCoor, destCoor);
        return false;
    }

    public abstract Boolean isValidMove(CoordinateEnum destinationCoordinate);
    public abstract Boolean isValidMove(CoordinateEnum destinationCoordinate, boolean king);

    public List<CoordinateEnum> getValidMoves() {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<CoordinateEnum> possibleMoves = getPossibleMoves();
        if (KingPiece.isKingAtRisk(this.getPieceColor())) {
            return List.of();
        }
        possibleMoves.removeIf(Objects::isNull);
        for (int i = 0 ; i < possibleMoves.size() ; i++) {
            if (chessBoardInstance.getChessBoardPiece(possibleMoves.get(i)) != null) {
                possibleMoves.set(i, null);
            }
        }
        possibleMoves.removeIf(Objects::isNull);
        return possibleMoves;
    }

    public Integer getFrontOrRightSign() {
        if (this.getPieceColor().equals(BLACK)) {
            return -1;
        }
        return 1;
    }
    public Integer getDownOrLeftSign() {
        if (this.getPieceColor().equals(BLACK)) {
            return 1;
        }
        return -1;
    }

    public List<Piece> getRightPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int currentXCoordinate =  this.getCurrentCoordinate().getXCoordinate();
        int currentYCoordinate =  this.getCurrentCoordinate().getYCoordinate();
        for (int row = currentXCoordinate + 1; row < destCoor.getXCoordinate() ; row++) {
            list.add(chessBoard.getChessBoardPiece(row, currentYCoordinate));
        }
        return list;
    }

    public List<Piece> getLeftPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int currentXCoordinate =  this.getCurrentCoordinate().getXCoordinate();
        int currentYCoordinate =  this.getCurrentCoordinate().getYCoordinate();
        for (int row = currentXCoordinate - 1 ; row > destCoor.getXCoordinate() ; row--) {
            list.add(chessBoard.getChessBoardPiece(row, currentYCoordinate));
        }
        return list;
    }

    public List<CoordinateEnum> getRightAndLeftPossibleMoves() {
        List<CoordinateEnum> list = new ArrayList<>();
        int currentXCoordinate =  this.getCurrentCoordinate().getXCoordinate();
        int currentYCoordinate =  this.getCurrentCoordinate().getYCoordinate();
        for (int row = currentXCoordinate + 1; row < 8 ; row++) {
            list.add(CoordinateEnum.getCoordinateEnum(row, currentYCoordinate));
        }
        for (int row = currentXCoordinate - 1 ; row >= 0 ; row--) {
            list.add(CoordinateEnum.getCoordinateEnum(row, currentYCoordinate));
        }
        return list;
    }
    public List<Piece> getUpperPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int currentXCoordinate =  this.getCurrentCoordinate().getXCoordinate();
        int currentYCoordinate =  this.getCurrentCoordinate().getYCoordinate();
        for (int col = currentYCoordinate + 1; col < destCoor.getYCoordinate() ; col++) {
            list.add(chessBoard.getChessBoardPiece(currentXCoordinate, col));
        }
        return list;
    }

    public List<Piece> getLowerPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int currentXCoordinate =  this.getCurrentCoordinate().getXCoordinate();
        int currentYCoordinate =  this.getCurrentCoordinate().getYCoordinate();
        for (int col = currentYCoordinate - 1 ; col > destCoor.getYCoordinate() ; col--) {
            list.add(chessBoard.getChessBoardPiece(currentXCoordinate, col));
        }
        return list;
    }

    public List<CoordinateEnum> getUpAndDownPossibleMoves() {
        List<CoordinateEnum> list = new ArrayList<>();
        int currentXCoordinate =  this.getCurrentCoordinate().getXCoordinate();
        int currentYCoordinate =  this.getCurrentCoordinate().getYCoordinate();
        for (int col = currentYCoordinate + 1; col < 8 ; col++) {
            list.add(CoordinateEnum.getCoordinateEnum(currentXCoordinate, col));
        }
        for (int col = currentYCoordinate - 1 ; col >= 0 ; col--) {
            list.add(CoordinateEnum.getCoordinateEnum(currentXCoordinate, col));
        }
        return list;
    }

    public List<Piece> getUpperRightDiagonalPossibleMoves(CoordinateEnum destCoor) {
        ChessBoard chessBoard = ChessBoard.getInstance();
        List<Piece> list = new ArrayList<>();
        int yCoorUp = this.getCurrentCoordinate().getYCoordinate();
        for (int row = this.getCurrentCoordinate().getXCoordinate() + 1; row < destCoor.getXCoordinate() ; row++) {
            yCoorUp = yCoorUp + 1;
            list.add(chessBoard.getChessBoardPiece(row, yCoorUp));
        }
        return list;
    }

    public List<Piece> getLowerRightDiagonalPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int yCoorDown = this.getCurrentCoordinate().getYCoordinate();
        for (int row = this.getCurrentCoordinate().getXCoordinate() + 1; row < destCoor.getXCoordinate() ; row++) {
            yCoorDown = yCoorDown - 1;
            list.add(chessBoard.getChessBoardPiece(row, yCoorDown));
        }
        return list;
    }

    public List<CoordinateEnum> getUpAndDownRightDiagonal() {
        List<CoordinateEnum> upAndDownRightDiagonal = new ArrayList<>();
        int yCoorUp = this.getCurrentCoordinate().getYCoordinate();
        int yCoorDown = this.getCurrentCoordinate().getYCoordinate();
        for (int row = this.getCurrentCoordinate().getXCoordinate() + 1; row < 8 ; row++) {
            yCoorUp = yCoorUp + 1;
            upAndDownRightDiagonal.add(CoordinateEnum.getCoordinateEnum(row, yCoorUp));
            yCoorDown = yCoorDown - 1;
            upAndDownRightDiagonal.add(CoordinateEnum.getCoordinateEnum(row, yCoorDown));
        }
        return upAndDownRightDiagonal;
    }

    public List<CoordinateEnum> getUpAndDownLeftDiagonal() {
        List<CoordinateEnum> upAndDownLeftDiagonal = new ArrayList<>();
        int yCoorUp = this.getCurrentCoordinate().getYCoordinate();
        int yCoorDown = this.getCurrentCoordinate().getYCoordinate();
        for (int row = this.getCurrentCoordinate().getXCoordinate() - 1; row >= 0 ; row--) {
            yCoorUp = yCoorUp + 1;
            upAndDownLeftDiagonal.add(CoordinateEnum.getCoordinateEnum(row, yCoorUp));
            yCoorDown = yCoorDown - 1;
            upAndDownLeftDiagonal.add(CoordinateEnum.getCoordinateEnum(row, yCoorDown));
        }
        return upAndDownLeftDiagonal;
    }

    public List<Piece> getUpperLeftDiagonalPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int yCoorUp = this.getCurrentCoordinate().getYCoordinate();
        for (int row = this.getCurrentCoordinate().getXCoordinate() - 1; row > destCoor.getXCoordinate() ; row--) {
            yCoorUp = yCoorUp + 1;
            list.add(chessBoard.getChessBoardPiece(row, yCoorUp));
        }
        return list;
    }

    List<Piece> getLowerLeftDiagonalPossibleMoves(CoordinateEnum destCoor) {
        List<Piece> list = new ArrayList<>();
        ChessBoard chessBoard = ChessBoard.getInstance();
        int yCoorDown = this.getCurrentCoordinate().getYCoordinate();
        for (int row = this.getCurrentCoordinate().getXCoordinate() - 1; row > destCoor.getXCoordinate() ; row--) {
            yCoorDown = yCoorDown - 1;
            list.add(chessBoard.getChessBoardPiece(row, yCoorDown));
        }
        return list;
    }

    public Boolean isThereValidMoves(String color) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0 ; i < 8 ; i ++) {
            for (int j = 0 ; j < 8 ; j++) {
                Piece piece = chessBoardInstance.getChessBoardPiece(i,j);
                if (piece != null && piece.getPieceColor().equals(color)) {
                    pieces.add(piece);
                }
            }
        }

        for (Piece srcPiece : pieces) {
            CoordinateEnum srcCoor = srcPiece.getCurrentCoordinate();
            List<CoordinateEnum> validMoves = srcPiece.getValidMoves();
            for (CoordinateEnum validMove : validMoves) {
                Piece destPiece = chessBoardInstance.getChessBoardPiece(validMove);
                String validPieceColor = null, validPieceName = null;
                if (destPiece != null) {
                    validPieceColor = destPiece.getPieceColor();
                    validPieceName = destPiece.getPieceName();
                }
                chessBoardInstance.setChessBoardPiece(srcCoor, validMove);

                if (!KingPiece.isKingAtRisk(color)) {
                    //undo
                    undoMove(validPieceColor, validPieceName, srcCoor, validMove);
                    return true;
                }
                undoMove(validPieceColor, validPieceName, srcCoor, validMove);
            }

        }
        return false;
    }


    public void undoMove(String color, String pieceName, CoordinateEnum srcCoor, CoordinateEnum destCoor) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();
        Piece piece = chessBoardInstance.getChessBoardPiece(destCoor);
        piece.setCurrentCoordinate(srcCoor);
        chessBoardInstance.setPieceInCoordinate(srcCoor, piece);
        if (color != null ) {
            switch (pieceName) {
                case BISHOP_PIECE_NAME:
                    BishopPiece bishopPiece = new BishopPiece(color, destCoor);
                    chessBoardInstance.setPieceInCoordinate(destCoor, bishopPiece);
                    break;
                case KING_PIECE_NAME:
                    KingPiece kingPiece = new KingPiece(color, destCoor);
                    chessBoardInstance.setPieceInCoordinate(destCoor, kingPiece);
                    break;
                case KNIGHT_PIECE_NAME:
                    KnightPiece knightPiece = new KnightPiece(color, destCoor);
                    chessBoardInstance.setPieceInCoordinate(destCoor, knightPiece);
                    break;
                case PAWN_PIECE_NAME:
                    PawnPiece pawnPiece = new PawnPiece(color, destCoor);
                    chessBoardInstance.setPieceInCoordinate(destCoor, pawnPiece);
                    break;
                case QUEEN_PIECE_NAME:
                    QueenPiece queenPiece = new QueenPiece(color, destCoor);
                    chessBoardInstance.setPieceInCoordinate(destCoor, queenPiece);
                    break;
                case ROOK_PIECE_NAME:
                    RookPiece rookPiece = new RookPiece(color, destCoor);
                    chessBoardInstance.setPieceInCoordinate(destCoor, rookPiece);
                    break;
            }
        } else {
            chessBoardInstance.setPieceInCoordinate(destCoor, null);
        }


    }
}
