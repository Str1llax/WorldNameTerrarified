package git.str1llax.wnt.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import git.str1llax.wnt.config.ConfigData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Objects;

public class CustomButton extends Button {
    private final ResourceLocation textureLocation;
    private final int xTexSize;
    private final int yTexSize;
    private final int xTexStart;
    private final int yTexStart;
    private final int yOffset;
    private final String tooltipKey;

    public CustomButton(int x, int y, int widthIn, int heightIn, int xTexSize, int yTexSize, int xTexStart, int yTexStart, int yOffset, ResourceLocation texPath, String tooltipKey, IPressable onPress) {
        super(x, y, widthIn, heightIn, "", onPress);
        this.xTexSize = xTexSize;
        this.yTexSize = yTexSize;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yOffset = yOffset;
        this.textureLocation = texPath;
        this.tooltipKey = tooltipKey;
    }

    public CustomButton(int x, int y, int widthIn, int heightIn, String text, String tooltipKey, IPressable onPress) {
        super(x, y, widthIn, heightIn, text, onPress);
        this.xTexSize = 0;
        this.yTexSize = 0;
        this.xTexStart = 0;
        this.yTexStart = 0;
        this.yOffset = 0;
        this.textureLocation = null;
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        if (ConfigData.useTexturedButton.get() && textureLocation != null) {
            Minecraft mc = Minecraft.getInstance();
            if (this.visible) {
                mc.getTextureManager().bindTexture(Objects.requireNonNull(this.textureLocation));

                this.isHovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

                float scaleX = (float) this.width / (float) this.xTexSize;
                float scaleY = (float) this.height / (float) this.yTexSize;

                GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableBlend();
                GlStateManager.disableDepthTest();
                GlStateManager.pushMatrix();
                GlStateManager.translatef(this.x, this.y, 0.0f);
                GlStateManager.scalef(scaleX, scaleY, 1.0f);

                this.blit(
                        0, 0,
                        this.xTexStart, this.yTexStart + (this.isHovered ? this.yOffset : 0),
                        this.xTexSize, this.yTexSize);

                GlStateManager.popMatrix();
            }
        } else {
            super.renderButton(mouseX, mouseY, partialTicks);
        }
    }


    public void drawTooltip(int mouseX, int mouseY, Screen parentScreen) {
        if (this.visible && this.isHovered) {
            parentScreen.renderTooltip(new TranslationTextComponent(this.tooltipKey).getFormattedText(), mouseX, mouseY);
        }
    }
}
