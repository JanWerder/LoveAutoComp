package org.love2d;

/**
 * Created by janwe on 28.08.2015.
 */
public class PListHelper
{
    public static String genPList(String identifier, String bundle)
    {
        return

                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n<plist version=\"1.0\">\n<dict>\n<key>BuildMachineOSBuild</key>\n<string>13C64</string>\n<key>CFBundleDevelopmentRegion</key>\n<string>English</string>\n<key>CFBundleDocumentTypes</key>\n<array>\n<dict>\n<key>CFBundleTypeIconFile</key>\n<string>LoveDocument.icns</string>\n<key>CFBundleTypeName</key>\n<string>L?VE Project</string>\n<key>CFBundleTypeRole</key>\n<string>Viewer</string>\n<key>LSHandlerRank</key>\n<string>Owner</string>\n<key>LSItemContentTypes</key>\n<array>\n<string>org.love2d.love-game</string>\n</array>\n</dict>\n<dict>\n<key>CFBundleTypeName</key>\n<string>Folder</string>\n<key>CFBundleTypeOSTypes</key>\n<array>\n<string>fold</string>\n</array>\n<key>CFBundleTypeRole</key>\n<string>Viewer</string>\n<key>LSHandlerRank</key>\n<string>None</string>\n</dict>\n</array>\n<key>CFBundleExecutable</key>\n<string>love</string>\n<key>CFBundleIconFile</key>\n<string>Love.icns</string>\n<key>CFBundleIdentifier</key>\n<string>" + identifier + "</string>\n" + "<key>CFBundleInfoDictionaryVersion</key>\n" + "<string>6.0</string>\n" + "<key>CFBundleName</key>\n" + "<string>" + bundle + "</string>\n" + "<key>CFBundlePackageType</key>\n" + "<string>APPL</string>\n" + "<key>CFBundleShortVersionString</key>\n" + "<string>0.9.1</string>\n" + "<key>CFBundleSignature</key>\n" + "<string>LoVe</string>\n" + "<key>DTCompiler</key>\n" + "<string>com.apple.compilers.llvm.clang.1_0</string>\n" + "<key>DTPlatformBuild</key>\n" + "<string>5B130a</string>\n" + "<key>DTPlatformVersion</key>\n" + "<string>GM</string>\n" + "<key>DTSDKBuild</key>\n" + "<string>13C64</string>\n" + "<key>DTSDKName</key>\n" + "<string>macosx10.9</string>\n" + "<key>DTXcode</key>\n" + "<string>0510</string>\n" + "<key>DTXcodeBuild</key>\n" + "<string>5B130a</string>\n" + "<key>LSApplicationCategoryType</key>\n" + "<string>public.app-category.games</string>\n" + "<key>NSHumanReadableCopyright</key>\n" + "<string>? 2006-2014 L?VE Development Team</string>\n" + "<key>NSPrincipalClass</key>\n" + "<string>NSApplication</string>\n" + "</dict>\n" + "</plist>\n";
    }
}