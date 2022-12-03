package com.example.semester1;

import com.example.semester1.containers.RoomNavigationContainer;
import com.example.semester1.core.Classes.ActivityList;
import com.example.semester1.core.Classes.Inventory;
import com.example.semester1.core.Command;
import com.example.semester1.core.Game;
import com.example.semester1.events.GameEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class GameController {

    Game game;

    @FXML
    public void initialize() throws IOException {
        dayLabel.setOnMouseClicked(dayTester);

        game = Game.createInstance("Player name");

        // Related to activities:
        // Sets activitiesList to "observe" currentActivities, and display all of its items.
        activitiesList1.setItems(currentActivities1);
        activitiesList1.setFocusTraversable(false);
        activitiesList2.setItems(currentActivities2);
        activitiesList2.setFocusTraversable(false);
        this.updateActivities();
        this.updateInventory();

        this.updateDay();
        this.updatePower();
        this.updatePoints();


        this.roomNavigationContainer = new RoomNavigationContainer();
        this.roomOuterAnchorPane.getChildren().add(this.roomNavigationContainer);

        this.roomNavigationContainer.setRoom(game.getCurrentRoom());
        this.roomNavigationContainer.addEventHandler(GameEvent.CHANGE_ROOM, event -> {
            game.goRoom(event.getCommand());
            this.roomNavigationContainer.setRoom(game.getCurrentRoom());
        });

        this.roomNavigationContainer.addEventHandler(GameEvent.USE_APPLIANCE, event -> {
            String applianceId = event.getValue();

            // get command to pass to game.useAppliance method
            Command command = event.getCommand();

            if (game.useCommand(command)) {
                writeInConsoleArea("Task completed: " + game.getActivityManager().getAllActivities().getByAlias(game.getCurrentRoom().getAppliance(applianceId).getActivityId()).getDisplayName());

                // Updates everything on the GUI
                this.updateAll();
            } else {
                writeInConsoleArea("You can't do that");
            }
        });
        this.roomNavigationContainer.addEventHandler(GameEvent.PICKUP_ITEM, event -> {
            String itemId = event.getValue();

            // Get command to pass to game.pickupItem method
            Command command = event.getCommand();

            if (game.pickupItem(command)) {
                writeInConsoleArea("Picked up: " + game.getPlayer().getItem(itemId).getDisplayName());

                // Updates everything on the GUI
                this.updateAll();
            } else {
                writeInConsoleArea("You can't do that");
            }
        });
    }

    // Activities
    ObservableList<String> currentActivities1 = FXCollections.observableArrayList();
    ObservableList<String> currentActivities2 = FXCollections.observableArrayList();


    @FXML
    private AnchorPane roomOuterAnchorPane;

    private RoomNavigationContainer roomNavigationContainer;

    @FXML
    ListView activitiesList1 = new ListView();

    @FXML
    ListView activitiesList2 = new ListView();

    @FXML
    Label activities;

    //Stats declare
    @FXML
    Label dayLabel;

    @FXML
    Label pointsLabel;

    @FXML
    Label powerLabel;

    //Stats methods

    public void updateAll() {
        this.updatePower();
        this.updateDay();
        this.updatePoints();
        this.updateActivities();
        this.roomNavigationContainer.updateView();
        this.updateInventory();
    }

    public void updatePower() {
        powerLabel.setText("Current Power: " + game.getPower());
    }

    public void updateDay() {
        dayLabel.setText("Day Number: " + game.getDay());
    }

    public void updatePoints() {
        pointsLabel.setText("Current points: " + game.getPoints());
    }

    // Day overlay


    // Console area
    @FXML
    Label consoleArea;

    static int i;

    EventHandler<MouseEvent> dayTester = new EventHandler<>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            game.sleepCommand();
            updateAll();
        }
    };


    public void writeInConsoleArea(String text) {
        String existingText = consoleArea.getText();

        String[] existingTextArray = new String[5];
        String[] existingTextArraySplit = existingText.split("\n");

        for (int i = 0; (i < existingTextArraySplit.length); i++) {
            if (!existingTextArraySplit[i].isEmpty()) {
                existingTextArray[i] = existingTextArraySplit[i];
            }
            if (i == 4) {
                break;
            }
        }

        int _length = 0;
        for (String string : existingTextArray) {
            if (string != null) {
                _length++;
            }
        }

        if (_length < 5) {
            for (int i = 0; i < existingTextArray.length; i++) {
                if (existingTextArray[i] == null) {
                    existingTextArray[i] = text + "\n";
                    break;
                }
            }
        } else {
            for (int i = 0; i < (existingTextArray.length); i++) {
                if (i == 4) {
                    existingTextArray[i] = text + "\n";
                } else {
                    existingTextArray[i] = existingTextArray[i + 1];
                }
            }
        }

        StringBuilder output = new StringBuilder();

        for (String string : existingTextArray) {
            if (string != null) {
                output.append(string).append("\n");
            } else {
                output.append(" \n");
            }
        }

        consoleArea.setText(output.toString());
    }

    // Method to show current activities: Should be updated everytime a new day begins OR an activity has been completed.
    public void updateActivities() {
        activitiesList1.getItems().clear();
        activitiesList2.getItems().clear();

        // Daily:
        ActivityList missingDailyActivities = game.getActivityManager().getMissingDailyActivities();
        for (int i = 0; i < missingDailyActivities.size(); i++) {
            currentActivities1.add(missingDailyActivities.get(i).getDisplayName());
        }

        // Extra:
        ActivityList missingExtraActivities = game.getActivityManager().getMissingNoneDailyActivities();
        for (int i = 0; i < missingExtraActivities.size(); i++) {
            currentActivities2.add(missingExtraActivities.get(i).getDisplayName());
        }
    }

    // Inventory
    @FXML
    HBox inventoryHBox;

    public void updateInventory() {
        // Deletes all items in the visual inventory
        inventoryHBox.getChildren().clear();

        // Gets all items in the inventory
        Inventory inventory = game.getPlayer().getInventory();

        // Try-catch statement, for loading a file
        try {
            for (int i = 0; i < inventory.size(); i++) {
                // Gets the image and loads it
                Image image = new Image(ResourceLoader.loadAsInputStream("assets/game/items/" + inventory.get(i).getId() + ".png"));

                // Creates a imageView and a stackPane
                ImageView imageView = new ImageView(image);
                StackPane stackPane = new StackPane();

                // Setting up the imageView
                imageView.setFitWidth(50);
                imageView.setFitHeight(50);

                // Setting up the stackPane
                stackPane.setPrefSize(50, 50);
                stackPane.setMaxHeight(50);
                stackPane.setMaxWidth(50);
                stackPane.setAlignment(Pos.TOP_LEFT);
                stackPane.setStyle("-fx-border-color:#11111199; -fx-border-width: 2 2 2 2; -fx-border-style: solid; -fx-border-insets: 0 0 0 10");

                // Adds the imageView to the stackPane
                stackPane.getChildren().add(imageView);

                // Adds the stackPane to the visual inventory
                inventoryHBox.getChildren().add(stackPane);
            }
        } catch (Exception e) {
            // Message if an exception happens in the try area
            System.out.println(e.getMessage() + " | " + e.getClass());
        }
    }
}