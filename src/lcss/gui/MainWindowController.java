/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package lcss.gui;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextAreaBuilder;
import static lcss.LCSS.lcss;

import lcss.load.Loader;
import lcss.load.ReadFromTXT;
import lcss.structure.FinalResult;
import lcss.structure.GeographicalSpots;
import lcss.structure.Structure;

/**
 *
 * @author Panos
 */
public class MainWindowController implements Initializable {

    /**
     * Static elements
     */
    private static String firstDataset = "";
    private static String secondDataset = "";
    private static ArrayList<GeographicalSpots> union = null;

    public static int locationX = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth()) / 2;
    public static int locationY = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight()) / 2;
    private final ArrayList<Menu> menus1 = new ArrayList<>();
    private final ArrayList<Menu> menus2 = new ArrayList<>();

    @FXML
    private Menu sel1;
    @FXML
    private Menu sel2;

    @FXML
    private Button cancel1;
    @FXML
    private Button cancel2;

    @FXML
    private ScrollPane scrollPane;
    private TextArea textArea;

    @FXML
    private Button procedure;
    @FXML
    private Button maps;

    private GoogleMaps gmaps = null;

    /**
     * Initialize the Data Members 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        gmaps = new GoogleMaps();

        cancel1.setDisable(true);
        cancel2.setDisable(true);

        fillMenus(menus1);

        sel1.setText("Dataset 1");

        for (int i = 0; i < menus1.size(); i++) {
            sel1.getItems().add(menus1.get(i));
        }

        fillMenus(menus2);
        sel2.setText("Dataset 2");

        for (int i = 0; i < menus2.size(); i++) {
            sel2.getItems().add(menus2.get(i));
        }

        textArea = TextAreaBuilder.create()
                .prefWidth(400)
                .prefRowCount(50)
                .prefHeight(400.0)
                .wrapText(true)
                .editable(false)
                .build();

        scrollPane.setContent(textArea);
        scrollPane.setFitToWidth(true);

        procedure.setDisable(true);
        maps.setDisable(true);
    }

    /**
     * Cancels the Datasets for a new call of Lcss
     * @param e 
     */
    @FXML
    public void actionCancel(ActionEvent e) {
        Button button = (Button) e.getSource();
        if (button.getId().equalsIgnoreCase("cancel1")) {
            firstDataset = "";
            sel1.setText("Dataset 1");
            sel1.setDisable(false);
            cancel1.setDisable(true);
            if (checkDatasets()) {
                procedure.setDisable(false);
                maps.setDisable(false);
            } else {
                procedure.setDisable(true);
                maps.setDisable(true);
            }
            Structure.setFirst(new ArrayList<GeographicalSpots>());
        } else {
            secondDataset = "";
            sel2.setText("Dataset 2");
            sel2.setDisable(false);
            cancel2.setDisable(true);
            if (checkDatasets()) {
                procedure.setDisable(false);
                maps.setDisable(false);
            } else {
                procedure.setDisable(true);
                maps.setDisable(true);
            }
            Structure.setSecond(new ArrayList<GeographicalSpots>());
        }
        textArea.setText("");
    }

    /**
     * This action calls Lcss and makes the Union of Latitude and Longitude spots 
     */
    @FXML
    public void actionProcedure() {

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                procedure.setDisable(true);
                maps.setDisable(true);
                cancel1.setDisable(true);
                cancel2.setDisable(true);

                FinalResult results = lcss(Structure.getFirst(), Structure.getSecond());
                MainWindowController.setUnion(results.getUnion());
                createUnionFile();

                procedure.setDisable(false);
                maps.setDisable(false);
                cancel1.setDisable(false);
                cancel2.setDisable(false);
                textArea.setText(results.toString());
                return 42;
            }
        };

        new Thread(task).start();

        textArea.setText("... Please Wait ... ");
    }

    /**
     * This action calls the Google Maps into an extra frame of JavaFx 
     */
    @FXML
    public void actionGoogleMaps() {

        Task<Integer> task = new Task<Integer>() {
            @Override
            protected Integer call() throws Exception {
                procedure.setDisable(true);
                maps.setDisable(true);
                cancel1.setDisable(true);
                cancel2.setDisable(true);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        gmaps.show();
                        procedure.setDisable(false);
                        maps.setDisable(false);
                        cancel1.setDisable(false);
                        cancel2.setDisable(false);
                    }
                });

                return 42;
            }
        };

        new Thread(task).start();

    }

    /**
     * This method creates the file , which contains the union of Latitude and Longitude spots 
     * The created file can be used into matlab.
     */
    public void createUnionFile() {
        String file1, file2;
        file1 = firstDataset.substring(0, firstDataset.indexOf('.'));
        file2 = secondDataset.substring(0, secondDataset.indexOf('.'));
        File file = new File("src/resources/unionDatasets/" + file1 + "-" + file2 + ".m");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                file.delete();
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        FileOutputStream fop = null;
        try {
            fop = new FileOutputStream(file);

            // get the content in bytes
            byte[] contentInBytes = makeTXT().getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Creates the String of union file
     * @return String
     */
    public static String makeTXT() {

        Structure.getFirst();
        Structure.getSecond();
        String str = "";
        String file1, file2;
        file1 = firstDataset.substring(0, firstDataset.indexOf('.'));
        file2 = secondDataset.substring(0, secondDataset.indexOf('.'));

        str = str + "lat1=[ ";
        for (int i = 0; i < Structure.getFirst().size(); i++) {
            str = str + Structure.getFirst().get(i).getLatitude() + "\n";
        }
        str = str + "] \n\n";

        str = str + "lon1=[ ";
        for (int i = 0; i < Structure.getFirst().size(); i++) {
            str = str + Structure.getFirst().get(i).getLongitude() + "\n";
        }
        str = str + "] \n\n";

        str = str + "lat2=[ ";
        for (int i = 0; i < Structure.getSecond().size(); i++) {
            str = str + Structure.getSecond().get(i).getLatitude() + "\n";
        }
        str = str + "] \n\n";

        str = str + "lon2=[ ";
        for (int i = 0; i < Structure.getSecond().size(); i++) {
            str = str + Structure.getSecond().get(i).getLongitude() + "\n";
        }
        str = str + "] \n\n";

        str = str + "lat3=[ ";
        for (int i = 0; i < union.size(); i++) {
            str += union.get(i).getLatitude() + "\n";
        }
        str = str + "] \n\n";

        str = str + "lon3=[ ";
        for (int i = 0; i < union.size(); i++) {
            str = str + union.get(i).getLongitude() + "\n";
        }
        str = str + "] \n\n";

        str = str + "zoom on\n"
                + "geoshow(lat1,lon1,'Color','red')\n"
                + "geoshow(lat2,lon2,'Color','blue')\n"
                + "geoshow(lat3,lon3,'Color','yellow')\n"
                + "legend('" + file1 + "','" + file2 + "','most common path')";

        return str;
    }

    /**
     * folder returns the sub folders from datasets
     *
     * @return Vector<Strings> names sub folders.
     */
    public Vector<String> subFoldersList() {
        Loader load = new Loader();
        File file = new File(load.getDatasetsPackage());
        String[] names = file.list();
        Vector<String> folders = new Vector<String>();
        for (String name : names) {
            if (new File(load.getDatasetsPackage() + name).isDirectory()) {
                folders.add(name);
            }
        }
        return folders;
    }

    /**
     * This method returns all files from a directory
     *
     * @param folder
     * @return Vector files names
     */
    public Vector<String> subFileList(String folder) {

        Vector<String> textFiles = new Vector<String>();
        File dir = new File(folder);
        for (File file : dir.listFiles()) {
            if (file.getName().endsWith((".txt"))) {
                textFiles.add(file.getName());
            }
        }
        return textFiles;

    }

    /**
     * Fills the menus with Sub menus and files.
     * @param menus 
     */
    public void fillMenus(ArrayList<Menu> menus) {
        Vector<String> folders = subFoldersList();

        if (folders != null && folders.size() >= 1) {
            Loader load = new Loader();
            for (final String folder : folders) {
                Menu subMenu_s1 = new Menu(folder);
                Vector<String> files = subFileList(load.getDatasetsPackage() + folder);
                for (String file : files) {
                    //Sub-Menu
                    MenuItem subMenuItem1 = new MenuItem(file);
                    subMenuItem1.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent e) {
                            final MenuItem mItem = (MenuItem) e.getSource();
                            if (mItem.getParentMenu().getParentMenu().getText().equalsIgnoreCase("Dataset 1")) {
                                firstDataset = mItem.getText();
                                sel1.setText(mItem.getText());
                                sel1.setDisable(true);
                                cancel1.setDisable(false);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Loader loader = new Loader();
                                        ReadFromTXT readFile = new ReadFromTXT();
                                        Structure.setFirst(readFile.read(loader.getDatasetsPackage() + mItem.getParentMenu().getText() + "/" + getFirstDataset()));
                                        System.out.println("Size First : " + Structure.getFirst().size());
                                        if (checkDatasets()) {
//                                            MyBrowser.createHtml(firstDataset, secondDataset);
                                        }
                                    }
                                });

                                if (checkDatasets()) {
                                    procedure.setDisable(false);
                                    maps.setDisable(false);
                                } else {
                                    procedure.setDisable(true);
                                    maps.setDisable(true);
                                }

                            } else {
                                secondDataset = mItem.getText();
                                sel2.setText(mItem.getText());
                                sel2.setDisable(true);
                                cancel2.setDisable(false);

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Loader loader = new Loader();
                                        ReadFromTXT readFile = new ReadFromTXT();
                                        Structure.setSecond(readFile.read(loader.getDatasetsPackage() + mItem.getParentMenu().getText() + "/" + getSecondDataset()));
                                        System.out.println("Size Second : " + Structure.getSecond().size());
                                        if (checkDatasets()) {
//                                            System.out.println("Mainw edwwwwwwwwwwwww 222");
//                                            MyBrowser.createHtml(firstDataset, secondDataset);
                                        }
                                    }
                                });

                                if (checkDatasets()) {
                                    procedure.setDisable(false);
                                    maps.setDisable(false);
                                } else {
                                    procedure.setDisable(true);
                                    maps.setDisable(true);
                                }
                            }
                        }
                    });
                    subMenu_s1.getItems()
                            .add(subMenuItem1);
                }
                menus.add(subMenu_s1);
            }

        } else {

            Menu subMenu_s1 = new Menu("Nothing");
            menus.add(subMenu_s1);
        }
    }
    /**
     *Checks if the user clicks the datasets
    */
    public static boolean checkDatasets() {
        return (!firstDataset.equalsIgnoreCase("") && !secondDataset.equalsIgnoreCase(""));
    }

    /**
     *Setters and Getters
     */
    
    public static String getFirstDataset() {
        return MainWindowController.firstDataset;
    }

    public static void setFirstDataset(String firstDataset) {
        MainWindowController.firstDataset = firstDataset;
    }

    public static String getSecondDataset() {
        return MainWindowController.secondDataset;
    }

    public static void setSecondDataset(String secondDataset) {
        MainWindowController.secondDataset = secondDataset;
    }

    public static ArrayList<GeographicalSpots> getUnion() {
        return union;
    }

    public static void setUnion(ArrayList<GeographicalSpots> union) {
        MainWindowController.union = union;
    }

}
