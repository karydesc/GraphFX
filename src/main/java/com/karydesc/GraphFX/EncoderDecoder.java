package com.karydesc.GraphFX;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncoderDecoder {
    private final AppController controller;
    public EncoderDecoder(AppController controller){
        this.controller = controller;
    }
    String encode(AppController.NodeFX node) {
        return String.format("n[%s,%s,%s]\n", node.name, node.getCenterX(), node.getCenterY()); //example output n[A,100.0,150.0]
    }

    String encode(AppController.Edge edge) {
        return String.format("e[%s,%s,%s]\n", edge.source.name, edge.destination.name, edge.getWeight()); //example output e[D,F,0.5]
    }

    void readFromFile(String filename) {
        File myFile; //file object to represent the input file
        Scanner myReader; //scanner to read the file line by line
        try {
            myFile = new File(filename); //initialize file object with the given filename
            myReader = new Scanner(myFile); //initialize scanner to read the file
        } catch (IOException e) {
            System.out.println("An error occurred opening the file"); //error handling for file not found or reading issues
            return; //exit the function on error
        }
        while (myReader.hasNext()) { //iterate through each line in the file
            String line = myReader.nextLine(); //read the current line
            String regex;
            if (line.charAt(0) == 'n') { //check if the line starts with 'n' (node representation)
                regex = "n\\[(.*?),(.*?),(.*?)]"; //regex to match node format: n[name,value1,value2]
                Pattern pattern = Pattern.compile(regex); //compile the regex pattern
                Matcher matcher = pattern.matcher(line); //create a matcher for the current line
                if (matcher.matches()) { //check if the line matches the regex pattern
                    // Parse the node details
                    String name;
                    int value1;
                    int value2;
                    try {
                        name = matcher.group(1); //capture the name of the node
                        value1 = (int) Float.parseFloat(matcher.group(2)); //convert first value to an integer
                        value2 = (int) Float.parseFloat(matcher.group(3)); //convert second value to an integer
                    } catch (Exception e) {
                        System.out.println("Error when parsing the coordinates in line: " + line);
                        continue;
                    }
                    controller.addNode(value1, value2, name); //add the parsed node to the graph
                }

            } else if (line.charAt(0) == 'e') { //check if the line starts with 'e' (edge representation)
                regex = "e\\[(.*?),(.*?),(.*?)]"; //regex to match edge format: e[source,destination,weight]
                Pattern pattern = Pattern.compile(regex); //compile the regex pattern
                Matcher matcher = pattern.matcher(line); //create a matcher for the current line

                if (matcher.matches()) { //check if the line matches the regex pattern
                    // Parse the edge details
                    String source = matcher.group(1); //capture the source node name
                    String destination = matcher.group(2);//capture the destination node name
                    float weight;
                    try {
                        weight = Float.parseFloat(matcher.group(3));
                    } catch (Exception e) {
                        System.out.println("Error converting from float on line: " + line);
                        continue;
                    } //convert weight to a float
                    controller.addEdge(controller.circles.get(source), controller.circles.get(destination), weight); //add the edge to the graph
                }
            }

        }
    }

    void saveToFile(String filename, Map<String, AppController.NodeFX> circles, Set<AppController.Edge> edges) throws IOException {
        FileWriter myFile; //file writer object to write to the file
        try {
            myFile = new FileWriter(filename); //initialize the file writer with the given filename
        } catch (IOException e) {
            System.out.println("An error occurred."); //error handling for issues creating the file
            return; //exit the function on error
        }
        for (AppController.NodeFX node : circles.values()) { //iterate through all nodes in the graph
            myFile.write(encode(node)); //write the encoded node representation to the file
        }
        for (AppController.Edge edge : edges) { //iterate through all edges in the graph
            myFile.write(encode(edge)); //write the encoded edge representation to the file
        }
        myFile.close(); //close the file writer to save changes
    }

}