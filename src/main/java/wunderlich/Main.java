package wunderlich;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import wunderlich.model.Item;
import wunderlich.model.Stash;

import java.io.IOException;

public class Main extends Application {

    public static void main(String [] args) throws IOException {
        Application.launch();
    }


    TableView<Item> items = new TableView<>();
    TextField tabIndex = new TextField("");
    TextField poesessid = new TextField("");
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane parent = new BorderPane();
        parent.setCenter(items);
        stage.setScene(new Scene(parent,600,400));



        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(60000),
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {

                    }
                }
//                actionEvent -> loadStash()
        ));
        timeline.setCycleCount(Animation.INDEFINITE);

        items.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Item, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(itemStringCellDataFeatures -> new SimpleStringProperty(itemStringCellDataFeatures.getValue().name));
        items.getColumns().add(nameCol);
        {
            TableColumn<Item, String> ilevelCol = new TableColumn<>("Item Level");
            ilevelCol.setCellValueFactory(itemStringCellDataFeatures -> new SimpleStringProperty("" + itemStringCellDataFeatures.getValue().ilvl));
            items.getColumns().add(ilevelCol);
        }
        {
            TableColumn<Item, String> ilevelCol = new TableColumn<>("typ");
            ilevelCol.setCellValueFactory(itemStringCellDataFeatures -> new SimpleStringProperty(itemStringCellDataFeatures.getValue().typeLine));
            items.getColumns().add(ilevelCol);
        }
        {
            TableColumn<Item, String> stackSizeCol = new TableColumn<>("stackSize");
            stackSizeCol.setCellValueFactory(itemStringCellDataFeatures -> {
                Item item = itemStringCellDataFeatures.getValue();
                return new SimpleStringProperty(""+item.getStackSize());
                    }
            );
            items.getColumns().add(stackSizeCol);
        }


        {
            HBox sum = new HBox(3);
            sum.setPadding(new Insets(3));
            sum.setAlignment(Pos.CENTER_LEFT);
            sum.getChildren().add(new Label("Punkte:"));
            TextField summField = new TextField();
            items.getItems().addListener((ListChangeListener<Item>) change -> {
                long sum1 = 0;
                for (Item item : items.getItems()) {
                    if (item.frameType == 3 && !item.identified) {
                        sum1++;
                    }
                    if (item.frameType == 5 && ("Divine Orb".equals(item.typeLine) || "Exalted Orb".equals(item.typeLine))) {
                        sum1+= item.getStackSize() * 5;
                    }
                }
                summField.setText("" + sum1);
            });

            sum.getChildren().add(summField);
            parent.setBottom(sum);
        }

        {
            HBox control = new HBox(3);
            control.setPadding(new Insets(3));
            control.setAlignment(Pos.CENTER_LEFT);
            control.getChildren().add(new Label("TabIndex:"));
            control.getChildren().add(tabIndex);
            control.getChildren().add(new Label("POESESSID:"));
            control.getChildren().add(poesessid);
            Button start = new Button("start");
            start.setOnAction(actionEvent -> {
                timeline.play();
                loadStash();
            });
            start.disableProperty().bind(tabIndex.textProperty().isEmpty().or(poesessid.textProperty().isEmpty()).or(timeline.statusProperty().isEqualTo(Animation.Status.RUNNING)));
            control.getChildren().add(start);
            ProgressBar progressBar = new ProgressBar();
            progressBar.progressProperty().bind(Bindings.createDoubleBinding(() -> {
                if (timeline.getStatus().equals(Animation.Status.RUNNING)) {
                    return -1.0;
                }
                return 0.0;
            }, timeline.statusProperty()));
            control.getChildren().add(progressBar);
            parent.setTop(control);
        }



        stage.show();
    }

    private void loadStash() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://www.pathofexile.com/character-window/get-stash-items?accountName=Gandalfuss&tabIndex="+tabIndex.getText()+"&league=Perandus&tabs=1");
        httpGet.setHeader("Cookie", "POESESSID="+poesessid.getText()+"; session_start=0;");

        CloseableHttpResponse response1 = null;
        try {
            response1 = httpclient.execute(httpGet);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String content = EntityUtils.toString(entity1);
                System.out.println(content);

                ObjectMapper objectMapper = new ObjectMapper();
                Stash stash = objectMapper.readValue(content, Stash.class);

                EntityUtils.consume(entity1);
                items.getItems().clear();
                for (Item item: stash.items){
                    items.getItems().add(item);
                }
            } finally {
                response1.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
