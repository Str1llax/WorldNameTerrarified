package git.str1llax.wnt.utils;

import git.str1llax.wnt.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import java.util.Objects;

public class CustomButton extends GuiButton {
    private final ResourceLocation textureLocation;
    private final int xTexSize;
    private final int yTexSize;
    private final int xTexStart;
    private final int yTexStart;
    private final int yOffset;
    private final String tooltipKey;

    public CustomButton(int buttonId, int x, int y, int widthIn, int heightIn, int xTexSize, int yTexSize, int xTexStart, int yTexStart, int yOffset, ResourceLocation texPath, String key) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.xTexSize = xTexSize;
        this.yTexSize = yTexSize;
        this.xTexStart = xTexStart;
        this.yTexStart = yTexStart;
        this.yOffset = yOffset;
        this.textureLocation = texPath;
        this.tooltipKey = key;
    }

    public CustomButton(int buttonId, int x, int y, int widthIn, int heightIn, String text, String tooltipKey) {
        super(buttonId, x, y, widthIn, heightIn, text);
        this.xTexSize = 0;
        this.yTexSize = 0;
        this.xTexStart = 0;
        this.yTexStart = 0;
        this.yOffset = 0;
        this.textureLocation = null;
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (ModConfig.useTexturedButton) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(Objects.requireNonNull(this.textureLocation));

                this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

                float scaleX = (float) this.width / (float) this.xTexSize;
                float scaleY = (float) this.height / (float) this.yTexSize;

                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableBlend();
                GlStateManager.disableDepth();
                GlStateManager.pushMatrix();
                GlStateManager.translate(this.x, this.y, 0.0f);
                GlStateManager.scale(scaleX, scaleY, 1.0f);

                this.drawTexturedModalRect(
                        0, 0,
                        this.xTexStart, this.yTexStart + (this.hovered ? this.yOffset : 0),
                        this.xTexSize, this.yTexSize);

                GlStateManager.popMatrix();
            }
        } else {
            super.drawButton(mc, mouseX, mouseY, partialTicks);
        }
    }

    public void drawTooltip(int mouseX, int mouseY, GuiScreen parentScreen) {
        if (this.visible && this.hovered) {
            parentScreen.drawHoveringText(new TextComponentTranslation(this.tooltipKey).getFormattedText(), mouseX, mouseY);
        }
    }
}
