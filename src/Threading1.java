/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

 import java.io.IOException;
 import java.net.Socket;
 import java.util.Scanner;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import java.util.concurrent.*;
 import javafx.stage.Stage;
 import javafx.scene.control.TextArea;
 import javafx.application.Application;
 import javafx.scene.Scene;
 import javafx.scene.control.Button; 
 import javafx.scene.layout.VBox;
 import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.animation.Timeline;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.effect.Reflection;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.shape.Circle;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.Animation; 
import javafx.animation.KeyFrame; 
import javafx.util.Duration; 
import javafx.animation.KeyValue;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import javafx.scene.control.ProgressBar;

 public class Threading1 extends Application {
        private static SharedMemoryObject sharedMemory;
        private ProgressBar[] progressBars = new ProgressBar[3];
    public CountDownLatch startSignal = new CountDownLatch(1);
    
    private static final Path FLAG_FILE = Paths.get("javafx_launched.txt");
    private Stage primaryStage;
    private Scene mainScene;
    private Scene gameScene;
    private Scene gameOverScene;
    private Pane mainWindow;

    public Threading1() {
        SharedMemoryObject sharedMemory = new SharedMemoryObject(this);
        progressBars = new ProgressBar[3];
        for (int i = 0; i < 3; i++) {
            progressBars[i] = new ProgressBar(0);
        }
        createMainWindow();
    }
    
    

        public static void main(String[] args) {
        // Check if the flag file exists
        if (!Files.exists(FLAG_FILE)) {
            new Thread(() -> Application.launch(Threading1.class)).start();


            // Wait for JavaFX application to start
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Write the flag file
            try {
                Files.write(FLAG_FILE, new byte[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
   

        Scanner input = new Scanner(System.in);
        System.out.println("Enter S for server, C for Client");
        String choice = input.next();
        if (choice.equals("S")) {
            ServerListener server = new ServerListener(null);
            server.BuisnessLogic();
        } else {
            try {
                // Create an instance of Threading1
                Threading1 threading1 = new Threading1();
                Socket con = new Socket("127.0.0.1", 8888);
                ThreadedClient writerS = new ThreadedClient(con, true, threading1, sharedMemory);
                ThreadedClient readerS = new ThreadedClient(con, false, threading1, sharedMemory);
                
                System.out.println("Initiating Writer");
                Thread t1 = new Thread(writerS);
                System.out.println("Initiating Reader");
                Thread t2 = new Thread(readerS);
                System.out.println("Starting thread 1");
                t1.start();
                System.out.println("Starting thread 2");
                t2.start();
                try {
                    t1.join();
                    t2.join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Threading1.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (IOException ex) {
                Logger.getLogger(Threading1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        SharedMemoryObject sharedMemory = new SharedMemoryObject(null);
        this.primaryStage = primaryStage;
        mainWindow = createMainWindow();
        Pane gameWindow = createGameWindow(primaryStage);
        Pane gameOverWindow = createGameOverWindow();

        mainScene = new Scene(mainWindow, 700, 700);
        gameScene = new Scene(gameWindow, 700, 700);
        TextArea textArea = new TextArea(); // Declare and initialize the textArea variable
        gameOverScene = new Scene(gameOverWindow, 700, 700);

        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setTitle("Race From The Archives");
        startSignal.countDown();
         
       
    }


    private void createTextAnimation(Text text) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(text.fillProperty(), Color.BLACK)),
            new KeyFrame(Duration.seconds(1), new KeyValue(text.fillProperty(), Color.BLUE)),
            new KeyFrame(Duration.seconds(2), new KeyValue(text.fillProperty(), Color.BLACK))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    private Text createAnimatedText() {
        Text text = new Text("Race From The Archives!");
        text.setFill(Color.BLACK);
        text.setFont(Font.font("Chiller", FontWeight.BOLD, 40));
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        text.setEffect(r);
        text.setTranslateY(150);
        text.setTranslateX(0);
        createTextAnimation(text);
        return text;
    }
    
    public void addMarker () {

        System.out.println("addMarker is called"); // Debug line
            Platform.runLater(() -> {
                if (mainWindow == null) {
                    System.out.println("mainWindow is null"); // Debug line
                } else {
                    Circle circle = new Circle(100,100,25, Color.BLUE);
                    mainWindow.getChildren().add(circle);
                    mainWindow.applyCss();                 
                    mainWindow.layout();
                    System.out.println("Circle is added"); // Debug line
                    System.out.println("mainWindow is visible: " + mainWindow.isVisible());
                    System.out.println("mainWindow is part of a Scene: " + (mainWindow.getScene() != null));
                
                }
            });
    }

        Pane createMainWindow() {
        SharedMemoryObject sharedMemory = new SharedMemoryObject(this);
        mainWindow = new Pane();
        int pos = sharedMemory.getMypos();
        Text animatedText = createAnimatedText();
        animatedText.setX(90.0f);
        animatedText.setY(50.0f);
        animatedText.setCache(true);
        Button playGame = new Button("Play Game");
        playGame.setStyle("-fx-min-width:250px; -fx-min-height: 70px; -fx-background-color: Green; -fx-text-fill: white; ");
        mainWindow.getChildren().add(animatedText);
        mainWindow.getChildren().add(playGame);
        //inner shadow
        playGame.setOnMouseEntered(e -> {
            InnerShadow innerShadow = new InnerShadow();
            innerShadow.setOffsetX(0);
            innerShadow.setOffsetY(0);
            innerShadow.setColor(Color.BLACK);
            innerShadow.setRadius(20);        
            playGame.setEffect(innerShadow);
        });
        // Remove inner shadow effect from the "Missions" button on mouse exit
        playGame.setOnMouseExited(e -> {
            playGame.setEffect(null);
        });
    playGame.setOnAction(event -> {
    primaryStage.setScene(gameScene);});
    return mainWindow;
    }


    private Pane createGameWindow(Stage primaryStage) {
        //instance of SharedMemoryObject
SharedMemoryObject sharedMemoryObject = new SharedMemoryObject(this);
        Pane gameWindow = new Pane();
    
        Image image1 = new Image("output-onlinegiftools.gif");
        Image image2 = new Image("output-onlinegiftools.gif");
        Image image3 = new Image("output-onlinegiftools.gif");
        Image image4 = new Image("add7f09d39fd31e375de494a8fb287eb-removebg-preview.PNG");
        Image image5 = new Image("add7f09d39fd31e375de494a8fb287eb-removebg-preview.PNG");
        Image image6 = new Image("add7f09d39fd31e375de494a8fb287eb-removebg-preview.PNG");
    
        ImageView imageView1 = new ImageView(image1);
        ImageView imageView2 = new ImageView(image2);
        ImageView imageView3 = new ImageView(image3);
        ImageView imageView4 = new ImageView(image4);
        ImageView imageView5 = new ImageView(image5);
        ImageView imageView6 = new ImageView(image6);


        imageView1.setVisible(false);
        imageView2.setVisible(false);
        imageView3.setVisible(false);

        // x property for all Images
        double x = 450.0; // Replace with your desired x value
        imageView1.setLayoutX(x);
        imageView2.setLayoutX(x);
        imageView3.setLayoutX(x);
        imageView4.setLayoutX(x);
        imageView5.setLayoutX(x);
        imageView6.setLayoutX(x);
    
        // y property for all Images
        double y1 = 100.0; // y value for the first image
        double yDistance = 200.0; // distance between the images
        imageView1.setLayoutY(y1);
        imageView2.setLayoutY(y1 + yDistance);
        imageView3.setLayoutY(y1 + 2 * yDistance);
        imageView4.setLayoutY(y1);
        imageView5.setLayoutY(y1 + yDistance);
        imageView6.setLayoutY(y1 + 2 * yDistance);
        
    // y property for all progress bars
double yBar1 = 150.0; // y value for the first progress bar
double yBarDistance = 200.0; // distance between the progress bars
double xBar1 = 650.0; // x value for the first progress bar
double xBarDistance = 150.0; // distance between the progress bars

// Create a progress bar for each player
ProgressBar[] progressBars = new ProgressBar[4];
for (int i = 0; i < 3; i++) {
    progressBars[i] = new ProgressBar(0);
    progressBars[i].setLayoutY(yBar1 + i * yBarDistance); // Set the y position
    progressBars[i].setLayoutX(xBar1); // Set the x position
    progressBars[i].setPrefWidth(300); // Set the width
    progressBars[i].setPrefHeight(50); // Set the height
    gameWindow.getChildren().add(progressBars[i]);
}
// Update the progress bars based on the player positions in the SharedMemoryObject
for (int i = 0; i < 3; i++) {
    int playerPosition = sharedMemoryObject.positions[i];
    double progress = playerPosition / 100.0; // Assuming the maximum position is 100
    progressBars[i].setProgress(progress);
}

// Call this method whenever a player moves
updateProgressBars(sharedMemoryObject);

gameWindow.getChildren().addAll(imageView1, imageView2, imageView3);
gameWindow.getChildren().addAll(imageView4, imageView5, imageView6);
    
        return gameWindow;
    }


  // Method to update progress bars based on the positions of players
public void updateProgressBars(SharedMemoryObject sharedMemoryObject) {
    // Ensure the update is done on the UI thread
    Platform.runLater(() -> {
        for (int i = 0; i < progressBars.length; i++) {
            // Get the player position from SharedMemoryObject
            int playerPosition = sharedMemoryObject.positions[i];
            
            // Calculate progress as a fraction (assuming max position is 100)
            double progress = playerPosition / 100.0;
            
            // Update the progress bar
            progressBars[i].setProgress(progress);
        }
    });
}

    private Pane createGameOverWindow() {
        Pane gameOverWindow = new Pane();
        return gameOverWindow;
    }
    public static void tpools() {
        ExecutorService ex = Executors.newFixedThreadPool(3);
        /*ex.execute(new MyFirstThread("123456789",100));
        ex.execute(new MyFirstThread("ABCDEFGHIJKLMNOPQRSTUVWXYZ",100));
        ex.execute(new MyFirstThread("!@#$%^&*()+-_=",100));*/
        ex.shutdown();
    }
}