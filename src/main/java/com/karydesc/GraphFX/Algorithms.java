package com.karydesc.GraphFX;

import javafx.application.Platform;
import javafx.scene.paint.Color;

import java.util.*;

public class Algorithms {
    public static void runDijkstra(AppController.NodeFX source, Map<String, AppController.NodeFX> circles, Map<AppController.NodeFX, Map<AppController.NodeFX, AppController.Edge>> adjList) {
        for (String x : circles.keySet()) { //set initial values on all nodes
            AppController.NodeFX node = circles.get(x);
            node.minDistance = Float.MAX_VALUE;
            node.previous = null;
            Platform.runLater(node::reset);
        }
        Platform.runLater(() -> source.setMinDistance(0F));
        source.minDistance = 0F;

        //priority queue with custom comparator
        PriorityQueue<AppController.NodeFX> priorityQueue = new PriorityQueue<>(10, Comparator.comparing(node -> node.minDistance));
        priorityQueue.add(source);


        while (!priorityQueue.isEmpty()) { //dijkstra implementation
            AppController.NodeFX current = priorityQueue.poll(); //get current node
            Platform.runLater(current::setActive); //set it as active

            for (AppController.NodeFX node : current.getAdjacent()) { //iterate through its neighbors
                Float newDistance = current.minDistance + adjList.get(current).get(node).getWeight(); //current distance to source
                if (newDistance < node.minDistance) {//if the new distance is better than the old
                    priorityQueue.remove(node); //remove the node from priority list
                    node.minDistance = newDistance; //adjust distance property
                    node.previous = current; //set pointer to previous node
                    Platform.runLater(() -> node.setMinDistance(newDistance)); //on main thread, change its label to the new distance
                    priorityQueue.add(node); //read the node with updated values
                }
            }
            try {
                Thread.sleep(250); //sleep for once second for visualization purposes
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(current::setVisited); //set node as visited


        }
        for (AppController.NodeFX user : circles.values()) { //this part is where we retrace our steps to the source for every node (for loop iterates through every node)
            if (user.equals(source)) continue; //we don't need the source

            System.out.print("\nShortest path from source to " + user.getName() + ": ");

            if (user.minDistance == Float.MAX_VALUE) { //if the minDistance value has not been touched at all, it means that the node has no available path to the source
                System.out.println("No path found.");
                continue;
            }

            // Use a stack to reconstruct the path
            Stack<String> pathStack = new Stack<>();
            AppController.NodeFX currIter = user;

            while (currIter != null) {//currIter being null means we reached the source, before we reach it we iterate through the pointers and push the names to the stack
                pathStack.push(currIter.getName());
                currIter = currIter.previous;
            }

            // Print the path in the correct order
            while (!pathStack.isEmpty()) {
                System.out.print(pathStack.pop());
                if (!pathStack.isEmpty()) {
                    System.out.print(" -> ");
                }
            }
        }


    }


    public static void runCommunityDetection(int maxIterations, Map<String, AppController.NodeFX> circles, Map<AppController.NodeFX, Map<AppController.NodeFX, AppController.Edge>> adjList) {
        Map<AppController.NodeFX, Integer> labels = new HashMap<>();
        // Step 1: Initialize each node with a unique label
        int labelId = 0;
        for (AppController.NodeFX node : circles.values()) {
            labels.put(node, labelId++);
        }

        boolean changed = true;
        int iteration = 0;

        // Step 2: Label propagation loop
        while (changed && iteration < maxIterations) {
            changed = false;
            iteration++;

            List<AppController.NodeFX> nodes = new ArrayList<>(circles.values());
            Collections.shuffle(nodes); // important!

            for (AppController.NodeFX node : nodes) {

                Map<Integer, Integer> labelCounts = new HashMap<>();

                for (AppController.NodeFX neighbor : node.getAdjacent()) {
                    AppController.Edge edge = adjList.get(node).get(neighbor);
                    int neighborLabel = labels.get(neighbor);

                    // Weight influence
                    int weight = (int) Math.max(edge.getWeight() * 10, 1);

                    labelCounts.put(
                            neighborLabel,
                            labelCounts.getOrDefault(neighborLabel, 0) + weight
                    );
                }

                // Find most frequent label
                int bestLabel = labels.get(node);
                int maxCount = -1;

                for (Map.Entry<Integer, Integer> entry : labelCounts.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        bestLabel = entry.getKey();
                    }
                }

                // Update label if changed
                if (labels.get(node) != bestLabel) {
                    labels.put(node, bestLabel);
                    changed = true;
                }
            }
        }

    // Step 3: Build communities
    Map<Integer, Set<AppController.NodeFX>> communities = new HashMap<>();

    for (AppController.NodeFX node : labels.keySet()) {
        int label = labels.get(node);
        communities.computeIfAbsent(label, k -> new HashSet<>()).add(node);
    }

    // Step 4: Assign colors
    Map<Integer, Color> colorMap = new HashMap<>();
    for (Integer label : communities.keySet()) {
        colorMap.put(label, Color.color(Math.random(), Math.random(), Math.random()));
    }

    Platform.runLater(() -> {
        for (AppController.NodeFX node : labels.keySet()) {
            node.setFill(colorMap.get(labels.get(node)));
        }
    });

    // Step 5: Print communities
    for (Set<AppController.NodeFX> community : communities.values()) {
        if (community.size() > 1) {
            System.out.println("\n--- Community ---");
            for (AppController.NodeFX node : community) {
                System.out.print(" " + node.name + " ");
            }
        }
    }
}
    public static void friendSuggestion(Map<String, AppController.NodeFX> circles, Map<AppController.NodeFX, Map<AppController.NodeFX, AppController.Edge>> adjList) {
        Map<AppController.NodeFX, Set<AppController.NodeFX>> friendSuggestions = new HashMap<>(); //keep track of friend suggestions
        for (String x : circles.keySet()) {
            AppController.NodeFX current = circles.get(x);
            friendSuggestions.put(current, new HashSet<>());
            for (AppController.NodeFX adjNode1 : current.getAdjacent()) {
                for (AppController.NodeFX adjNode2 : adjNode1.getAdjacent()) {
                    if (!current.getAdjacent().contains(adjNode2) && !current.equals(adjNode2)) {
                        friendSuggestions.get(current).add(adjNode2);
                    }
                }
            }
        }

        for (Map.Entry<AppController.NodeFX, Set<AppController.NodeFX>> entry : friendSuggestions.entrySet()) {
            AppController.NodeFX user = entry.getKey();
            Set<AppController.NodeFX> suggestions = entry.getValue();
            System.out.println("\nUser: " + user.getName());
            System.out.println("Suggested Friends: ");
            for (AppController.NodeFX suggestedFriend : suggestions) {
                System.out.println(suggestedFriend.getName());
            }
        }
    }
}
