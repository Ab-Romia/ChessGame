package BoardGUI;


import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import ChessCore.ChessBoard;
import ChessCore.Enum.*;
import ChessCore.Pieces.*;

public class ChessBoardGUI extends JPanel {
    private static final int SIZE = 8;
    private static final int SQUARE_SIZE = 120;
    private static final Color LIGHT_BROWN = new Color(160, 70, 45);
    private static final Color YELLOW_WHITE = new Color(255, 255, 200);
    private char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private ChessBoard board = ChessBoard.getInstance();
    private Piece selectedPiece = null;
    private boolean turn = true;
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
                    selectedPiece = board.getChessBoardPiece(CoordinateEnum.getCoordinateEnum(col, SIZE-1-row));
                    selectedRow = row;
                    selectedCol = col;
                    if(selectedPiece!=null&&board.srcInv(selectedPiece.getCurrentCoordinate()))
                    {
                        JOptionPane.showMessageDialog(null, "Invalid move");
                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;

                    }
                    if(board.getCurrentTurnColor()!=selectedPiece.getPieceColor())
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
                    boolean inv = true;
                    if (board.destInv(src, dest)) {

                        selectedPiece = null;
                        selectedRow = -1;
                        selectedCol = -1;
                        JOptionPane.showMessageDialog(null, "Invalid Move");
                    } else {
                        try {
                            board.play(CoordinateEnum.getCoordinateEnum(selectedCol, SIZE - 1 - selectedRow), CoordinateEnum.getCoordinateEnum(col, SIZE - 1 - row), selectedPiece.getPieceColor());
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                            inv = false;

                            JOptionPane.showMessageDialog(null, "Invalid move");
                        }
                        if (inv) {
                            selectedPiece = null;
                            selectedRow = -1;
                            selectedCol = -1;
                        }
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
                }


                // If this square is the selected square, draw a border around it
                if (selectedPiece != null && selectedRow == row && selectedCol == col) {
                    List<CoordinateEnum> temp = board.getChessBoardPiece(CoordinateEnum.getCoordinateEnum(col, SIZE - row - 1)).getValidMoves();
                    g.setColor(Color.BLUE);
                    g.drawRect(col * SQUARE_SIZE+5, row * SQUARE_SIZE+5, SQUARE_SIZE-10, SQUARE_SIZE-10);
                    System.out.println(temp);
                    for(CoordinateEnum c : temp)
                    { g.setColor(Color.BLUE);
                    g.drawRect(c.getXCoordinate() * SQUARE_SIZE+5, (SIZE-1-c.getYCoordinate() )* SQUARE_SIZE+5, SQUARE_SIZE-10, SQUARE_SIZE-10);}
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
                    g.drawString(SIZE- row+ "", col * SQUARE_SIZE, row * SQUARE_SIZE + g.getFontMetrics().getAscent());
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