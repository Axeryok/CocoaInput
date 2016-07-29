package com.Axeryok.CocoaInput.asm;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import com.Axeryok.CocoaInput.CocoaInput;
import com.Axeryok.CocoaInput.adapter.ChatAllowedCharactersAdapter;
import com.Axeryok.CocoaInput.adapter.NetHandlerPlayServerAdapter;

public class CocoaInputTransformer implements IClassTransformer, Opcodes {
	
	
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		try {
			// name : 現在ロードされようとしているクラス名が格納されています。
			if (transformedName.equals("net.minecraft.client.gui.GuiTextField"))
				return replaceClass(bytes,
						"net/minecraft/client/gui/GuiTextField.class");			
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
				return this.transformChatAllowedCharacters(bytes);
            else if (transformedName
                     .equals("net.minecraft.network.NetHandlerPlayServer"))
                return this.transformNetHandlerPlayServer(bytes);
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
		if(!CocoaInput.isActive){
			return bytes;
		}
		ZipFile zf = null;
		InputStream zi = null;
		try {
			try{
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
			}catch(FileNotFoundException e){
				File c=new File(CocoaInputCorePlugin.location,inpu);
				if (c != null) {
					zi = new FileInputStream(c);
					int len = (int) c.length();
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

	private byte[] transformNetHandlerPlayServer(byte[] bytes){
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw =new ClassWriter(1);
		ClassVisitor cv= new NetHandlerPlayServerAdapter(cw);
		cr.accept(cv, 0);
		return cw.toByteArray();
	}
	
	private byte[] transformChatAllowedCharacters(byte[] bytes){
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw =new ClassWriter(1);
		ClassVisitor cv= new ChatAllowedCharactersAdapter(cw);
		cr.accept(cv, 0);
		return cw.toByteArray();
	}
}