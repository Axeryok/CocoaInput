package com.Axeryok.CocoaInput.asm;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.Opcodes;

public class CocoaInputTransformer implements IClassTransformer, Opcodes {
	// 改変対象のクラスの完全修飾名です。
	// 後述でMinecraft.jar内の難読化されるファイルを対象とする場合の簡易な取得方法を紹介します。
	private static final String TARGET_CLASS_NAME = "net.minecraft.src.TargetClass";
	int flg;

	// クラスがロードされる際に呼び出されるメソッドです。
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		// FMLRelauncher.side() : Client/Server どちらか一方のを対象とする場合や、
		// 一つのMODで Client/Sever 両方に対応したMODで、この値を判定して処理を変える事ができます。
		// 今回は"CLIENT"と比較し、Client側のファイルを対象としている例です。
		// Client側専用のMODとして公開するのであれば、判定は必須ではありません。
		flg = 0;
		// text
		// screen
		// Edit
		try {
			// name : 現在ロードされようとしているクラス名が格納されています。
			if (transformedName.equals("net.minecraft.client.gui.GuiTextField"))
				return replaceClass(bytes,
						"net/minecraft/client/gui/GuiTextField.class");
			else if (transformedName
					.equals("net.minecraft.client.gui.GuiScreen"))
				return replaceClass(bytes,
						"net/minecraft/client/gui/GuiScreen.class");
			
			else if (transformedName
					.equals("net.minecraft.client.gui.GuiScreenBook"))
				return replaceClass(bytes,
						"net/minecraft/client/gui/GuiScreenBook.class");
			else if (transformedName
					.equals("net.minecraft.client.gui.inventory.GuiEditSign"))
				return replaceClass(bytes,
						"net/minecraft/client/gui/inventory/GuiEditSign.class");
			else if (transformedName
					.equals("net.minecraft.util.ChatAllowedCharacters"))
				return replaceClass(bytes,
						"net/minecraft/util/ChatAllowedCharacters.class");
            else if (transformedName
                     .equals("net.minecraft.network.NetHandlerPlayServer"))
                return replaceClass(bytes,
                        "net/minecraft/network/NetHandlerPlayServer.class");
			// 処理対象外なので何もしな
			else
				return bytes;

			// --------------------------------------------------------------
			// クラスファイル丸ごと差し替える場合
			// --------------------------------------------------------------

			// --------------------------------------------------------------
			// ASMを使用し、既存のクラスファイルに改変を施す場合。
			// --------------------------------------------------------------
			// return hookDoRenderLivingMethod(bytes);

		} catch (Exception e) {
			throw new RuntimeException("failed : TutorialTransformer loading",
					e);
		}
	}

	// 下記の想定で実装されています。
	// 対象クラスの bytes を ModifiedTargetClass.class ファイルに置き換える
	private byte[] replaceClass(byte[] bytes, String inpu) throws IOException {
		ZipFile zf = null;
		InputStream zi = null;
		try {
			zf = new ZipFile(CocoaInputCorePlugin.location);
			// 差し替え後のファイルです。coremodのjar内のパスを指定します。
			ZipEntry ze = zf.getEntry(inpu);

			if (ze != null) {
				zi = zf.getInputStream(ze);
				int len = (int) ze.getSize();
				bytes = new byte[len];

				// ヒープサイズを超えないように、ストリームからファイルを1024ずつ読み込んで bytes に格納する
				int MAX_READ = 1024;
				int readed = 0, readsize, ret;
				while (readed < len) {
					readsize = MAX_READ;
					if (len - readed < MAX_READ) {
						readsize = len - readed;
					}
					ret = zi.read(bytes, readed, readsize);
					if (ret == -1)
						break;
					readed += ret;
				}
			}
			return bytes;
		}

		finally {
			if (zi != null) {
				zi.close();
			}

			if (zf != null) {
				zf.close();
			}
		}
	}

	// 下記の想定で実装されています。
	// EntityLiving.class の doRenderLiving の先頭に
	// tutorial/test.class の passTestRender(EntityLiving, double, double,
	// double)メソッドの呼び出しを追加する。

}