package com.Axeryok.CocoaInput.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.Axeryok.CocoaInput.DeobfuscationHelper;
import com.Axeryok.CocoaInput.ModLogger;

public class GuiScreenBookAdapter extends ClassVisitor{
	String className="net.minecraft.client.gui.GuiScreenBook";
	public GuiScreenBookAdapter(ClassVisitor cv){
		super(Opcodes.ASM5,cv);
	}

	//initGuiの先頭に wrapper = new GuiEditSignWrapper(this);を追加
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		if("initGui".equals(name)||"func_73866_w_".equals(DeobfuscationHelper.mapMethodName(className, name, desc))){
			ModLogger.log("Found method:"+name+" and add code at the beginning of this method.");
			return new MethodVisitor(Opcodes.ASM5, super.visitMethod(access, name, desc, signature, exceptions)){
				@Override
				public void visitCode(){
					this.visitIntInsn(Opcodes.ALOAD,0);
					this.visitTypeInsn(Opcodes.NEW,"com/Axeryok/CocoaInput/wrapper/GuiScreenBookWrapper");
					this.visitInsn(Opcodes.DUP);
					this.visitIntInsn(Opcodes.ALOAD, 0);
					this.visitMethodInsn(Opcodes.INVOKESPECIAL, "com/Axeryok/CocoaInput/wrapper/GuiScreenBookWrapper", "<init>", "(Lnet/minecraft/client/gui/GuiScreenBook;)V",false);
					this.visitFieldInsn(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiScreenBook", "wrapper", "Lcom/Axeryok/CocoaInput/wrapper/GuiScreenBookWrapper;");
					super.visitCode();
				}
			};
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
