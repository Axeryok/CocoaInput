package com.Axeryok.CocoaInput.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.Axeryok.CocoaInput.DeobfuscationHelper;
import com.Axeryok.CocoaInput.ModLogger;

public class MinecraftAdapter extends ClassVisitor{
	String className="net.minecraft.client.Minecraft";
	public MinecraftAdapter(ClassVisitor cv){
		super(Opcodes.ASM5,cv);
	}

	//toggleFullScreenの先頭にCocoaInput.toggleFullScreen();return;を追加
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		if("func_71352_k".equals(DeobfuscationHelper.mapMethodName(className, name, desc))||name.equals("toggleFullscreen")){
			ModLogger.log("Found method:"+name+" and add code at the beginning of this method.");
			return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)){
				@Override
				public void visitCode(){
					Label skip = new Label();
					super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/Axeryok/CocoaInput/CocoaInput", "toggleFullScreen", "()Z",false);
					mv.visitJumpInsn(Opcodes.IFEQ, skip);
					super.visitInsn(Opcodes.RETURN);
					mv.visitLabel(skip);
					super.visitCode();
				}
			};
		}

		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
