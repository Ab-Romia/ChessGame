package BoardGUI;


import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

import ChessCore.ChessBoard;
import ChessCore.Enum.*;
import ChessCore.Pieces.*;

public class ChessBoardGUI extends JPanel {
    private static final int SIZE = 8;
    private static final int SQUARE_SIZE = 120;
    private static final Color LIGHT_BROWN = new Color(160, 70, 45);
    private static final Color YELLOW_WHITE = new Color(255, 255, 200);
    private final char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private final ChessBoard board = ChessBoard.getInstance();
    private Piece selectedPiece = null;
//    private boolean turn = true;
    private int selectedRow = -1;
    private int selectedCol = -1;
    public ChessBoardGUI() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / SQUARE_SIZE;
                int row = e.getY() / SQUARE_SIZE;
                if (selectedPiece == null) {

                    System.out.println("Selected: " + CoordinateEnum.getCoordinateEnum(col, SIZE-row-1));
                    selectedPiece = board.getChessBoardPiece(CoordinateEnum.getCoordinateEnum(col, SIZE-row-1));
                    selectedRow = row;
                    selectedCol = col;
                    if(selectedPiece!=null&&board.srcInv(selectedPiece.getCurrentCoordinate()))
                    {
                        JOptionPane.showMessageDialog(null, "Invalid move");
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;

                    }
                    if(!Objects.equals(board.getCurrentTurnColor(), selectedPiece.getPieceColor()))
                    {
                        JOptionPane.showMessageDialog(null, board.getCurrentTurnColor()+"'s turn");
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                    }

                    repaint();
                } else {
                    // Move the selected piece
                    CoordinateEnum src = CoordinateEnum.getCoordinateEnum(selectedCol, SIZE - 1 - selectedRow);
                    CoordinateEnum dest = CoordinateEnum.getCoordinateEnum(col, SIZE - 1 - row);

                    if (board.destInv(src, dest)) {

                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                        JOptionPane.showMessageDialog(null, "Invalid Move");
                    } else {
                        try {
                            if(Objects.equals(selectedPiece.getPieceName(), "Pawn")&&(Objects.equals(selectedPiece.getPieceColor(), "White") &&row==0|| Objects.equals(selectedPiece.getPieceColor(), "Black") &&row==7))
                            {
                                String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                                String temp;
                                int x = JOptionPane.showOptionDialog(null, "Choose a piece to promote the pawn to:", "Pawn Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//                                int x = JOptionPane.showOptionDialog(null, "Choose a piece to promote to", "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                                if(x==0)
                                    temp = "Q";

                                else if(x==1)
                                    temp ="R";
                                else if(x==2)
                                    temp = "B";
                                else if(x==3)
                                    temp = "K";
                                else
                                    temp = "Q";
                                board.play(CoordinateEnum.getCoordinateEnum(selectedCol, SIZE - 1 - selectedRow), CoordinateEnum.getCoordinateEnum(col, SIZE - 1 - row), temp);

                            }
                            else
                            {
                                board.play(CoordinateEnum.getCoordinateEnum(selectedCol, SIZE - 1 - selectedRow), CoordinateEnum.getCoordinateEnum(col, SIZE - 1 - row), "");
                            }
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                        // Update the board
                        repaint();
                    }
                }
            }
        });
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {

                if ((row + col) % 2 == 0) {
                    g.setColor(YELLOW_WHITE);
                } else {
                    g.setColor(LIGHT_BROWN);
                }
                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                // Draw the piece if there is one at this position
                Piece p = board.getChessBoardPiece(CoordinateEnum.getCoordinateEnum(col, SIZE - row - 1));
                if (p != null) {
                    // Load the image for this piece
                    ImageIcon imageIcon = new ImageIcon("PiecesPNG/" + p.getPC() + ".png");
                    Image image = imageIcon.getImage();
                    // Draw the image
                    g.drawImage(image, col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, this);
                    if (p.getPieceName().equals("King") && KingPiece.isKingAtRisk(p.getPieceColor())) {
                    g.setColor(Color.RED);
                    g.drawRect(col * SQUARE_SIZE+5, row * SQUARE_SIZE+5, SQUARE_SIZE-10, SQUARE_SIZE-10);}
                }


                // If this square is the selected square, draw a border around it
                // If this square is the selected square, draw a border around it
                // If this square is the selected square, draw a border around it
                if (selectedPiece != null) {
                    List<CoordinateEnum> temp = selectedPiece.getValidMoves();
                    g.setColor(Color.BLUE);
//                    System.out.println(temp);
                        if(selectedPiece.isValidMove(CoordinateEnum.getCoordinateEnum(col,SIZE-1-row)))
                             g.drawRect(col * SQUARE_SIZE+5, row * SQUARE_SIZE+5, SQUARE_SIZE-10, SQUARE_SIZE-10);

                }

                // Add text to the square
                if (row == 7) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Default", Font.BOLD, 20));
                    g.drawString(letters[col] + "", col * SQUARE_SIZE, (row + 1) * SQUARE_SIZE - g.getFontMetrics().getDescent());
                }
                if (col == 0) {
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Default", Font.BOLD, 20));
                    g.drawString(SIZE- row+ "", 0, row * SQUARE_SIZE + g.getFontMetrics().getAscent());
                }
            }
        }
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE * SQUARE_SIZE, SIZE * SQUARE_SIZE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess Board");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new ChessBoardGUI());
            frame.pack();
            frame.setVisible(true);
        });
    }
}