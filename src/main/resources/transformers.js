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
        'guitextfield':{
            'target':{
                'type':'CLASS',
                'name':'net.minecraft.client.gui.GuiTextField'
            },
            'transformer': guitextfield_transform
        },
        'guieditsign':{
              'target':{
              'type':'CLASS',
              'name':'net.minecraft.client.gui.inventory.GuiEditSign'
            },
            'transformer': guieditsign_transform
        },
        'guiscreenbook':{
                'target':{
                'type':'CLASS',
                'name':'net.minecraft.client.gui.GuiScreenBook'
            },
            'transformer': guiscreenbook_transform
        }
    }
}

function guitextfield_transform( node ) {
    node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,"wrapper","Ljp/axer/CocoaInput/wrapper/GuiTextFieldWrapper;",null,null));  //GuiTextFieldWrapper wrapperフィールドを追加
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "<init>" ) {//コンストラクタの編集
            for( var i = 0; i < method.instructions.size(); i ++ ) {
                var insn = method.instructions.get( i );
                if( insn.opcode==Opcodes.RETURN ) {//コンストラクタの末尾(return前)に wrapper = new GuiTextFieldWrapper(this);を追加
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new TypeInsnNode(Opcodes.NEW,"jp/axer/CocoaInput/wrapper/GuiTextFieldWrapper") );
                    method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP) );
                    method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
                    method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKESPECIAL, "jp/axer/CocoaInput/wrapper/GuiTextFieldWrapper", "<init>", "(Lnet/minecraft/client/gui/GuiTextField;)V",false) );
                    method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiTextField", "wrapper", "Ljp/axer/CocoaInput/wrapper/GuiTextFieldWrapper;") );
                    break;
                }

            }
        }
        else if(method.name=="setFocused"||method.name=="func_146195_b"){//setFocusedの編集,先頭にwrapper.setFocused(var1);を追加
            var insn = method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiTextField", "wrapper", "Ljp/axer/CocoaInput/wrapper/GuiTextFieldWrapper;") );
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ILOAD, 1 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jp/axer/CocoaInput/wrapper/GuiTextFieldWrapper", "setFocused", "(Z)V",false) );
        }
        else if(method.name=="tick"||method.name=="func_146178_a"){
            var insn=method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/client/gui/GuiTextField", "wrapper", "Ljp/axer/CocoaInput/wrapper/GuiTextFieldWrapper;") );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "jp/axer/CocoaInput/wrapper/GuiTextFieldWrapper", "updateCursorCounter", "()V",false) );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.RETURN) );
        }

    }
    return node;
}

function guieditsign_transform( node ) {
    node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,"wrapper","Ljp/axer/CocoaInput/wrapper/GuiEditSignWrapper;",null,null));  //GuiEditSignWrapper wrapperフィールドを追加
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "initGui"||method.name=="func_73866_w_" ){//initGuiの先頭に wrapper = new GuiEditSignWrapper(this);を追加
            var insn = method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new TypeInsnNode(Opcodes.NEW,"jp/axer/CocoaInput/wrapper/GuiEditSignWrapper") );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP) );
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKESPECIAL, "jp/axer/CocoaInput/wrapper/GuiEditSignWrapper", "<init>", "(Lnet/minecraft/client/gui/inventory/GuiEditSign;)V",false) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/inventory/GuiEditSign", "wrapper", "Ljp/axer/CocoaInput/wrapper/GuiEditSignWrapper;") );
        }

    }
    return node;
}

function guiscreenbook_transform( node ) {
    node.fields.add(new FieldNode(Opcodes.ACC_PUBLIC,"wrapper","Ljp/axer/CocoaInput/wrapper/GuiScreenBookWrapper;",null,null));  //GuiScreenBookWrapper wrapperフィールドを追加
    for( var i in node.methods ) {
        var method = node.methods[ i ];
        if( method.name == "initGui"||method.name=="func_73866_w_" ){//initGuiの先頭に wrapper = new GuiEditSignWrapper(this);を追加
            var insn = method.instructions.get(0);
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new TypeInsnNode(Opcodes.NEW,"jp/axer/CocoaInput/wrapper/GuiScreenBookWrapper") );
            method.instructions.insertBefore( insn, new InsnNode( Opcodes.DUP) );
            method.instructions.insertBefore( insn, new IntInsnNode( Opcodes.ALOAD, 0 ) );
            method.instructions.insertBefore( insn, new MethodInsnNode(Opcodes.INVOKESPECIAL, "jp/axer/CocoaInput/wrapper/GuiScreenBookWrapper", "<init>", "(Lnet/minecraft/client/gui/GuiScreenBook;)V",false) );
            method.instructions.insertBefore( insn, new FieldInsnNode(Opcodes.PUTFIELD, "net/minecraft/client/gui/GuiScreenBook", "wrapper", "Ljp/axer/CocoaInput/wrapper/GuiScreenBookWrapper;") );
        }

    }
    return node;
}