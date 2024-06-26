/*
 * Copyright 2013 Joachim Ansorg, mail@ansorg-it.com
 * File: FixShebangInspection.java, Class: FixShebangInspection
 * Last modified: 2013-05-09
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ansorgit.plugins.bash.editor.inspections.inspections;

import com.ansorgit.plugins.bash.editor.inspections.quickfix.RegisterShebangCommandQuickfix;
import com.ansorgit.plugins.bash.editor.inspections.quickfix.ShebangQuickfix;
import com.ansorgit.plugins.bash.lang.psi.BashVisitor;
import com.ansorgit.plugins.bash.lang.psi.api.BashShebang;
import consulo.annotation.component.ExtensionImpl;
import consulo.language.editor.inspection.LocalQuickFix;
import consulo.language.editor.inspection.ProblemsHolder;
import consulo.language.editor.rawHighlight.HighlightDisplayLevel;
import consulo.language.psi.PsiElementVisitor;
import consulo.util.xml.serializer.InvalidDataException;
import consulo.util.xml.serializer.WriteExternalException;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Pattern;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.util.LinkedList;
import java.util.List;

/**
 * This inspection offers quickfixes to replace an unknwown shebang line with well-known shebang commands.
 *
 * @author Joachim Ansorg
 */
@ExtensionImpl
public class FixShebangInspection extends AbstractBashInspection {
    private static final List<String> DEFAULT_COMMANDS = List.of("/bin/bash", "/bin/sh");
    private static final String ELEMENT_NAME_SHEBANG = "shebang";

    private List<String> validShebangCommands = DEFAULT_COMMANDS;

    @Pattern("[a-zA-Z_0-9.]+")
    @Nonnull
    @Override
    public String getID() {
        return "FixShebang";
    }

    @Nonnull
    public String getShortName() {
        return "Fix shebang";
    }

    @Nls
    @Nonnull
    public String getDisplayName() {
        return "Fix unusual shebang lines";
    }

    //@Override
    public JComponent createOptionsPanel() {
        FixShebangSettings settings = new FixShebangSettings();
        JTextArea textArea = settings.getValidCommandsTextArea();
        textArea.setText(String.join("\n", validShebangCommands));

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                updateShebangLines(documentEvent);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                updateShebangLines(documentEvent);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                updateShebangLines(documentEvent);
            }
        });

        return settings.getSettingsPanel();
    }

    private void updateShebangLines(DocumentEvent documentEvent) {
        validShebangCommands.clear();
        try {
            Document doc = documentEvent.getDocument();
            for (String item : doc.getText(0, doc.getLength()).split("\n")) {
                if (item.trim().length() != 0) {
                    validShebangCommands.add(item);
                }
            }
        } catch (BadLocationException e) {
            throw new RuntimeException("Could not save shebang inspection settings input", e);
        }
    }

    @Nonnull
    @Override
    public HighlightDisplayLevel getDefaultLevel() {
        return HighlightDisplayLevel.WARNING;
    }

    @Override
    public String getStaticDescription() {
        return "This inspection can replace unknown shebang commands with one of the registered commands, like /bin/sh .";
    }

    //@Override
    public void readSettings(Element node) throws InvalidDataException {
        validShebangCommands = new LinkedList<>();

        List<Element> shebangs = node.getChildren(ELEMENT_NAME_SHEBANG);
        for (Element shebang : shebangs) {
            validShebangCommands.add(shebang.getText());
        }

        if (validShebangCommands.isEmpty()) {
            validShebangCommands.addAll(DEFAULT_COMMANDS);
        }
    }

    //@Override
    public void writeSettings(Element node) throws WriteExternalException {
        for (String shebangCommand : validShebangCommands) {
            Element shebandElement = new Element(ELEMENT_NAME_SHEBANG);
            shebandElement.setText(shebangCommand.trim());
            node.addContent(shebandElement);
        }
    }

    @Nonnull
    @Override
    public PsiElementVisitor buildVisitor(@Nonnull final ProblemsHolder holder, final boolean isOnTheFly) {
        return new BashVisitor() {
            @Override
            public void visitShebang(BashShebang shebang) {
                //check command with and withouot parameters
                boolean isValid = validShebangCommands.contains(shebang.shellCommand(false)) || validShebangCommands.contains(shebang.shellCommand(true));

                if (isOnTheFly && !isValid && validShebangCommands.size() > 0) {
                    //quickfix to register the shebang line
                    holder.registerProblem(shebang, "Mark as valid shebang command", new RegisterShebangCommandQuickfix(FixShebangInspection.this, shebang));

                    LinkedList<LocalQuickFix> quickFixes = new LinkedList<>();
                    for (String validCommand : validShebangCommands) {
                        if (!validCommand.equals(shebang.shellCommand(false)) && !validCommand.equals(shebang.shellCommand(true))) {
                            quickFixes.add(new ShebangQuickfix(shebang, validCommand));
                        }
                    }

                    if (!quickFixes.isEmpty()) {
                        holder.registerProblem(shebang, "Replace with valid shebang command", quickFixes.toArray(new LocalQuickFix[quickFixes.size()]));
                    }
                }
            }
        };
    }

    public void registerShebangCommand(String command) {
        validShebangCommands.add(command);
    }
}
