package org.love2d;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.security.CodeSource;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import org.apache.commons.io.FileUtils;

public class Main {

    static String jar_path;
    static String input_s;
    static String output_s;
    static String bundle_identifier;
    static String bundle_name;

    public static byte[] combineByteArrays(byte[] A, byte[] B)
    {
        int aLen = A.length;
        int bLen = B.length;

        byte[] C = (byte[]) Array.newInstance(A.getClass().getComponentType(), aLen + bLen);
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);

        return C;
    }

    public static void main(String[] args)
    {
        if (args.length < 4)
        {
            System.out.println("Wrong Number of Arguments. Usage: lovec input_dir output_dir bundle_identifier bundle_name [-w]\n\tinput_dir: the LOVE project\n\toutput_dir: the directory to put builds in\n\tbundle_identifier: the macosx bundle identifier ex: com.example.MyGame\n\tbundle_name: The macosx bundle name, aka, the name of your game ex: MyGame\n\tThe -w switch watches the input folder for changes and recompiles on every change.");
            return;
        }

        if(args.length > 4)
        {
            //Watch parameter
            if(args[4].equalsIgnoreCase("-w")){
                try {
                    Main.input_s = args[0];
                    Main.output_s = args[1];
                    Main.bundle_identifier = args[2];
                    Main.bundle_name = args[3];
                    DevelopmentFolderWatcher.watchDirectoryPath(Paths.get(args[0]));
                }catch(Exception e){
                    System.out.println("Folder Watcher could not be initiated");
                }
            }
        }

        Main.compile(args[0],args[1],args[2],args[3]);
    }

    static void compile(String input_s, String output_s, String bundle_identifier, String bundle_name){
        System.out.println("\nSetting stuff up");
        System.out.println("================\n");
        try
        {
            CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            jar_path = jarFile.getParentFile().getPath() + File.separator;
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        File input = new File(input_s);
        File output = new File(output_s);
        if (!output.exists())
        {
            System.out.println("Creating output directory: " + output.toString());
            output.mkdir();
        }
        if ((!input.exists()) || (!input.isDirectory()))
        {
            System.out.println("No such directory: " + input_s);
            return;
        }
        String name = input.getName();

        File output_love = new File(output_s + File.separator + name + ".love");

        output_love.delete();

        File win32_out = new File(output_s + File.separator + "win32");
        if (!win32_out.exists())
        {
            System.out.println("Win32 output dir missing, creating");
            win32_out.mkdir();
        }
        File win64_out = new File(output_s + File.separator + "win64");
        if (!win64_out.exists())
        {
            System.out.println("Win64 output dir missing, creating");
            win64_out.mkdir();
        }
        File osx_out = new File(output_s + File.separator + "macosx");
        if (!osx_out.exists())
        {
            System.out.println("OSX output dir missing, creating");
            osx_out.mkdir();
        }
        System.out.println("\nCopying 32-bit *.DLLs");
        System.out.println("==============\n");
        File[] arrayOfFile;
        int j = (arrayOfFile = new File(jar_path + "win32_exec").listFiles()).length;
        for (int i = 0; i < j; i++)
        {
            File f = arrayOfFile[i];
            if ((!f.toString().endsWith(".exe")) && (!new File(win32_out.toString() + File.separator + f.getName()).exists())) {
                try
                {
                    System.out.print("Attempting to copy " + f.getName() + " to output directory...\t\t");
                    Files.copy(f.toPath(), new File(win32_out.toString() + File.separator + f.getName()).toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.COPY_ATTRIBUTES,
                            LinkOption.NOFOLLOW_LINKS});
                    System.out.println("done.");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else if (!f.toString().endsWith(".exe")) {
                System.out.println(f.getName() + " already exists in output dir");
            }
        }
        System.out.println("\nCopying 64-bit *.DLLs");
        System.out.println("==============\n");

        j = (arrayOfFile = new File(jar_path + "win64_exec").listFiles()).length;
        for (int i = 0; i < j; i++)
        {
            File f = arrayOfFile[i];
            if ((!f.toString().endsWith(".exe")) && (!new File(win64_out.toString() + File.separator + f.getName()).exists())) {
                try
                {
                    System.out.print("Attempting to copy " + f.getName() + " to output directory...\t\t");
                    Files.copy(f.toPath(), new File(win64_out.toString() + File.separator + f.getName()).toPath(), new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.COPY_ATTRIBUTES,
                            LinkOption.NOFOLLOW_LINKS });
                    System.out.println("done.");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else if (!f.toString().endsWith(".exe")) {
                System.out.println(f.getName() + " already exists in output dir");
            }
        }
        System.out.println("\nZipping " + name + ".love");
        for (int i = 0; i < ("Zipping " + name + ".love").length(); i++) {
            System.out.print("=");
        }
        System.out.println("\n");

        try
        {
            ZipFile zip = new ZipFile(output_love);
            ZipParameters parameters = new ZipParameters();

            parameters.setCompressionMethod(0);
            parameters.setCompressionLevel(3);

            System.out.println("Beginning to add files to " + name + ".love");
            addFolderToZip(input, zip, parameters);
            System.out.println("Love creation completed");
        }
        catch (ZipException e)
        {
            e.printStackTrace();
        }
        System.out.println("\nWin32 EXE Packaging");
        System.out.println("===================\n");
        try
        {
            System.out.println("Reading love.exe as binary");
            byte[] love_exe = readSmallBinaryFile(jar_path + "win32_exec" + File.separator + "love.exe");
            System.out.println("Reading " + name + ".love as binary");
            byte[] zip_data = readSmallBinaryFile(output_love.toString());

            System.out.println("Combining bytes of love.exe and " + name + ".love");
            byte[] totalData = combineByteArrays(love_exe, zip_data);

            System.out.println("Writing combined bytes to " + name + ".exe");
            writeSmallBinaryFile(totalData, output_s + File.separator + "win32" + File.separator + name + ".exe");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("\nWin64 EXE Packaging");
        System.out.println("===================\n");
        try
        {
            System.out.println("Reading love.exe as binary");
            byte[] love_exe = readSmallBinaryFile(jar_path + "win64_exec" + File.separator + "love.exe");
            System.out.println("Reading " + name + ".love as binary");
            byte[] zip_data = readSmallBinaryFile(output_love.toString());

            System.out.println("Combining bytes of love.exe and " + name + ".love");
            byte[] totalData = combineByteArrays(love_exe, zip_data);

            System.out.println("Writing combined bytes to " + name + ".exe");
            writeSmallBinaryFile(totalData, output_s + File.separator + "win64" + File.separator + name + ".exe");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        System.out.println("\nMacOSX .APP Packaging");
        System.out.println("=====================\n");

        File macosx_app = new File(osx_out.toString() + File.separator + name + ".app");
        try
        {
            if (macosx_app.exists()) {
                FileUtils.deleteDirectory(macosx_app);
            }
            System.out.println("Creating OSX App directory");
            macosx_app.mkdir();
            System.out.println("Copying love framework into app");
            FileUtils.copyDirectory(new File(jar_path + "LOVE.app"), macosx_app);
            System.out.println("Setting up plist");
            FileUtils.writeStringToFile(new File(macosx_app + File.separator + "Contents" + File.separator + "Info.plist"), PListHelper.genPList(bundle_identifier, bundle_name));
            System.out.println("Embedding " + name + ".love into app");
            FileUtils.copyFile(output_love, new File(macosx_app + File.separator + "Contents" + File.separator + "Resources" + File.separator + name + ".love"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    static void addFolderToZip(File folder, ZipFile zf, ZipParameters pars)
            throws ZipException
    {
        File[] arrayOfFile;
        int j = (arrayOfFile = folder.listFiles()).length;
        for (int i = 0; i < j; i++)
        {
            File f = arrayOfFile[i];
            if (f.isFile())
            {
                System.out.print("Adding file " + f.getName() + " to love...\t\t");
                zf.addFile(f, pars);
                System.out.println("done.");
            }
            if (f.isDirectory())
            {
                System.out.print("Adding directory " + f.getName() + " to love...\t\t");
                zf.addFolder(f, pars);
                System.out.println("done.");
            }
        }
    }

    static byte[] readSmallBinaryFile(String aFileName)
            throws IOException
    {
        Path path = Paths.get(aFileName, new String[0]);
        return Files.readAllBytes(path);
    }

    static void writeSmallBinaryFile(byte[] aBytes, String aFileName)
            throws IOException
    {
        Path path = Paths.get(aFileName, new String[0]);
        Files.write(path, aBytes, new OpenOption[0]);
    }
}
