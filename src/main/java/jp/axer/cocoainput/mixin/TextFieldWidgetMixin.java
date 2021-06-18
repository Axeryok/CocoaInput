package jp.axer.cocoainput.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import jp.axer.cocoainput.wrapper.TextFieldWidgetWrapper;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(TextFieldWidget.class)
public class TextFieldWidgetMixin {
	 TextFieldWidgetWrapper wrapper;
	 
	 @Inject(method="<init>*",at=@At("RETURN"))
	 private void init(CallbackInfo ci) {
		 wrapper = new TextFieldWidgetWrapper((TextFieldWidget)(Object)this);
	 }
	 
	 @Inject(method="setFocus",at=@At("HEAD"))
	 private void setFocus(boolean b,CallbackInfo ci) {
		 wrapper.setFocused(b);
	 }
	 @Inject(method="setCanLoseFocus",at=@At("HEAD"))
	 private void setCanLoseFocus(boolean b,CallbackInfo ci) {
		 wrapper.setCanLoseFocus(b);
	 }
	 @Inject(method="tick",at=@At("HEAD"),cancellable=true)
	 private void tick(CallbackInfo ci) {
		 wrapper.updateCursorCounter();
		 ci.cancel();
	 }
}
