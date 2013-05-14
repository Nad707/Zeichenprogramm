/*
 * Created on 19.06.2006
 */
package de.hska.iwii.gui.drawing;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.RotateEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Erzeugt das Hauptfenster der Anwendung.
 * @author Holger Vogelsang
 */
public class MainWindow extends Application {
    
	private ToggleButton selectButton;
    private ToggleButton circleButton;
    private ToggleButton rectButton;
    private ToggleButton lineButton;
    
    // Tasten, die nur im Falle einer Selektion aktiv sind
    private ButtonBase[] selectionEnabledButtons = new ButtonBase[ 4 ];
    
    // Menueeintraege, die nur im Falle einer Selektion aktiv sind
    private MenuItem[] selectionEnabledMenus = new MenuItem[ 6 ];
    
    // Menueeintraege, die nur im Falle eines nicht leeren Clipboards aktiv sind
    private MenuItem[] clipboardInUseEnabledMenus = new MenuItem[ 1 ];
    
//    // Tasten, die nur im Falle eines veraenderten Modells aktiv sind
//    private ButtonBase[] changedModelEnabledButtons = new ButtonBase[ 1 ];
//    
//    // Menueeintraege, die nur im Falle eines veraenderten Modells aktiv sind
//    private MenuItem[] changedModelEnabledMenus = new MenuItem[ 1 ];
//    
    // Menueeintrtrag zur Gruppierung
    private MenuItem groupMenu;
    private MenuItem selectionGroupEnabledMenu;
    
    // Sortierung
//    private MenuItem sortMenu;

    private ToggleGroup typeGroup;
    private ScrollPane scrollPane;
    
    private Scene scene;
    private transient static Pane mainPanel;

    // Verwaltung des einzigen Listeners
    private static transient DrawingListener listener;

    // Startposition der Maus beim Verschieben
    private Point2D  mouseStartMove;
    
    // Modus: Figur erzeugen oder verschieben
    private boolean interactiveCreation = false;
    
    private ToolBar bar;
    private MenuBar menuBar;
    
    private ResourceBundle bundle;
    
	/**
	 * Anzeigefenster erzeugen.
	 */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Painter");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
                if (!checkForUnsafedModel()) {
                	event.consume();
                }
			}
        });
        createUI(primaryStage);
    }
    
    /**
     * Zugriff auf die Ressource-Datei. Ein Mnemonic wird
     * dabei ueberlesen. Bastelloesung: Das Mnemonic muss
     * '&' sein, ein escapen ist nicht moeglich und
     * es wird nur der erste Treffer untersucht.
     * @param key Schluessel, dessen Wert gesucht wird.
     * @return Wert zum Schluessel.
     */
    private String getResource(String key) {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle("i18n/lang");
        }
        String text = bundle.getString(key);
        int index = text.indexOf('&');
        if (index >= 0) {
            return text.substring(0, index) + text.substring(index + 1);
        }
        return text;
    }
    
    /**
     * Bau der GUI.
     * @param primaryStage Hauptfenster der Anwendung.
     */
    private void createUI(Stage primaryStage) {
        bar = new ToolBar();

//        changedModelEnabledButtons[ 0 ] = createButton(bar, "Save.Image", "Save.Tooltip");
//        changedModelEnabledButtons[ 0 ].setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//	            saveModel();
//	            checkButtonsAndMenus();
//			}
//        });
//        
//        bar.getItems().add(new Separator());
//        
        // Figur in die oberste Ebene verschieben
        selectionEnabledButtons[ 0 ] = createButton(bar, "Top.Image", "Top.Tooltip");
        selectionEnabledButtons[ 0 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresToTop();
		            checkButtonsAndMenus();
				}
			}
        });
        
        // Figur eine Ebene noch oben verschieben
        selectionEnabledButtons[ 1 ] = createButton(bar, "Up.Image", "Up.Tooltip");
        selectionEnabledButtons[ 1 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresUp();
		            checkButtonsAndMenus();
				}
			}
        });
        
        // Figur eine Ebene noch unten verschieben
        selectionEnabledButtons[ 2 ] = createButton(bar, "Down.Image", "Down.Tooltip");
        selectionEnabledButtons[ 2 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresDown();
		            checkButtonsAndMenus();
				}
			}
        });
        
        // Figur in die unterste Ebene verschieben
        selectionEnabledButtons[ 3 ] = createButton(bar, "Bottom.Image", "Bottom.Tooltip");
        selectionEnabledButtons[ 3 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresToBottom();
		            checkButtonsAndMenus();
				}
			}
        });

        bar.getItems().add(new Separator());

        // Figur auswaehlen
        selectButton = new ToggleButton("", new ImageView(new Image("Images/" + getResource("Select.Image"))));
        selectButton.setTooltip(new Tooltip(getResource("Select.Tooltip")));
        
        // Kreis erstellen
        circleButton = new ToggleButton("", new ImageView(new Image("Images/" + getResource("CreateCircle.Image"))));
        circleButton.setTooltip(new Tooltip(getResource("CreateCircle.Tooltip")));
        circleButton.setSelected(true);
        
        // Rechteck erstellen
        rectButton = new ToggleButton("", new ImageView(new Image("Images/" + getResource("CreateRectangle.Image"))));
        rectButton.setTooltip(new Tooltip(getResource("CreateRectangle.Tooltip")));

        // Linie erstellen
        lineButton = new ToggleButton("", new ImageView(new Image("Images/" + getResource("CreateLine.Image"))));
        lineButton.setTooltip(new Tooltip(getResource("CreateLine.Tooltip")));
        
        typeGroup = new ToggleGroup();
        circleButton.setToggleGroup(typeGroup);
        rectButton.setToggleGroup(typeGroup);
        lineButton.setToggleGroup(typeGroup);
        selectButton.setToggleGroup(typeGroup);
        
        bar.getItems().add(selectButton);
        bar.getItems().add(circleButton);
        bar.getItems().add(rectButton);
        bar.getItems().add(lineButton);
        
        final VBox pane = new VBox();
    	pane.fillWidthProperty().set(true);

        // Menue bauen (Datei)
        Menu fileMenu = new Menu(getResource("File.Text"));
//        MenuItem item = createMenuItem(fileMenu, "Open.Text", null, "Open.Acc");
//        item.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//	            if (checkForUnsafedModel()) {
//	            	loadModel();
//	            }
//	            checkButtonsAndMenus();
//			}
//        });
        
//        changedModelEnabledMenus[ 0 ] = createMenuItem(fileMenu, "Save.Text", "Save.Image", "Save.Acc");
//        changedModelEnabledMenus[ 0 ].setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//	            saveModel();
//	            checkButtonsAndMenus();
//			}
//        });
//        
//        fileMenu.getItems().add(new SeparatorMenuItem());
        MenuItem item = createMenuItem(fileMenu, "Exit.Text", null, "Exit.Acc");
        item.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
	            if (checkForUnsafedModel()) {
	            	Platform.exit();
	            }
	            checkButtonsAndMenus();
			}
        });

        // Menue bauen (Bearbeiten)
        Menu editMenu = new Menu(getResource("Edit.Text"));
        selectionEnabledMenus[ 0 ] = createMenuItem(editMenu, "Copy.Text", "Copy.Image", "Copy.Acc");
        selectionEnabledMenus[ 0 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.copyFigures();
		            checkButtonsAndMenus();
				}
			}
        });
        
        
        clipboardInUseEnabledMenus[ 0 ] = createMenuItem(editMenu, "Paste.Text", "Paste.Image", "Paste.Acc");
        clipboardInUseEnabledMenus[ 0 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.pasteFigures();
		            checkButtonsAndMenus();
				}
			}
        });
        
        selectionEnabledMenus[ 1 ] = createMenuItem(editMenu, "Delete.Text", "Delete.Image", "Delete.Acc");
        selectionEnabledMenus[ 1 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
	            listener.deleteFigures();
	            checkButtonsAndMenus();
			}
        });

        // Menue bauen (Figur)
        Menu figureMenu = new Menu(getResource("Figure.Text"));
        selectionEnabledMenus[ 2 ] = createMenuItem(figureMenu, "Top.Text", "Top.Image", "Top.Acc");
        selectionEnabledMenus[ 2 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresToTop();
		            checkButtonsAndMenus();
				}
			}
        });
        
        selectionEnabledMenus[ 3 ] = createMenuItem(figureMenu, "Up.Text", "Up.Image", "Up.Acc");
        selectionEnabledMenus[ 3 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresUp();
		            checkButtonsAndMenus();
				}
			}
        });
        
        selectionEnabledMenus[ 4 ] = createMenuItem(figureMenu, "Down.Text", "Down.Image", "Down.Acc");
        selectionEnabledMenus[ 4 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresDown();
		            checkButtonsAndMenus();
				}
			}
        });
        
        selectionEnabledMenus[ 5 ] = createMenuItem(figureMenu, "Bottom.Text", "Bottom.Image", "Bottom.Acc");
        selectionEnabledMenus[ 5 ].setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.moveSelectedFiguresToBottom();
		            checkButtonsAndMenus();
				}
			}
        });
        
//        sortMenu = createMenuItem(figureMenu, "Sort.Text", "Sort.Image", "Sort.Acc");
//        sortMenu.setOnAction(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//	            listener.sortFigures();
//	            checkButtonsAndMenus();
//			}
//        }); 
//        
        figureMenu.getItems().add(new SeparatorMenuItem());
        
        // Figuren gruppieren
        groupMenu = createMenuItem(figureMenu, "Group.Text", "Group.Image", "Group.Acc");
        groupMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.groupFigures();
		            checkButtonsAndMenus();
				}
			}
        });
        
        // Gruppe aufloesen
        selectionGroupEnabledMenu = createMenuItem(figureMenu, "Ungroup.Text", "Ungroup.Image", "Ungroup.Acc");
        selectionGroupEnabledMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listener != null) {
		            listener.ungroupFigures();
		            checkButtonsAndMenus();
				}
			}
        });
        
        
        menuBar = new MenuBar();
        
        menuBar.getMenus().addAll(fileMenu, editMenu, figureMenu);
        
        // Zeichenflaeche in ScrollPane packen
        scrollPane = new ScrollPane();
        
		VBox.setVgrow(scrollPane, Priority.ALWAYS);
        scene = new Scene(pane, 800, 500);
        
        // Dummy erzeugen
        if (mainPanel == null) {
        	mainPanel = new Pane();
        	mainPanel.setPrefSize(1000, 800);
        }
        scrollPane.setContent(mainPanel);

        primaryStage.setScene(scene);
        primaryStage.show();

        pane.getChildren().addAll(menuBar, bar, scrollPane);
        
        mainPanel.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mousePressed(event);
                event.consume();
			}
        });
        
        mainPanel.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseDragged(event);
                event.consume();
			}
        });
        
        mainPanel.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseReleased(event);
                event.consume();
			}
        });
        
        mainPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseClicked(event);
                event.consume();
			}
        });
        
        mainPanel.setOnRotate(new EventHandler<RotateEvent>() {
            @Override public void handle(RotateEvent event) {
            	rotate(event);
                event.consume();
            }
        });
        
        mainPanel.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override public void handle(ScrollEvent event) {
            	translate(event);
                event.consume();
            }
        });
        
        mainPanel.setOnZoom(new EventHandler<ZoomEvent>() {
            @Override public void handle(ZoomEvent event) {
            	zoom(event);
                event.consume();
            }
        });
        
       checkButtonsAndMenus();
    }

    /**
     * Erzeugt einen Menueeintrag und registriert die Hauptanwendung als Listener.
     * @param menu          Menue, zu dem der Eintrag hinzugefuegt werden soll.
     * @param label         Schluessel aus der Resource-Datei als Bezeichnung fuer den Eintrag (darf null sein).
     * @param iconName      Schluessel aus der Resource-Datei fuer den Namen des zu verwendenen Bildes aus dem Ordner "images".
     *                      Der Name darf <code>null</code> sein, wenn kein Bild
     *                      verwendet werden soll.
     * @param keyStroke     Tastaturkuerzel wie z.B. "control c"
     * @return Erzeugter Menueeintrag.
     */
    private MenuItem createMenuItem(Menu menu, String label, String iconName, String keyStroke) {
    	if (label != null) {
    		label = getResource(label);
    	}
        MenuItem item = new MenuItem(label);
        item.setMnemonicParsing(true);
        if (iconName != null) {
            item.setGraphic(new ImageView(new Image("Images/" + getResource(iconName))));
        }
        if (keyStroke != null) {
        	item.setAccelerator(KeyCombination.keyCombination(getResource(keyStroke)));
        }
        menu.getItems().add(item);
        return item;
    }

    /**
     * Erzeugt einen neuen Button ohne Text.
     * @param bar           Toolbar-Leiste, zu der der Button hinzugefuegt wird.
     * @param iconName      Schluessel aus der Resource-Datei fuer den Namen des zu verwendenen Bildes aus dem Ordner "images".
     * @param toolTipText   Schluessel fuer den ToolTip-Text des Buttons.
     * @return              Erzeugter Button
     */
    private Button createButton(ToolBar bar, String iconName, String toolTipText) {
        Button button = new Button();
        if (iconName != null) {
            button.setGraphic(new ImageView(new Image("Images/" + getResource(iconName))));
        }
        button.setTooltip(new Tooltip(getResource(toolTipText)));
        bar.getItems().add(button);
        return button;
    }

    
    /**
     * Model auf Nachfrage sichern, wenn es seit der letzten Sicherung
     * veraendert wurde.
     * @return <code>true</code>, falls die Aktion durchgefuehrt werden soll.
     */
    private boolean checkForUnsafedModel() {
    	boolean performAction = true;
//        if (listener.isModelChanged()) {
//            String[] options = {getResource("SaveQuestion.YesOption"),
//            					getResource("SaveQuestion.NoOption"),
//                                getResource("FileExistsQuestion.CancelOption")};
//            switch (JOptionPane.showOptionDialog(this, getResource("SaveQuestion.Text"),
//                                                 getResource("SaveQuestion.Title"),
//                                             JOptionPane.YES_NO_CANCEL_OPTION,
//                                             JOptionPane.QUESTION_MESSAGE,
//                                             null, options, options[ 0 ])) {
//            case 0:
//                saveModel();
//                break;
//            case 1:
//            	break;
//           default:
//            	performAction = false;
//            }
//        }
        return performAction;
    }

//    /**
//     * Versucht, ein neues Modell zu laden.
//     */
//    private void loadModel() {
//        FileChooser chooser = new FileChooser();
//        chooser.setInitialDirectory(new File(currentPath));
//        File file = chooser.showOpenDialog(primaryStage);
//        if (file != null) {
//            currentPath = file.getAbsolutePath();
//            listener.loadModel(currentPath);
//        }
//    }
//
//    /**
//     * Versucht, das aktuelle Modell zu speichern.
//     */
//    private void saveModel() {
//        FileChooser chooser = new FileChooser();
//        chooser.setInitialDirectory(new File(currentPath));
//        File file = chooser.showSaveDialog(primaryStage);
//        if (file != null) {
//            currentPath = file.getAbsolutePath();
//            // Existiert die Datei schon?
////            if (file.exists()) {
////            	String[] options = {getResource("FileExistsQuestion.YesOption"),
////            			getResource("FileExistsQuestion.NoOption")};
////            	if (JOptionPane.showOptionDialog(this, getResource("FileExistsQuestion.Text"),
////            			getResource("FileExistsQuestion.Title"),
////            			JOptionPane.YES_NO_OPTION,
////            			JOptionPane.QUESTION_MESSAGE,
////            			null, options, options[ 0 ]) == 1) {
////            		return;
////            }
//            listener.saveModel(currentPath);
//            // Pfad vom Dateinamen abtrennen
//            int lastSep = currentPath.lastIndexOf(System.getProperty("file.separator"));
//            if (lastSep >= 0) {
//            	currentPath = currentPath.substring(0, lastSep);
//            }
//        }
//    }

    /**
     * Tasten und Menueeintraege sperren und entsperren.
     * In Abhaengigkeit der Selektion in der Anwendung werden Tasten 
     * und Menueeintraege nur dann entsperrt, wenn sie auch sinnvoll bedienbar sind.
     *
     */
    private void checkButtonsAndMenus() {
        for (ButtonBase button: selectionEnabledButtons) {
            button.setDisable(listener != null ? listener.getSelectedFiguresCount() == 0 : true);
        }
        for (MenuItem item: selectionEnabledMenus) {
            item.setDisable(listener != null ? listener.getSelectedFiguresCount() == 0 : true);
        }
        for (MenuItem item: clipboardInUseEnabledMenus) {
            item.setDisable(listener != null ? listener.getFiguresInClipboardCount() == 0 : true);
        }
//        for (ButtonBase button: changedModelEnabledButtons) {
//            button.setDisable(listener != null ? !listener.isModelChanged() : true);
//        }
//        for (MenuItem item: changedModelEnabledMenus) {
//            item.setDisable(listener != null ? !listener.isModelChanged() : true);
//        }
        groupMenu.setDisable(listener != null ? listener.getSelectedFiguresCount() <= 1 : true);
        selectionGroupEnabledMenu.setDisable(listener != null ? !listener.isGroupSelected() : true);
    }

    /**
     * Mausklick zur Selektion: Listener informieren.
     * @param event Aktuelles Mausereignis.
     */
    public void mouseClicked(MouseEvent event) {
        if (selectButton.isSelected() && listener != null) {
            listener.selectFigure((Node) event.getTarget(), event.getX(), event.getY(), event.isShiftDown());
        }
        checkButtonsAndMenus();
    }

    /**
     * Maustaste wurde gedrueckt und ist noch gedrueckt.
     * @param event Aktuelles Mausereignis.
     */
    public void mousePressed(MouseEvent event) {
		if (listener != null) {
	        String actionType = "";
	        mouseStartMove = null;
	        if (circleButton.isSelected()) {
	            actionType = "ellipse";
	        }
	        else if (rectButton.isSelected()) {
	            actionType = "rectangle";
	        }
	        else if (lineButton.isSelected()) {
	            actionType = "line";
	        }
	        
	        if (selectButton.isSelected()) {
	            mouseStartMove = new Point2D(event.getX(), event.getY());
	            interactiveCreation = false;
	        }
	        else {
	            interactiveCreation = true;
				listener.startCreateFigure(actionType, event.getX(), event.getY());
	        }
	        checkButtonsAndMenus();
		}
    }

    /**
     * Maustaste war gedrueckt und wurde wieder losgelassen.
     * @param event Aktuelles Mausereignis.
     */
    public void mouseReleased(MouseEvent event) {
		if (listener != null) {
	    	if (interactiveCreation) {
				listener.endCreateFigure(event.getX(), event.getY());
	    	}
	    	else {
				listener.endMoveFigure((Node) event.getTarget(), event.getX(), event.getY());
	    	}
	    	mouseStartMove = null;
	        checkButtonsAndMenus();
		}
    }

    /**
     * Dragging mit der Maus: Listener informieren.
     * @param event Aktuelles Mausereignis.
     */
    public void mouseDragged(MouseEvent event) {
		if (listener != null) {
	        // Erste Mausbewegung beim Verschieben -> Start einleiten
	        if (mouseStartMove != null) {
	            listener.startMoveFigure((Node) event.getTarget(), event.getX(), event.getY());
	            mouseStartMove = null;
	        }
	        if (interactiveCreation) {
	            listener.workCreateFigure(event.getX(), event.getY());
	        }
	        else {
	            listener.workMoveFigure((Node) event.getTarget(), event.getX(), event.getY());
	        }
	        checkButtonsAndMenus();
		}
    }

    /**
     * Rotieren einer Figur mittels Geste: Listener informieren.
     * @param event Aktuelles Gestenereignis.
     */
	private void rotate(RotateEvent event) {
		if (listener != null) {
			listener.rotate((Node) event.getTarget(), event.getAngle());
		}
	}

    /**
     * Zoomen einer Figur mittels Geste: Listener informieren.
     * @param event Aktuelles Gestenereignis.
     */
	private void zoom(ZoomEvent event) {
		if (listener != null) {
			listener.zoom((Node) event.getTarget(), event.getZoomFactor());
		}
	}

    /**
     * Verschieben einer Figur mittels Geste: Listener informieren.
     * @param event Aktuelles Gestenereignis.
     */
	private void translate(ScrollEvent event) {
		if (listener != null) {
			listener.translate((Node) event.getTarget(), event.getDeltaX(), event.getDeltaY());
		}
	}

    /**
     * Einen Listener registrieren.
     * In dieser Implementierung wird nur noch ein Listener unterstuetzt.
     * @param listener Neuer Listener, der im Falle von Ereignissen
     *                 informiert werden soll.
     */
    public static void setDrawingListener(DrawingListener newListener) {
        listener = newListener;
    }
    
    /**
     * Die Zeichenflaeche uebergeben.
     * @param newMainPanel Zeichenflaeche.
     */
	public static void setMainPanel(Pane newMainPanel) {
		mainPanel = newMainPanel;
	}
}
