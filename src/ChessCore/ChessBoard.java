
package ChessCore;
import ChessCore.Enum.CoordinateEnum;
import ChessCore.Pieces.*;
import Exceptions.*;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


import static ChessCore.Enum.CoordinateEnum.*;
import static ChessCore.Utils.Constants.*;

public class ChessBoard {

    private static List<String> outputs = new ArrayList<>();

    private boolean gameEnded = false;


    public static List<String> getOutputs() {
        return outputs;
    }

    public static void addToOutputs(String output) {
        getOutputs().add(output);
    }

    private String currentTurnColor = WHITE;

    private static ChessBoard chessBoardInstance;

    private ChessBoard() {
        initializeGame();
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

    private Piece[][] chessBoard = new Piece[8][8];

    public Piece getChessBoardPiece(CoordinateEnum coordinateEnum) {
        return chessBoard[coordinateEnum.getXCoordinate()][coordinateEnum.getYCoordinate()];
    }

    public Piece getChessBoardPiece(int xCoor, int yCoor) {
        return chessBoard[xCoor][yCoor];
    }


    public void setChessBoardPiece(CoordinateEnum srcCoor, CoordinateEnum destCoor) {

        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCoor);
        srcPiece.setCurrentCoordinate(destCoor);
        chessBoardInstance.setPieceInCoordinate(destCoor, srcPiece);
        chessBoardInstance.setPieceInCoordinate(srcCoor, null);

    }

    private void initializeGame() {
        createPlayerBoard(List.of(a8, b8, c8, d8, e8, f8, g8, h8), BLACK);
        createPlayerBoard(BLACK, 6);
        createPlayerBoard(List.of(a1, b1, c1, d1, e1, f1, g1, h1), WHITE);
        createPlayerBoard(WHITE, 1);
    }

    private void addPieceInCoordinate(CoordinateEnum coordinate, Piece piece) {
        this.chessBoard[coordinate.getXCoordinate()][coordinate.getYCoordinate()] = piece;
    }

    public void setPieceInCoordinate(CoordinateEnum coordinate, Piece piece) {
        this.chessBoard[coordinate.getXCoordinate()][coordinate.getYCoordinate()] = piece;
    }

    private void createPlayerBoard(
            List<CoordinateEnum> piecePos, String player) {
        addPieceInCoordinate(piecePos.get(0), new RookPiece(player, piecePos.get(0)));
        addPieceInCoordinate(piecePos.get(1), new KnightPiece(player, piecePos.get(1)));
        addPieceInCoordinate(piecePos.get(2), new BishopPiece(player, piecePos.get(2)));
        addPieceInCoordinate(piecePos.get(3), new QueenPiece(player, piecePos.get(3)));
        addPieceInCoordinate(piecePos.get(4), new KingPiece(player, piecePos.get(4)));
        addPieceInCoordinate(piecePos.get(5), new BishopPiece(player, piecePos.get(5)));
        addPieceInCoordinate(piecePos.get(6), new KnightPiece(player, piecePos.get(6)));
        addPieceInCoordinate(piecePos.get(7), new RookPiece(player, piecePos.get(7)));
    }

    private void createPlayerBoard(String player, Integer col) {
        for (int row = 0; row < 8; row++) {
            this.chessBoard[row][col] = new PawnPiece(player, getCoordinateEnum(row, col));
        }
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

    public void doCaptured(CoordinateEnum destCoor, Piece srcPiece) {
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCoor);
        if (destPiece != null) {
            if (!Objects.equals(destPiece.getPieceColor(), srcPiece.getPieceColor())) {
//                System.out.println(CAPTURED + ' ' + destPiece.getPieceName());
                outputs.add(CAPTURED + ' ' + destPiece.getPieceName());
            }
        }
    }
    public boolean srcInv(CoordinateEnum srcCoor) {
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCoor);
        if (srcPiece == null) {
            outputs.add(NO_PIECE_AT_THIS_POSITION);
            return true;
        }
        return false;
    }
    public boolean destInv(CoordinateEnum srcCoor,CoordinateEnum destCoor) {
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCoor);
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCoor);
        if (destPiece != null && !currentTurnColor.equals(srcPiece.getPieceColor())) {
            if (Objects.equals(destPiece.getPieceColor(), currentTurnColor)) {
                outputs.add(IN_VALID_MOVE);
                return true;
            }
        }
        return false;
    }
    public boolean isKingAtRisk(CoordinateEnum srcCoor, CoordinateEnum destCoor) {
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCoor);
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCoor);
        if (KingPiece.isKingAtRisk(currentTurnColor)) {
            outputs.add(currentTurnColor + ' ' + IN_CHECK);

            return true;
        }
        return false;
    }


    public void play(CoordinateEnum srcCoor, CoordinateEnum destCoor, String name) throws Exception {
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCoor);
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCoor);
        if (srcCoor == c3 && destCoor == c4){
            System.out.println("");
        }

        if (gameEnded) {
//            System.out.println(GAME_ALREADY_ENDED);
            outputs.add(GAME_ALREADY_ENDED);
            throw new GamedEnded();
        }
        if (srcPiece == null) {
            outputs.add(NO_PIECE_AT_THIS_POSITION); //check
            throw new NullPiece();
        }
        if (currentTurnColor.equals(srcPiece.getPieceColor())) { // check right turn

            if (chessBoardInstance.isInsuffient()) {
                outputs.add(INSUFFIENT_MATERIAL);
//                System.out.println(INSUFFIENT_MATERIAL);
                throw new Insufficient();
            }
            if (srcPiece instanceof PawnPiece) {
                PawnPiece pawnPiece = (PawnPiece) srcPiece;
                pawnPiece.setPromoteTo(name);
            }
           if (getChessBoardPiece(srcCoor).isValidMove(destCoor)) {
                srcPiece = this.getChessBoardPiece(srcPiece.getCurrentCoordinate());
                doCaptured(destCoor, srcPiece);
                if (srcPiece instanceof PawnPiece)
                {
                    PawnPiece pp = (PawnPiece) srcPiece;
                    if(pp.isEnPessant(destCoor))
                        pp.doEnpassant(destCoor);

                    setChessBoardPiece(srcPiece.getCurrentCoordinate(), destCoor);

                }
                else if (srcPiece instanceof KingPiece) {
                    KingPiece kp = (KingPiece) srcPiece;
                    if (kp.isRightCastling(destCoor)) {
                        if (kp.getPieceColor().equals(WHITE)) {

                            setChessBoardPiece(h1, f1);
                            setChessBoardPiece(e1, g1);

                        } else {

                            setChessBoardPiece(h8, f8);
                            setChessBoardPiece(e8, g8);

                        }
                    } else if (kp.isLeftCastling(destCoor)) {
                        if (kp.getPieceColor().equals(WHITE)) {

                            setChessBoardPiece(a1, d1);
                            setChessBoardPiece(e1, c1);
                        } else {

                            setChessBoardPiece(a8, d8);
                            setChessBoardPiece(e8, c8);
                        }
                    } else {
                        setChessBoardPiece(srcPiece.getCurrentCoordinate(), destCoor);

                    }
                }
                    else
                         setChessBoardPiece(srcPiece.getCurrentCoordinate(), destCoor);
                currentTurnColor = Objects.equals(currentTurnColor, WHITE) ? BLACK : WHITE;
               if (KingPiece.isKingAtRisk(currentTurnColor)) {
                   outputs.add(currentTurnColor + ' ' + IN_CHECK);
//                   throw new InCheck(currentTurnColor);
//                   System.out.println(currentTurnColor + ' ' + IN_CHECK);
               }
//                if (KingPiece.isKingAtRisk(srcPiece.getPieceColor())) { // check
//                    throw new Exception("[" + KING_IN_DANGER + "]" + IN_VALID_MOVE);
//                }
               String oppColor = Objects.equals(srcPiece.getPieceColor(), WHITE) ? BLACK : WHITE;
               if (KingPiece.didWin(currentTurnColor)) {
                   gameEnded = true;
                   outputs.set(outputs.size()-1, srcPiece.getPieceColor() + ' ' + WON);
                   throw new Won(srcPiece.getPieceColor());
//                   System.out.println(srcPiece.getPieceColor() + ' ' + WON);
               }
               // string color opposite color of srcPiece//


               else if (!srcPiece.isThereValidMoves(oppColor)) {
//                    System.out.println();
                   outputs.add(STALEMATE);
                   gameEnded = true;
                   throw new Stalemate();
               }
               printChessBoard();

            } else {
//                System.out.println(IN_VALID_MOVE);
                outputs.add(IN_VALID_MOVE);
                throw new InvalidMove();
            }
            if (chessBoardInstance.isInsuffient()) {
                outputs.add(INSUFFIENT_MATERIAL);

//                System.out.println(INSUFFIENT_MATERIAL);
                gameEnded= true;
                throw new Insufficient();
            }
        } else {
//            System.out.println(IN_VALID_MOVE);
            outputs.add(IN_VALID_MOVE);
            throw new InvalidMove();
        }

    }

    private boolean isInsuffient() {
        List<String> condition1 = new ArrayList<>(List.of(KING_PIECE_NAME));
        List<String> condition2 = new ArrayList<>(List.of(KING_PIECE_NAME, BISHOP_PIECE_NAME));
        List<String> condition3 = new ArrayList<>(List.of(KING_PIECE_NAME, KNIGHT_PIECE_NAME));

        List<String> whitePiece = new ArrayList<>();
        List<String> blackPiece = new ArrayList<>();


        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0 ; j < 8 ; j++) {
                Piece piece = chessBoardInstance.getChessBoardPiece(i, j);
                if (piece != null){
                    if (piece.getPieceColor().equals(WHITE)) {
                        whitePiece.add(piece.getPieceName());
                    } else {
                        blackPiece.add(piece.getPieceName());
                    }
                }
            }
        }

        Collections.sort(condition1);
        Collections.sort(condition2);
        Collections.sort(condition3);
        Collections.sort(whitePiece);
        Collections.sort(blackPiece);

        if (whitePiece.equals(condition1) && blackPiece.equals(condition1)) {
            return true;
        }
        if (whitePiece.equals(condition2) && blackPiece.equals(condition2)) {
            return true;
        }
        if (whitePiece.equals(condition3) && blackPiece.equals(condition3)) {
            return true;
        }
        return false;
    }
}
