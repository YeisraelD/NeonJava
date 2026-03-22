### How This Program Actually Works

So, you've got a messy folder full of random files, and you want them organized. Here’s the breakdown of how this little Java tool handles that

### 1. Scanning
First, the program goes to the folder you pointed it at. It literally asks the operating system: "Hey, give me a list of everything inside this directory." It ignores other folders and just focuses on the actual files. It stores all those filenames in a big list (`ArrayList`) so it doesn't lose track of anything.

### 2. Grouping
Once it has the list, it looks at each file one by one. It finds the last dot (`.`) in the filename to figure out the extension (like `.jpg` or `.pdf`). 
- If it sees a `.txt` file, it says: "Okay, you belong in the 'txt' pile."
- If a file doesn't have an extension at all, it doesn't panic—it just puts it in a special "no_extension" pile.
It uses a `HashMap` for this basically a digital set of bins labeled by extension.

### 3. Creating Folders
Now that it knows which extensions exist, it checks if folders for them already exist. If you have images but no `jpg` folder, it creates one right there on the spot. 

### 4. Moving Files
This is the final step. It picks up each file and physically moves it from the main folder into its new matching subfolder. It’s like it’s tidying up a messy room by putting everything into labeled boxes.

### Summary
That's pretty much it! It's just a loop that:
1. **Scans** everything.
2. **Decides** where it goes based on the extension.
3. **Makes** a folder if it needs to.
4. **Moves** the file in.


If you're curious about the actual Java code doing the heavy lifting

### The File Handling API
We use the **`java.io.File`** class to list the files initially, but for the actual heavy lifting (moving and creating directories), we switch to the more modern **`java.nio.file`** API (Path, Paths, and Files). It's faster and handles errors much better.

### Storage & Grouping
- **`ArrayList<String>`**: Used to store the simple list of filenames found during the scan. This is great for keeping things in order.
- **`HashMap<String, List<File>>`**: This is the heart of the sorting. The "Key" is the file extension (like `txt`), and the "Value" is a list of all files that have that extension. 
- **`computeIfAbsent`**: We use this cool Map method to say: "If this extension doesn't have a list yet, create one; otherwise, just add this file to the existing list."

### Extension Detection
The program finds the extension by looking for the `lastIndexOf('.')`. If it finds one, it takes everything after that dot and converts it to `.toLowerCase()` so that `FILE.JPG` and `image.jpg` both end up in the same place.

### The Move Operation
When moving a file, we use `Files.move()` with **`StandardCopyOption.REPLACE_EXISTING`**. This ensures that if a file with the same name already exists in the destination folder, it gets updated instead of causing the program to crash.
