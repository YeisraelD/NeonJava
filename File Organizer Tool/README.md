# File Organizer Tool Walkthrough

so i want to make a program that can organize files in a directory by their extensions into grouped subfolders.


- [FileOrganizer.java](file:///c:/Users/yeisr/OneDrive/Documents/git/NeonJava/NeonJava/File%20Organizer%20Tool/FileOrganizer.java): The core logic.
- `v_test_dir/`: A test directory I created with several dummy files:
    - `file1.txt`, `file2.txt`
    - `image1.jpg`, `image2.png`
    - `document.pdf`
    - `file_without_extension`

## How to run it
if background execution is slow, run these commands in your console from the root of the `NeonJava` folder:

```bash
cd "File Organizer Tool"
javac FileOrganizer.java
java FileOrganizer v_test_dir
```

## Expected Results
After running the tool, the `v_test_dir` directory should be organized as follows:

- `v_test_dir/`
    - `txt/`
        - `file1.txt`
        - `file2.txt`
    - `jpg/`
        - `image1.jpg`
    - `png/`
        - `image2.png`
    - `pdf/`
        - `document.pdf`
    - `no_extension/`
        - `file_without_extension`

The console output should look like this:
```
Scanning directory: v_test_dir
Files found: 6
 - document.pdf
 - file1.txt
 - file2.txt
 - file_without_extension
 - image1.jpg
 - image2.png

Organizing files...
Moved: document.pdf -> pdf
Moved: file1.txt -> txt
Moved: file2.txt -> txt
Moved: file_without_extension -> no_extension
Moved: image1.jpg -> jpg
Moved: image2.png -> png
Done!
```

