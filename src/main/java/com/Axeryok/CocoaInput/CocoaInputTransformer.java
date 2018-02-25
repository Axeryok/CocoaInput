package com.Axeryok.CocoaInput;

import java.io.FileOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import com.Axeryok.CocoaInput.adapter.ChatAllowedCharactersAdapter;
import com.Axeryok.CocoaInput.adapter.GuiEditSignAdapter;
import com.Axeryok.CocoaInput.adapter.GuiScreenBookAdapter;
import com.Axeryok.CocoaInput.adapter.GuiTextFieldAdapter;
import com.Axeryok.CocoaInput.adapter.MinecraftAdapter;
import com.Axeryok.CocoaInput.adapter.NetHandlerPlayServerAdapter;
import com.sun.jna.Platform;

import net.minecraft.launchwrapper.IClassTransformer;

public class CocoaInputTransformer implements IClassTransformer, Opcodes {
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		if(transformedName.indexOf("jgl")!=-1){
			System.out.println("asassa:"+transformedName);
		}
		if     	(transformedName.equals("net.minecraft.client.gui.GuiTextField"))
			return transformGuiTextField(transformedName,bytes);
		else if (transformedName.equals("net.minecraft.client.gui.GuiScreenBook"))
			return transformGuiScreenBook(transformedName,bytes);
		else if (transformedName.equals("net.minecraft.client.gui.inventory.GuiEditSign"))
			return this.transformGuiEditSign(transformedName,bytes);
		else if (transformedName.equals("net.minecraft.util.ChatAllowedCharacters"))
			return this.transformChatAllowedCharacters(transformedName,bytes);
		else if (transformedName.equals("net.minecraft.network.NetHandlerPlayServer"))
			return this.transformNetHandlerPlayServer(transformedName,bytes);
		else if (transformedName.equals("net.minecraft.client.Minecraft"))
			return this.transformMinecraft(transformedName,bytes);
		else
			return bytes;
	}

	private byte[] transformNetHandlerPlayServer(String name,byte[] bytes){
		ModLogger.log("Open classfile "+name+".");
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw =new ClassWriter(1);
		ClassVisitor cv= new NetHandlerPlayServerAdapter(cw);
		cr.accept(cv, 0);
		ModLogger.log("Finish modify classfile "+name+".");
		return cw.toByteArray();
	}

	private byte[] transformChatAllowedCharacters(String name,byte[] bytes){
		ModLogger.log("Open classfile "+name+".");
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw =new ClassWriter(1);
		ClassVisitor cv= new ChatAllowedCharactersAdapter(cw);
		cr.accept(cv, 0);
		ModLogger.log("Finish modify classfile "+name+".");
		return cw.toByteArray();
	}

	private byte[] transformGuiEditSign(String name,byte[] bytes) {
		ModLogger.log("Open classfile "+name+".");
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw=new ClassWriter(1);
		//add field "GuiEditSignWrapper wrapper" to GuiEditSign
		cw.visitField(Opcodes.ACC_PUBLIC,"wrapper","Lcom/Axeryok/CocoaInput/wrapper/GuiEditSignWrapper;",null,null).visitEnd();;
		ClassVisitor cv=new GuiEditSignAdapter(cw);
		cr.accept(cv, 0);
		ModLogger.log("Finish modify classfile "+name+".");
		return cw.toByteArray();
	}

	private byte[] transformGuiScreenBook(String name,byte[] bytes) {
		ModLogger.log("Open classfile "+name+".");
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw=new ClassWriter(1);
		//add field "GuiScreenBookWrapper wrapper" to GuiScreen
		cw.visitField(Opcodes.ACC_PUBLIC,"wrapper","Lcom/Axeryok/CocoaInput/wrapper/GuiScreenBookWrapper;",null,null).visitEnd();;
		ClassVisitor cv=new GuiScreenBookAdapter(cw);
		cr.accept(cv, 0);
		ModLogger.log("Finish modify classfile "+name+".");
		return cw.toByteArray();
	}

	private byte[] transformGuiTextField(String name,byte[] bytes){
		ModLogger.log("Open classfile "+name+".");
		ClassReader cr=new ClassReader(bytes);
		ClassWriter cw=new ClassWriter(1);
		//add field "GuiTextFieldWrapper wrapper" to GuiTextField
		cw.visitField(Opcodes.ACC_PUBLIC,"wrapper","Lcom/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper;",null,null).visitEnd();;
		ClassVisitor cv=new GuiTextFieldAdapter(cw);
		cr.accept(cv, 0);
		ModLogger.log("Finish modify classfile "+name+".");
		return cw.toByteArray();
	}

	private byte[] transformMinecraft(String name,byte[] bytes){//Mac向けFullScreen問題を解決する
		if(Platform.isMac()){
			ModLogger.log("Open classfile "+name+".");
			ClassReader cr=new ClassReader(bytes);
			ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS|ClassWriter.COMPUTE_FRAMES);
			ClassVisitor cv=new MinecraftAdapter(cw);
			cr.accept(cv, 0);
			ModLogger.log("Finish modify classfile "+name+".");
			debugFilePut(cw.toByteArray());
			return cw.toByteArray();
		}
		return bytes;
	}

	private void debugFilePut(byte[] bytes) {
		try{
			FileOutputStream output=new FileOutputStream("test.class");
			output.write(bytes);
			output.close();
		}
		catch(Exception e){}
	}


}