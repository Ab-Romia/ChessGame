package ChessCore.ChessBoard;


import ChessCore.Enum.CoordinateEnum;
import ChessCore.Pieces.*;

import Exceptions.*;
import ChessCore.Undo.Caretaker;

import java.util.*;

import static ChessCore.Enum.CoordinateEnum.*;
import static ChessCore.Utils.Constants.*;
public class Player {
        private final ChessBoard chessBoardInstance;
        final Caretaker caretaker = new Caretaker();

        private static final List<String> outputs = new ArrayList<>();

        public static List<String> getOutputs() {
        return outputs;
    }
         public static void addToOutputs(String output) {
             getOutputs().add(output);
        }
        public Player(ChessBoard chessBoardInstance) {
             this.chessBoardInstance = chessBoardInstance;
         }
        public boolean srcInv(CoordinateEnum srcCor) {
            Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCor);
            if (srcPiece == null) {
            Player.addToOutputs(NO_PIECE_AT_THIS_POSITION);
            return true;
            }
            return false;
        }
        public boolean destInv(CoordinateEnum srcCor,CoordinateEnum destCor) {
        Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCor);
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCor);
        if (destPiece != null && !chessBoardInstance.getCurrentTurnColor().equals(srcPiece.getPieceColor())) {
            if (Objects.equals(destPiece.getPieceColor(), chessBoardInstance.getCurrentTurnColor())) {
                Player.addToOutputs(IN_VALID_MOVE);
                return true;
            }
        }
        return false;
    }
        public void play(CoordinateEnum srcCor, CoordinateEnum destCor, String name) throws Exception {
            Piece srcPiece = chessBoardInstance.getChessBoardPiece(srcCor);

            if (chessBoardInstance.isGameEnded()) {
            throw new GamedEnded();
        }
        if (srcPiece == null) {
            throw new NullPiece();
        }
        if (chessBoardInstance.getCurrentTurnColor().equals(srcPiece.getPieceColor())) {
            if (chessBoardInstance.isInsufficient()) {
                throw new Insufficient();
            }
            if (srcPiece instanceof PawnPiece) {
                PawnPiece pawnPiece = (PawnPiece) srcPiece;
                pawnPiece.setPromoteTo(name);
                caretaker.saveState(chessBoardInstance);
                pawnPiece.testIsEnPassantValid();

            }
            if (srcPiece.isValidMove(destCor)) {
                if(!(srcPiece instanceof PawnPiece)) {
                    caretaker.saveState(chessBoardInstance);
                }

                srcPiece = chessBoardInstance.getChessBoardPiece(srcPiece.getCurrentCoordinate());
                doCaptured(destCor, srcPiece);
                if (srcPiece instanceof PawnPiece) {
                    PawnPiece pp = (PawnPiece) srcPiece;
                    if(pp.isEnPassant(destCor)) {
                        pp.doEnpassant(destCor);

                    }

                    chessBoardInstance.setChessBoardPiece(srcPiece.getCurrentCoordinate(), destCor);


                } else if (srcPiece instanceof KingPiece) {

                    KingPiece kp = (KingPiece) srcPiece;
                    if (kp.isRightCastling(destCor)) {
                        if (kp.getPieceColor().equals(WHITE)) {
                            chessBoardInstance.setChessBoardPiece(h1, f1);
                            chessBoardInstance.setChessBoardPiece(e1, g1);
                        } else {
                            chessBoardInstance.setChessBoardPiece(h8, f8);
                            chessBoardInstance.setChessBoardPiece(e8, g8);
                        }

                        outputs.add(kp.getPieceColor()+" King-Side Castling");
                    } else if (kp.isLeftCastling(destCor)) {
                        if (kp.getPieceColor().equals(WHITE)) {
                            chessBoardInstance.setChessBoardPiece(a1, d1);
                            chessBoardInstance.setChessBoardPiece(e1, c1);
                        } else {
                            chessBoardInstance.setChessBoardPiece(a8, d8);
                            chessBoardInstance.setChessBoardPiece(e8, c8);
                        }

                        outputs.add(kp.getPieceColor()+" Queen-Side Castling");
                    } else {
                        chessBoardInstance.setChessBoardPiece(srcPiece.getCurrentCoordinate(), destCor);

                    }

                } else {
                    chessBoardInstance.setChessBoardPiece(srcPiece.getCurrentCoordinate(), destCor);

                }
                chessBoardInstance.setCurrentTurnColor(Objects.equals(chessBoardInstance.getCurrentTurnColor(), WHITE) ? BLACK : WHITE);

                if (KingPiece.isKingAtRisk(chessBoardInstance.getCurrentTurnColor())) {
                    String color = chessBoardInstance.getCurrentTurnColor().equals(WHITE) ? BLACK : WHITE;

                    if(KingPiece.didWin(chessBoardInstance.getCurrentTurnColor()))
                        outputs.add(color + " Won" );
                    else
                        outputs.add(chessBoardInstance.getCurrentTurnColor() + " in Check" );
                }
                if (KingPiece.didWin(chessBoardInstance.getCurrentTurnColor())) {
                    String color = chessBoardInstance.getCurrentTurnColor().equals(WHITE) ? BLACK : WHITE;

                    chessBoardInstance.setGameEnded(true);
                    throw new Won(color);
                } else if (!srcPiece.isThereValidMoves(Objects.equals(srcPiece.getPieceColor(), WHITE) ? BLACK : WHITE)) {
                    chessBoardInstance.setGameEnded(true);
                    throw new Stalemate();
                }
            } else {
                throw new InvalidMove();
            }
            if (chessBoardInstance.isInsufficient()) {
                chessBoardInstance.setGameEnded(true);
                throw new Insufficient();
            }
        } else {
            throw new InvalidMove();
        }
    }
        public void undo() {
        caretaker.undo(chessBoardInstance);

    }
        public void doCaptured(CoordinateEnum destCor, Piece srcPiece) {
        Piece destPiece = chessBoardInstance.getChessBoardPiece(destCor);
        if (destPiece != null) {
            if (!Objects.equals(destPiece.getPieceColor(), srcPiece.getPieceColor())) {
//                System.out.println(CAPTURED + ' ' + destPiece.getPieceName());
                outputs.add(CAPTURED + ' ' + destPiece.getPieceName());
            }
        }
    }
}

