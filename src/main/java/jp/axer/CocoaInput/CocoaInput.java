package jp.axer.cocoainput;

import com.sun.jna.Platform;
import jp.axer.cocoainput.arch.darwin.DarwinController;
import jp.axer.cocoainput.arch.dummy.DummyController;
import jp.axer.cocoainput.plugin.CocoaInputController;
import jp.axer.cocoainput.util.ModLogger;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mod("cocoainput")
public class CocoaInput {
    private static CocoaInputController controller;

    public CocoaInput() throws Exception {
        ModList.get().getModFileById("cocoainput").getConfig();
        if(Platform.isMac()) {
            CocoaInput.applyController(new DarwinController());
        }
        else{
            CocoaInput.applyController(new DummyController());
        }
        MinecraftForge.EVENT_BUS.register(this);
        ModLogger.log("CocoaInput has been initialized.");
    }

    public static double getScreenScaledFactor() {
        return Minecraft.getInstance().mainWindow.getGuiScaleFactor();
    }

    public static void applyController(CocoaInputController controller) throws IOException {
        CocoaInput.controller = controller;
        ModLogger.log("CocoaInput is now using controller:" + controller.getClass().toString());
    }

    public static CocoaInputController getController() {
        return CocoaInput.controller;
    }

    public static void copyLibrary(String libraryName, String libraryPath) throws IOException {
        InputStream libFile;
        try {//Modファイルを検出し、jar内からライブラリを取り出す
            ZipFile jarfile = new ZipFile(ModList.get().getModFileById("cocoainput").getFile().getFilePath().toString());
            libFile = jarfile.getInputStream(new ZipEntry(libraryPath));
        } catch (FileNotFoundException e) {//存在しない場合はデバッグモードであるのでクラスパスからライブラリを取り出す
            ModLogger.log("Couldn't get library path. Is this debug mode?'");
            libFile = ClassLoader.getSystemResourceAsStream(libraryPath);
        }
        File nativeDir = new File(Minecraft.getInstance().gameDir.getAbsolutePath().concat("/native"));
        File copyLibFile = new File(Minecraft.getInstance().gameDir.getAbsolutePath().concat("/native/" + libraryName));
        try {
            nativeDir.mkdir();
            FileOutputStream fos = new FileOutputStream(copyLibFile);
            copyLibFile.createNewFile();
            IOUtils.copy(libFile, fos);
            fos.close();
        } catch (IOException e1) {
            ModLogger.error("Attempted to copy library to ./native/" + libraryName + " but failed.");
            throw e1;
        }
        System.setProperty("jna.library.path", nativeDir.getAbsolutePath());
        ModLogger.log("CocoaInput has copied library to native directory.");
    }
}
