var MethodInsnNode = org.objectweb.asm.tree.MethodInsnNode;
var VarInsnNode = org.objectweb.asm.tree.VarInsnNode;
var IntInsnNode = org.objectweb.asm.tree.IntInsnNode;
var InsnNode = org.objectweb.asm.tree.InsnNode;
var TypeInsnNode = org.objectweb.asm.tree.TypeInsnNode;
var FieldInsnNode = org.objectweb.asm.tree.FieldInsnNode;
var FieldNode = org.objectweb.asm.tree.FieldNode;

var Opcodes = org.objectweb.asm.Opcodes;

function initializeCoreMod(){
    return{

        'sharedconstants':{
            'target':{
                'type':'CLASS',
                'name':'net.minecraft.util.SharedConstants'
            },
            'transformer': sharedconstants_transform
        },


        'guitextfield':{
            'target':{
                'type':'CLASS',
                'name':'net.minecraft.client.gui.widget.TextFieldWidget'
            },
            'transformer': guitextfield_transform
        },
        'guieditsign':{
              'target':{
              'type':'CLASS',
              'name':'net.minecraft.client.gui.screen.EditSignScreen'
            },
            'transformer': guieditsign_transform
        },
        'guiscreenbook':{
                'target':{
                'type':'CLASS',
                'name':'net.minecraft.client.gui.screen.EditBookScreen'
            },
            'transformer': guiscreenbook_transform
        }
    }
}

function sharedconstants_transform( node ) {
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "func_71566_a" || method.name == "isAllowedCharacter"  ) {//コンストラクタの編集
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.operand == 167) {
		    insn.operand = 0;
		    break;
                }
	    }
        }
    }
}

function guitextfield_transform( node ) {
    node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,"wrapper","Ljp/axer/cocoainput/wrapper/TextFieldWidgetWrapper;",null,null));  //GuiTextFieldWrapper wrapperフィールドを追加
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "<init>" ) {//コンストラクタの編集
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.opcode==Opcodes.RETURN ) {//コンストラクタの末尾(return前)に wrapper = new GuiTextFieldWrapper(this);を追加
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new TypeInsnNode(Opcodes.NEW,"jp/axer/cocoainput/wrapper/TextFieldWidgetWrapper") );
                    method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP) );
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKESPECIAL, "jp/axer/cocoainput/wrapper/TextFieldWidgetWrapper", "<init>", "(Lnet/minecraft/client/gui/widget/TextFieldWidget;)V",false) );
                    method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/widget/TextFieldWidget", "wrapper", "Ljp/axer/cocoainput/wrapper/TextFieldWidgetWrapper;") );
                    break;
                }

            }
        }
        else if(method.name=="setFocused2"||method.name=="func_146195_b"){//setFocusedの編集,先頭にwrapper.setFocused(var1);を追加
            var insn = method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/widget/TextFieldWidget", "wrapper", "Ljp/axer/cocoainput/wrapper/TextFieldWidgetWrapper;") );
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ILOAD, 1 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jp/axer/cocoainput/wrapper/TextFieldWidgetWrapper", "setFocused", "(Z)V",false) );
        }
        else if(method.name=="setCanLoseFocus" || method.name == "func_146205_d"){//setCanLoseFocusの編集,先頭にwrapper.setCanLoseFocus(var1);を追加
            var insn = method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/widget/TextFieldWidget", "wrapper", "Ljp/axer/cocoainput/wrapper/TextFieldWidgetWrapper;") );
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ILOAD, 1 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jp/axer/cocoainput/wrapper/TextFieldWidgetWrapper", "setCanLoseFocus", "(Z)V",false) );
        }
        else if(method.name=="tick"||method.name=="func_146178_a"){
            var insn=method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/widget/TextFieldWidget", "wrapper", "Ljp/axer/cocoainput/wrapper/TextFieldWidgetWrapper;") );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jp/axer/cocoainput/wrapper/TextFieldWidgetWrapper", "updateCursorCounter", "()V",false) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.RETURN) );
        }

    }
    return node;
}

function guieditsign_transform( node ) {
    node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,"wrapper","Ljp/axer/cocoainput/wrapper/EditSignScreenWrapper;",null,null));  //GuiEditSignWrapper wrapperフィールドを追加
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "<init>" ){
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.opcode==Opcodes.RETURN ) {//コンストラクタの末尾(return前)に wrapper = new EditSignScreenWrapper(this);を追加
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new TypeInsnNode(Opcodes.NEW,"jp/axer/cocoainput/wrapper/EditSignScreenWrapper") );
                    method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP) );
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKESPECIAL, "jp/axer/cocoainput/wrapper/EditSignScreenWrapper", "<init>", "(Lnet/minecraft/client/gui/screen/EditSignScreen;)V",false) );
                    method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/screen/EditSignScreen", "wrapper", "Ljp/axer/cocoainput/wrapper/EditSignScreenWrapper;") );
                    break;
                }
            }
        }

    }
    return node;
}

function guiscreenbook_transform( node ) {
    node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,"wrapper","Ljp/axer/cocoainput/wrapper/EditBookScreenWrapper;",null,null));  //GuiScreenBookWrapper wrapperフィールドを追加
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "<init>" ){
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.opcode==Opcodes.RETURN ) {//コンストラクタの末尾(return前)に wrapper = new EditBookScreenWrapper(this);を追加
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new TypeInsnNode(Opcodes.NEW,"jp/axer/cocoainput/wrapper/EditBookScreenWrapper") );
                    method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP) );
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKESPECIAL, "jp/axer/cocoainput/wrapper/EditBookScreenWrapper", "<init>", "(Lnet/minecraft/client/gui/screen/EditBookScreen;)V",false) );
                    method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/screen/EditBookScreen", "wrapper", "Ljp/axer/cocoainput/wrapper/EditBookScreenWrapper;") );
                    break;
                }
            }
        }

    }
    return node;
}
