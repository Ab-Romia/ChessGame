package BoardGUI;



import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.table.DefaultTableModel;
import ChessCore.ChessBoard.ChessBoard;
import ChessCore.Enum.*;
import ChessCore.Pieces.*;
import Exceptions.*;

public class ChessBoardGUI extends JPanel {
    private static final int SIZE = 8;
    private static final int SQUARE_SIZE = 120;
    private static final Color LIGHT_BROWN = new Color(160, 70, 45);
    private static final Color YELLOW_WHITE = new Color(255, 255, 200);
    private final char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private final ChessBoard board = ChessBoard.getInstance();
    private Piece selectedPiece = null;
    private CoordinateEnum lastMoveSrc = null;
    private CoordinateEnum lastMoveDest = null;
    private boolean flip = false;
//    private boolean turn = true;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private final DefaultTableModel movesTableModel;
    private final JTable movesTable;
    private JButton undoButton;
    public ChessBoardGUI() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = e.getX() / SQUARE_SIZE;
                int row = e.getY() / SQUARE_SIZE;
                if(flip)
                {
//                    col = SIZE - 1 - col;
                    row = SIZE - 1 - row;
                }


                    if (selectedPiece == null) {

                        System.out.println("Selected: " + CoordinateEnum.getCoordinateEnum(col, SIZE - row - 1));
                        selectedPiece = board.getChessBoardPiece(CoordinateEnum.getCoordinateEnum(col, SIZE - row - 1));
                        selectedRow = row;
                        selectedCol = col;
                        if (selectedPiece != null && board.srcInv(selectedPiece.getCurrentCoordinate())) {
                            JOptionPane.showMessageDialog(null, "Invalid move");
                            selectedPiece = null;
                            selectedRow = -1;
                            selectedCol = -1;

                        }
                        try {
                            if (!Objects.equals(board.getCurrentTurnColor(), selectedPiece.getPieceColor())) {
                                JOptionPane.showMessageDialog(null, board.getCurrentTurnColor() + "'s turn");
                                selectedPiece = null;
                                selectedRow = -1;
                                selectedCol = -1;
                            }
                        } catch (NullPointerException ex) {
                            System.out.println(ex.getMessage());
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
                                if (Objects.equals(selectedPiece.getPieceName(), "Pawn") && (Objects.equals(selectedPiece.getPieceColor(), "White") && row == 0 && selectedRow == 1 || Objects.equals(selectedPiece.getPieceColor(), "Black") && row == 7 && selectedRow == 6)) {
                                    String[] options = {"Queen", "Rook", "Bishop", "Knight"};
                                    String temp;
                                    int x = JOptionPane.showOptionDialog(null, "Choose a piece to promote the pawn to:", "Pawn Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
//                                int x = JOptionPane.showOptionDialog(null, "Choose a piece to promote to", "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                                    if (x == 0)
                                        temp = "Q";

                                    else if (x == 1)
                                        temp = "R";
                                    else if (x == 2)
                                        temp = "B";
                                    else if (x == 3)
                                        temp = "K";
                                    else
                                        temp = "Q";
                                    board.play(CoordinateEnum.getCoordinateEnum(selectedCol, SIZE - 1 - selectedRow), CoordinateEnum.getCoordinateEnum(col, SIZE - 1 - row), temp,false);

                                } else {
                                    board.play(CoordinateEnum.getCoordinateEnum(selectedCol, SIZE - 1 - selectedRow), CoordinateEnum.getCoordinateEnum(col, SIZE - 1 - row), "",false);
                                }
                                updateMovesTable(src, dest);
                                lastMoveSrc = src;
                                lastMoveDest = dest;
                                flip = !flip;
                            }
                            catch (Won | Insufficient | Stalemate w) {

                                JOptionPane.showMessageDialog(null, w.getMessage());
                                updateMovesTable(src, dest,w.getMessage());
                            } catch (NullPointerException | InvalidMove ex) {
                                System.out.println(ex.getMessage());
                            }

                         catch (GamedEnded ge) {
                            JOptionPane.showMessageDialog(null, ge.getMessage());
                            System.exit(0);}
                            catch (Exception ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage());
                                throw new RuntimeException(ex);
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
        movesTableModel = new DefaultTableModel();
        movesTableModel.addColumn("Source");
        movesTableModel.addColumn("Destination");
        movesTable = new JTable(movesTableModel);
            Font font = new Font("Romia", Font.PLAIN, 18);
            movesTable.setFont(font);
            movesTable.setRowHeight(25);
            movesTable.getTableHeader().setFont(font);
        JScrollPane scrollPane = new JScrollPane(movesTable);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            // Call the undo method when the button is clicked
            try {
                board.undo();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            repaint();
        });

    }
    private void updateMovesTable(CoordinateEnum src, CoordinateEnum dest) {
        movesTableModel.addRow(new Object[]{src.toString(), dest.toString()});
    }
    private void updateMovesTable(CoordinateEnum src, CoordinateEnum dest, String mode) {
        movesTableModel.addRow(new Object[]{src.toString(), dest.toString()+" "+mode});
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int col = 0; col < SIZE; col++) {
            for (int row = 0; row < SIZE; row++) {
                int rowF = row;
                if(flip) {
//                    colF = SIZE - 1 -col;
                    rowF = SIZE -1 -row;
                }
                CoordinateEnum currentSquare = CoordinateEnum.getCoordinateEnum(col, SIZE - rowF - 1);

                if ((rowF + col) % 2 == 0) {
                    g.setColor(YELLOW_WHITE);
                } else {
                    g.setColor(LIGHT_BROWN);
                }
                g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

                if (currentSquare.equals(lastMoveSrc) || currentSquare.equals(lastMoveDest))
                {
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(col * SQUARE_SIZE+5, row * SQUARE_SIZE+5, SQUARE_SIZE-10, SQUARE_SIZE-10);
                }


                // Draw the piece if there is one at this position
                Piece p = board.getChessBoardPiece(CoordinateEnum.getCoordinateEnum(col, SIZE - rowF - 1));
                if (selectedPiece != null) {

                    g.setColor(Color.DARK_GRAY);
//                    System.out.println(temp);
                    if(rowF==selectedRow && col==selectedCol)
                        g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    g.setColor(Color.GREEN);
                    if(selectedPiece.isValidMove(CoordinateEnum.getCoordinateEnum(col,SIZE-1-rowF))) {
                        g.fillRect(col * SQUARE_SIZE + 5, row * SQUARE_SIZE + 5, SQUARE_SIZE - 10, SQUARE_SIZE - 10);
                        if(board.isCapture(selectedPiece.getCurrentCoordinate(),CoordinateEnum.getCoordinateEnum(col,SIZE-1-rowF))) {
                            g.setColor(new Color(255, 0, 0, 100));
                            g.fillRect(col * SQUARE_SIZE + 10, row * SQUARE_SIZE + 10, SQUARE_SIZE - 20, SQUARE_SIZE - 20);
                        }
                    }

                }
                if (p != null) {
                    // Load the image for this piece
                    ImageIcon imageIcon = new ImageIcon("PiecesPNG/" + p.getPC() + ".png");
                    Image image = imageIcon.getImage();
                    if (p.getPieceName().equals("King") && KingPiece.isKingAtRisk(p.getPieceColor())) {
                        g.setColor(Color.RED);
                        g.fillRect(col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);}

                    // Draw the image
                    g.drawImage(image, col * SQUARE_SIZE, row * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE, this);

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
                    g.drawString(SIZE- rowF+ "", 0, row * SQUARE_SIZE + g.getFontMetrics().getAscent());
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

            ChessBoardGUI chessBoardGUI = new ChessBoardGUI();
            frame.add(chessBoardGUI, BorderLayout.CENTER);

            JScrollPane scrollPane = new JScrollPane(chessBoardGUI.movesTable);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            frame.add(scrollPane, BorderLayout.EAST);
            frame.add(chessBoardGUI.undoButton, BorderLayout.SOUTH);

            frame.pack();
            frame.setVisible(true);
        });
    }
}