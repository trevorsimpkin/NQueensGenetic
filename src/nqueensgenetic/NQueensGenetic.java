/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nqueensgenetic;

import java.util.Arrays;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author trevor
 */
public class NQueensGenetic extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        final int [] squareSize = {15};
        final int [] sizearr = {50};
        final int [] iterations = {0};
        int popSize = 100;
        final int [] maxIter = {100000};
        double mutation = 0.001;
        
        String selectionMethod = "tournament";
        BorderPane background = new BorderPane();
        VBox sidePanel = new VBox();
        HBox topPanel = new HBox();
        HBox numQueensPanel = new HBox();
        GridPane root = new GridPane();
        final int WINDOW = 1420;
        Text scenetitle = new Text("N-Queens Genetic Algorithm");
        Text numQueensLabel = new Text("Number of Queens (N): ");
        Text boardLabel = new Text("Boardsize (and # of Queens): ");
        Text popLabel = new Text("Population size:             " + popSize);
        Text iterLabel = new Text("Max number of iterations:    " + maxIter);
        Text mutLabel = new Text("Mutation rate:               " + mutation);
        Text clashLabel = new Text("Number of potential clashes: ");
        Text selectLabel = new Text("Selection method:            " + selectionMethod);
        scenetitle.setStyle("-fx-font: 32 helvetica");
        scenetitle.setId("welcome-text");
        scenetitle.setFill(Color.CORAL);
        topPanel.getChildren().add(scenetitle);
        topPanel.setAlignment(Pos.CENTER);
        Button runBtn = new Button("Run Run");
        Button eightBtn = new Button("8"); 
        Button fiftyBtn = new Button("50");
        Button hundredBtn = new Button("100");
        sidePanel.getChildren().add(runBtn);
        final Text actiontarget = new Text();
        sidePanel.getChildren().add(actiontarget);
        sidePanel.getChildren().add(numQueensLabel);
        numQueensPanel.getChildren().add(eightBtn);
        numQueensPanel.getChildren().add(fiftyBtn);
        numQueensPanel.getChildren().add(hundredBtn);
        sidePanel.getChildren().add(numQueensPanel);
        sidePanel.getChildren().add(boardLabel);
        sidePanel.getChildren().add(popLabel);
        sidePanel.getChildren().add(iterLabel);
        sidePanel.getChildren().add(mutLabel);
        sidePanel.getChildren().add(clashLabel);
        sidePanel.getChildren().add(selectLabel);
        background.setLeft(sidePanel);
        background.setTop(topPanel);
        background.setCenter(root);
        Scene scene = new Scene(background, WINDOW, WINDOW, Color.BLACK);
        primaryStage.setScene(scene);
        runBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.getChildren().clear();
                root.setGridLinesVisible(false);
                Qboard[] population = new Qboard[popSize];
                for (int i = 0; i <population.length; i++) {
                    population[i]= new Qboard(sizearr[0]);
                } 
                Arrays.sort(population);
                int topFit = population[0].getFitness();

                printBoard(population[0],sizearr[0], root, squareSize[0]);
                if (selectionMethod.equals("tournament")) {
                    while(topFit!=numOfPotentialClashes&&iterations<maxIter) {
                        tournamentSelection(population, mutation, size);
                        topFit = population[0].getFitness();
                        iterations++;
                        if ((iterations)%(maxIter[0]/10)==0) {
                            System.out.println(((double)iterations/maxIter[0])*100.00 + "%");
                            System.out.println("Top Fitness: " + topFit);
                            population[0].printBoard();
                        }
                    }
                }
                actiontarget.setId("actiontarget");
                actiontarget.setText("Runningggggggggg...");
                 
            }
        });
         eightBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                
                sizearr[0] = 8;
                squareSize[0] = 25;
                numQueensLabel.setText("Number of Queens (N): 8");
                int numOfPotentialClashes = ((sizearr[0]-1)*(sizearr[0])/2);
                clashLabel.setText("Number of potential clashes: " + numOfPotentialClashes);
                
                 
            }
        });
        fiftyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                
                sizearr[0] = 50;
                squareSize[0] = 15;
                numQueensLabel.setText("Number of Queens (N): 50");
                int numOfPotentialClashes = ((sizearr[0]-1)*(sizearr[0])/2);
                clashLabel.setText("Number of potential clashes: " + numOfPotentialClashes);
                
                 
            }
        });
        hundredBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                
                sizearr[0] = 100;
                squareSize[0] = 8;
                numQueensLabel.setText("Number of Queens (N): 100");
                int numOfPotentialClashes = ((sizearr[0]-1)*(sizearr[0])/2);
                clashLabel.setText("Number of potential clashes: " + numOfPotentialClashes);
                
                 
            }
        });
       
        primaryStage.show();
    }
 public static int sumFitness (Qboard[] population) {
        int sum = 0;
        for (int i = 0; i <population.length; i++) {
            sum+=population[i].getFitness();
        }
        return sum; 
    }
    public static void setProbs (Qboard[] population, int totfitness) {
        double totProb = 0.0;
        for (int i = 0; i <population.length; i++) {
            totProb += population[i].calcProb(totfitness);
            population[i].setProb(totProb);
        } 
    }
    public static Qboard [] roulleteSelection(Qboard[] population, double mutation, int size) {
        Qboard[] newPopulation = new Qboard[population.length];
        double rand;
        double prob;
        int j;
        int halfpop = population.length/2;
        for (int i =0; i < halfpop; i++) {
            rand = Math.random();
            j = 0;
            prob = population[j].getProb();
            while (rand>prob&&j<population.length-1) {
                prob += population[j].getProb();
                j++;
            }
            
            newPopulation[i] = population[j];
        }
        mateBoard(newPopulation, mutation, size);
        Arrays.sort(newPopulation);
        return newPopulation;         
    }

    public static void tournamentSelection(Qboard[] population, double mutation, int size) {
        randomShuffleHalf(population);
        mateBoard(population, mutation, size);
        Arrays.sort(population);
        
    }
    public static void randomShuffleHalf(Qboard[] population) {
        int halfpop = population.length/2;
        int rand;
        Qboard temp;
        for (int i = 0; i<halfpop; i++){                 
            rand = (int) (Math.random() * halfpop);
            temp = population[i];
            population[i] = population[rand];
            population[rand] = temp; 
        }
    }
    public static void printBoard(Qboard board, int size, GridPane root, int squareSize){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                StackPane stack = new StackPane();
                Rectangle square = new Rectangle();
                square.setWidth(squareSize);
                square.setHeight(squareSize);
                Text text = new Text("Q");
                if(board.boardIndex(j)==i+1){
                    square.setFill(Color.WHITE);
                    square.setStroke(Color.RED);
                    stack.getChildren().addAll(square,text);
                }
                else {
                    square.setFill(Color.WHITE);
                    square.setStroke(Color.BLUE);
                    stack.getChildren().add(square);
                }
                root.add(stack, j, i);
                
            }
        } 
        
    }
    public static void mateBoard(Qboard[] population, double mutation, int size) {
        int crossover; 
        int halfpop = population.length/2;        
        for (int j=0; j<halfpop; j+=2) {
            crossover = (int)((Math.random()*size));           
            population[halfpop+j-1]= new Qboard(population[j], population[j+1], crossover, mutation, size); 
            population[halfpop+j]= new Qboard(population[j+1], population[j], crossover, mutation, size); 
        }
        if (population[population.length-1]==null) population[population.length-1]=population[0]; 
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }
    
}
