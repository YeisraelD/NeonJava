# Notepad - JavaFX Text Editor Report

This is the breakdown of my Notepad application. No bloated libraries or unnecessary overhead—just clean JavaFX paired with native Java File I/O, wrapped in a multi-tabbed UI.

Here is how I structured the architecture and code to get the job done.

---

## The Architecture

The project is split into a clean Model-View pattern:
1. **NotePad.java (The Engine):** Handles raw system File I/O operations (BufferedReader/BufferedWriter) and encapsulates file metadata into a custom FileData carrier class.
2. **NotePadGUI.java (The Control Deck):** Builds the JavaFX stage, handles layout (BorderPane, TabPane), listens to user caret movements, and translates them to status updates.

---

## Core Features Implemented

### 1. Multi-Tabbed File I/O with State Tracking
Most basic student notepad apps crash or lose track of files when you have multiple tabs open. To solve this, I used JavaFX UserData binding.
* When I open a file or use "Save As", the actual `java.io.File` object is bound directly to the active Tab using `tab.setUserData(file)`.
* When I trigger a normal Save (Ctrl+S), the app checks the tab's UserData. If a file reference exists, it overwrites it instantly without nagging with popup dialogs. If it is a brand-new file, it automatically upgrades the operation to a "Save As" flow.

### 2. Keyboard Accelerators (Power-User Shortcuts)
Mouse clicking is slow. I wired global keyboard listeners to the menu items using JavaFX `KeyCombination`:
* `Ctrl+N` -> Instantly spawns a new tab.
* `Ctrl+O` -> Launches file selector to load files.
* `Ctrl+S` -> Performs silent, fast saves.
* `Ctrl+Shift+S` -> Triggers "Save As" to duplicate/rename files.

### 3. Real-Time Caret Tracking & Status Bar
The status bar at the bottom gives real-time stats by tracking the text caret:
* **Caret Position:** Substrings the text up to the current cursor position, splits it by newline (`\n`), and calculates the exact Ln X, Col Y coordinates dynamically.
* **Character Count:** Counts character lengths on the fly.
* **Metadata Labels:** Display placeholders for text formats (Plain text), CRLF, and UTF-8 encoding.

---

## Key Code Highlight

Here is how the silent overwrite save logic works by reading the state from the active tab's metadata:

```java
public void handleNormalSaving(Stage stage) {
    Tab currentTab = tabPane.getSelectionModel().getSelectedItem();
    TextArea textArea = getCurrentTextArea();

    if (currentTab != null && textArea != null) {
        // Retrieve the stored File object bound to this tab
        File savedFile = (File) currentTab.getUserData();
        
        if (savedFile != null) {
            // Overwrite directly
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(savedFile))) {
                writer.write(textArea.getText());
            } catch (IOException e) {
                alert("Error saving file: " + e.getMessage());
            }
        } else {
            // No file exists yet -> fallback to "Save As" dialog
            handleSaving(stage);
        }
    }
}
```

---

## Verdict
The app compiles cleanly, handles multiple open documents without crossing streams, and supports fast keyboard-based navigation.
