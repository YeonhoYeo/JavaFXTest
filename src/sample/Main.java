package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    // 동적 컴포넌트를 담기 위해 맵사용
    private Map<String, Object> component = new HashMap();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

        // fxml 앵커패인 루트 설정
        AnchorPane acp = (AnchorPane) primaryStage.getScene().getRoot();

        // 탭패인 생성
        TabPane tp_main = new TabPane();

        // 앵커 설정
        acp.setBottomAnchor(tp_main, 0.0);
        acp.setLeftAnchor(tp_main, 0.0);
        acp.setTopAnchor(tp_main, 0.0);
        acp.setRightAnchor(tp_main, 0.0);

        acp.getChildren().add(tp_main);

        // 컴포넌트 이벤트 처리를 위한 alert 추가
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // 거래처 생성
        List<String>  lst_code = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            lst_code.add(String.valueOf(i));

        // 동적 탭생성
        lst_code.forEach(s -> {
            String str_name = s + "번 거래처";
            // 좌측 테이블뷰
            component.put(s+":TableView", new TableView<String>());
            // 컴포넌트 확인 예제
            if (component.get(s+":TableView") instanceof TableView){
                // 테이블 컬럼은 리스트에 담는다.
                List<TableColumn<String[], String>> comlumnList = new ArrayList<>();
                comlumnList.add(new TableColumn(str_name + " " + comlumnList.size() + " 번컬럼"));
                comlumnList.get(comlumnList.size()-1).setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue()[0]));
                comlumnList.add(new TableColumn(str_name + " " + comlumnList.size() + " 번컬럼"));
                comlumnList.get(comlumnList.size()-1).setCellValueFactory(cellDataFeatures -> new SimpleStringProperty(cellDataFeatures.getValue()[1]));
                // 테이블뷰에 컬럼 추가
                comlumnList.forEach(tableColumn -> {
                    tableColumn.setMinWidth(200);
                    ((TableView) component.get(s+":TableView")).getColumns().add(tableColumn);
                });
                component.put(s+":comlumnList", comlumnList);
            }

            // 우측 조회 폼
            component.put(s+":Button", new Button(str_name + " 버튼"));
            // 버튼 이벤트 설정
            ((Button) component.get(s+":Button")).setOnAction(actionEvent -> {
                component.forEach((s1, o) -> {
                    if(s.equals(s1.split(":")[0])){
                        ((TableView) component.get(s+":TableView")).getItems().add(s1.split(":"));

                    }
                });
                alert.setContentText(str_name+" 컴포넌트 추가");
                alert.show();
            });
            component.put(s+":TextField", new TextField(str_name + " 텍스트 필드"));
            component.put(s+":TextArea", new TextArea(str_name + " 텍스트 에어리어"));
            component.put(s+":ListView", new ListView<String>());
            ((ListView) component.get(s+":ListView")).getItems().add(str_name + " 리스트 데이터");
            // split pane 추가
            SplitPane sp = new SplitPane();
            sp.getItems().addAll((TableView) component.get(s+":TableView"),
                    new VBox((Button) component.get(s+":Button"),
                            (TextField) component.get(s+":TextField"),
                            (TextArea) component.get(s+":TextArea"),
                            (ListView) component.get(s+":ListView")));

            // tab 추가
            Tab tb = new Tab(str_name);
            tb.setContent(sp);
            tp_main.getTabs().add(tb);
        });

        // Map 에서 직접 접근하여 컴포넌트 제어
        ((Button) component.get("3:Button")).setText("직접제어");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
