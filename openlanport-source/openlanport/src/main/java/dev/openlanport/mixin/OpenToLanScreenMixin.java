package dev.openlanport.mixin;

import dev.openlanport.PortHolder;
import net.minecraft.client.gui.screen.OpenToLanScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds a "Port Number" text field to the Open to LAN screen (1.6.4).
 *
 * keyPressed / mouseClicked / tick are not declared in OpenToLanScreen itself
 * (they are inherited from Screen), so they are added as overrides instead of
 * being injected into.
 */
@Mixin(OpenToLanScreen.class)
public abstract class OpenToLanScreenMixin extends Screen {

    @Unique
    private TextFieldWidget openlanport$portField;

    @Inject(method = "init", at = @At("TAIL"))
    private void openlanport$init(CallbackInfo ci) {
        openlanport$portField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, 140, 200, 20);
        openlanport$portField.setMaxLength(5);
        openlanport$portField.setText(PortHolder.lastText);
    }

    @Inject(method = "buttonClicked", at = @At("HEAD"))
    private void openlanport$onButton(ButtonWidget button, CallbackInfo ci) {
        // 101 = "Start LAN World"
        if (button.id == 101 && openlanport$portField != null) {
            PortHolder.apply(openlanport$portField.getText());
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void openlanport$render(int mouseX, int mouseY, float tickDelta, CallbackInfo ci) {
        if (openlanport$portField == null) {
            return;
        }
        boolean valid = PortHolder.isValid(openlanport$portField.getText());
        String label = "Port Number (" + PortHolder.MIN_PORT + "-" + PortHolder.MAX_PORT + ", empty = automatic)";
        this.drawString(this.textRenderer, label, this.width / 2 - 100, 128, valid ? 0xA0A0A0 : 0xFF5555);
        openlanport$portField.render();
    }

    @Override
    protected void keyPressed(char chr, int code) {
        super.keyPressed(chr, code);
        if (openlanport$portField != null) {
            openlanport$portField.keyPressed(chr, code);
            String text = openlanport$portField.getText();
            String digits = text.replaceAll("[^0-9]", "");
            if (!digits.equals(text)) {
                openlanport$portField.setText(digits);
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (openlanport$portField != null) {
            openlanport$portField.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (openlanport$portField != null) {
            openlanport$portField.tick();
        }
    }
}
