package jp.axer.cocoainput.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
