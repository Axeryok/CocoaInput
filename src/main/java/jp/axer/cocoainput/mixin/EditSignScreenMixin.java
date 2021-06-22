package jp.axer.cocoainput.mixin;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jp.axer.cocoainput.wrapper.EditSignScreenWrapper;
import net.minecraft.client.gui.screen.EditSignScreen;

@Mixin(EditSignScreen.class)
public class EditSignScreenMixin {
	 EditSignScreenWrapper wrapper;
	 
	 @Inject(method="init",at=@At("RETURN"))
	 private void init(CallbackInfo ci) {
		 wrapper = new EditSignScreenWrapper((EditSignScreen)(Object)this);
	 }
	 
	 @Redirect(method="tick",at = @At(value="FIELD", target="Lnet/minecraft/client/gui/screen/EditSignScreen;frame:I",opcode=Opcodes.PUTFIELD))
	 private void injectCurosr(EditSignScreen esc,int n) {
		 esc.frame=wrapper.renewCursorCounter();
	 }
}
