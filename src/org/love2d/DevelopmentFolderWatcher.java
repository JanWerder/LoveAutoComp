package org.love2d;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class DevelopmentFolderWatcher {

    public static void watchDirectoryPath(Path path) throws IOException, InterruptedException {
        // Sanity check - Check if path is a folder
        try {
            Boolean isFolder = (Boolean) Files.getAttribute(path,
                    "basic:isDirectory", NOFOLLOW_LINKS);
            if (!isFolder) {
                throw new IllegalArgumentException("Path: " + path + " is not a folder");
            }
        } catch (IOException ioe) {
            // Folder does not exists
            ioe.printStackTrace();
        }

        System.out.println("Watching path: " + path);

        // We obtain the file system of the Path
        FileSystem fs = path.getFileSystem ();

        // We create the new WatchService using the new try() block
        WatchService service = fs.newWatchService();

            // We register the path to the service
            // We watch for creation events
            path.register(service, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            // Start the infinite polling loop
            WatchKey key = null;
            while(true) {
                key = service.take();

                // Dequeueing events
                Kind<?> kind = null;
                for(WatchEvent<?> watchEvent : key.pollEvents()) {
                    // Get the type of the event
                    kind = watchEvent.kind();
                    if (OVERFLOW == kind) {
                        continue; //loop
                    } else if (ENTRY_CREATE == kind || ENTRY_MODIFY == kind || ENTRY_DELETE == kind) {
                        // A new Path was created
                        Path newPath = ((WatchEvent<Path>) watchEvent).context();
                        // Output
                        System.out.println("New change: " + newPath);
                        Main.compile(Main.input_s,Main.output_s,Main.bundle_identifier,Main.bundle_name);
                    }
                }

                if(!key.reset()) {
                    break; //loop
                }
            }

        }
}
