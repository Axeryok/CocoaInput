package com.Axeryok.CocoaInput.adapter;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.Axeryok.CocoaInput.DeobfuscationHelper;
import com.Axeryok.CocoaInput.ModLogger;

public class ChatAllowedCharactersAdapter extends ClassVisitor{
	String className="net.minecraft.util.ChatAllowedCharacters";
	public ChatAllowedCharactersAdapter(ClassVisitor cv){
		super(Opcodes.ASM4,cv);
	}
	
	//isAllowedCharacter 内条件式 character != 167 を character != 0 に変更
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions){
		if("isAllowedCharacter".equals(name)||"func_71566_a".equals(DeobfuscationHelper.mapMethodName(className, name, desc))){
			return new MethodVisitor(Opcodes.ASM4, super.visitMethod(access, name, desc, signature, exceptions)){
				@Override
		        public void visitIntInsn(int opcode, int operand){
					if(operand==167){
						ModLogger.log("Replace behavior.");
						super.visitIntInsn(opcode, 0);
					}
					super.visitIntInsn(opcode, operand);
				}
			};
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
}
