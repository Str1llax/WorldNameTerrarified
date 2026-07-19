package git.str1llax.wnt.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class WNTConfigScreen extends Screen {
    private final Screen parent;

    private Button enableTooltipButton;
    private Button useTextureButton;
    private Button startRandomButton;
    private Button useLocaleButton;
    private TextFieldWidget localeCodeField;
    private TextFieldWidget worldNameLengthField;
    private TextFieldWidget buttonXField;
    private TextFieldWidget buttonYField;
    private TextFieldWidget buttonSizeField;
    private Button doneButton;
    private Button exitButton;
    private Button resetAllButton;

    private static final int DEFAULT_COLOR = 14737632;;
    private static final int RED_COLOR = 0xFF3333;

    public WNTConfigScreen(@Nonnull Screen parent) {
        super(new TranslationTextComponent("config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();

        assert this.minecraft != null;
        this.minecraft.keyboardListener.enableRepeatEvents(true);

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        int panelY = this.height - 20;
        int startY = this.height / 10;

        enableTooltipButton = createConfigButton(centerX, startY, 120, 20, ConfigData.enableButtonTooltip);
        this.addButton(enableTooltipButton);
        useTextureButton = createConfigButton(centerX, startY + 30, 120, 20, ConfigData.useTexturedButton);
        this.addButton(useTextureButton);
        startRandomButton = createConfigButton(centerX, startY + 30 * 2, 120, 20, ConfigData.startWithRandom);
        this.addButton(startRandomButton);
        useLocaleButton = createConfigButton(centerX, startY + 30 * 3, 120, 20, ConfigData.useMcLocale);
        this.addButton(useLocaleButton);

        localeCodeField = createTextField(centerX, startY + 30 * 4, 120, 20, 8, ConfigData.localeCode);
        validateForString(localeCodeField, ConfigData.localeCode);
        worldNameLengthField = createTextField(centerX, startY + 30 * 5, 120, 20, 10, ConfigData.worldNameLength);
        validateForIntegers(worldNameLengthField, ConfigData.worldNameLength);
        buttonXField = createTextField(centerX, startY + 30 * 6, 120, 20, 10, ConfigData.buttonX);
        validateForIntegers(buttonXField, ConfigData.buttonX);
        buttonYField = createTextField(centerX, startY + 30 * 7, 120, 20, 10, ConfigData.buttonY);
        validateForIntegers(buttonYField, ConfigData.buttonY);
        buttonSizeField = createTextField(centerX, startY + 30 * 8, 120, 20, 10, ConfigData.buttonSize);
        validateForIntegers(buttonSizeField, ConfigData.buttonSize);

        // Done button
        doneButton = new Button(centerX - 60, panelY - 10, 120, 20, new TranslationTextComponent("button.config.done").getFormattedText(), button -> {
            ConfigData.localeCode.set(localeCodeField.getText());
            ConfigData.worldNameLength.set(Integer.parseInt(worldNameLengthField.getText()));
            ConfigData.buttonX.set(Integer.parseInt(buttonXField.getText()));
            ConfigData.buttonY.set(Integer.parseInt(buttonYField.getText()));
            ConfigData.buttonSize.set(Integer.parseInt(buttonSizeField.getText()));

            WNTConfig.save();
            assert this.minecraft != null;
            this.minecraft.displayGuiScreen(parent);
        });
        this.addButton(doneButton);

        // Exit button
        exitButton = new Button(centerX - 30 - 100, panelY - 10, 60, 20, new TranslationTextComponent("button.config.exit").getFormattedText(), button -> {
            assert this.minecraft != null;
            this.minecraft.displayGuiScreen(parent);
        });
        this.addButton(exitButton);

        // Reset ALL button
        resetAllButton = new Button(centerX - 30 + 100, panelY - 10, 60, 20, new TranslationTextComponent("button.config.reset_all").getFormattedText(), button -> {
            WNTConfig.resetAll();
            updateFields();
            doneButton.active = true;
        });
        this.addButton(resetAllButton);

    }

    private void updateFields() {
        updateButtonState(enableTooltipButton, ConfigData.enableButtonTooltip);
        updateButtonState(useTextureButton, ConfigData.useTexturedButton);
        updateButtonState(startRandomButton, ConfigData.startWithRandom);
        updateButtonState(useLocaleButton, ConfigData.useMcLocale);
        updateTextFieldState(localeCodeField, ConfigData.localeCode);
        updateTextFieldState(worldNameLengthField, ConfigData.worldNameLength);
        updateTextFieldState(buttonXField, ConfigData.buttonX);
        updateTextFieldState(buttonYField, ConfigData.buttonY);
        updateTextFieldState(buttonSizeField, ConfigData.buttonSize);
    }

    private void validateForIntegers(TextFieldWidget textField, ConfigEntry<Integer> entry) {
        textField.setValidator(text -> text.isEmpty() || text.matches("-?\\d+"));
    }

    private void validateForString(TextFieldWidget textField, ConfigEntry<String> entry) {
        textField.setValidator(text -> text.isEmpty() || text.matches("[a-zA-Z0-9_]+"));
    }

    private void updateButtonState(Button button, ConfigEntry<Boolean> config) {
        button.setMessage(
                String.format("%s: %s", new TranslationTextComponent(config.translationKey).getFormattedText(),
                        new TranslationTextComponent(config.get() ? "options.on" : "options.off")
                                .setStyle(new Style().setColor(config.get() ? TextFormatting.GREEN : TextFormatting.RED)).getFormattedText()));
    }

    private void updateTextFieldState(TextFieldWidget textField, ConfigEntry<?> config) {
        if (!config.isRanged()) return;
        if (config.inRangeParsing(textField.getText())) {
            textField.setTextColor(DEFAULT_COLOR);
        } else {
            textField.setTextColor(RED_COLOR);
            this.doneButton.active = false;
        }
    }

    private Button createConfigButton(int x, int y, int sizeX, int sizeY, ConfigEntry<Boolean> config) {
        Button button = new Button(x - sizeX / 2, y - sizeY / 2, sizeX, sizeY,
                String.format("%s: %s", new TranslationTextComponent(config.translationKey).getFormattedText(),
                        new TranslationTextComponent(config.get() ? "options.on" : "options.off")
                                .setStyle(new Style().setColor(config.get() ? TextFormatting.GREEN : TextFormatting.RED)).getFormattedText()), configButton -> {
            config.set(!config.get());
            updateButtonState(configButton, config);
        });
        this.addButton(new Button(x + 70, y - 10, 40, 20, new TranslationTextComponent("button.config.reset").getFormattedText(), resetButton -> {
            config.reset();
            updateButtonState(button, config);
        }));

        return button;
    }

    private <V extends Comparable<? super V>> TextFieldWidget createTextField(int x, int y, int sizeX, int sizeY, int maxLen, ConfigEntry<V> config) {
        TextFieldWidget field = new TextFieldWidget(this.font, x - sizeX / 2, y - sizeY / 2, sizeX, sizeY, "");
        field.setMaxStringLength(maxLen);
        field.setText(config.get().toString());
        this.children.add(field);
        this.addButton(new Button(x + 70, y - 10, 40, 20, new TranslationTextComponent("button.config.reset").getFormattedText(), button -> {
            config.reset();
            field.setText(config.get().toString());
        }));

        return field;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();

        this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 10, 0xFFFFFF);

        this.localeCodeField.render(mouseX, mouseY, partialTicks);
        this.worldNameLengthField.render(mouseX, mouseY, partialTicks);
        this.buttonXField.render(mouseX, mouseY, partialTicks);
        this.buttonYField.render(mouseX, mouseY, partialTicks);
        this.buttonSizeField.render(mouseX, mouseY, partialTicks);

        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        this.localeCodeField.tick();
        this.worldNameLengthField.tick();
        this.buttonXField.tick();
        this.buttonYField.tick();
        this.buttonSizeField.tick();
    }

    @Override
    public void onClose() {
        assert this.minecraft != null;
        this.minecraft.keyboardListener.enableRepeatEvents(false);
        this.minecraft.displayGuiScreen(this.parent);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clickedLocale = this.localeCodeField.mouseClicked(mouseX, mouseY, button);
        boolean clickedLength = this.worldNameLengthField.mouseClicked(mouseX, mouseY, button);
        boolean clickedX = this.buttonXField.mouseClicked(mouseX, mouseY, button);
        boolean clickedY = this.buttonYField.mouseClicked(mouseX, mouseY, button);
        boolean clickedSize = this.buttonSizeField.mouseClicked(mouseX, mouseY, button);

        this.localeCodeField.setFocused2(clickedLocale);
        this.worldNameLengthField.setFocused2(clickedLength);
        this.buttonXField.setFocused2(clickedX);
        this.buttonYField.setFocused2(clickedY);
        this.buttonSizeField.setFocused2(clickedSize);

        if (clickedLocale || clickedLength || clickedX || clickedY || clickedSize) {
            this.setFocused(null);
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.localeCodeField.keyPressed(keyCode, scanCode, modifiers)
        || this.worldNameLengthField.keyPressed(keyCode, scanCode, modifiers)
        || this.buttonXField.keyPressed(keyCode, scanCode, modifiers)
        || this.buttonYField.keyPressed(keyCode, scanCode, modifiers)
        || this.buttonSizeField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (this.localeCodeField.charTyped(codePoint, modifiers)
        || this.worldNameLengthField.charTyped(codePoint, modifiers)
        || this.buttonXField.charTyped(codePoint, modifiers)
        || this.buttonYField.charTyped(codePoint, modifiers)
        || this.buttonSizeField.charTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }
}
