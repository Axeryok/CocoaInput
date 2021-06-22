package jp.axer.cocoainput.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jp.axer.cocoainput.wrapper.EditBookScreenWrapper;
import net.minecraft.client.gui.screen.EditBookScreen;

@Mixin(EditBookScreen.class)
public class EditBookScreenMixin {
	 EditBookScreenWrapper wrapper;
	 
	 @Inject(method="init*",at=@At("RETURN"))
	 private void init(CallbackInfo ci) {
		 wrapper = new EditBookScreenWrapper((EditBookScreen)(Object)this);
	 }
	 
	 @Redirect(method="tick",at = @At(value="FIELD", target="Lnet/minecraft/client/gui/screen/EditBookScreen;frameTick:I",opcode=Opcodes.PUTFIELD))
	 private void injectCurosr(EditBookScreen esc,int n) {
		 esc.frameTick=wrapper.renewCursorCounter();
	 }
}
