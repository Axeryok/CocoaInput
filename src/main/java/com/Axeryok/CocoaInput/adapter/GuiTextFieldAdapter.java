package com.Axeryok.CocoaInput.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.Axeryok.CocoaInput.DeobfuscationHelper;
import com.Axeryok.CocoaInput.ModLogger;

public class GuiTextFieldAdapter extends ClassVisitor{
	String className="net.minecraft.client.gui.GuiTextField";
	public GuiTextFieldAdapter(ClassVisitor cv){
		super(Opcodes.ASM5,cv);
	}

	//initGuiの末尾(return前)に wrapper = new GuiTextFieldWrapper(this);を追加
	//setFocusedの先頭にwrapper.setFocused(First arg,oldFocused);を追加
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		if(name.equals("<init>")){
			return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)){
				@Override
				public void visitInsn(int opcode){
					if(opcode==Opcodes.RETURN){
						ModLogger.log("Found method:<init> and add code at the end of this method.");
						this.visitIntInsn(Opcodes.ALOAD,0);
						this.visitTypeInsn(Opcodes.NEW,"com/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper");
						this.visitInsn(Opcodes.DUP);
						this.visitIntInsn(Opcodes.ALOAD, 0);
						this.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper", "<init>", "(Lnet/minecraft/client/gui/GuiTextField;)V",false);
						this.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiTextField", "wrapper", "Lcom/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper;");
						super.visitInsn(Opcodes.RETURN);
					}
					else{
						super.visitInsn(opcode);
					}
				}
				
			};
		}
		if(name.equals("setFocused")){
			ModLogger.log("Found method:"+name+" and add code at the beginning of this method.");
			return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)){
				@Override
				public void visitCode(){
					this.visitIntInsn(Opcodes.ALOAD,0);
					this.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiTextField", "wrapper", "Lcom/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper;");
					this.visitIntInsn(Opcodes.ILOAD, 1);
					this.visitIntInsn(Opcodes.ALOAD, 0);
					this.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiTextField","isFocused", "Z");
					this.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper", "setFocused", "(ZZ)V",false);
					super.visitCode();
				}
			};
		}
		if("func_146195_b".equals(DeobfuscationHelper.mapMethodName(className, name, desc))){
			return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)){
				@Override
				public void visitCode(){
					this.visitIntInsn(Opcodes.ALOAD,0);
					this.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiTextField", "wrapper", "Lcom/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper;");
					this.visitIntInsn(Opcodes.ILOAD, 1);
					this.visitIntInsn(Opcodes.ALOAD, 0);
					this.visitFieldInsn(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiTextField","field_146213_o", "Z");
					this.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/Axeryok/CocoaInput/wrapper/GuiTextFieldWrapper", "setFocused", "(ZZ)V",false);
					super.visitCode();
				}
			};
		}
		
		
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
