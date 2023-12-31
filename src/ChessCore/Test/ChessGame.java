package ChessCore.Test;

import ChessCore.ChessBoard.ChessBoard;
import ChessCore.ChessBoard.Player;
import ChessCore.Enum.CoordinateEnum;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static ChessCore.Utils.Constants.FILE_NAME;
import static ChessCore.Utils.Constants.OUTPUT_FILE;

/**
 *
 * @author romia
 */
public class ChessGame {
    public static List<String> readFile() {
        Path path = Paths.get(FILE_NAME);
        List<String> lines = null;
        try {
            lines =  Files.readAllLines(path);
        } catch (IOException e) {
            System.err.println("Error reading file: " + FILE_NAME);
            e.printStackTrace();
        }
        return lines;
    }

    // Online Java Compiler
// Use this editor to write, compile and run your Java code online


    public static void writeToFile(List<String> output) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));
            for (String s : output) {
                writer.write(s);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ChessBoard chessBoardInstance = ChessBoard.getInstance();

        List<String> moves = readFile();
        String name;
        for (String move : moves) {
            name = "";
            String[] values = move.split(",");
            CoordinateEnum srcCoor = CoordinateEnum.checkName(values[0]).orElse(null);
            CoordinateEnum dstCoor = CoordinateEnum.checkName(values[1]).orElse(null);
            if(values.length == 3) {
                 name = values[2];
            }
            try {
                chessBoardInstance.play(srcCoor, dstCoor, name,false);

            } catch (Exception e) {
                break;
            }
        }
        writeToFile(Player.getOutputs());
        chessBoardInstance.printChessBoard();
    }
}
