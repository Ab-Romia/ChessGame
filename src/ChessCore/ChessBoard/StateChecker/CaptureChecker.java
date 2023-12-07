package ChessCore.ChessBoard.StateChecker;

import ChessCore.ChessBoard.ChessBoard;
import ChessCore.ChessBoard.StateChecker.GameStateChecker;
import ChessCore.Enum.CoordinateEnum;
import ChessCore.Pieces.PawnPiece;
import ChessCore.Pieces.Piece;
public class CaptureChecker implements GameStateChecker {
    private final CoordinateEnum srcCor;
    private final CoordinateEnum destCor;

    public CaptureChecker(CoordinateEnum srcCor, CoordinateEnum destCor) {
        this.srcCor = srcCor;
        this.destCor = destCor;
    }
    @Override
    public boolean check(ChessBoard chessBoard) {
        Piece srcPiece = chessBoard.getChessBoardPiece(srcCor);
        Piece destPiece = chessBoard.getChessBoardPiece(destCor);
        if (srcPiece instanceof PawnPiece && ((PawnPiece) srcPiece).isEnPassant(destCor))
            return true;
        if (chessBoard.getCurrentTurnColor().equals(srcPiece.getPieceColor()) && srcPiece.isValidMove(destCor))
            if (destPiece != null)
                return !destPiece.getPieceColor().equals(srcPiece.getPieceColor());
        return false;
    }
}
