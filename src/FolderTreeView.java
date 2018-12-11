import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.function.Function;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class FolderTreeView extends Application {

    private static final String ROOT_FOLDER = "C:\\Users\\Dell\\Desktop\\AI"; // TODO: change or make selectable

    @Override
    public void start(Stage primaryStage) throws IOException {

        // create root
        TreeItem<Path> treeItem = new TreeItem<Path>(Paths.get( ROOT_FOLDER));
        treeItem.setExpanded(true);

        // create tree structure
        createTree( treeItem);

        // sort tree structure by name
        treeItem.getChildren().sort( Comparator.comparing( new Function<TreeItem<Path>, String>() {
            @Override
            public String apply(TreeItem<Path> t) {
                return t.getValue().toString().toLowerCase();
            }
        }));

        // create components
        TreeView<Path> treeView = new TreeView<Path>(treeItem);
        StackPane root = new StackPane();
        root.getChildren().add(treeView);
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setTitle("Folder Tree View Example");
        primaryStage.show();

    }

    /**
     * Recursively create the tree
     * @param rootItem
     * @throws IOException
     */
    public static void createTree(TreeItem<Path> rootItem) throws IOException {

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(rootItem.getValue())) {

            for (Path path : directoryStream) {

                TreeItem<Path> newItem = new TreeItem<Path>(path);
                newItem.setExpanded(true);

                rootItem.getChildren().add(newItem);

                if (Files.isDirectory(path)) {
                    createTree(newItem);
                }
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}