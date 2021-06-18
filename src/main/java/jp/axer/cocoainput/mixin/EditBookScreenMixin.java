package jp.axer.cocoainput.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}
