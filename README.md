# GraphFX: Interactive Graph Algorithm Visualizer

A desktop application built with Java and JavaFX for creating, managing, and visualizing graph data structures and algorithms.

## Features
* **Interactive Canvas:** Dynamically add, remove, drag, and connect nodes on the canvas. Supports zooming and panning for navigating larger graphs.
* **Algorithms Visualized:** Execute and visualize Dijkstra's Shortest Path and a custom Community Detection algorithm in real-time.
* **Multithreaded UI:** Heavy algorithm calculations run on dedicated background threads. The application strictly uses `Platform.runLater` to ensure the JavaFX UI remains responsive and updates safely.
* **Random Graph Generation:** Automatically generate and populate large test graphs with randomized node placements, edges, and connection weights.
* **Data Persistence:** Save and load graph states locally. This is powered by a custom regex-based encoder/decoder to parse text files.
* **Friend Suggestions:** Basic recommendation logic based on node adjacency mapping.

## Tech Stack
* **Language:** Java
* **GUI Framework:** JavaFX (FXML)
* **Build Tool:** Maven

---

## Prerequisites
To build and run this project, you will need:
* Java Development Kit (JDK) 17 or higher.
* (Optional) Maven installed on your system. *Note: The project includes a Maven Wrapper (`mvnw`), so installing Maven locally is not strictly required.*

## How to Run
Because this project utilizes the Maven Wrapper, you can easily compile and run it directly from your terminal without manually configuring JavaFX modules.

**On macOS / Linux:**
` ` `bash
./mvnw clean javafx:run
` ` `

**On Windows:**
` ` `cmd
mvnw.cmd clean javafx:run
` ` `

*(Note: Ensure your terminal is in the root directory containing the `pom.xml` file before executing the commands).*
