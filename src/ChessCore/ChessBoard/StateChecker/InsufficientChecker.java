package ChessCore.ChessBoard.StateChecker;
import ChessCore.ChessBoard.ChessBoard;
import ChessCore.ChessBoard.StateChecker.GameStateChecker;
import ChessCore.Pieces.Piece;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static ChessCore.Utils.Constants.*;
public class InsufficientChecker implements GameStateChecker {
    @Override
    public boolean check(ChessBoard chessBoard) {
        List<String> condition1 = new ArrayList<>(List.of(KING_PIECE_NAME));
        List<String> condition2 = new ArrayList<>(List.of(KING_PIECE_NAME, BISHOP_PIECE_NAME));
        List<String> condition3 = new ArrayList<>(List.of(KING_PIECE_NAME, KNIGHT_PIECE_NAME));

        List<String> whitePiece = new ArrayList<>();
        List<String> blackPiece = new ArrayList<>();

        for (int i = 0 ; i < 8 ; i++) {
            for (int j = 0 ; j < 8 ; j++) {
                Piece piece = chessBoard.getChessBoardPiece(i, j);
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

        if (InsufficientCondition(condition1, condition1, condition2, whitePiece, blackPiece, whitePiece.equals(condition2), whitePiece.equals(condition3), blackPiece.equals(condition3)))
            return true;
        if (InsufficientCondition(condition1, condition2, condition3, whitePiece, blackPiece, whitePiece.equals(condition1), whitePiece.equals(condition3), blackPiece.equals(condition1)))
            return true;
        return whitePiece.equals(condition3) && blackPiece.equals(condition2);
    }
    private boolean InsufficientCondition(List<String> condition1, List<String> condition2, List<String> condition3, List<String> whitePiece, List<String> blackPiece, boolean equals, boolean equals2, boolean equals3) {
        if (whitePiece.equals(condition2) && blackPiece.equals(condition1)) {
            return true;
        }
        if (equals && blackPiece.equals(condition3)) {
            return true;
        }
        if (equals2 && equals3) {
            return true;
        }
        return whitePiece.equals(condition2) && blackPiece.equals(condition3);
    }

}
